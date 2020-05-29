package com.exam.pojo.vo;

import com.exam.utils.BaseEntity;
import lombok.Data;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
@Data
public class UserVo extends BaseEntity implements Serializable, AuthCachePrincipal {
    private Integer id;

    private String loginName;

    private String nickName;

    private String icon;

    private String tel;

    private String email;

    private Byte locked;

    private String createDate;

    private String updateDate;

    private String userType;

    //在考试管理的时候确定学生是否选中
    private Boolean studentCheckFlag = false;

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
