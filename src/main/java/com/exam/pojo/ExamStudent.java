package com.exam.pojo;

public class ExamStudent {
    private Integer esId;

    private Integer studentId;

    private Integer examId;

    private String status;

    private Long totalScore;

    private String reading;

    public Integer getEsId() {
        return esId;
    }

    public void setEsId(Integer esId) {
        this.esId = esId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading == null ? null : reading.trim();
    }
}