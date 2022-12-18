package com.example.appinteligentes;

public class ImageModel {

    //private String prediccion;
    //private String clase;

    private String image;

    public ImageModel(String image){
        this.image = image;
        /*this.prediccion = prediccion;
        this.clase = clase;*/
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
    /*
    public void setClase(String clase) {
        this.clase = clase;
    }

    public void setPrediccion(String prediccion) {
        this.prediccion = prediccion;
    }

    public String getClase() {
        return clase;
    }

    public String getPrediccion() {
        return prediccion;
    }*/
}
