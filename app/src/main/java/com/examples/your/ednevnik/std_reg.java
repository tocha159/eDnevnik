package com.examples.your.ednevnik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class std_reg extends AppCompatActivity {
    private EditText stdr_id, stdr_ip, stdr_usr, stdr_pass;
    private Button stdr_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_reg);
        EditText stdr_id=(EditText) findViewById(R.id.sr_id);
        EditText stdr_ip=(EditText) findViewById(R.id.sr_ip);
        EditText stdr_usr=(EditText) findViewById(R.id.sr_usr);
        EditText stdr_pass=(EditText) findViewById(R.id.sr_pass);
        Button stdr_btn=(Button) findViewById(R.id.sr_btn);


    }
}
