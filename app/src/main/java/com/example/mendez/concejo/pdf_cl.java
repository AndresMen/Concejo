package com.example.mendez.concejo;

/**
 * Created by Mendez on 31/05/2018.
 */

public class pdf_cl {
    private int image;
    private String nom;
    private String dir;
    private String an;
    private String tip;

    public pdf_cl(int image, String nom, String dir, String an, String tip) {
        this.image = image;
        this.nom = nom;
        this.dir = dir;
        this.an = an;
        this.tip = tip;
    }

    public int getImage() {
        return image;
    }

    public String getNom() {
        return nom;
    }

    public String getDir() {
        return dir;
    }

    public String getAn() {
        return an;
    }

    public String getTip() {
        return tip;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
