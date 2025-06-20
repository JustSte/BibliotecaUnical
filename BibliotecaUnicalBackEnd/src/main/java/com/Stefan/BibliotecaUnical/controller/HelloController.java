package com.Stefan.BibliotecaUnical.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @PreAuthorize("hasAnyRole('USER' ,'STAFF', 'ADMIN')")
    @GetMapping("/")
    public String greet(HttpServletRequest request)
    {
        return "Benvenuto nella Biblioteca Unical!\n" + request.getSession().getId();
    }

    @PreAuthorize("hasAnyRole('USER' ,'STAFF', 'ADMIN')")
    @GetMapping("/helloUser")
    public String helloUser()
    {
        return "helloUser";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/helloAdmin")
    public String helloAdmin()
    {
        return "helloAdmin";
    }
}
