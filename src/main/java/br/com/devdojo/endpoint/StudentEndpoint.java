package br.com.devdojo.endpoint;

import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by Joel on 21/06/2022.
 */

@RestController //diferença pela @Controller é que ele já adiciona o @ResponseBody para todos os métodos
                //@ResponseBody converte a requisição em Json e manda no corpo da requisição
// o @Controller foi criado para o padão MVC retornando uma view
@RequestMapping("v1")
public class StudentEndpoint {
    private final StudentRepository studentDAO;
    @Autowired //quando instanciar a classe StudentEndponit essa anotação já vai criar o studentDAO com as dependencias para serem usados
    public StudentEndpoint(StudentRepository studentDAO) {
        this.studentDAO = studentDAO;
    }

    //@RequestMapping(method = RequestMethod.GET) //path = "/list" //o path só usa se tiver mais de um metodo GET
    @GetMapping(path = "user/students") //essa anotação substitui o de cima
    public ResponseEntity<?> listAll(Pageable pageable){ //só de colocar esse Pageable aqui e no return aí na requisição do postman adicionar => students?page=0&size=3
        //pra usar o sort é só colocar na requisição => students?sort=name,desc&sort=email,desc
        //System.out.println("-----acessou a student/list ------->>>"+dataUtil.formaLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        //return new ResponseEntity<>(studentDAO.findAll(pageable), HttpStatus.OK); //dessa maneira retorna a requisição por páginas
        return new ResponseEntity<>(studentDAO.findAll(), HttpStatus.OK);//tirando o pageable retorna todos os estudantes
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @GetMapping(path = "user/students/{id}") //essa anotação substitui o de cima
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
                                                                        //essa anotação é só se caso nesse mêtodo precise utilizar os dados do usuario
                                                                        //passados na requisição, usuarios lá da SecurityConfig.java
        verifyIfStudentExist(id);
        Optional<Student> student = studentDAO.findById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "user/students/findByName/{name}")
    public ResponseEntity<?> findStudentByName (@PathVariable String name){
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.POST)
    @PostMapping(path = "admin/students") //essa anotação substitui o de cima
    @Transactional(rollbackFor = Exception.class)//informa que esse metodo é do tipo transacional. retornos Rollback do banco, incluir duas vezes por exemplo
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.CREATED);
    }

    //@RequestMapping(method = RequestMethod.DELETE) //dar olhada no conceito idempotent
    @DeleteMapping(path = "admin/students/{id}") //essa anotação substitui o de cima
    //@PreAuthorize("hasRole('ADMIN')") //pra deletar precisa ser um usuario setado na securityConfig.java como role=ADMIN
                                        // mas já foi atualizado colocando .antMatchers e colocando os prefixos nas URLs
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfStudentExist(id);
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.PUT)
    @PutMapping(path = "admin/students")  //essa anotação substitui o de cima
    public ResponseEntity<?> update(@RequestBody Student student) {
        verifyIfStudentExist(student.getId());
        studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyIfStudentExist (Long id){
        Optional<Student> student = studentDAO.findById(id);
        if(!student.isPresent())
            throw new ResourceNotFoundException("Student not found for ID: "+ id);
    }
}