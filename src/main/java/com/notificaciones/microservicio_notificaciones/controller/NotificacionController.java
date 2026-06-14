package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.assembler.NotificacionModelAssembler;
import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/notificacion")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final NotificacionModelAssembler notificacionAssembler;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Obtener notificaciones", description = "Consulta de notificaciones disponibles")
    public ResponseEntity<?> listarNotificaciones(){
        List<NotificacionResponseDTO> juegos = notificacionService.obtenerNotificaciones()
                .stream().map(notificacionAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<NotificacionResponseDTO> collectionModel =  CollectionModel.of(juegos, linkTo(methodOn(NotificacionController.class).listarNotificaciones()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificacion por id", description = "Consulta de notificacion en especifico")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        NotificacionResponseDTO dto = notificacionService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(notificacionAssembler.toModel(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Generar notificacion", description = "Generar notificacion automatica")
    public ResponseEntity<?> generarNotificacion(){
        NotificacionResponseDTO dto = notificacionService.generarNotificacion();
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionAssembler.toModel(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generarCorreo")
    public ResponseEntity<?> generarCorreo(@RequestParam Long id, @RequestParam String correo){
        notificacionService.generarEmail(id, correo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
