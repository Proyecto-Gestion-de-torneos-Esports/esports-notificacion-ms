package com.notificaciones.microservicio_notificaciones.assembler;

import com.notificaciones.microservicio_notificaciones.controller.NotificacionController;
import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NotificacionModelAssembler implements RepresentationModelAssembler<NotificacionResponseDTO, NotificacionResponseDTO> {

    @Override
    public NotificacionResponseDTO toModel(NotificacionResponseDTO notificacion){

        notificacion.add(linkTo(methodOn(NotificacionController.class).listarNotificaciones()).withRel("juegos"));
        notificacion.add(linkTo(methodOn(NotificacionController.class).buscarPorId(notificacion.getIdNotificacion())).withSelfRel());
        return notificacion;
    }
}
