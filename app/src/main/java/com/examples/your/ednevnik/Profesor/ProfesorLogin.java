package com.examples.your.ednevnik.Profesor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.MainActivity;
import com.examples.your.ednevnik.Model.Professor;
import com.examples.your.ednevnik.R;
import com.orm.SugarContext;

public class ProfesorLogin extends AppCompatActivity {
    EditText input_username;
    EditText input_password;
    Button btn_login;
    TextView link_signup;
    SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_login);
        init();
        propertis();

    }
    public void init(){
        SugarContext.init(this);
        input_username= (EditText) findViewById(R.id.input_username);
        input_password= (EditText) findViewById(R.id.input_password);
        btn_login= (Button) findViewById(R.id.btn_login);
        link_signup= (TextView) findViewById(R.id.link_signup);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }
    public void propertis(){

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                String username_=input_username.getText().toString();
                String password_=input_password.getText().toString();

                if(username_.equals("")||password_.equals("")){
                    Toast.makeText(getApplicationContext(), R.string.message_info_noinput,Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Professor.find(Professor.class, "username = ? and password = ?", username_,password_).isEmpty()){
                        Toast.makeText(getApplicationContext(), R.string.message_info_incorrect_data,Toast.LENGTH_SHORT).show();

                    }
                    else{
                        //prefs.edit().putLong("profid",Professor.find(Professor.class, "username = ? and password = ?", username_,password_).get(0).getId()).commit();
                        prefs.edit().putLong("profid",Professor.find(Professor.class, "username = ?", username_).get(0).getId()).commit();
                        Toast.makeText(getApplicationContext(), R.string.message_info_correct_data,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfesorLogin.this, Navi_main.class));
                        finish();
                    }
                }
            }
        });
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                startActivity(new Intent(ProfesorLogin.this, ProfesorRegister.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfesorLogin.this, MainActivity.class));
        finish();

    }
}
