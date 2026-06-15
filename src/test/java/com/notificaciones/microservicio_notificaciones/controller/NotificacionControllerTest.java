package com.notificaciones.microservicio_notificaciones.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificaciones.microservicio_notificaciones.assembler.NotificacionModelAssembler;
import com.notificaciones.microservicio_notificaciones.dto.NotificacionResponseDTO;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.service.EstadoService;
import com.notificaciones.microservicio_notificaciones.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(NotificacionController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "ADMIN")
@Import(NotificacionModelAssembler.class)
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificacionService notificacionService;

    @MockBean
    private EstadoService estadoService;

    @MockBean
    private JavaMailSender mailSender;

    private NotificacionResponseDTO notificacion;
    private Estado estado;
    private final String FECHA_PRUEBA = "15-06-2026";

    @BeforeEach
    void setUp() {
        estado = new Estado();
        estado.setIdEstado(1L);
        estado.setEstado("Pendiente");

        notificacion = new NotificacionResponseDTO();
        notificacion.setIdNotificacion(1L);
        notificacion.setMensaje("La partida va a comenzar en 5 minutos");
        notificacion.setTipo("Aviso de partida");
        notificacion.setFechaEnvio(FECHA_PRUEBA);
        notificacion.setEstado(estado.getEstado());
    }

    @Test
    void testObtenerTodos() throws Exception {
        when(notificacionService.obtenerNotificaciones()).thenReturn(List.of(notificacion));

        mockMvc.perform(get("/api/notificacion")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists()) // HATEOAS Colección
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.notificacionResponseDTOList[0].idNotificacion").value(1L))
                .andExpect(jsonPath("$._embedded.notificacionResponseDTOList[0].mensaje").value("La partida va a comenzar en 5 minutos"))
                .andExpect(jsonPath("$._embedded.notificacionResponseDTOList[0].tipo").value("Aviso de partida"))
                .andExpect(jsonPath("$._embedded.notificacionResponseDTOList[0].fechaEnvio").value(FECHA_PRUEBA))
                .andExpect(jsonPath("$._embedded.notificacionResponseDTOList[0].estado").value(estado.getEstado()));

        verify(notificacionService).obtenerNotificaciones();
    }

    @Test
    void testBuscarPorId() throws Exception {
        when(notificacionService.buscarPorId(1L)).thenReturn(notificacion);

        mockMvc.perform(get("/api/notificacion/1")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNotificacion").value(1L))
                .andExpect(jsonPath("$.mensaje").value("La partida va a comenzar en 5 minutos"))
                .andExpect(jsonPath("$.tipo").value("Aviso de partida"))
                .andExpect(jsonPath("$.fechaEnvio").value(FECHA_PRUEBA))
                .andExpect(jsonPath("$.estado").value(estado.getEstado()))
                .andExpect(jsonPath("$._links.self.href").exists()); // HATEOAS Link

        verify(notificacionService).buscarPorId(1L);
    }

    @Test
    void generarNotificacion() throws Exception {
        when(notificacionService.generarNotificacion()).thenReturn(notificacion);

        mockMvc.perform(post("/api/notificacion")
                        .with(csrf()) // Seguridad CSRF para POST
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idNotificacion").value(1L))
                .andExpect(jsonPath("$.tipo").value("Aviso de partida"))
                .andExpect(jsonPath("$.mensaje").value("La partida va a comenzar en 5 minutos"))
                .andExpect(jsonPath("$.fechaEnvio").value(FECHA_PRUEBA))
                .andExpect(jsonPath("$.estado").value(estado.getEstado()))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(notificacionService).generarNotificacion();
    }

    @Test
    void testGenerarCorreo() throws Exception {
        doNothing().when(notificacionService).generarEmail(1L, "correo@gmail.com");

        mockMvc.perform(post("/api/notificacion/generarCorreo")
                        .with(csrf())
                        .param("id", "1")
                        .param("correo", "correo@gmail.com"))
                .andExpect(status().isCreated());

        verify(notificacionService).generarEmail(1L, "correo@gmail.com");
    }
}