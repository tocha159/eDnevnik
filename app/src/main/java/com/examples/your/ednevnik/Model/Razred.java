package com.examples.your.ednevnik.Model;

import com.orm.SugarRecord;

/**
 * Created by PINJUH on 4.6.2017..
 */

public class Razred extends SugarRecord {
    Predmet predmet;
    Student student;

    public Razred() {
    }

    public Razred(Predmet predmet, Student student) {
        this.predmet = predmet;
        this.student = student;
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
