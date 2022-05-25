package com.example.neosavings.ui.Modelo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Categoria {


    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String Categoría;

    private String Tipo;

    public String getCategoría() {
        return Categoría;
    }

    public void setCategoría(String categoría) {
        Categoría = categoría;
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
}
