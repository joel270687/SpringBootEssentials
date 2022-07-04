package br.com.devdojo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Joel on 04/07/2022.
 */
public class PasswordEncoder {//criado somente para criptografar a senha manualmente
    public static void main(String[] args) { //atalho psvm
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin"));
    }
}
