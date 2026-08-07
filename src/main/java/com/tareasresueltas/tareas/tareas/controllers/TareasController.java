package com.tareasresueltas.tareas.tareas.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.tareasresueltas.tareas.tareas.services.TareasService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("api/v1")
public class TareasController {
    private final TareasService tareasService;

    public TareasController(TareasService tareasService) {
        this.tareasService = tareasService;
    }

    @GetMapping()
    public String listTarea() {
        this.tareasService.tareas();
        return "Hola a todo";
    }
    
}
