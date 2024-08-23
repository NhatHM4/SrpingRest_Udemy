package vn.hoidanit.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
        RestResponse<Object> res = new RestResponse<Object>();
        res.setError(invalidException.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<RestResponse<List<String>>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        RestResponse<List<String>> res = new RestResponse<List<String>>();
        List<String> listError = fieldErrors.stream()
                .map(error -> "Field '" + error.getField() + "': " + error.getDefaultMessage())
                .collect(Collectors.toList());
        res.setMessage(listError);
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());

        return ResponseEntity.badRequest().body(res);
    }

}
