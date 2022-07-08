package br.com.devdojo.handler;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Created by Joel on 08/07/2022.
 */
@RestControllerAdvice
public class RestResponseExceptionHandler extends DefaultResponseErrorHandler { //ctrl o , para sobreescrever

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        System.out.println("-----------Inside hasError-----------");
        return super.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        System.out.println("-----------Inside handleError-----------");
        System.out.println("-----------Doing something whit status code: "+ response.getStatusCode());
        //para retornar o body tem que usar o apache commons-io adicionado nas dependencias
        //e com esse body, pode-se fazer a conversão dele para JSON e dar uma mensagem mais amigavel, customizada quando houver excessão
        System.out.println("-----------Doing something whit body: "+ IOUtils.toString(response.getBody(),"UTF-8"));
        //super.handleError(response); //removendo esse super que vem de padrão ao sobreescrever o handler não retorna aquele texto com: Exception in thread "main" e o erro GIGANTE
    }
}
