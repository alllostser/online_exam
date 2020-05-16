package com.exam.utils;

import com.exam.pojo.SysUser;
import com.exam.pojo.vo.UserVo;

public class PoToVoUtil {

    public static UserVo SysUserToVo(SysUser user){
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setLoginName(user.getLoginName());
        userVo.setNickName(user.getNickName());
        userVo.setTel(user.getTel());
        userVo.setEmail(user.getEmail());
        userVo.setIcon(user.getIcon());
        userVo.setLocked(user.getLocked());
        userVo.setCreateDate(TimeUtils.dateToStr(user.getCreateDate()));
        userVo.setUpdateDate(TimeUtils.dateToStr(user.getUpdateDate()));
        userVo.setDelFlag(user.getDelFlag());
        userVo.setUserType(user.getUserType());
        return userVo;
    }
}
