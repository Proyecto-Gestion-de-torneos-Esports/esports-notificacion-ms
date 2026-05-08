package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estado")
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    public List<Estado> obtenerEstados(){
        return estadoService.obtenerEstados();
    }

    @PostMapping
    public Estado crearEstado(@RequestBody Estado estado){
        return estadoService.crearEstado(estado);
    }

    @DeleteMapping("/{id}")
    public void eliminarEstado(@PathVariable Long id){
        estadoService.eliminarEstado(id);
    }

}
