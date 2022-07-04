package br.com.devdojo.repository;

import br.com.devdojo.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Joel on 04/07/2022.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long>{
    User findByUsername(String username);
}
