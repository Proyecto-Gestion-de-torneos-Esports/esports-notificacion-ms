package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.service.EstadoService;
import com.notificaciones.microservicio_notificaciones.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacionController.class)
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionService notificacionService;

    @MockitoBean
    private EstadoService estadoService;

    @MockitoBean
    private JavaMailSender mailSender;

    private NotificacionResponseDTO notificacion;
    private Estado estado;

    @BeforeEach
    void setUp(){
        estado = new Estado();
        estado.setIdEstado(1L);
        estado.setEstado("Pendiente");

        notificacion = new NotificacionResponseDTO();
        notificacion.setIdNotificacion(1L);
        notificacion.setMensaje("La partida va a comenzar en 5 minutos");
        notificacion.setTipo("Aviso de partida");
        notificacion.setFechaEnvio(notificacionService.fechaFormateada());
        notificacion.setEstado(estado.getEstado());
    }

    @Test
    void testObtenerTodos() throws Exception{
        when(notificacionService.obtenerNotificaciones()).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/api/notificacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNotificacion").value(1L))
                .andExpect(jsonPath("$[0].mensaje").value("La partida va a comenzar en 5 minutos"))
                .andExpect(jsonPath("$[0].tipo").value("Aviso de partida"))
                .andExpect(jsonPath("$[0].fechaEnvio").value(notificacionService.fechaFormateada()))
                .andExpect(jsonPath("$[0].estado").value(estado.getEstado()));

        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerNotificaciones();

        assertNotNull(notificaciones);
        assertEquals(1, notificaciones.size());
    }

    @Test
    void testBuscarPorId() throws Exception{
        when(notificacionService.buscarPorId(1L)).thenReturn(notificacion);

        mockMvc.perform(get("/api/notificacion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNotificacion").value(1L))
                .andExpect(jsonPath("$.mensaje").value("La partida va a comenzar en 5 minutos"))
                .andExpect(jsonPath("$.tipo").value("Aviso de partida"))
                .andExpect(jsonPath("$.fechaEnvio").value(notificacionService.fechaFormateada()))
                .andExpect(jsonPath("$.estado").value(estado.getEstado()));

            verify(notificacionService).buscarPorId(1L);
    }

    @Test
    void generarNotificacion() throws Exception{
        when(notificacionService.generarNotificacion()).thenReturn(notificacion);

        mockMvc.perform(post("/api/notificacion"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idNotificacion").value(1L))
                .andExpect(jsonPath("$.tipo").value("Aviso de partida"))
                .andExpect(jsonPath("$.mensaje").value("La partida va a comenzar en 5 minutos"))
                .andExpect(jsonPath("$.fechaEnvio").value(notificacionService.fechaFormateada()))
                .andExpect(jsonPath("$.estado").value(estado.getEstado()));

        verify(notificacionService).generarNotificacion();
    }

    @Test
    void testGenerarCorreo() throws Exception{
        doNothing().when(notificacionService).generarEmail(1L, "correo@gmail.com");

        mockMvc.perform(post("/api//notificacion/generarCorreo")
                        .param("id","1")
                        .param("correo", "correo@gmail.com"))
                .andExpect(status().isCreated());

        verify(notificacionService).generarEmail(1L, "correo@gmail.com");
    }
}
