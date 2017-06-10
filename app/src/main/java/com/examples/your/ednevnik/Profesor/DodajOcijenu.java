package com.examples.your.ednevnik.Profesor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.Model.Ocjena;
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


public class DodajOcijenu extends AppCompatActivity {
    TextView ocjena_info_ime;
    TextView ocjena_info_prezime;
    TextView ocjena_info_korisnickoime;
    TextView ocjena_info_predmet;
    EditText ocjena_datum;
    Spinner ocjena_tip;
    EditText ocjena_info_napomena;
    RadioGroup ocjena_grupa;
    SharedPreferences prefs;
    int ocjena=0;
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
                builder.setMessage("Jeste li sigurni da želite dodati ocjenu?");

                builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if((ocjena>0)&&(!ocjena_datum.getText().equals(""))){
                            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            try {
                                Date d = format.parse(ocjena_datum.getText().toString());
                                Ocjena o=new Ocjena(d.getTime(),(String)ocjena_tip.getSelectedItem(),ocjena,ocjena_info_napomena.getText().toString(),s,p);
                                o.save();
                                Toast.makeText(getApplicationContext(),"Uspiješno ste dodali ocjenu za ovog učenika",Toast.LENGTH_SHORT).show();

                            } catch (ParseException e) {
                                Toast.makeText(getApplicationContext(),"Došlo je do neočekivane greške",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            ocjena_datum.setText("");
                            ocjena_grupa.clearCheck();
                            ocjena=0;

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Niste odabrali datum ili ocjenu",Toast.LENGTH_SHORT).show();
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
        ocjena_info_ime= (TextView) findViewById(R.id.ocjena_info_ime);
        ocjena_info_prezime= (TextView) findViewById(R.id.ocjena_info_prezime);
        ocjena_info_korisnickoime= (TextView) findViewById(R.id.ocjena_info_korisnickoime);
        ocjena_info_predmet= (TextView) findViewById(R.id.ocjena_info_predmet);

        ocjena_datum= (EditText) findViewById(R.id.ocjena_date);
        ocjena_tip= (Spinner) findViewById(R.id.spinner_ocjena_tip);
        ocjena_info_napomena= (EditText) findViewById(R.id.ocjena_info_napomena);
        ocjena_grupa= (RadioGroup) findViewById(R.id.ocjena_grupa);

        intent = getIntent();
        ocjena_datum.setInputType(InputType.TYPE_NULL);
        ocjena_datum.requestFocus();

        s=Student.findById(Student.class,prefs.getLong("id_student_info",1));
        p=Predmet.findById(Predmet.class, prefs.getLong("id_predmet_info", 1));



        ocjena_info_ime.setText(s.getName());
        ocjena_info_prezime.setText(s.getSurname());
        ocjena_info_korisnickoime.setText(s.getUsername());
        ocjena_info_predmet.setText(p.getNaziv_predmeta());

    }
    public void properties(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                ocjena_datum.setText(dateFormatter.format(newDate.getTime()));

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        ocjena_datum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.show();
            }
        });

        ocjena_grupa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ocjena_1:
                        ocjena=1;
                        break;
                    case R.id.ocjena_2:
                        ocjena=2;
                        break;
                    case R.id.ocjena_3:
                        ocjena=3;
                        break;
                    case R.id.ocjena_4:
                        ocjena=4;
                        break;
                    case R.id.ocjena_5:
                        ocjena=5;
                        break;
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_ocijenu);
        init();
        properties();
        setTitle("Dodaj ocjenu");


    }
}
