package com.exam.pojo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ExamDetail {
    private Integer id;

    private Integer examId;

    private Integer stuId;

    private BigDecimal score;
}