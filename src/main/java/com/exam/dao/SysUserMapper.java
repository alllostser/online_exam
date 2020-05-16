package com.exam.dao;

import com.exam.pojo.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    /**
     *更新用户信息
     * */
    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    SysUser selectByUsernameAndPassword2(String username);

    /**
     *根据id查询用户
     * */
    SysUser findUserById(Integer id);

    /**
     * 获取用户列表
     * */
    List<SysUser> selectAll();

    /**
     * 用户模糊查询
     * */
    List<SysUser> selectByUserTypeAndkeyword(@Param("userType") Integer userType, @Param("keyword") String keyword);
}