package com.examples.your.ednevnik.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.examples.your.ednevnik.Constants;
import com.examples.your.ednevnik.Login.StudentLogin;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.R;
import com.orm.SugarContext;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PINJUH on 28.5.2017..
 */

public class StudentRegister extends AppCompatActivity {
    EditText input_name;
    EditText input_surname;
    EditText input_username;
    EditText input_password;
    Button btnsignup;
    TextView link_login;
    RequestQueue red;
    List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_register);
        init();
        propertis();



    }
    public void init(){
        SugarContext.init(this);
        red= Volley.newRequestQueue(this);
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
                startActivity(new Intent(StudentRegister.this, StudentLogin.class));
                finish();

            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name_=input_name.getText().toString();
                final String surname_=input_surname.getText().toString();
                final String username_=input_username.getText().toString();
                final String password_=input_password.getText().toString();

                if(name_.equals("")||(surname_.equals("")||username_.equals("")||password_.equals(""))){
                    Toast.makeText(getApplicationContext(), R.string.message_info_noinput,Toast.LENGTH_SHORT).show();
                }
                else {

                    List<Student> student=new ArrayList<>();
                    student = Student.find(Student.class, "username = ?", username_);
                    if(student.isEmpty()){
                        StringRequest stringRequest=new StringRequest(Request.Method.GET, Constants.API_LINK,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            boolean check =false;
                                            JSONArray json = new JSONArray(response);
                                            students=new ArrayList<>();
                                            for (int i = 0; i < json.length(); i++) {
                                                if(username_.equals(json.getJSONObject(i).getString("login"))){
                                                    Student st= new
                                                            Student(name_,
                                                            surname_,
                                                            json.getJSONObject(i).getString("avatar_url"),
                                                            username_,
                                                            password_);
                                                    st.save();
                                                    check=true;
                                                    Toast.makeText(getApplicationContext(), R.string.message_info_succregister,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            if(!check){
                                                Toast.makeText(getApplicationContext(), "Ovaj korisnik ne postoji",Toast.LENGTH_SHORT).show();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        red.add(stringRequest);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.message_info_alreadyreg,Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

}