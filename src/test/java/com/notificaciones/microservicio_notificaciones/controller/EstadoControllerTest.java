package com.notificaciones.microservicio_notificaciones.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificaciones.microservicio_notificaciones.assembler.EstadoModelAssembler;
import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.service.EstadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(EstadoController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "ADMIN")
@Import(EstadoModelAssembler.class)
public class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EstadoService estadoService;

    private Estado estado;

    @BeforeEach
    void setUp() {
        estado = new Estado();
        estado.setIdEstado(1L);
        estado.setEstado("Pendiente");
    }

    @Test
    void testObtenerTodos() throws Exception {
        when(estadoService.obtenerEstados()).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/estado")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists()) // Validación de colección HATEOAS
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(estadoService).obtenerEstados();
    }

    @Test
    void testCrearEstado() throws Exception {
        when(estadoService.crearEstado(any(Estado.class))).thenReturn(estado);

        Estado estadoNuevo = new Estado();
        estadoNuevo.setEstado("Pendiente");

        mockMvc.perform(post("/api/estado")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoNuevo))
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEstado").value(1L))
                .andExpect(jsonPath("$.estado").value("Pendiente"))
                .andExpect(jsonPath("$._links.plataforma.href").exists());

        verify(estadoService).crearEstado(any(Estado.class));
    }
    @Test
    void testEliminarEstado() throws Exception {
        doNothing().when(estadoService).eliminarEstado(1L);

        mockMvc.perform(delete("/api/estado/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(estadoService, times(1)).eliminarEstado(1L);
    }
}