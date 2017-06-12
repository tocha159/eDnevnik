package com.examples.your.ednevnik.Profesor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.Model.Izostanak;
import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.R;
import com.orm.SugarContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DodajIzostanak extends AppCompatActivity {
    TextView izostanak_info_ime;
    TextView izostanak_info_prezime;
    TextView izostanak_info_korisnickoime;
    TextView izostanak_info_predmet;
    EditText izostanak_datum;
    EditText izostanak_napomena;
    SharedPreferences prefs;
    Student s;
    Predmet p;

    private android.app.DatePickerDialog DatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ocjena_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.ocjena_spremi:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Odaberite opciju");
                builder.setMessage("Jeste li sigurni da želite dodati izostanak?");

                builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!izostanak_datum.getText().equals("")){
                            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                            try {
                                Date d = format.parse(izostanak_datum.getText().toString());
                                if(Izostanak.find(Izostanak.class,"datum = ? and ucenik = ? and predmet = ?",String.valueOf(d.getTime()),String.valueOf(s.getId()),String.valueOf(p.getId())).isEmpty()){
                                    Izostanak i=new Izostanak(d.getTime(),izostanak_napomena.getText().toString(),s,p);
                                    i.save();
                                    Toast.makeText(getApplicationContext(),"Uspiješno ste dodali izostanak za ovog učenika",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DodajIzostanak.this,ViewIzostanciStudent.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Već ste zabilježili izostanak učenika za ovaj datum i predmet",Toast.LENGTH_SHORT).show();
                                }
                            } catch (ParseException e) {
                                Toast.makeText(getApplicationContext(),"Došlo je do neočekivane greške",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Niste odabrali datum",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {

    }


    public void init(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SugarContext.init(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        izostanak_info_ime= (TextView) findViewById(R.id.izostanak_info_ime);
        izostanak_info_prezime= (TextView) findViewById(R.id.izostanak_info_prezime);
        izostanak_info_korisnickoime= (TextView) findViewById(R.id.izostanak_info_korisnickoime);
        izostanak_info_predmet= (TextView) findViewById(R.id.izostanak_info_predmet);
        izostanak_napomena= (EditText) findViewById(R.id.izostanak_info_napomena);
        izostanak_datum= (EditText) findViewById(R.id.izostanak_date);


        izostanak_datum.setInputType(InputType.TYPE_NULL);
        izostanak_datum.requestFocus();

        s=Student.findById(Student.class,prefs.getLong("id_student_info",1));
        p=Predmet.findById(Predmet.class, prefs.getLong("id_predmet_info", 1));



        izostanak_info_ime.setText(s.getName());
        izostanak_info_prezime.setText(s.getSurname());
        izostanak_info_korisnickoime.setText(s.getUsername());
        izostanak_info_predmet.setText(p.getNaziv_predmeta());

    }
    public void properties(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                izostanak_datum.setText(dateFormatter.format(newDate.getTime()));

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        izostanak_datum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.show();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_izostanak);
        init();
        properties();
        setTitle("Dodaj izostanak");
    }
}
