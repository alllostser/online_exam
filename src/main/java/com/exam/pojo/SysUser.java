package com.exam.pojo;

import lombok.Data;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.Date;
@Data
public class SysUser implements Serializable, AuthCachePrincipal {
    private Integer id;

    private String loginName;

    private String nickName;

    private String icon;

    private String password;

    private String salt;

    private String tel;

    private String email;

    private Byte locked;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private String remark;

    private Byte delFlag;

    private Integer userType;


    @Override
    public String getAuthCacheKey() {
        return null;
    }
}