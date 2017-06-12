package com.examples.your.ednevnik.Model;

import com.orm.SugarRecord;

/**
 * Created by PINJUH on 12.6.2017..
 */

public class ZakljucnaOcjena extends SugarRecord {
    int zakljucna;
    Predmet predmet;
    Student student;

    public ZakljucnaOcjena() {
    }

    public ZakljucnaOcjena(int zakljucna, Predmet predmet, Student student) {
        this.zakljucna = zakljucna;
        this.predmet = predmet;
        this.student = student;
    }

    public int getZakljucna() {
        return zakljucna;
    }

    public void setZakljucna(int zakljucna) {
        this.zakljucna = zakljucna;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
