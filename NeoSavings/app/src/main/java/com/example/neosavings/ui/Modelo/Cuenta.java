package com.example.neosavings.ui.Modelo;


import androidx.room.*;

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
}
