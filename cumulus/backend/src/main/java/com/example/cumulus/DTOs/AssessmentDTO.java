package com.example.cumulus.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AssessmentDTO {
    List<QuestionDTO> questions;

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
