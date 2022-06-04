package com.example.neosavings.ui.Modelo;

import android.content.Context;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.neosavings.ui.Database.UsuarioRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RegistrosPagosProgramados {

    @Embedded
    public PagoProgramado pagoProgramado;


    @Relation(
            parentColumn = "PagoProgramadoID",
            entityColumn = "PagoProgramadoID"
    )
    public List<Registro> Registros;


    public PagoProgramado getPagoProgramado() {
        return pagoProgramado;
    }

    public void setPagoProgramado(PagoProgramado pagoProgramado) {
        this.pagoProgramado = pagoProgramado;
    }

    public List<Registro> getRegistros() {
        return Registros;
    }

    public void setRegistros(List<Registro> registros) {
        Registros = registros;
    }

    public Date getNextPago(){

        Date next=pagoProgramado.getFechaInicio();

        if(Registros==null){
            Registros=new ArrayList<>();
        }

        if(Registros.size()>0){
            Registros.sort(new Comparator<Registro>() {
                @Override
                public int compare(Registro o1, Registro o2) {
                    return o1.getFecha().compareTo(o2.getFecha());
                }
            });
        }else{
            return next;
        }

        for (Registro r : Registros){
            next=r.Fecha;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(next);
        if(pagoProgramado.getPeriodicidad().equals("DIARIAMENTE")){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }else if(pagoProgramado.getPeriodicidad().equals("SEMANALMENTE")){
            calendar.add(Calendar.DAY_OF_YEAR,7);
        }else if(pagoProgramado.getPeriodicidad().equals("MENSUALMENTE")) {
            calendar.add(Calendar.MONTH, 1);
        }else if(pagoProgramado.getPeriodicidad().equals("TRIMESTRALMENTE")) {
            calendar.add(Calendar.MONTH, 3);
        }else if(pagoProgramado.getPeriodicidad().equals("SEMESTRALMENTE")){
            calendar.add(Calendar.MONTH, 6);
        }else{
            calendar.add(Calendar.YEAR,1);
        }

        String aux = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        try {
            next=new SimpleDateFormat("dd/MM/yyyy").parse(aux);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return next;

    }

    public boolean GenerarRegistrosHastaFechaActual(Context context) throws ParseException {

        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        Date FechaActual = calendar.getTime();
        Date FechaCrear = getNextPago();
        String aux = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        FechaActual = new SimpleDateFormat("dd/MM/yyyy").parse(aux);
        calendar.setTime(FechaCrear);
        UsuarioRepository mRepository = new UsuarioRepository(context);
        boolean GastoNuevo=false;


        if (pagoProgramado.getFechaInicio().getTime() <= FechaCrear.getTime()) {
            while (FechaCrear.getTime() <= FechaActual.getTime() && FechaCrear.getTime() <= pagoProgramado.getFechaFin().getTime()) {
                Registro r = new Registro();
                r.setFormaPago(pagoProgramado.getFormaPago());
                r.setCoste(pagoProgramado.getCoste());
                r.setDescripcion(pagoProgramado.getDescripcion());
                r.setGasto(pagoProgramado.isGasto());
                r.setCategoria(pagoProgramado.getCategoría());
                r.setFecha(FechaCrear);
                r.setPagoProgramadoID(pagoProgramado.getPagoProgramadoID());
                r.setRegistroUserID(pagoProgramado.getUserId());

                mRepository.insertRegistro(r);
                GastoNuevo=true;


                if (pagoProgramado.getPeriodicidad().equals("DIARIAMENTE")) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                } else if (pagoProgramado.getPeriodicidad().equals("SEMANALMENTE")) {
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                } else if (pagoProgramado.getPeriodicidad().equals("MENSUALMENTE")) {
                    calendar.add(Calendar.MONTH, 1);
                } else if (pagoProgramado.getPeriodicidad().equals("TRIMESTRALMENTE")) {
                    calendar.add(Calendar.MONTH, 3);
                } else if (pagoProgramado.getPeriodicidad().equals("SEMESTRALMENTE")) {
                    calendar.add(Calendar.MONTH, 6);
                } else {
                    calendar.add(Calendar.YEAR, 1);
                }
                FechaCrear = calendar.getTime();
                aux = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
                FechaCrear = new SimpleDateFormat("dd/MM/yyyy").parse(aux);
            }

        }
        return GastoNuevo;
    }

}
