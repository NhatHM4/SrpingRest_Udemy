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
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;
import vn.hoidanit.jobhunter.domain.response.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IllegalStateException.class,
            IdInvalidException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception invalidException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Exception occurs ... ");
        res.setMessage(invalidException.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = {
            NoResourceFoundException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleNoResourceFoundException(
            NoResourceFoundException invalidException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError("404 Not Found !!!");
        res.setMessage(invalidException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class, ConstraintViolationException.class })
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

    @ExceptionHandler(value = {
            StorageException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleFileUpload(Exception exception) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Exception upload file ... ");
        res.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = {
            PermissionInvalidException.class,
    })
    public ResponseEntity<RestResponse<Object>> handlePermissionException(PermissionInvalidException exception) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError("Forbidden !!!! ");
        res.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

}
