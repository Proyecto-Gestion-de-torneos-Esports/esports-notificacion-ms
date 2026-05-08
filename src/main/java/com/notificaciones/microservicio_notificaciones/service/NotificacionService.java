package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    private NotificacionResponseDTO mapToDTO(Notificacion noti){
        return new NotificacionResponseDTO(
                noti.getIdNotificacion(),
                noti.getTipo(),
                noti.getMensaje(),
                noti.getFecha_envio(),
                noti.getEstado().getEstado()
        );
    }

    public List<NotificacionResponseDTO> obtenerNotificaciones(){
        return notificacionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotificacionResponseDTO> buscarPorId(Long id){
        return notificacionRepository.findById(id).map(this::mapToDTO);
    }

    public void generarNotificacion(){
        Notificacion noti = new Notificacion();

        noti.setIdNotificacion(null);
        noti.setTipo("Aviso de partida");
        noti.setMensaje("La partida va a comenzar en 5 minutos");
        noti.setFecha_envio(LocalDate.now());
        noti.setEstado(new Estado(1L, "Pendiente"));

        notificacionRepository.save(noti);
    }


}