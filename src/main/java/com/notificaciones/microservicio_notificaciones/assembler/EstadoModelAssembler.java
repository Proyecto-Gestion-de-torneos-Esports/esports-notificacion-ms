package com.notificaciones.microservicio_notificaciones.assembler;

import com.notificaciones.microservicio_notificaciones.controller.EstadoController;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstadoModelAssembler implements RepresentationModelAssembler<Estado, EntityModel<Estado>> {

    @Override
    public EntityModel<Estado> toModel(Estado estado){
        return EntityModel.of(estado,
                linkTo(methodOn(EstadoController.class).obtenerEstados()).withRel("plataforma"));
    }
}
