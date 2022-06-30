package br.com.devdojo.repository;

import br.com.devdojo.model.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Joel on 28/06/2022.
 */
public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByNameIgnoreCaseContaining(String name); //O Spring já se encarrega de fazer essa query, passando o Name como parametro
                                            //assim não precisamos se preoculpar com fazer a query
}
