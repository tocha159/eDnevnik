package com.examples.your.ednevnik.Profesor;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.examples.your.ednevnik.Model.Ocjena;
import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.Model.ZakljucnaOcjena;
import com.examples.your.ednevnik.R;

import java.util.List;

/**
 * Created by PINJUH on 18.6.2017..
 */

public class ZakljuciOcjenaSingle extends DialogFragment {
    SharedPreferences prefs;
    Student s;
    Predmet p;
    TextView prosjek_text;
    RadioGroup ocjena_grupa;
    int ocjena_=1;
    View cancelActionView;
    View doneActionView;
    TextView potvrdi_;
    TextView odustani_;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.zakljuci_ocjena_single,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        s= Student.findById(Student.class,prefs.getLong("id_student_info",1));
        p= Predmet.findById(Predmet.class, prefs.getLong("id_predmet_info", 1));
        ocjena_grupa= (RadioGroup) view.findViewById(R.id.ocjena_grupa);
        prosjek_text= (TextView) view.findViewById(R.id.prosjek_text);

        potvrdi_= (TextView) view.findViewById(R.id.potvrdi);
        odustani_= (TextView) view.findViewById(R.id.odustani);
        cancelActionView = view.findViewById(R.id.action_cancel);
        doneActionView = view.findViewById(R.id.action_done);

        List<Ocjena> ocjena=Ocjena.find(Ocjena.class,"ucenik = ? and predmet = ?",String.valueOf(s.getId()),String.valueOf(p.getId()));

        if(!ocjena.isEmpty()){
            prosjek_text.setText("Prosjek: "+String.valueOf(dajProsjek(ocjena)));
            ocjena_=dajOcjenu(dajProsjek(ocjena));
        }
        else {
            prosjek_text.setText("Prosjek: 0.0");
        }


        switch(ocjena_) {
            case 1:
                ocjena_grupa.check(R.id.ocjena_1);
                break;
            case 2:
                ocjena_grupa.check(R.id.ocjena_2);
                break;
            case 3:
                ocjena_grupa.check(R.id.ocjena_3);
                break;
            case 4:
                ocjena_grupa.check(R.id.ocjena_4);
                break;
            case 5:
                ocjena_grupa.check(R.id.ocjena_5);
                break;
        }

        cancelActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        doneActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Zaključivanje ocjene");
                builder.setMessage("Jeste li sigurni da želite zaključiti "+String.valueOf(ocjena_)+"?");
                builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ZakljucnaOcjena.deleteAll(ZakljucnaOcjena.class,"predmet = ? and student = ?", String.valueOf(p.getId()), String.valueOf(s.getId()));
                        ZakljucnaOcjena zakljucna=new ZakljucnaOcjena(ocjena_,p,s);
                        zakljucna.save();
                        getDialog().dismiss();

                    }
                });
                builder.show();

            }
        });
        ocjena_grupa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ocjena_1:
                        ocjena_=1;
                        break;
                    case R.id.ocjena_2:
                        ocjena_=2;
                        break;
                    case R.id.ocjena_3:
                        ocjena_=3;
                        break;
                    case R.id.ocjena_4:
                        ocjena_=4;
                        break;
                    case R.id.ocjena_5:
                        ocjena_=5;
                        break;
                }
            }
        });


        return view;
    }

    public float dajProsjek(List<Ocjena>ocjena){
        int suma=0;
        for(Ocjena o:ocjena){
            suma+=o.getOcjena();
        }
        return (float) suma/ocjena.size();
    }
    public int dajOcjenu(float prosjek){
        if(Float.compare(prosjek,1.5f)< 0)
            return 1;
        else if((Float.compare(prosjek,1.5f)>=0)&&((Float.compare(prosjek,2.5f)<0)))
            return 2;
        else if((Float.compare(prosjek,2.5f)>=0)&&((Float.compare(prosjek,3.5f)<0)))
            return 3;
        else if((Float.compare(prosjek,3.5f)>=0)&&((Float.compare(prosjek,4.5f)<0)))
            return 4;
        else
            return 5;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ((ViewOcjeneStudent)getActivity()).refresh();

    }
}
