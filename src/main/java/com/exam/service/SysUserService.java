package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.SysUser;

public interface SysUserService {
    /**
     * 添加用户
     * */
    ServerResponse addUser(SysUser user);

    /**
     * 根据id查找用户
     * */
    ServerResponse findUserById(Integer id);

    /**
     * 根据id更新用户
     * */
    ServerResponse updateUser(SysUser sysUser);

    /**
     * 获取用户列表
     * */
    ServerResponse userList(Integer userType,String keyword, Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 删除用户
     * */
    ServerResponse deleteSysUserByIds(String ids);

    /**
     *登录
     */
    SysUser findSysUserByLoginName(String username);
}
