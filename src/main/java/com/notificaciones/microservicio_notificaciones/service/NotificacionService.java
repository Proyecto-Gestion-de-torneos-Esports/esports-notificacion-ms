package com.notificaciones.microservicio_notificaciones.service;

import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listarNotificaciones(){
        return notificacionRepository.listarNotificaciones();
    }

    public Notificacion generarNotificacion(Notificacion noti){
        return notificacionRepository.generarNotificacion(noti);
    }



}
