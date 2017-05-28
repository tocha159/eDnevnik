package com.examples.your.ednevnik.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.Login.ProfesorLogin;
import com.examples.your.ednevnik.Model.Professor;
import com.examples.your.ednevnik.R;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class ProfesorRegister extends AppCompatActivity {
    EditText input_name;
    EditText input_surname;
    EditText input_username;
    EditText input_password;
    Button btnsignup;
    TextView link_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_register);
        init();
        propertis();
    }
    public void init(){
        SugarContext.init(this);
        input_name= (EditText) findViewById(R.id.input_name);
        input_surname= (EditText) findViewById(R.id.input_surname);
        input_username= (EditText) findViewById(R.id.input_username);
        input_password= (EditText) findViewById(R.id.input_password);
        btnsignup= (Button) findViewById(R.id.btn_signup);
        link_login= (TextView) findViewById(R.id.link_login);

    }
    public void propertis(){
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfesorRegister.this, ProfesorLogin.class));
                finish();

            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_=input_name.getText().toString();
                String surname_=input_surname.getText().toString();
                String username_=input_username.getText().toString();
                String password_=input_password.getText().toString();

                if(name_.equals("")||(surname_.equals("")||username_.equals("")||password_.equals(""))){
                    Toast.makeText(getApplicationContext(), R.string.message_info_noinput,Toast.LENGTH_SHORT).show();
                }
                else {
                    List<Professor> professor=new ArrayList<>();
                    professor = Professor.find(Professor.class, "username = ?", username_);
                    if(professor.isEmpty()){
                        Professor p=new Professor(name_,surname_,username_,password_);
                        p.save();
                        Toast.makeText(getApplicationContext(), R.string.message_info_succregister,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfesorRegister.this,ProfesorLogin.class));
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.message_info_alreadyreg,Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
}
