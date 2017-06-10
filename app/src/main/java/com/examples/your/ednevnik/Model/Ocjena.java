package com.examples.your.ednevnik.Model;

import com.orm.SugarRecord;

/**
 * Created by PINJUH on 9.6.2017..
 */

public class Ocjena extends SugarRecord {
    long datum;
    String tip;
    int ocjena;
    String napomena;
    Student ucenik;
    Predmet predmet;

    public Ocjena() {
    }

    public Ocjena(long datum, String tip, int ocjena, String napomena, Student ucenik, Predmet predmet) {
        this.datum = datum;
        this.tip = tip;
        this.ocjena = ocjena;
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getOcjena() {
        return ocjena;
    }

    public void setOcjena(int ocjena) {
        this.ocjena = ocjena;
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
