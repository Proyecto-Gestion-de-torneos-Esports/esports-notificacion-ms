package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notificacion")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listarNotificaciones(){
        return ResponseEntity.status(HttpStatus.OK).body(notificacionService.obtenerNotificaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(notificacionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> generarNotificacion(){
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionService.generarNotificacion());
    }

    @PostMapping("/generarCorreo")
    public ResponseEntity<?> generarCorreo(@RequestParam Long id, @RequestParam String correo){
        notificacionService.generarEmail(id, correo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
