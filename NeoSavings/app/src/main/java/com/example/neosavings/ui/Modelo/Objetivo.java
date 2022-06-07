package com.example.neosavings.ui.Modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Objetivo {

    @PrimaryKey(autoGenerate = true)
    private long ObjetivoID;

    private String Nombre;

    private String CantidadObjetivo;

    private String Ahorrado;

    private Date FechaDeseada;

    private String Descripción;

    private String Estado;

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public void setEstadoPausado() {
        Estado = "PAUSADO";
    }

    public void setEstadoActivo() {
        Estado = "ACTIVO";
    }

    public void setEstadoConcluido(){
        Estado = "CONCLUIDO";
    }

    public long getObjetivoID() {
        return ObjetivoID;
    }

    public void setObjetivoID(long objetivoID) {
        ObjetivoID = objetivoID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCantidadObjetivo() {
        return CantidadObjetivo;
    }

    public void setCantidadObjetivo(String cantidadObjetivo) {
        CantidadObjetivo = cantidadObjetivo;
    }

    public String getAhorrado() {
        return Ahorrado;
    }

    public void setAhorrado(String ahorrado) {
        Ahorrado = ahorrado;
    }

    public Date getFechaDeseada() {
        return FechaDeseada;
    }

    public void setFechaDeseada(Date fechaDeseada) {
        FechaDeseada = fechaDeseada;
    }

    public String getDescripción() {
        return Descripción;
    }

    public void setDescripción(String descripción) {
        Descripción = descripción;
    }
}
