package com.exemple.cerclemoveapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener {

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
            finish();
        }

    }
}
