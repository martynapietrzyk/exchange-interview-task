package pl.interview.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.interview.exception.AccountAlreadyExistsException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDto> handleAllExceptionsSimple(Exception exc) {
        log.error("Exception occurred", exc);
        return new ResponseEntity<>(new MessageDto("Non Business exception happened, please contact system administrator"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<MessageDto> handleAccountAlreadyExistsException(AccountAlreadyExistsException exc) {
        return new ResponseEntity<>(new MessageDto("Account for that PESEL already exists."), HttpStatus.CONFLICT);
    }


    static record MessageDto(String message) {
    }
}
