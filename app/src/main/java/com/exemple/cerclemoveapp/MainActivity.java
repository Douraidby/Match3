package com.exemple.cerclemoveapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.widget.ImageView;
import android.os.Handler;

import java.util.Vector;


import static java.lang.Math.floor;

public class MainActivity extends AppCompatActivity {
    private Menu m = null;
    Cercle contina[][];             //conteneur des cercles de la grille
    Cercle cercledown;              //cercle la ou on presse down
    Cercle cercleswiped;            //cercle swiped avec cercledown
    Cercle voisins[];               //4 cercles voisins du cercledown
    Vector<Cercle> cerclesmatch;    //Coordonnées des cercles qui macth
    Vector<Cercle> cerclesaudessus; //cercles au dessus du cerclesmatch
    Canvas canvas;
    Bitmap tempBitmap;
    Bitmap bmp;
    boolean swiped;                 //si la couleur echangée ou non
    boolean match;                  //si il ya un match horizontal ou vertical

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contina = new Cercle[5][8];
        Point point;


        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 5; j++) {
                point = new Point(i, j);
                if ((j == 0 && (i == 0 || i == 3 || i == 5)) || (j == 4 && (i == 0 || i == 2)))                                 //Yellow
                    contina[j][i] = new Cercle(point, Color.YELLOW);
                if ((j == 0 && (i == 1 || i == 2 || i == 4)) || (j == 1 && i == 0) || (j == 2 && i == 5) || (j == 3 && (i == 5 || i == 6)) || (j == 4 && i == 6))     //Green
                    contina[j][i] = new Cercle(point, Color.GREEN);
                if ((j == 0 && (i == 6 || i == 7)) || (j == 1 && i == 1) || (j == 3 && (i == 0 )) || (j == 4 && i == 7))                           //Purple
                    contina[j][i] = new Cercle(point, 0xFF8B00FF);
                if ((j == 1 && (i == 2 || i == 6)) || (j == 2 && (i == 2 || i == 6)) || (j == 3 && (i == 1 || i == 4 || i == 7)) || (j == 4 && i == 3))            //Orange
                    contina[j][i] = new Cercle(point, 0xFFFF7F00);
                if ((j == 1 && (i == 4 || i == 5 || i == 7)) || (j == 2 && (i == 0 || i == 4)) || (j == 3 && i == 3) || (j == 4 && i == 4))                    //Blue
                    contina[j][i] = new Cercle(point, Color.BLUE);
                if ((j == 1 && i == 3) || (j == 2 && (i == 1 || i == 3 || i == 7)) || (j == 4 && (i == 1 || i == 5)) || j == 3 && i==2)                                 //Red
                    contina[j][i] = new Cercle(point, Color.RED);

            }




        DessinerJeu();
        ImageView monimage = (ImageView) findViewById(R.id.imageView);
        monimage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GererTouch(event);
                return true;
            }
        });


    }

    //Dessiner les cercles
    public void DessinerJeu()
    {
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.grid58);
        tempBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);

        canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bmp, 0, 0, null);

        float RayonCercle = tempBitmap.getHeight() / 10 - 10;   //Rayon d'un cercle
        float lgcarre = (tempBitmap.getWidth() + 7) / 8;        //+7 et +4 c pour ajuster la largeur et la longeur d'un carré
        float longcarre = (tempBitmap.getHeight() + 4) / 5;

        Paint monpaint = new Paint();

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 5; j++) {
                monpaint.setColor(contina[j][i].getCouleur());
                canvas.drawCircle(contina[j][i].getCentre().x * lgcarre + lgcarre / 2, contina[j][i].getCentre().y * longcarre + longcarre / 2, RayonCercle, monpaint);
            }

        ImageView monimage = (ImageView) findViewById(R.id.imageView);
        monimage.setImageBitmap(tempBitmap);
        swiped = false;
        match = false;
    }

    //Gestion des touches d'ecrans
    public void GererTouch(MotionEvent event) {

        String Tag = "APRES DESSINERJEU";
        int action = event.getActionMasked();

        int largcarre = findViewById(R.id.imageView).getWidth() / 8;
        int idX = (int) floor(event.getX(0) / largcarre);
        int idY = (int) floor(event.getY(0) / largcarre);


        switch (action) {

            case MotionEvent.ACTION_DOWN:
                GererDown(idX, idY);
                Log.d("APRES GERER DOWN", "CercleDown   X: " + String.valueOf(cercledown.getCentre().x) + "     Y: " + String.valueOf(cercledown.getCentre().y) + "         " + WhatColor(-1*cercledown.getCouleur()));
                Log.d("Contina", " X: " + String.valueOf(contina[idY][idX].getCentre().x) + "     Y: " + String.valueOf(contina[idY][idX].getCentre().y) + "         " + WhatColor(-1*contina[idY][idX].getCouleur()));
                break;
            case MotionEvent.ACTION_UP:
                if (swiped) {
                    if (match = VerifierMatch()) {
                        RemplacerMatch();
                        DessinerJeu();
                        ScanNewMatch();
                        Log.d("Dessiner JEU    ", "CercleSwiped   X: " + String.valueOf(cercleswiped.getCentre().x) + "     Y: " + String.valueOf(cercleswiped.getCentre().y) + "         " + WhatColor(-1*cercleswiped.getCouleur()));

                    }
                    else
                        AnnulerSwipe();

                }
//                  Log.d(Tag, "CercleSwiped   X: " + String.valueOf(cercleswiped.getCentre().x) + "     Y: " + String.valueOf(cercleswiped.getCentre().y) + "         " + WhatColor(-1*cercleswiped.getCouleur()));

                break;
            case MotionEvent.ACTION_MOVE:
                swiped = GererMove(idX, idY);
                //               Log.d(Tag, "UP---Coord X:" + String.valueOf(idX) + "     Coord Y:" + String.valueOf(idY));
                break;
            default:
        }
    }

    public String WhatColor(int c)
    {
        switch (c){
            case 16776961:
                return "Blu";
            case 33024:
                return"Orange";
            case 7667457:
                return "Violet";
            case 16711936:
                return "Green";
            case 256:
                return "Yellow";
            case 65536:
                return "Red";
        }
        return "Ne sais pas";
    }



/*            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DessinerJeu();
                }
            }, 1000);  */




    public void GererDown(int x, int y){

        voisins = new Cercle[4];
        cercledown = new Cercle(new Point(x,y),contina[y][x].getCouleur());
 //       Log.d("GererDown  ", "Contina    X: " + String.valueOf(contina[y][x].getCentre().x) +"   Y: "+String.valueOf(contina[y][x].getCentre().y)+" Couleur: "+WhatColor(-1*contina[y][x].getCouleur()) );

        //recuperer les voisins qui sont a l'interieur de la grille seulement
        if (y!=0)
            voisins[0] = new Cercle(contina[y - 1][x].getCentre(), contina[y - 1][x].getCouleur());
            else    voisins[0] = null;

        if (y!=4)
            voisins[1] = new Cercle(contina[y + 1][x].getCentre(), contina[y + 1][x].getCouleur());
            else    voisins[1] = null;

        if (x!=0)
            voisins[2] = new Cercle(contina[y][x - 1].getCentre(), contina[y][x - 1].getCouleur());
            else    voisins[2] = null;

        if (x!=7)
            voisins[3] = new Cercle(contina[y][x + 1].getCentre(), contina[y][x + 1].getCouleur());
            else    voisins[3] = null;
    }



    public boolean GererMove(int x, int y) {

        for (int i=0;i<4;i++) {
            if (voisins[i] != null && voisins[i].getCentre().equals(x, y)) {                                            //si on est rendu sur un voisin non null

                contina[y][x].setCouleur(cercledown.getCouleur());                                                  //on change la couleur du voisin dans le contina
                contina[cercledown.getCentre().y][cercledown.getCentre().x].setCouleur(voisins[i].getCouleur());    //on change le couleur du cercledown
                cercleswiped = new Cercle(voisins[i].getCentre(), voisins[i].getCouleur());                          //on recupere le cercle swiped
//Log.d("GererMOVE  ", "Contina    X: " + String.valueOf(contina[y][x].getCentre().x) +"   Y: "+String.valueOf(contina[y][x].getCentre().y)+" Couleur: "+WhatColor(-1*contina[y][x].getCouleur()) );
                return true;

            }
        }
        return false;
    }



    public boolean VerifierMatch(){

        cerclesmatch = new Vector<Cercle>();
        //match horizontal
        for (int j=0;j<5;j++)
            for (int i=1;i<7;i++)
                if ( contina[j][i].getCouleur()==contina[j][i-1].getCouleur()&& contina[j][i].getCouleur()== contina[j][i+1].getCouleur()) {
                    Log.d("VerifierMatch  ", "Contina   X: " + String.valueOf(contina[j][i-1].getCentre().x) +"   Y: "+String.valueOf(contina[j][i-1].getCentre().y)+" Couleur: "+WhatColor(-1*contina[j][i-1].getCouleur()) );
                    cerclesmatch.add(contina[j][i-1]);
                    cerclesmatch.add(contina[j][i]);
                    cerclesmatch.add(contina[j][i+1]);
                    return true;
                }
        //match verticlal
/*        for (int i=0;i<8;i++)
            for (int j=1;j<4;j++)
                if (contina[j][i].getCouleur()==contina[j-1][i].getCouleur() && contina[j][i].getCouleur()==contina[j+1][i].getCouleur())
                    return true;*/

        //a faire le cas de match horisontal et vertical en meme temps
        return false;

    }


    public void RemplacerMatch(){
        int Xm;
        int Ym;

        for (Cercle c: cerclesmatch){
            Xm = c.getCentre().x;
            Ym = c.getCentre().y;
            while ( (Ym-1)>=0 ) {
                contina[Ym][Xm].setCouleur(contina[Ym - 1][Xm].getCouleur());
                Ym--;

            }
        }
        cerclesmatch.clear();
    }

    public void ScanNewMatch(){

        while (VerifierMatch())
        RemplacerMatch();


    }

    public void AnnulerSwipe(){
        if (cercleswiped!=null) {
            int couleurTemp;
            couleurTemp = contina[cercledown.getCentre().y][cercledown.getCentre().x].getCouleur();
            contina[cercledown.getCentre().y][cercledown.getCentre().x].setCouleur(cercleswiped.getCouleur());
            contina[cercleswiped.getCentre().y][cercleswiped.getCentre().x].setCouleur(couleurTemp);
            swiped =false;
            Log.d("Annuler SWIPE","--------------------oui");

            //contina[cercleswiped.getCentre().y][cercleswiped.getCentre().x].setCouleur(cercledown.getCouleur());
            //contina[cercledown.getCentre().y][cercledown.getCentre().x].setCouleur(cercleswiped.getCouleur());
        }
    }





    public void GererUp(int x,int y){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.quitter){
            finishAffinity();
            return true;
        }else{
            return false;
        }
    }

}
