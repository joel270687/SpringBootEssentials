package br.com.devdojo.javaclient;

import br.com.devdojo.handler.RestResponseExceptionHandler;
import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Joel on 08/07/2022.
 */
public class JavaClientDAO {
    private RestTemplate restTemplateUser = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/user/students")
            .basicAuthentication("joel","joel")
            .errorHandler(new RestResponseExceptionHandler()) //retorna o erro se retornar algum problema no transação com o BD
            .build();//faz uma template requisição para essa api "conforme url" passando os parametros
    private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/admin/students")
            .basicAuthentication("admin","admin")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    public Student findById(Long id){
        return restTemplateUser.getForObject("/{id}", Student.class,id); //retorna a requisição em um objeto absoluto
        //ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class,id); //retorna o ResponseEntity<Student> contendo status, header e o objeto
    }



    public List<Student> listAll() {
        //se quiser ordenação incluir essa url: "/?sort=id,desc&sort=name,asc"
        // NÃO FUNCIONOU E FALTOU IMPLEMENTAR UMA CLASSE: CustomSortDeserializer.java que estava depreciada
        // MAS COMO ESSA É SÓ UMA CLASSE PARA TESTAR A REQUISIÇÃO COLOCANDO A REQUISIÇÃO DENTRO DE UMA JSON E OBJETOS NÃO CORRIGI O ERRO
        // VERIFICAR UMA FORMA MAIS EFICAZ PARA FAZER ISSO POSTERIORMENTE, TALVES A QUE EU USEI NO PROJETO DAS ODDS MARTINGALE DE JOGOS
        //ResponseEntity<PageableResponse<Student>> exchange = restTemplateUser.exchange("/", HttpMethod.GET,
        //        null, new ParameterizedTypeReference<PageableResponse<Student>>() {}); //retornando a lista com o ResponseEntity, lista de objetos
        //return exchange.getBody().getContent();

        //para vários estudantes, usado SEM O PAGEABLE
        //RETORNANDO OS OBJETOS DIRETO
          //Student students[] = restTemplateUser.getForObject("/", Student[].class); //retorna a requisição em um array de objetos , ele não aceita Lista só array
        //Pararetonrar esse acima tem que converter o array em Lista

        //para vários estudantes, usado SEM O PAGEABLE
        //RETORNANDO O RESPONSE
        ResponseEntity<List<Student>> exchange = restTemplateUser.exchange("/", HttpMethod.GET,
                  null, new ParameterizedTypeReference<List<Student>>() {}); //retornando a lista com o ResponseEntity, lista de objetos
        return exchange.getBody();
    }

    public Student save(Student student){
        //POST por exchange... pega o objeto cria um JSON e envia
        ResponseEntity<Student> exchangePost = restTemplateAdmin.exchange("/", HttpMethod.POST,
                       new HttpEntity<>(student,createJSONHeader()), Student.class);
        return exchangePost.getBody();

        //POST por object
        //return restTemplateAdmin.postForObject("/",student,Student.class);

        //POST por entity, que vai retornar os valores de retorno da reuisição como status e etc
        //ResponseEntity<Student> studentPostFoEntity = restTemplateAdmin.postForEntity("/", student, Student.class);
        //return studentPostFoEntity.getBody();
    }

    public void update(Student student){
        restTemplateAdmin.put("/",student);
    }

    public void delete(Long id){
        restTemplateAdmin.delete("/{id}",id);
    }

    private static HttpHeaders createJSONHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
