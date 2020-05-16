package com.exam.shiro;


import com.exam.pojo.SysUser;
import com.exam.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


public class UserRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");
        //给资源进行授权
        SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();
        //添加资源的授权字符串
//        info.addStringPermission("user:add");

        //到数据库查询当前登录用户的权限
//        Subject subject = SecurityUtils.getSubject();

//        User byId = service.findById(user.getId());

        //当前登录用户，账号
        SysUser shiroUser = (SysUser) principalCollection.getPrimaryPrincipal();
        System.out.println(shiroUser);
        Integer userType = shiroUser.getUserType();
        String userType1 = userType.toString();
        if ("1".equals(userType1)) {
            info.addRole("admin");
            info.addRole("teacher");
        } else if ("2".equals(userType1)) {
            info.addRole("teacher");
        } else if ("3".equals(userType1)) {
            info.addRole("student");
        }
        return info;

    }
    @Autowired
    SysUserService sysUserService;
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证逻辑");

        //1.将token转换为UsernamePasswordToken
        UsernamePasswordToken userToken = (UsernamePasswordToken)authenticationToken;
        //2.获取token中的登录账户
        String userCode = userToken.getUsername();
        //3.查询数据库，是否存在指定的用户名和密码的用户(主键/账户/密码/账户状态/盐)
        SysUser user = sysUserService.findSysUserByLoginName(userCode);
        //4.1 如果没有查询到，抛出异常
        if( user == null ) {
            throw new UnknownAccountException("账户"+userCode+"不存在！");
        }
        if( user.getLocked() != 0){
            throw new LockedAccountException(user.getLoginName()+"被锁定！");
        }

        //4.2 如果查询到了，封装查询结果，
        Object principal = user.getLoginName();
        Object credentials = user.getPassword();
        return new SimpleAuthenticationInfo(user,credentials,"");

        }

    }


