package com.example.neosavings.ui.Modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"PagoProgramadoID","UserID"},unique = true)},foreignKeys = @ForeignKey(entity = Usuario.class, childColumns = "UserID",parentColumns = "userID", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class PagoProgramado {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true,name = "PagoProgramadoID")
    private Integer PagoProgramadoID;

    private String Nombre;

    private String Descripcion;

    private String Categoría;

    private String NombreUser;

    @ColumnInfo(index = true,name = "UserID")
    private long UserId;

    private String FormaPago;

    private String Coste;

    private boolean Recordatorio;

    private boolean Gasto;

    private String Periodicidad;

    private Date FechaInicio;

    private Date FechaFin;

    public boolean isGasto() {
        return Gasto;
    }

    public void setGasto(boolean gasto) {
        Gasto = gasto;
    }

    public String getCoste() {
        return Coste;
    }

    public void setCoste(String coste) {
        Coste = coste;
    }

    public boolean isRecordatorio() {
        return Recordatorio;
    }

    public void setRecordatorio(boolean recordatorio) {
        Recordatorio = recordatorio;
    }

    public Integer getPagoProgramadoID() {
        return PagoProgramadoID;
    }

    public void setPagoProgramadoID(Integer pagoProgramadoID) {
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
