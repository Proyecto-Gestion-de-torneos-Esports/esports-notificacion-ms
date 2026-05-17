package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.exception.EstadoNotFoundException;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.repository.EstadoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public List<Estado> obtenerEstados(){
        return estadoRepository.findAll();
    }

    @Transactional
    public Estado crearEstado(Estado estado){
        log.info("Estado creado con exito!");
        return estadoRepository.save(estado);
    }

    @Transactional
    public void eliminarEstado(Long id){
        Optional<Estado> estado = estadoRepository.findById(id);
        if(estado.isPresent()){
            log.info("Estado con id {} encontrado y eliminado con exito",id);
            estadoRepository.deleteById(id);
        }
        log.warn("Error al eliminar, estado con id {} no encontrado",id);
        throw new EstadoNotFoundException("Estado con id "+id+" no encontrado");
    }

}
