package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public List<Estado> obtenerEstados(){
        return estadoRepository.findAll();
    }

    public Estado crearEstado(Estado estado){
        return estadoRepository.save(estado);
    }

    public void eliminarEstado(Long id){
        estadoRepository.deleteById(id);
    }

}
