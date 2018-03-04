package com.online.foodplus.models;

import java.util.ArrayList;

/**
 * Created by thanhthang on 27/12/2016.
 */

public class Survey {
    private String question;
    private ArrayList<Answer> answer;

    public Survey() {
    }

    public Survey(String question, ArrayList<Answer> answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Answer> answer) {
        this.answer = answer;
    }
}