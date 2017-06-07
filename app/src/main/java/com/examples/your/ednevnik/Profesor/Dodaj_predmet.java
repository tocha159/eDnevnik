package com.examples.your.ednevnik.Profesor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Professor;
import com.examples.your.ednevnik.R;
import com.thebluealliance.spectrum.SpectrumPalette;

public class Dodaj_predmet extends AppCompatActivity implements SpectrumPalette.OnColorSelectedListener{
    Spinner predmet_odabrani;
    String color_choice="";
    String predmet_choice="";
    SpectrumPalette spectrumPalette;
    View cancelActionView;
    View doneActionView;
    TextView potvrdi_;
    TextView odustani_;
    SharedPreferences prefs;

    public void init(){
        spectrumPalette = (SpectrumPalette) findViewById(R.id.palette);
        predmet_odabrani= (Spinner) findViewById(R.id.predmet_odabrani);
        potvrdi_= (TextView) findViewById(R.id.potvrdi);
        odustani_= (TextView) findViewById(R.id.odustani);
        cancelActionView = findViewById(R.id.action_cancel);
        doneActionView = findViewById(R.id.action_done);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

    }
    public void properties(){
        spectrumPalette.setOnColorSelectedListener(this);
        predmet_odabrani.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                predmet_choice=predmet_odabrani.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                predmet_choice="";

            }
        });
        cancelActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        doneActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(color_choice.equals("")||(predmet_choice.equals(""))){
                    Toast.makeText(getApplicationContext(), R.string.subjects_nochoice,Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Predmet.find(Predmet.class, "nazivpredmet = ? and profesor = ?", predmet_choice,String.valueOf(prefs.getLong("profid", 1))).isEmpty()){
                        try {
                            Predmet p = new Predmet(predmet_choice, color_choice, Professor.findById(Professor.class, prefs.getLong("profid", 1)));
                            p.save();
                            Toast.makeText(getApplicationContext(),"Uspiješno ste dodali predmet",Toast.LENGTH_SHORT).show();
                            setResult(20);
                            finish();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Greska,pokusajte se ponovo logirat",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Predmet se već nalazi u listi vaših predmeta",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_predmet);
        init();
        properties();

    }

    @Override
    public void onColorSelected(@ColorInt int color) {
        Toast.makeText(this, getString(R.string.color_choice) + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show();
        color_choice="#"+Integer.toHexString(color).toUpperCase();
    }
}
