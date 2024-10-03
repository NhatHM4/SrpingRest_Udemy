package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.RestLoginDTO;
import vn.hoidanit.jobhunter.domain.dto.RestLoginDTO.UserLogin;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

        private final AuthenticationManagerBuilder authenticationManagerBuilder;

        private final SecurityUtil securityUtil;

        private final UserService userService;

        @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
        private String refreshTokenExpiration;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                        UserService userService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(), loginDTO.getPassword());
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                RestLoginDTO restLoginDTO = new RestLoginDTO();
                User user = userService.getUserByUserName((String) authentication.getName());
                RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin(
                                user.getId(),
                                user.getEmail(),
                                user.getName());
                restLoginDTO.setUser(userLogin);
                // create a token
                String accessToken = this.securityUtil.createAccessToken(user.getEmail(), restLoginDTO.getUser());
                restLoginDTO.setAccessToken(accessToken);
                String refreshToken = this.securityUtil.createRefreshToken(user.getEmail(), userLogin);
                this.userService.updateRefreshToken(refreshToken, user.getEmail());
                ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Long.parseLong(refreshTokenExpiration))
                                .build();
                return ResponseEntity
                                .ok()
                                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                                .body(restLoginDTO);
        }

        @GetMapping("/auth/account")
        @ApiMessage("fetch account")
        public ResponseEntity<RestLoginDTO.UserGetAccount> getAccount() {
                String userName = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                User user = userService.getUserByUserName(userName);
                RestLoginDTO.UserGetAccount userGetAccount = new RestLoginDTO.UserGetAccount();
                RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin(
                                user.getId(),
                                user.getEmail(),
                                user.getName());
                userGetAccount.setUser(userLogin);

                return ResponseEntity.ok(userGetAccount);
        }

        @GetMapping("/auth/refresh")
        @ApiMessage("get new token")
        public ResponseEntity<RestLoginDTO> getMethodName(
                        @CookieValue(name = "refresh_token", defaultValue = "") String refresh_token)
                        throws IdInvalidException {
                if (refresh_token.equals("")) {
                        throw new IdInvalidException(" refresh token not exists !!!");
                }
                Jwt jwt = securityUtil.decodeToken(refresh_token);
                String email = jwt.getSubject();
                User currentUser = userService.getUserByRefreshTokenAndEmail(refresh_token, email);
                RestLoginDTO restLoginDTO = new RestLoginDTO();
                RestLoginDTO.UserLogin userLogin = null;
                if (currentUser == null) {
                        throw new IdInvalidException(" User is not exists !!!");
                }
                userLogin = new RestLoginDTO.UserLogin(
                                currentUser.getId(),
                                currentUser.getEmail(),
                                currentUser.getName());
                restLoginDTO.setUser(userLogin);
                // create a token
                String accessToken = this.securityUtil.createAccessToken(email, restLoginDTO.getUser());
                restLoginDTO.setAccessToken(accessToken);
                String refreshToken = this.securityUtil.createRefreshToken(email, userLogin);
                this.userService.updateRefreshToken(refreshToken, email);
                ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Long.parseLong(refreshTokenExpiration))
                                .build();
                return ResponseEntity
                                .ok()
                                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                                .body(restLoginDTO);
        }

        @SuppressWarnings("null")
        @PostMapping("/auth/logout")
        @ApiMessage("logout user")
        public ResponseEntity<Void> logout() throws IdInvalidException {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                if ("".equals(email)) {
                        throw new IdInvalidException(" email is not exists !!!");
                }
                this.userService.updateRefreshToken(null, email);
                ResponseCookie deleteSpringCookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                return ResponseEntity
                                .ok()
                                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                                .build();
        }

}
