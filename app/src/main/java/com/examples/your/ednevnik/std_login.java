package com.examples.your.ednevnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class std_login extends AppCompatActivity implements View.OnClickListener {
    private EditText stdusr, stdpass;
    private Button std_log,std_re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_login);

        EditText stduser=(EditText) findViewById(R.id.std_usr);
        EditText stdpass=(EditText) findViewById(R.id.std_pass);
        Button stdlog=(Button) findViewById(R.id.std_login);
        Button std_re=(Button) findViewById(R.id.std_reg);



    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.std_login:
                get_login();
                break;
            case R.id.std_reg:
                startActivity(new Intent(std_login.this,std_reg.class));
                break;
            default:
        }
    }
    public void get_login(){

    }


}
