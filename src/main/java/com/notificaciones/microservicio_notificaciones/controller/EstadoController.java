package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.assembler.EstadoModelAssembler;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estado")
@Tag(name = "Estado", description = "Operaciones relacionadas con el estado de la notificacion")
public class EstadoController {

    private final EstadoService estadoService;
    private final EstadoModelAssembler estadoAssembler;

    @GetMapping
    @Operation(summary = "Obtener estados", description = "Consulta de estados")
    public ResponseEntity<?> obtenerEstados(){
        List<EntityModel<Estado>> estados = estadoService.obtenerEstados()
                .stream().map(estadoAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Estado>> collectionModel =  CollectionModel.of(estados, linkTo(methodOn(EstadoController.class).obtenerEstados()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PostMapping
    @Operation(summary = "Crear estado", description = "Creación de estado")
    public ResponseEntity<?> crearEstado(@RequestBody Estado estado){
        Estado estado1 = estadoService.crearEstado(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoAssembler.toModel(estado1));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estado", description = "Eliminacion de estados")
    public ResponseEntity<?> eliminarEstado(@PathVariable Long id){
        estadoService.eliminarEstado(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
