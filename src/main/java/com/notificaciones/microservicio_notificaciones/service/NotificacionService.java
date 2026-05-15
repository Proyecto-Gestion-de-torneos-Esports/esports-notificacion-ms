package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.client.AuditoriaClient;
import com.notificaciones.microservicio_notificaciones.dto.AuditoriaRequestDTO;
import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.exception.NotificacionNotFoundException;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.repository.EstadoRepository;
import com.notificaciones.microservicio_notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            return notificacion.map(this::mapToDTO).orElseThrow();
        }else{
            throw new NotificacionNotFoundException("Notificación no encontrada con id: "+id);
        }
    }

    public String fechaFormateada(){
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return fecha.format(formatoFecha);
    }

    public Notificacion generarNotificacion(){
        Notificacion noti = new Notificacion();

        noti.setTipo("Aviso de partida");
        noti.setMensaje("La partida va a comenzar en 5 minutos");
        noti.setFecha_envio(fechaFormateada());

        Estado estado =estadoRepository.findById(1L).orElseThrow();
        noti.setEstado(estado);

        notificacionRepository.save(noti);
        log.info("Notificación generada con exito");
        registrarAuditoria("Notificación generada");
        return noti;
    }

    public void generarEmail(String correo){
        Notificacion noti = generarNotificacion();
        SimpleMailMessage mensaje =new SimpleMailMessage();

        mensaje.setTo(correo);
        mensaje.setSubject("Notificación Nro. "+noti.getIdNotificacion()+ " " +noti.getTipo());
        mensaje.setText(noti.getMensaje()+ "\nNotificación generada automaticamente");

        mailSender.send(mensaje);
        log.info("Email enviado con exito!");

        Estado estadoEnviado = estadoRepository.findById(2L).orElseThrow();
        noti.setEstado(estadoEnviado);
        registrarAuditoria("Email de notificación enviado");
        notificacionRepository.save(noti);
    }

    @Scheduled(fixedRate = 60000)
    public void enviarEmail(String correo, LocalDateTime hora){
        LocalDateTime ahora =LocalDateTime.now().withSecond(0).withNano(0);
        if(ahora.equals(hora.minusMinutes(5))){
            generarEmail(correo);
        }
    }

    public void registrarAuditoria(String detalle){
        AuditoriaRequestDTO auditoria = new AuditoriaRequestDTO();
        LocalDate ahora = LocalDate.now();
        auditoria.setDetalle(detalle);
        auditoria.setFecha(ahora);

        auditoriaClient.generarAuditoria(auditoria);
        log.info("Auditoria generada con exito!");
    }



}