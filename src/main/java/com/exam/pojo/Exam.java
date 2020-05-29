package com.exam.pojo;

import com.exam.utils.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class Exam extends BaseEntity{
    private Integer examId;

    private String examName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examStartDate;

    private Long examLastTime;

    private Integer reviewerId;

    private Integer createBy;

    private Date createDate;

    private Date updateDate;

    private BigDecimal score;

}