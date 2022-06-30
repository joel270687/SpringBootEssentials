package br.com.devdojo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Joel on 29/06/2022.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) //faz com que essa response seja automaticamente 404
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
