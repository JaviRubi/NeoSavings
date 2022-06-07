package com.example.neosavings.ui.Modelo;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class Cuenta {

    @Embedded
    public Usuario user;


    @Relation(
            parentColumn = "userID",
            entityColumn = "RegistroUserID"

    )
    public List<Registro> Registros;





    public void addRegistro(Registro registro){
        if(Registros==null){
            Registros= new LinkedList<Registro>();
        }
        Registros.add(registro);
    }


    public List<Registro> getRegistros() {
        return Registros;
    }

    public void setRegistros(List<Registro> registros) {
        Registros = registros;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Double getSaldoActual(){
        Double valorInicial = Double.valueOf(user.getValor());
        if(Registros!=null){
            if(Registros.size()!=0) {
                for (Registro r : Registros){
                    if(r.isGasto()) {
                        valorInicial = valorInicial - Double.valueOf(r.getCoste());
                    }else{
                        valorInicial = valorInicial + Double.valueOf(r.getCoste());
                    }
                }
            }
        }
        return valorInicial;
    }

    public Date getMinDate(){
        String aux= new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        Date Min=new Date();
        try {
            Min= new SimpleDateFormat("dd/MM/yyyy").parse(aux);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(Registros==null){
            return Min;
        }

        if(Registros.size()==0){
            return Min;
        }

        for (Registro r: Registros){
            if(r.getFecha().getTime()<Min.getTime()){
                Min=r.getFecha();
            }
        }

        return Min;
    }
}
