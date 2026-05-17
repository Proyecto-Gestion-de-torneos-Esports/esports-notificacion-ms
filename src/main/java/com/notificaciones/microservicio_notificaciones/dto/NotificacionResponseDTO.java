package com.notificaciones.microservicio_notificaciones.dto;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponseDTO {

    private Long idNotificacion;
    private String tipo;
    private String mensaje;
    private String fechaEnvio;
    private String estado;


}
