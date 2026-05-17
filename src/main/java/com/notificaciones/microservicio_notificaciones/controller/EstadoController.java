package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estado")
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    public ResponseEntity<List<Estado>> obtenerEstados(){
        return ResponseEntity.status(HttpStatus.OK).body(estadoService.obtenerEstados());
    }

    @PostMapping
    public ResponseEntity<Estado> crearEstado(@RequestBody Estado estado){
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.crearEstado(estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstado(@PathVariable Long id){
        estadoService.eliminarEstado(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
