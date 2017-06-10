package com.examples.your.ednevnik.Model;

import com.orm.SugarRecord;

/**
 * Created by PINJUH on 10.6.2017..
 */

public class Izostanak extends SugarRecord{
    long datum;
    String napomena;
    Student ucenik;
    Predmet predmet;

    public Izostanak() {
    }

    public Izostanak(long datum, String napomena, Student ucenik, Predmet predmet) {
        this.datum = datum;
        this.napomena = napomena;
        this.ucenik = ucenik;
        this.predmet = predmet;
    }

    public long getDatum() {
        return datum;
    }

    public void setDatum(long datum) {
        this.datum = datum;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Student getUcenik() {
        return ucenik;
    }

    public void setUcenik(Student ucenik) {
        this.ucenik = ucenik;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }
}
