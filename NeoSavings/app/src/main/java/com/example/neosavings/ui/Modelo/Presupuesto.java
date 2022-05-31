package com.example.neosavings.ui.Modelo;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.neosavings.ui.Database.UsuarioRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

@Entity
public class Presupuesto {

    @PrimaryKey(autoGenerate = true)
    private long PresupuestoID;

    private long UserID;

    private String UserName;

    private String Categoria;

    private String name;

    private String Presupuesto;

    private Date FechaInicio;

    private Date FechaFin;

    public long getPresupuestoID() {
        return PresupuestoID;
    }

    public void setPresupuestoID(long presupuestoID) {
        PresupuestoID = presupuestoID;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresupuesto() {
        return Presupuesto;
    }

    public void setPresupuesto(String presupuesto) {
        Presupuesto = presupuesto;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Double presupuestoHastaAhora(Context context){
        UsuarioRepository mRepository=new UsuarioRepository(context);
        Flowable<List<Registro>> allRegistrosbyFecha = mRepository.getAllRegistrosbyFecha(this.FechaInicio, this.FechaFin);
        List<Registro> registros=allRegistrosbyFecha.blockingFirst();

        if(registros==null){
            registros=new ArrayList<>();
        }

        if((!(Categoria.equals("TODAS")) && this.UserID>0)) {
            List<Registro> AuxUsuario = new ArrayList<>();
            AuxUsuario.addAll(registros);
            registros.clear();
            for (Registro r : AuxUsuario) {
                if ((r.isGasto() && r.getCategoria().equals(this.Categoria) && r.getRegistroUserID() == this.getUserID())) {
                    registros.add(r);
                }
            }

            Double PresupuestoFinal = Double.valueOf(0);

            for (Registro r : registros) {
                PresupuestoFinal += Double.valueOf(r.getCoste());
            }
            return PresupuestoFinal;
        }

        if(!Categoria.equals("TODAS") && UserID<0){

            List<Registro> AuxUsuario = new ArrayList<>();
            AuxUsuario.addAll(registros);
            registros.clear();
            for (Registro r : AuxUsuario) {
                if ((r.isGasto() && r.getCategoria().equals(this.Categoria))) {
                    registros.add(r);
                }
            }

            Double PresupuestoFinal = Double.valueOf(0);

            for (Registro r : registros) {
                PresupuestoFinal += Double.valueOf(r.getCoste());
            }
            return PresupuestoFinal;

        }

        if(Categoria.equals("TODAS") && UserID>0){

            List<Registro> AuxUsuario = new ArrayList<>();
            AuxUsuario.addAll(registros);
            registros.clear();
            for (Registro r : AuxUsuario) {
                if ((r.isGasto() && r.getRegistroUserID()==UserID)) {
                    registros.add(r);
                }
            }

            Double PresupuestoFinal = Double.valueOf(0);

            for (Registro r : registros) {
                PresupuestoFinal += Double.valueOf(r.getCoste());
            }
            return PresupuestoFinal;

        }

        Double PresupuestoFinal = Double.valueOf(0);

        for (Registro r : registros) {
            PresupuestoFinal += Double.valueOf(r.getCoste());
        }
        return PresupuestoFinal;
    }

    public List<Registro> getRegistros(Context context){

        UsuarioRepository mRepository=new UsuarioRepository(context);
        Flowable<List<Registro>> allRegistrosbyFecha = mRepository.getAllRegistrosbyFecha(this.FechaInicio, this.FechaFin);
        List<Registro> registros=allRegistrosbyFecha.blockingFirst();

        if(registros==null){
            registros=new ArrayList<>();
        }

        if((!(Categoria.equals("TODAS")) && this.UserID>0)) {
            List<Registro> AuxUsuario = new ArrayList<>();
            AuxUsuario.addAll(registros);
            registros.clear();
            for (Registro r : AuxUsuario) {
                if ((r.isGasto() && r.getCategoria().equals(this.Categoria) && r.getRegistroUserID() == this.getUserID())) {
                    registros.add(r);
                }
            }
        }

        if(!Categoria.equals("TODAS") && UserID<0){

            List<Registro> AuxUsuario = new ArrayList<>();
            AuxUsuario.addAll(registros);
            registros.clear();
            for (Registro r : AuxUsuario) {
                if ((r.isGasto() && r.getCategoria().equals(this.Categoria))) {
                    registros.add(r);
                }
            }

        }

        if(Categoria.equals("TODAS") && UserID>0){

            List<Registro> AuxUsuario = new ArrayList<>();
            AuxUsuario.addAll(registros);
            registros.clear();
            for (Registro r : AuxUsuario) {
                if ((r.isGasto() && r.getRegistroUserID()==UserID)) {
                    registros.add(r);
                }
            }
        }

        return registros;

    }
}
