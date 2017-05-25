package com.examples.your.ednevnik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class prof_reg extends AppCompatActivity {

    private EditText ip, un, pass,email;
    private Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_reg);

        EditText ip=(EditText) findViewById(R.id.et_ip);
        EditText un=(EditText) findViewById(R.id.et_username);
        EditText pass=(EditText) findViewById(R.id.et_pass);
        EditText email=(EditText) findViewById(R.id.et_email);
        Button reg=(Button) findViewById(R.id.btn_register);



    }
}
