package com.example.neosavings.ui.Modelo;


import android.content.Context;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.neosavings.ui.Database.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

public class PagosDeudas {

    @Embedded
    private Deuda deuda;

    @Relation(
            parentColumn = "DeudaID",
            entityColumn = "DeudaID"
    )
    private List<Registro> Registros;

    public Deuda getDeuda() {
        return deuda;
    }

    public void setDeuda(Deuda deuda) {
        this.deuda = deuda;
    }

    public List<Registro> getRegistros() {
        return Registros;
    }

    public void setRegistros(List<Registro> registros) {
        Registros = registros;
    }

    public Double GetDeudaRestante(){
        Double deudaInicial= Double.valueOf(0);

        if(Registros==null){
            Registros=new ArrayList<>();
        }

        for (Registro r: Registros) {
            if(deuda.isDeuda()){
                if(r.isGasto()){
                    deudaInicial-=Double.valueOf(r.getCoste());
                }else{
                    deudaInicial+=Double.valueOf(r.getCoste());
                }

            }else{

                if(r.isGasto()){
                    deudaInicial+=Double.valueOf(r.getCoste());
                }else{
                    deudaInicial-=Double.valueOf(r.getCoste());
                }

            }
        }
        return deudaInicial;
    }

    public void ActualizarEstadoPrestamo(Context context){

        if(Registros==null){
            Registros=new ArrayList<>();
        }

        Double DeudaRestante=GetDeudaRestante();



        if(DeudaRestante<0){
            DeudaRestante=DeudaRestante*-1;
            getDeuda().setDeuda(!getDeuda().isDeuda());
            getDeuda().setCosteDeuda(String.valueOf(DeudaRestante));
            UsuarioRepository mRepository=new UsuarioRepository(context);
            mRepository.UpdateDeudaID(getDeuda());
        }

    }
}
