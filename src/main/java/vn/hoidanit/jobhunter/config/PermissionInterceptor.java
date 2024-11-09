package vn.hoidanit.jobhunter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.PermissionInvalidException;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (email != null && !"".equals(email) && !"anonymousUser".equals(email)) {
            User user = this.userService.getUserByUserName(email);
            if (user.getRole() != null && user.getRole().getPermissions() != null
                    && user.getRole().getPermissions().size() > 0) {
                boolean isAllow = user.getRole().getPermissions().stream().anyMatch(
                        permission -> permission.getApiPath().equals(path)
                                && permission.getMethod().equals(httpMethod));
                if (!isAllow) {
                    throw new PermissionInvalidException(" You don't have permission for this url !!!");
                }
            } else {
                throw new PermissionInvalidException(" You don't have permission for this url !!!");
            }

        }

        return true;
    }
}
