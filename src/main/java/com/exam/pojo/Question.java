package com.exam.pojo;

import com.exam.utils.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 试题实体类
 * */
@Data
public class Question extends BaseEntity {
    private Integer id;

    private String type;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private BigDecimal score;

    private Integer createBy;

    private Date createDate;

    private Integer updateBy;

    private Date updateDate;

    private String title;

    private String answer;

    private String analyse;
}