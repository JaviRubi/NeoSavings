package com.example.neosavings.ui.Modelo;

import androidx.room.Embedded;
import androidx.room.Relation;

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
}
