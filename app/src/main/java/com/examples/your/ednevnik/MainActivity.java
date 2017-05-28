package com.examples.your.ednevnik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.examples.your.ednevnik.Login.ProfesorLogin;
import com.examples.your.ednevnik.Login.StudentLogin;

public class MainActivity extends AppCompatActivity {


    private Button prof, std;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button prof=(Button) findViewById(R.id.btn_prof);
        final Button std=(Button) findViewById(R.id.btn_std);


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
            }
        });
    }
}
