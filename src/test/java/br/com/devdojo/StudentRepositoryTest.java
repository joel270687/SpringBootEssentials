package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Joel on 11/07/2022.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Essa anotação faz com que o teste seja feito no banco de dados real, e faz o rollback
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;
    @Rule
    public ExpectedException thrown = ExpectedException.none(); //esse objeto espera as exceções que esperamos que aconteçam
    //e se não acontecer as excessções o teste vai ser falho

    @Test
    public void createShouldPersistData(){
        Student student = new Student("Test","teste@teste.com"); //tem que criar o contrutor de Student com todos as variáveis e um sem nenhuma variável
        this.studentRepository.save(student);
        //agora fazendo as Asserções
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo("Test");
        assertThat(student.getEmail()).isEqualTo("teste@teste.com");
    }

    @Test
    public void deleteShouldPersistData(){
        Student student = new Student("Test","teste@teste.com"); //tem que criar o contrutor de Student com todos as variáveis e um sem nenhuma variável
        this.studentRepository.save(student);
        studentRepository.delete(student);
        assertThat(studentRepository.findById(student.getId())).isNull();
    }

    @Test
    public void updateShouldChangePersistData(){
        Student student = new Student("Test","teste@teste.com"); //tem que criar o contrutor de Student com todos as variáveis e um sem nenhuma variável
        this.studentRepository.save(student);
        student.setName("Teste2");
        student.setEmail("teste2@teste2.com");
        this.studentRepository.save(student);
        //student = this.studentRepository.findById(student.getId()); //a forma mais confiável mas não funcionou
        assertThat(student.getName()).isEqualTo("Teste2");
        assertThat(student.getEmail()).isEqualTo("teste2@teste2.com");
    }

    @Test
    public void findByNameIgnoreContainingShouldIgnoreCase(){
        Student student = new Student("Test","teste@teste.com"); //tem que criar o contrutor de Student com todos as variáveis e um sem nenhuma variável
        Student student2 = new Student("Test","teste@teste.com"); //tem que criar o contrutor de Student com todos as variáveis e um sem nenhuma variável
        this.studentRepository.save(student);
        this.studentRepository.save(student2);
        List<Student> studentList = studentRepository.findByNameIgnoreCaseContaining("Test");
        assertThat(studentList.size()).isEqualTo(2); //ou seja vai retornar os dois que criamos acima, se colocar 3 vai dar Exception
    }

    @Test
    public void createWhenNameIsNullShouldThrowConstraintViolationException(){
        thrown.expect(ConstraintViolationException.class); //ou seja ele espera o constraintViolationExeption
        thrown.expectMessage("O campo nome é obrigatório"); //ou pode usar a mensagem que lançaremos na classe
        this.studentRepository.save(new Student());
    }

    @Test
    public void createWhenEmailIsNullShouldThrowConstraintViolationException(){
        thrown.expect(ConstraintViolationException.class);
        Student student = new Student();
        student.setName("Teste");
        this.studentRepository.save(student);
    }

    @Test
    public void createWhenEmailIsNotValidShouldThrowConstraintViolationException(){
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Digite um email válido"); //ou pode usar a mensagem que lançaremos na classe
        Student student = new Student();
        student.setName("Teste");
        student.setEmail("teste.com");
        this.studentRepository.save(student);
    }


}
