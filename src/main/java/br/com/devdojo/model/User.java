package br.com.devdojo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Joel on 04/07/2022.
 */
@Entity
public class User extends AbstractEntity{
    @NotEmpty//não vázio
    @Column(unique = true)//valor único
    private String username;
    @NotEmpty
    @JsonIgnore//não mostra nas requisições, para não ficar mostrando o password
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private boolean admin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
