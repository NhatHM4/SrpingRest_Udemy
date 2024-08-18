package vn.hoidanit.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoidanit.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<Object> handleIdException(IdInvalidException invalidException) {
        System.out.println("global exe");
        RestResponse<Object> res = new RestResponse<Object>();
        res.setError(invalidException.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

}
