package com.example.cumulus.DTOs;

import com.example.cumulus.Entities.AnswerText;
import com.example.cumulus.Entities.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class QuestionDTO {
    private Question Question;
    private String AnswerText;

    public com.example.cumulus.Entities.Question getQuestion() {
        return Question;
    }

    public void setQuestion(com.example.cumulus.Entities.Question question) {
        Question = question;
    }

    public String getAnswerText() {
        return AnswerText;
    }

    public void setAnswerText(String answerText) {
        AnswerText = answerText;
    }
}
