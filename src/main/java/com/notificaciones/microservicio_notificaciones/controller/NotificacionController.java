package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificacion")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public List<NotificacionResponseDTO> listarNotificaciones(){
        return notificacionService.obtenerNotificaciones();
    }

    @GetMapping("/{id}")
    public Optional<NotificacionResponseDTO> buscarPorId(@PathVariable Long id){
        return notificacionService.buscarPorId(id);
    }

}
