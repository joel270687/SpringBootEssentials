package br.com.devdojo.javaclient;

import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Joel on 05/07/2022.
 */
public class JavaSpringClientTest {  //Classe criada somente para testar como retornar uma requisição e pegar os objetos essa seria uma classe do CLIENT
    public static void main(String[] args) {
        Student studentPost = new Student();
        studentPost.setName("Update por teste DAO");
        studentPost.setEmail("update@dao.com");

        JavaClientDAO dao = new JavaClientDAO();
        //System.out.println(dao.findById(new Long(20)));
        //List<Student> students = dao.listAll();
        //System.out.println(students);
        //System.out.println(dao.save(studentPost));

        //para atualizar precisa setar o id
        //studentPost.setId(42L); //colocou o L no final do 29 pra identificar como Long
        //dao.update(studentPost);

        //Teste deletando
        dao.delete(42L);
    }
}
