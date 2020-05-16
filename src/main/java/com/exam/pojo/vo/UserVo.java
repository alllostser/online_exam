package com.exam.pojo.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserVo implements Serializable {
    private Integer id;

    private String loginName;

    private String nickName;

    private String icon;

    private String tel;

    private String email;

    private Byte locked;

    private String createDate;

    private String updateDate;

    private Byte delFlag;

    private Integer userType;

}
