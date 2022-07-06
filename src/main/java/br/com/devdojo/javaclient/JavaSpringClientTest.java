package br.com.devdojo.javaclient;

import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Joel on 05/07/2022.
 */
public class JavaSpringClientTest {  //Classe criada somente para testar como retornar uma requisição e pegar os objetos essa seria uma classe do CLIENT
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/user/students")
                .basicAuthentication("joel","joel")
                .build();//faz uma requisição para essa api "conforme url" passando os parametros
        Student student = restTemplate.getForObject("/{id}", Student.class,3); //retorna a requisição em um objeto absoluto
        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class,3); //retorna a requisição em entyti contendo status, header e o objeto
        //testando o retorno com1 estudante
        System.out.println(student);
        System.out.println(forEntity.getBody());
        //para vários estudantes, usado sem o pageable
        /*Student students[] = restTemplate.getForObject("/", Student[].class); //retorna a requisição em um array de objetos , ele não aceita Lista só array
        System.out.println(Arrays.toString(students));
        ResponseEntity<List<Student>> exchange = restTemplate.exchange("/", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Student>>() {}); //retornando a lista com o ResponseEntity, lista de objetos
        System.out.println(exchange.getBody());*/

        //para vários estudantes, usaando o pageable

        // NÃO FUNCIONOU E FALTOU IMPLEMENTAR UMA CLASSE: CustomSortDeserializer.java que estava depreciada
        // MAS COMO ESSA É SÓ UMA CLASSE PARA TESTAR A REQUISIÇÃO COLOCANDO A REQUISIÇÃO DENTRO DE UMA JSON E OBJETOS NÃO CORRIGI O ERRO
        // VERIFICAR UMA FORMA MAIS EFICAZ PARA FAZER ISSO POSTERIORMENTE, TALVES A QUE EU USEI NO PROJETO DAS ODDS MARTINGALE DE JOGOS
        ResponseEntity<PageableResponse<Student>> exchange = restTemplate.exchange("/?sort=id,desc&sort=name,asc", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Student>>() {}); //retornando a lista com o ResponseEntity, lista de objetos
        System.out.println("-----------------------------------------");
        System.out.println(exchange);
    }
}
