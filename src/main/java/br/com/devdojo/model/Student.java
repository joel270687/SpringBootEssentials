package br.com.devdojo.model;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Joel on 21/06/2022.
 */

@Entity
public class Student extends AbstractEntity{
    @NotEmpty(message = "O campo nome é obrigatório")
    private String name;
    @NotEmpty
    @Email(message = "Digite um email válido")
    private String email;

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Student(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }



    public Student() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
