package com.example.appinteligentes;

import java.lang.reflect.Array;

public class ResponseModel {

    private String probabilidades;
    private String clase;

    public ResponseModel(String probabilidades,String clase){
        this.probabilidades = probabilidades;
        this.clase = clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public void setProbabilidades(String probabilidades) {
        this.probabilidades = probabilidades;
    }

    public String getClase() {
        return clase;
    }

    public String getProbabilidades() {
        return probabilidades;
    }
}
