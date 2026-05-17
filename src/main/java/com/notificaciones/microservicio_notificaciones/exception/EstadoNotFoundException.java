package com.notificaciones.microservicio_notificaciones.exception;

public class EstadoNotFoundException extends RuntimeException{
    public EstadoNotFoundException(String mensaje){
        super(mensaje);
    }
}
