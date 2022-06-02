package com.example.neosavings.ui.Modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class PagoProgramado {

    @PrimaryKey(autoGenerate = true)
    private long PagoProgramadoID;

    private String Nombre;

    private String Descripcion;

    private String Categoría;

    private String NombreUser;

    private long UserId;

    private String FormaPago;

    private String Recordatorio;

    private String Periodicidad;

    private Date FechaInicio;

    private Date FechaFin;

    public long getPagoProgramadoID() {
        return PagoProgramadoID;
    }

    public void setPagoProgramadoID(long pagoProgramadoID) {
        PagoProgramadoID = pagoProgramadoID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCategoría() {
        return Categoría;
    }

    public void setCategoría(String categoría) {
        Categoría = categoría;
    }

    public String getNombreUser() {
        return NombreUser;
    }

    public void setNombreUser(String nombreUser) {
        NombreUser = nombreUser;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(String formaPago) {
        FormaPago = formaPago;
    }

    public String getRecordatorio() {
        return Recordatorio;
    }

    public void setRecordatorio(String recordatorio) {
        Recordatorio = recordatorio;
    }

    public String getPeriodicidad() {
        return Periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        Periodicidad = periodicidad;
    }

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        FechaFin = fechaFin;
    }
}
