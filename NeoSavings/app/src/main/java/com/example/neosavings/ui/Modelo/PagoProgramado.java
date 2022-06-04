package com.example.neosavings.ui.Modelo;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.neosavings.ui.Database.UsuarioRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public void crearRegistrosIniciales(Context context) throws ParseException {
        Calendar calendar= (Calendar) Calendar.getInstance().clone();
        Date FechaActual=calendar.getTime();
        Date FechaCrear= (Date) FechaInicio.clone();
        String aux=new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        FechaActual=new SimpleDateFormat("dd/MM/yyyy").parse(aux);
        calendar.setTime(FechaInicio);
        UsuarioRepository mRepository=new UsuarioRepository(context);



        if(FechaInicio.getTime()<=FechaCrear.getTime()){
            while(FechaCrear.getTime()<=FechaActual.getTime() && FechaCrear.getTime()<=FechaFin.getTime()){
                Registro r=new Registro();
                r.setFormaPago(this.FormaPago);
                r.setCoste(this.Coste);
                r.setDescripcion(this.Descripcion);
                r.setGasto(this.Gasto);
                r.setCategoria(this.getCategoría());
                r.setFecha(FechaCrear);
                r.setPagoProgramadoID(this.PagoProgramadoID);
                r.setRegistroUserID(this.UserId);

                mRepository.insertRegistro(r);

                if(Periodicidad.equals("DIARIAMENTE")){
                    calendar.add(Calendar.DAY_OF_YEAR,1);
                }else if(Periodicidad.equals("SEMANALMENTE")){
                    calendar.add(Calendar.DAY_OF_YEAR,7);
                }else if(Periodicidad.equals("MENSUALMENTE")) {
                    calendar.add(Calendar.MONTH, 1);
                }else if(Periodicidad.equals("TRIMESTRALMENTE")) {
                    calendar.add(Calendar.MONTH, 3);
                }else if(Periodicidad.equals("SEMESTRALMENTE")){
                    calendar.add(Calendar.MONTH, 6);
                }else{
                    calendar.add(Calendar.YEAR,1);
                }
                FechaCrear=calendar.getTime();
                aux=new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
                FechaCrear=new SimpleDateFormat("dd/MM/yyyy").parse(aux);
            }

        }
    }
}
