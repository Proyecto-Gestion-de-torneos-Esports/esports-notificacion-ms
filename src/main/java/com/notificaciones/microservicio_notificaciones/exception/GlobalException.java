package com.notificaciones.microservicio_notificaciones.exception;


import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(EstadoNotFoundException.class)
    public ResponseEntity<?> manejoEstadoNoEncontrado(EstadoNotFoundException e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado",400);
        error.put("mensaje",e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NotificacionNotFoundException.class)
    public ResponseEntity<?> manejoNoEncontrado(NotificacionNotFoundException e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado",400);
        error.put("mensaje", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FeignException.FeignClientException.class)
    public ResponseEntity<?> manejoFeign(FeignException e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado", 503);
        error.put("mensaje", e.getMessage());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejoGeneral(Exception e){
        HashMap<String, Object> error = new HashMap<>();
        error.put("estado", 500);
        error.put("mensaje", "Error interno en el servidor");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
