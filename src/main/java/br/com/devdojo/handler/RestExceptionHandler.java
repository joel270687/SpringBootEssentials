package br.com.devdojo.handler;

import br.com.devdojo.error.ErrorDetails;
import br.com.devdojo.error.ResourceNotFoundDetails;
import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.error.ValidationErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joel on 29/06/2022.
 */
@ControllerAdvice  //permite utilizar essa classe criada abaixo através das diversas camadas do Spring
//por causa desse controller todas as vezes que essa excessão for lançada o proprio Spring sera responsável por lançar esse @ExceptionHandler abaixo
public class RestExceptionHandler{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundExcepton(ResourceNotFoundException rnfException){
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .details(rnfException.getMessage())
                .developerMessage(rnfException.getClass().getName())
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException manvException){
        List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();//retorna lista com erros das validações de variáveis setadas como @NotEmpty @Email e outros
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));//coloca todos erros em uma string separado por virgula , usando Java8
        String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        ValidationErrorDetails rnfDetails = ValidationErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Field Validation Error")
                .details("Field Validation Error")
                .developerMessage(manvException.getClass().getName())
                .field(fields)
                .fieldMessage(fieldMessages)
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.BAD_REQUEST);
    }
//a classe extendida ResponseEntityExceptionHandler sempre chama outra classe
// que para padronizar os erros e bom sobreescrever conforme abaixo
//Não funcionou
}
