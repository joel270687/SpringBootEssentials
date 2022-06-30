package br.com.devdojo.endpoint;

import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("students")
public class StudentEndpoint {
    private final StudentRepository studentDAO;
    @Autowired //quando instanciar a classe StudentEndponit essa anotação já vai criar o studentDAO com as dependencias para serem usados
    public StudentEndpoint(StudentRepository studentDAO) {
        this.studentDAO = studentDAO;
    }

    //@RequestMapping(method = RequestMethod.GET) //path = "/list" //o path só usa se tiver mais de um metodo GET
    @GetMapping //essa anotação substitui o de cima
    public ResponseEntity<?> listAll(){
        //System.out.println("-----acessou a student/list ------->>>"+dataUtil.formaLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(studentDAO.findAll(), HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @GetMapping(path = "/{id}") //essa anotação substitui o de cima
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id){
        verifyIfStudentExist(id);
        Optional<Student> student = studentDAO.findById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudentByName (@PathVariable String name){
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.POST)
    @PostMapping //essa anotação substitui o de cima
    @Transactional(rollbackFor = Exception.class)//informa que esse metodo é do tipo transacional. retornos Rollback do banco, incluir duas vezes por exemplo
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.CREATED);
    }

    //@RequestMapping(method = RequestMethod.DELETE) //dar olhada no conceito idempotent
    @DeleteMapping(path = "/{id}") //essa anotação substitui o de cima
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfStudentExist(id);
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.PUT)
    @PutMapping //essa anotação substitui o de cima
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