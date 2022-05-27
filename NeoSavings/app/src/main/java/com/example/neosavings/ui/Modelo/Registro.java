package com.example.neosavings.ui.Modelo;


import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;


@Entity(indices = {@Index(value = {"RegistroID","RegistroUserID"},unique = true)},foreignKeys = @ForeignKey(entity = Usuario.class, childColumns = "RegistroUserID",parentColumns = "userID", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class Registro {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true,name = "RegistroID")
    public long RegistroID;

    @ColumnInfo(index = true,name = "RegistroUserID")
    public long RegistroUserID;

    public boolean Gasto;

    public String Descripcion;

    public String Coste="0";

    public Date Fecha= Calendar.getInstance().getTime();

    public String Categoria;

    public String FormaPago;

    public String Ubicacion;


@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public Bitmap Ticket;

    public long getRegistroID() {
        return RegistroID;
    }

    public void setRegistroID(long registroID) {
        RegistroID = registroID;
    }

    public long getRegistroUserID() {
        return RegistroUserID;
    }

    public void setRegistroUserID(long registroUserID) {
        RegistroUserID = registroUserID;
    }

    public boolean isGasto() {
        return Gasto;
    }

    public void setGasto(boolean gasto) {
        Gasto = gasto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCoste() {
        return Coste;
    }

    public void setCoste(String coste) {
        Coste = coste;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(String formaPago) {
        FormaPago = formaPago;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public Bitmap getTicket() {
        return Ticket;
    }

    public void setTicket(Bitmap ticket) {
        Ticket = ticket;
    }


}
