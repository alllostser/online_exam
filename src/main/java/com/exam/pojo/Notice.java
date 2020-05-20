package com.exam.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Notice {
    private Integer noticeId;

    private String title;

    private String content;

    private String type;

    private Integer createBy;

    private Date createDate;

    private Integer updateBy;

    private Date updateDate;

}