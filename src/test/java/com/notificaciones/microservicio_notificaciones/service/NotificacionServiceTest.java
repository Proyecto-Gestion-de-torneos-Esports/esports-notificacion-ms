package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.client.AuditoriaClient;
import com.notificaciones.microservicio_notificaciones.dto.AuditoriaRequestDTO;
import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.repository.EstadoRepository;
import com.notificaciones.microservicio_notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private AuditoriaClient auditoriaClient;

    @InjectMocks
    private NotificacionService notificacionService;

    private Estado estado;
    private Notificacion notificacion;

    @BeforeEach
    void setUp(){
        estado = new Estado();
        estado.setIdEstado(1L);
        estado.setEstado("Pendiente");

        notificacion = new Notificacion();
        notificacion.setIdNotificacion(1L);
        notificacion.setMensaje("La partida va a comenzar en 5 minutos");
        notificacion.setTipo("Aviso de partida");
        notificacion.setFecha_envio(notificacionService.fechaFormateada());
        notificacion.setEstado(estado);
    }

    @Test
    void testObtenerNotificaciones(){
        when(notificacionRepository.findAll()).thenReturn(List.of(notificacion));

        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerNotificaciones();

        assertNotNull(notificaciones);
        assertEquals(1,notificaciones.size());
        assertEquals(estado.getEstado(), notificaciones.get(0).getEstado());
    }

    @Test
    void testBuscarPorId(){
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        NotificacionResponseDTO dto = notificacionService.buscarPorId(1L);

        assertEquals(notificacion.getIdNotificacion(), dto.getIdNotificacion());
        assertEquals(notificacion.getMensaje(), dto.getMensaje());
        assertEquals(notificacion.getTipo(), dto.getTipo());
        assertEquals(notificacion.getFecha_envio(), dto.getFechaEnvio());
        assertEquals(notificacion.getEstado().getEstado(), dto.getEstado());

        verify(notificacionRepository).findById(1L);
    }

    @Test
    void testGenerarNotificacion(){
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        NotificacionResponseDTO dto = notificacionService.generarNotificacion();

        assertEquals(notificacion.getMensaje(), dto.getMensaje());
        assertEquals(notificacion.getTipo(), dto.getTipo());
        assertEquals(notificacion.getFecha_envio(), dto.getFechaEnvio());
        assertEquals(notificacion.getEstado().getEstado(), dto.getEstado());

        verify(estadoRepository).findById(1L);
        verify(notificacionRepository).save(any(Notificacion.class));
        verify(auditoriaClient).generarAuditoria(any(AuditoriaRequestDTO.class));
    }

    void testGenerarEmail() {

        Estado enviado = new Estado();
        enviado.setIdEstado(2L);
        enviado.setEstado("Enviado");

        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        when(estadoRepository.findById(2L)).thenReturn(Optional.of(enviado));

        notificacionService.generarEmail(1L, "correo@test.com");

        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(notificacionRepository).save(notificacion);
        verify(auditoriaClient).generarAuditoria(any(AuditoriaRequestDTO.class));

        assertEquals("Enviado", notificacion.getEstado().getEstado());
    }
}
