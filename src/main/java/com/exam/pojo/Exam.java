package com.exam.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Exam {
    private Integer examId;

    private String examName;

    private Date examStartDate;

    private Long examLastTime;

    private Integer reviewerId;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Long score;
}