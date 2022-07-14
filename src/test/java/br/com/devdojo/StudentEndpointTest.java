package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Joel on 11/07/2022.
 */
//TESTES DOS ENDPOINTS

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //para iniciar o SpringBootTest sempre em uma porta aleatória e não ter problema de não iniciar por que alguma porta já está em uso
@AutoConfigureMockMvc //para usar o MockMvc tem que colocar essa anotação
public class StudentEndpointTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @MockBean //anotação para não alterar os dados reais do banco, ele faz com dados fictícios teste, mas utilizando o banco real
    private StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc; //faz os testes utilizando ele (MockMvc) ao invez de usar o restTemplate;

    //como a aplicação estão autenticados os endpoints protegidos
    //pra isso precisa criar uma classe para retornar um RestTeplateBuilder; abaixo
    @TestConfiguration
    static class Config{
        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder().basicAuthentication("admin","admin");
        }
    }

    @Before //anotação que força que toda vez que for executar um método ele executa esse antes
    public void setup(){
        Student student = new Student(1L, "Mockito1", "mockito@mockito.com");
        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.ofNullable(student));  //quando executar o metodo findAll() tem que retornar .thenRetun esses dados
    }

    //metodo erro listStudent passando Username e Passwords incorretos
    @Test
    public void listStudentsWhenUserameAndPassowordAreIncorrectShouldReturnStatusCode401(){
        restTemplate = restTemplate.withBasicAuth("1","1"); //passando o username e senha errados de proposito para testar
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students", String.class);
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    //metodo erro getStudent passando Username e Passwords incorretos
    @Test
    public void getStudentByIdStudentsWhenUserameAndPassowordAreIncorrectShouldReturnStatusCode401(){
        restTemplate = restTemplate.withBasicAuth("1","1"); //passando o username e senha errados de proposito para testar
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/1", String.class);
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    //metodo SUCESSO lisStudent passando Username e Passwords CORRETOS
    @Test
    public void listStudentsWhenUserameAndPassowordAreCorrectShouldReturnStatusCode200(){
        List<Student> students = asList(new Student(1L, "Mockito1", "mockito@mockito.com"),
                new Student(2L, "Mockito2", "mockito@mockito.com"),
                new Student(3L, "Mockito3", "mockito@mockito.com"));

        BDDMockito.when(studentRepository.findAll()).thenReturn(students);  //quando executar o metodo findAll() tem que retornar .thenRetun esses dados
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    //metodo SUCESSO getStudentsById passando Username e Passwords CORRETOS
    @Test
    public void getStudentByIdWhenUserameAndPassowordAreCorrectShouldReturnStatusCode200(){
        ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class,1L);
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    //metodo SUCESSO getStudentsById passando Username e Passwords CORRETOS
    @Test
    public void getStudentByIdWhenUserameAndPassowordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404(){
        ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, -1);
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
    //deletar quando o HasHole for admin e o estudante existir
    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExsistsShouldReturnStatusCode200(){
        BDDMockito.doNothing().when(studentRepository).deleteById(1L); //Mockito, não faz nada quando esse repositorio chamar o delete
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
        org.assertj.core.api.Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }
    //deletar quando o HasHole for admin e o estudante não existir
    @Test
    @WithMockUser  (username = "xx",password = "xx", roles = {"USER","ADMIN"}) //o role se não colocar por padrão e "USER"
    //pra usar essa anotação acima tem que colocar a dependencia no pom , essa anotação serve para incluir o usuario e senha na requisição test
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExsistShouldReturnStatusCode404() throws Exception {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L); //Mockito, não faz nada quando esse repositorio chamar o delete
        //as duas lihas abaixo foi usando o restTemplate
        //ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, -1L);
        //org.assertj.core.api.Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);

        //usando o mockMvc
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}",1L)) //executando esse delete
        .andExpect(MockMvcResultMatchers.status().isNotFound());  //nos esperamos = andExpect
    }
}
