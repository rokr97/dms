package my.bank.cjd.dms.documents.adapter.web.settings;

import my.bank.cjd.dms.documents.service.DocumentNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DocumentsRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DocumentNotFoundException.class)
    protected ResponseEntity<Object> handleNoResultFromDatabase(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}