package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.client.AuditoriaClient;
import com.notificaciones.microservicio_notificaciones.dto.AuditoriaRequestDTO;
import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.exception.EstadoNotFoundException;
import com.notificaciones.microservicio_notificaciones.exception.NotificacionNotFoundException;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.repository.EstadoRepository;
import com.notificaciones.microservicio_notificaciones.repository.NotificacionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final JavaMailSender mailSender;
    private final EstadoRepository estadoRepository;
    private final AuditoriaClient auditoriaClient;

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

    public NotificacionResponseDTO buscarPorId(Long id){
        Optional<Notificacion> notificacion = notificacionRepository.findById(id);

        if(notificacion.isPresent()){
            log.info("Notificacion con id {} encontrada con exito",id);
            return notificacion.map(this::mapToDTO).orElseThrow();
        }else{
            log.warn("Notificación con id {} no encontrada",id);
            throw new NotificacionNotFoundException("Notificación no encontrada con id: "+id);
        }
    }

    public String fechaFormateada(){
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return fecha.format(formatoFecha);
    }

    @Transactional
    public NotificacionResponseDTO generarNotificacion(){
        Notificacion noti = new Notificacion();

        noti.setTipo("Aviso de partida");
        noti.setMensaje("La partida va a comenzar en 5 minutos");
        noti.setFecha_envio(fechaFormateada());

        Estado estado =estadoRepository.findById(1L)
                .orElseThrow(()->new EstadoNotFoundException("Estado con id "+1L+" no encontrado"));
        noti.setEstado(estado);

        notificacionRepository.save(noti);
        log.info("Notificación generada con exito");
        registrarAuditoria("Notificación generada");
        return mapToDTO(noti);
    }

    @Transactional
    public void generarEmail(Long id, String correo){
        Notificacion noti = notificacionRepository.findById(id)
                .orElseThrow(()-> new NotificacionNotFoundException("Notificacion con id "+id+" no encontrada"));
        SimpleMailMessage mensaje =new SimpleMailMessage();

        mensaje.setTo(correo);
        mensaje.setSubject("Recordatorio de partida");
        mensaje.setText(noti.getMensaje()+"\nNotificación generada automaticamente");

        mailSender.send(mensaje);
        log.info("Email enviado con exito!");

        Estado estadoEnviado = estadoRepository.findById(2L)
                .orElseThrow(()->new EstadoNotFoundException("Estado con id "+2L+" no encontrado"));
        noti.setEstado(estadoEnviado);
        log.info("Estado cambiado con exito!");
        registrarAuditoria("Email de notificación enviado a correo: "+correo);
        notificacionRepository.save(noti);
    }

    @Transactional
    public void registrarAuditoria(String detalle){
        AuditoriaRequestDTO auditoria = new AuditoriaRequestDTO();
        LocalDate ahora = LocalDate.now();
        auditoria.setDetalle(detalle);
        auditoria.setFecha(ahora);

        auditoriaClient.generarAuditoria(auditoria);
        log.info("Auditoria generada con exito!");
    }



}