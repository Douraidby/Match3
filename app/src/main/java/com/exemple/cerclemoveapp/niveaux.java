package com.exemple.cerclemoveapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class niveaux extends AppCompatActivity implements View.OnClickListener {
    private Button niveau_1=null;
    private Button niveau_2=null;
    private Button niveau_3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveaux);

        niveau_1 = (Button) findViewById(R.id.niveau_1);
        niveau_1.setOnClickListener(this);

        niveau_2 = (Button) findViewById(R.id.niveau_2);
        niveau_2.setOnClickListener(this);

        niveau_3 = (Button) findViewById(R.id.niveau_3);
        niveau_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    /* Réagir au clic */
        if(v==niveau_1){
            Intent ecran_niveau_1 = new Intent(niveaux.this, MainActivity.class);
            startActivity(ecran_niveau_1);
        }else if (v==niveau_2){
            //Intent instruction = new Intent (menu.this, instructions.class);
            //startActivity(instruction);
        }else if (v==niveau_3){
            //finish();
        }

    }
}
