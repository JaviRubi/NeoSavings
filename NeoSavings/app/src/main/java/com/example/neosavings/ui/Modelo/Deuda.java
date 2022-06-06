package com.example.neosavings.ui.Modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import java.util.Date;

@Entity(indices = {@Index(value = {"DeudaID","UserID"},unique = true)},
        foreignKeys = @ForeignKey(entity = Usuario.class, childColumns = "UserID",parentColumns = "userID", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
@SuppressWarnings({RoomWarnings.INDEX_FROM_EMBEDDED_FIELD_IS_DROPPED, RoomWarnings.INDEX_FROM_EMBEDDED_ENTITY_IS_DROPPED})
public class Deuda {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "DeudaID",index = true)
    private Integer DeudaID;

    @ColumnInfo(name = "UserID",index = true)
    private long UserID;

    private long registroIDPrincipal;

    private String Nombre;

    private String Descripcion;

    private String NombreCuenta;

    private String CosteDeuda;

    private Date FechaInicio;

    private Date FechaVencimiento;

    private Date FechaNotificacion;

    private boolean isDeuda;

    private String Recordatorio;

    private boolean Notificado=false;

    public Date getFechaNotificacion() {
        return FechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        FechaNotificacion = fechaNotificacion;
    }

    public boolean isNotificado() {
        return Notificado;
    }

    public void setNotificado(boolean notificado) {
        Notificado = notificado;
    }

    public String getRecordatorio() {
        return Recordatorio;
    }

    public void setRecordatorio(String recordatorio) {
        Recordatorio = recordatorio;
    }

    public long getRegistroIDPrincipal() {
        return registroIDPrincipal;
    }

    public void setRegistroIDPrincipal(long registroIDPrincipal) {
        this.registroIDPrincipal = registroIDPrincipal;
    }

    public Integer getDeudaID() {
        return DeudaID;
    }

    public void setDeudaID(Integer deudaID) {
        DeudaID = deudaID;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
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

    public String getNombreCuenta() {
        return NombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        NombreCuenta = nombreCuenta;
    }

    public String getCosteDeuda() {
        return CosteDeuda;
    }

    public void setCosteDeuda(String costeDeuda) {
        CosteDeuda = costeDeuda;
    }

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return FechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        FechaVencimiento = fechaVencimiento;
    }

    public boolean isDeuda() {
        return isDeuda;
    }

    public void setDeuda(boolean deuda) {
        isDeuda = deuda;
    }
}
