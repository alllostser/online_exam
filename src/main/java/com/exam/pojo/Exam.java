package com.exam.pojo;

import com.exam.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Exam extends BaseEntity {
    private Integer examId;

    private String examName;

    private Date examStartDate;

    private Long examLastTime;

    private Integer reviewerId;

    private Date createDate;

    private Date updateDate;

    private Long score;

}