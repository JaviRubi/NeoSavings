package com.example.neosavings.ui.Modelo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"CategoriaID","Categoria"})
public class Categoria {


    private long CategoriaID;

    @NonNull
    private String Categoria;

    private String Tipo;

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public void setTipoGasto(){
        Tipo="GASTO";
    }

    public void setTipoIngreso(){
        Tipo="INGRESO";
    }

    public long getCategoriaID() {
        return CategoriaID;
    }

    public void setCategoriaID(long categoriaID) {
        CategoriaID = categoriaID;
    }
}
