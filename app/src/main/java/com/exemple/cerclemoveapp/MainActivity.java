package com.exemple.cerclemoveapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Vector;

import static java.lang.Math.floor;

public class MainActivity extends AppCompatActivity {

    Cercle contina[][];             //conteneur des cercles de la grille
    Cercle cercledown;              //cercle la ou on presse down
    Cercle voisins[];               //4 cercles voisins du cercledown
    Canvas canvas;
    Bitmap tempBitmap;
    Bitmap bmp;
    boolean swiped;                 //si la couleur echangée ou non

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.grid58);
        tempBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);

        canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bmp, 0, 0, null);

        float RayonCercle = tempBitmap.getHeight() / 10 - 10;  //Rayon d'un cercle
        float largcarre = (tempBitmap.getWidth() + 7) / 8;   //+7 et +4 c pour ajuster la largeur et la longeur d'un carré
        float longcarre = (tempBitmap.getHeight() + 4) / 5;



        contina = new Cercle[5][8];
        Point point;
        Paint monpaint = new Paint();

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 5; j++) {
                point = new Point(i, j);
                if ((j == 0 && (i == 0 || i == 3 || i == 5)) || (j == 4 && (i == 0 || i == 2)))                                 //Yellow
                    contina[j][i] = new Cercle(point, Color.YELLOW);
                if ((j == 0 && (i == 1 || i == 2 || i == 4)) || (j == 1 && i == 0) || (j == 2 && i == 5) || (j == 3 && (i == 5 || i == 6)) || (j == 4 && i == 6))     //Green
                    contina[j][i] = new Cercle(point, Color.GREEN);
                if ((j == 0 && (i == 6 || i == 7)) || (j == 1 && i == 1) || (j == 3 && (i == 0 || i == 2)) || (j == 4 && i == 7))                           //Purple
                    contina[j][i] = new Cercle(point, 0xFF8B00FF);
                if ((j == 1 && (i == 2 || i == 6)) || (j == 2 && (i == 2 || i == 6)) || (j == 3 && (i == 1 || i == 4 || i == 7)) || (j == 4 && i == 3))            //Orange
                    contina[j][i] = new Cercle(point, 0xFFFF7F00);
                if ((j == 1 && (i == 4 || i == 5 || i == 7)) || (j == 2 && (i == 0 || i == 4)) || (j == 3 && i == 3) || (j == 4 && i == 4))                    //Blue
                    contina[j][i] = new Cercle(point, Color.BLUE);
                if ((j == 1 && i == 3) || (j == 2 && (i == 1 || i == 3 || i == 7)) || (j == 4 && (i == 1 || i == 5)))                                 //Red
                    contina[j][i] = new Cercle(point, Color.RED);

            }


        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 5; j++) {
                monpaint.setColor(contina[j][i].getCouleur());
                canvas.drawCircle(contina[j][i].getCentre().x * largcarre + largcarre / 2, contina[j][i].getCentre().y * longcarre + longcarre / 2, RayonCercle, monpaint);
            }

        ImageView monimage = (ImageView) findViewById(R.id.imageView);
        monimage.setImageBitmap(tempBitmap);


        monimage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GererTouch(event);
                return true;
            }
        });


    }

    public void GererTouch(MotionEvent event) {

        String Tag = "onTouch";
        int action = event.getActionMasked();

        int largcarre = findViewById(R.id.imageView).getWidth()/8;
        int idX = (int) floor(event.getX(0)/largcarre);
        int idY = (int) floor(event.getY(0)/largcarre);

//        Log.d("LArgeur carre: ", String.valueOf(largcarre));
        switch (action){

            case MotionEvent.ACTION_DOWN:
                Log.d(Tag, "DOWN---Coord X:" + String.valueOf(idX) + "     Coord Y:" + String.valueOf(idY));
                GererDown(idX,idY);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(Tag, "UP---Coord X:" + String.valueOf(idX) + "     Coord Y:" + String.valueOf(idY));
                break;
            case MotionEvent.ACTION_MOVE:
                swiped = GererMove(idX, idY);
                break;
            default:
        }

        //repeindre le canvas
        if (swiped) {

            bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.grid58);
            tempBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);

            canvas = new Canvas(tempBitmap);
            canvas.drawBitmap(bmp, 0, 0, null);

            float RayonCercle = tempBitmap.getHeight() / 10 - 10;  //Rayon d'un cercle
            float lgcarre = (tempBitmap.getWidth() + 7) / 8;   //+7 et +4 c pour ajuster la largeur et la longeur d'un carré
            float longcarre = (tempBitmap.getHeight() + 4) / 5;


            Paint monpaint = new Paint();
//            tempBitmap.eraseColor(Color.TRANSPARENT);

            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 5; j++) {
                    monpaint.setColor(contina[j][i].getCouleur());
                    canvas.drawCircle(contina[j][i].getCentre().x * lgcarre + lgcarre / 2, contina[j][i].getCentre().y * longcarre + longcarre / 2, RayonCercle, monpaint);
                }

            ImageView monimage = (ImageView) findViewById(R.id.imageView);
            monimage.setImageBitmap(tempBitmap);
        }
    }


    public boolean GererMove(int x, int y) {

        for (int i=0;i<4;i++) {
            if (voisins[i] != null && voisins[i].getCentre().equals(x, y)) {                         //si on est rendu sur un voisin non null
                contina[y][x].setCouleur(cercledown.getCouleur());                                  //on change la couleur du voisin dans le contina
                contina[cercledown.getCentre().y][cercledown.getCentre().x].setCouleur(voisins[i].getCouleur());    //change le couleur du cercledown
                return true;
            }
        }
        return false;
    }


    public void GererDown(int x, int y){


        voisins = new Cercle[4];
        cercledown = new Cercle(new Point(x,y),contina[y][x].getCouleur());

        //recuperer les voisins qui sont a l'interieur de la grille seulement
        if (y!=0)
            voisins[0] = new Cercle(contina[y - 1][x].getCentre(), contina[y - 1][x].getCouleur());
        else
            voisins[0] = null;
        if (y!=4)
            voisins[1] = new Cercle(contina[y + 1][x].getCentre(), contina[y + 1][x].getCouleur());
        else
            voisins[1] = null;
        if (x!=0)
            voisins[2] = new Cercle(contina[y][x - 1].getCentre(), contina[y][x - 1].getCouleur());
        else
            voisins[2] = null;
        if (x!=7)
            voisins[3] = new Cercle(contina[y][x + 1].getCentre(), contina[y][x + 1].getCouleur());
        else
            voisins[3] = null;
    }

    public void GererUp(int x,int y){
    }


}
