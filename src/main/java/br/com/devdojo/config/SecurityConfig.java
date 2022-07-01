package br.com.devdojo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Joel on 01/07/2022.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//faz com que o security procure qual requisicao
                                            // o usuario informado pode acessar ex; @PreAuthorize("hasRole('ADMIN')") colocado no delete
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //faz com que toda aplicação, todas requisições precise ser autenticada
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic() //tipo de autenticação BasicAuth
                .and()
                .csrf().disable();//disabilitando a segurança para ataques Cross-Site Request Forgery (CSRF)
    }
    @Autowired //para fazer o spring configurar automatico
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //cria os usuarios para spring-security
        auth.inMemoryAuthentication()
                .withUser("joel").password("joel").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("USER","ADMIN");

    }
}
