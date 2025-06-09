package com.Stefan.BibliotecaUnical.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/")
    public String greet(HttpServletRequest request)
    {
        return "Benvenuto nella Biblioteca Unical!\n" + request.getSession().getId();
    }
}
