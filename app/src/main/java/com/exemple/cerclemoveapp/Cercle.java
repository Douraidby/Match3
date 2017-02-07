package com.exemple.cerclemoveapp;

import android.graphics.Point;


/**
 * Created by doura on 2/5/2017.
 */

public class Cercle {

    private Point centre;
    private int couleur;

    //Constructeur du cercle
    public Cercle(Point p, int c){

        centre = new Point(p);
        couleur = c;
    }

    public Point getCentre() {
        return centre;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }
    public void setXY(int x,int y)
    {
        this.centre.set(x,y);

    }

    public int switchcouleur(int newcolor)
    {
        int oldcolor = couleur;
        this.couleur = newcolor;
        return oldcolor;

    }
}

