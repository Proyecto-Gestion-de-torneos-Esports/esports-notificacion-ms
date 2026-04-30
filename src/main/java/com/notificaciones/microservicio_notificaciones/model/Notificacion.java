package com.notificaciones.microservicio_notificaciones.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {

    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id tiene que ser mayor a 0")
    private Long idNotificacion;

    @NotBlank(message = "El tipo no puede ser vacio ni nulo")
    private String tipo;

    @NotBlank(message = "El mensaje no puede ser vacio ni nulo")
    private String mensaje;

    @NotBlank(message = "El estado no puede ser vacio ni nulo")
    private String estado;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha_envio;

}
