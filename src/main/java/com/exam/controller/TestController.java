package com.exam.controller;

import com.exam.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/")
public class TestController {


        @Autowired
        SysUserService userSer;

        /**
         * @todo 用户登录
         * @since 获取当前用户，
         * 判断用户是否已经认证登录，
         * 用账号密码创建UsernamePasswordToken，
         * 调用subject的login方法
         * @param
         * @return
         */
        @RequestMapping(method = RequestMethod.POST,value = "login.do")
        public String logon(@RequestParam("userCode")String username, @RequestParam("password")String password, Model model) {
            System.out.println(username);
            //创建Subject实例对象
            //step1：获取Subject
            Subject subject = SecurityUtils.getSubject();
            System.out.println(subject.getPrincipal());
            System.out.println(subject.getPrincipals());
            System.out.println(subject.getPreviousPrincipals());
            System.out.println(subject.getSession());
            //2.封装用户数据
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            try {
                //3.执行登录方法
                subject.login(token);
                //登录成功跳转到主页
                return "redirect:/index.jsp";
            }catch (UnknownAccountException e){
                //登录失败：用户名不存在
                model.addAttribute("msg","用户名不存在");
                return "login";
            }catch (IncorrectCredentialsException e){
                //登录失败：密码错误
                model.addAttribute("msg","密码错误");
                return "login";
            }catch (AuthenticationException e){
                //登录失败：
                model.addAttribute("msg","登录失败");
            }
            return "login";
        }
}
