package vn.hoidanit.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoidanit.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {
            IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            UnsupportedOperationException.class
    })
    public ResponseEntity<Object> handleIdException(Exception invalidException) {
        System.out.println("global exe");
        RestResponse<Object> res = new RestResponse<Object>();
        res.setError(invalidException.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

}
