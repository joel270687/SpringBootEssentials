package br.com.devdojo.adapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by Joel on 01/07/2022.
 */
@Configuration
public class SpringBootEssentialsAdapter extends WebMvcConfigurerAdapter{
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver phmar = new PageableHandlerMethodArgumentResolver();
        //phmar.setFallbackPageable(new PageRequest(0,5)); //ativando este vai colocar como padrão no request students?page=0&size=5
        //Não funcionou o de cima porque está aparecendo Depreciado , olhar depois
        argumentResolvers.add(phmar);
    }
}
