package com.notificaciones.microservicio_notificaciones.exception;


public class NotificacionNotFoundException extends RuntimeException{

    public NotificacionNotFoundException(String mensaje){
        super(mensaje);
    }
}
