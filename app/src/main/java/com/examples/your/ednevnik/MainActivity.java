package com.examples.your.ednevnik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.examples.your.ednevnik.Profesor.ProfesorLogin;
import com.examples.your.ednevnik.Ucenik.StudentLogin;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton prof=(ImageButton) findViewById(R.id.btn_prof);
        ImageButton std=(ImageButton) findViewById(R.id.btn_std);

        prof.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent profIntent= new Intent(MainActivity.this, ProfesorLogin.class);
                startActivity(profIntent);
                finish();
            }
        });

        std.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent stdIntent= new Intent(MainActivity.this,StudentLogin.class);
                startActivity(stdIntent);
                finish();
            }
        });
    }
}
