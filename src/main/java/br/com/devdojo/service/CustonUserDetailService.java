package br.com.devdojo.service;

import br.com.devdojo.model.User;
import br.com.devdojo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Joel on 04/07/2022.
 */
@Component  //para o @Autowired funcionar precisa marcar essa classe como @Component
public class CustonUserDetailService implements UserDetailsService{
    private final UserRepository userRepository;
    @Autowired
    public CustonUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //usando java8 busca o usuario e se não encontrar já chama a escessão direto
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //criando listas de autorizações
        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                user.isAdmin() ? authorityListAdmin : authorityListUser);
    }
}
