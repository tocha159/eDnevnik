package com.examples.your.ednevnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.examples.your.ednevnik.R.id.prof_login;
import static com.examples.your.ednevnik.R.id.tv_reg;

public class prof_login extends AppCompatActivity implements View.OnClickListener{

    private EditText username, password;
    private Button login;
    private TextView reg_here;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_login);

        EditText username=(EditText) findViewById(R.id.et_username);
        EditText password=(EditText) findViewById(R.id.et_password);
        Button login=(Button) findViewById(R.id.prof_login);
        TextView reg_here=(TextView) findViewById(tv_reg);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.prof_login:
                get_login();
                break;
            case R.id.tv_reg:
               Intent registerIntent = new Intent(prof_login.this,prof_reg.class);
                prof_login.this.startActivity(registerIntent);
                break;
            default:
        }
    }
    public void get_login(){

    }
}
