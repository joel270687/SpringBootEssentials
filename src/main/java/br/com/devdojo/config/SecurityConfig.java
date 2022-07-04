package br.com.devdojo.config;

import br.com.devdojo.service.CustonUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Joel on 01/07/2022.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//faz com que o security procure qual requisicao
                                            // o usuario informado pode acessar ex; @PreAuthorize("hasRole('ADMIN')") colocado no delete
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private CustonUserDetailService custonUserDetailService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //faz com que toda aplicação, todas requisições precise ser autenticada
                http
                .csrf().disable()//disabilitando a segurança para ataques Cross-Site Request Forgery (CSRF)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET).permitAll()//permite todos os GET
                        .antMatchers(HttpMethod.POST).permitAll()//permite todos os POST
                        .antMatchers(HttpMethod.PUT).permitAll()//permite todos os PUT
                .anyRequest().authenticated()
                .and()
                .httpBasic();// //tipo de autenticação BasicAuth
                //.and()
                //.csrf().disable();//disabilitando a segurança para ataques Cross-Site Request Forgery (CSRF)
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(custonUserDetailService)
                .passwordEncoder(new BCryptPasswordEncoder()); //para descriptografar
    }
    //    @Autowired //para fazer o spring configurar automatico
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        //cria os usuarios para spring-security
//        auth.inMemoryAuthentication()
//                .withUser("joel").password("joel").roles("USER")
//                .and()
//                .withUser("admin").password("admin").roles("USER","ADMIN");
//
//    } //foi usado somente para testar os usuários em memória
}
