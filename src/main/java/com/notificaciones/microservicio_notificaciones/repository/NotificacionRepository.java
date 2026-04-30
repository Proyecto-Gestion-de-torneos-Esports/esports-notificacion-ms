package com.notificaciones.microservicio_notificaciones.repository;


import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificacionRepository {

    private List<Notificacion> listaNotificaciones = new ArrayList<>();

    public List<Notificacion> listarNotificaciones(){
        return listaNotificaciones;
    }

    public Notificacion buscarPorId(Long id){
        for(Notificacion noti: listaNotificaciones){
            if(noti.getIdNotificacion().equals(id)){
                return noti;
            }
        }
        return null;
    }

    public Notificacion generarNotificacion(Notificacion noti){
        listaNotificaciones.add(noti);
        return noti;
    }


}
