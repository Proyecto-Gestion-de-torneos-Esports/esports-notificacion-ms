package com.notificaciones.microservicio_notificaciones.controller;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.service.EstadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstadoController.class)
public class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadoService estadoService;

    private Estado estado;

    @BeforeEach
    void setUp(){
        estado = new Estado();
        estado.setIdEstado(1L);
        estado.setEstado("Pendiente");
    }

    @Test
    void testObtenerTodos() throws Exception{
        when(estadoService.obtenerEstados()).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstado").value(1L))
                .andExpect(jsonPath("$[0].estado").value("Pendiente"));

        List<Estado> estados = estadoService.obtenerEstados();

        assertNotNull(estados);
        assertEquals(1, estados.size());
    }

    @Test
    void testCrearEstado() throws Exception{
        when(estadoService.crearEstado(any(Estado.class))).thenReturn(estado);

        mockMvc.perform(post("/api/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "estado":"Pendiente"
                        }
                        """))
                .andExpect(status().isCreated());

        verify(estadoService).crearEstado(any(Estado.class));
    }

    @Test
    void testEliminarEstado() throws Exception{
        doNothing().when(estadoService).eliminarEstado(1L);

        mockMvc.perform(delete("/api/estado/1"))
                .andExpect(status().isNoContent());

        verify(estadoService).eliminarEstado(1L);
    }
}
