package com.examples.your.ednevnik.Model;

import com.orm.SugarRecord;

/**
 * Created by PINJUH on 2.6.2017..
 */

public class Predmet extends SugarRecord {
    String nazivpredmet;
    String boja;
    Professor profesor;

    public Predmet() {
    }

    public Predmet(String naziv_predmeta, String boja, Professor profesor) {
        this.nazivpredmet = naziv_predmeta;
        this.boja = boja;
        this.profesor = profesor;
    }

    public String getNaziv_predmeta() {
        return nazivpredmet;
    }

    public void setNaziv_predmeta(String naziv_predmeta) {
        this.nazivpredmet = naziv_predmeta;
    }

    public String getBoja() {
        return boja;
    }

    public void setBoja(String boja) {
        this.boja = boja;
    }

    public Professor getProfesor() {
        return profesor;
    }

    public void setProfesor(Professor profesor) {
        this.profesor = profesor;
    }
}
