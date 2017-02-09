package com.exemple.cerclemoveapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;
    private Button playButton=null;
    private Button instButton=null;
    private Button quitButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        instButton = (Button) findViewById(R.id.instButton);
        instButton.setOnClickListener(this);

        quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
    /* Réagir au clic */
        if(v==playButton){
            Intent ecranNiveau = new Intent(Menu.this, niveaux.class);
            startActivity(ecranNiveau);
        }else if (v==instButton){
            Intent instruction = new Intent (Menu.this, instructions.class);
            startActivity(instruction);
        }else if (v==quitButton){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set title
            alertDialogBuilder.setTitle("Quitter notre magnifique application ? ");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Appuyez sur oui pour quitter ! ")
                    .setCancelable(false)
                    .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            finish();
                        }
                    })
                    .setNegativeButton("Non",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            //finish();
        }

    }
}
