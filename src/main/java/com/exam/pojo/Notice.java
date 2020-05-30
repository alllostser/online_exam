package com.exam.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Notice {
    //公告的id
    private Integer noticeId;
    //公告的标题
    private String title;
    //公告的内容
    private String content;
    //公告的类型：1表示需要弹框提示2表示页面提示
    private String type;
    //公告的状态：1表示显示2表示不显示
    private Integer status;

    private Integer createBy;

    private Date createDate;

    private Integer updateBy;

    private Date updateDate;

}