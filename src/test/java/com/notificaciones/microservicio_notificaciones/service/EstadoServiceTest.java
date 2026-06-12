package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.repository.EstadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoService estadoService;

    private Estado estado;

    @BeforeEach
    void setUp(){
        estado = new Estado();
        estado.setIdEstado(1L);
        estado.setEstado("Pendiente");
    }

    @Test
    void testObtenerEstados(){
        when(estadoRepository.findAll()).thenReturn(List.of(estado));

        List<Estado> estados = estadoService.obtenerEstados();

        assertNotNull(estados);
        assertEquals(1, estados.size());
    }

    @Test
    void testCrearEstados(){
        when(estadoRepository.save(any(Estado.class))).thenReturn(estado);

        Estado estado1 = estadoService.crearEstado(estado);

        assertEquals(estado.getIdEstado(), estado1.getIdEstado());
        assertEquals(estado.getEstado(), estado1.getEstado());

        verify(estadoRepository).save(any(Estado.class));
    }

    @Test
    void eliminarEstado(){
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        estadoService.eliminarEstado(1L);

        verify(estadoRepository).findById(1L);
        verify(estadoRepository).deleteById(1L);
    }
}
