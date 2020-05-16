package com.exam.pojo;

public class ExamRecord extends ExamRecordKey {
    private Long finalScore;

    private String answer;

    public Long getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Long finalScore) {
        this.finalScore = finalScore;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }
}