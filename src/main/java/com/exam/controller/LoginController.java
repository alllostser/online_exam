package com.exam.controller;


import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.UserVo;
import com.exam.utils.CodeCacheUtils;
import com.exam.utils.Constants;
import com.exam.utils.PoToVoUtil;
import com.exam.utils.VerifyCodeUtil;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Controller
@CrossOrigin
public class LoginController {

    /**
     * 用户登录
     * @param username
     * @param password
     * @param code
     * @return
     */
    @RequestMapping("/login.do")
    @ResponseBody
    public ServerResponse login(String username, String password, String code) {
        //shiro
        Subject subject = SecurityUtils.getSubject();
        //获取验证码
        if (StringUtils.isBlank(code)) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.CODE_IS_EMPTY.getStatus(),Consts.StatusEnum.CODE_IS_EMPTY.getDesc());
        }//username="" username =null
        if (StringUtils.isBlank(username)) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USERNAME_NOT_EMPTY.getStatus(),Consts.StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PASSWORD_NOT_EMPTY.getStatus(),Consts.StatusEnum.PASSWORD_NOT_EMPTY.getDesc());
        }
        String trueCode = CodeCacheUtils.getKey(Constants.VALIDATE_CODE);
        //获取正确的验证码
        if (trueCode == null) {
            return ServerResponse.serverResponseByFail(44,"session 超时");
        }


        if (StringUtils.isBlank(trueCode)) {
            return ServerResponse.serverResponseByFail(44,"获取验证码超时");
        }
        String errorMsg = "";
        HashMap map = new HashMap();
        //验证码不对
        String sessionid = null;
        if (StringUtils.isBlank(code) || !trueCode.toLowerCase().equals(code.toLowerCase())) {
            errorMsg = "验证码错误";
        } else {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
//            if (!subject.isAuthenticated()) {
                try {
                    subject.login(usernamePasswordToken);
                    sessionid = (String) subject.getSession().getId();
                } catch (IncorrectCredentialsException e) {
                    errorMsg = "登录密码错误.";
                } catch (ExcessiveAttemptsException e) {
                    errorMsg = "登录失败次数过多";
                } catch (LockedAccountException e) {
                    errorMsg = "帐号已被锁定.";
                } catch (DisabledAccountException e) {
                    errorMsg = "帐号已被禁用.";
                } catch (ExpiredCredentialsException e) {
                    errorMsg = "帐号已过期.";
                } catch (UnknownAccountException e) {
                    errorMsg = "帐号不存在";
                    } catch (UnauthorizedException e) {
                    errorMsg = "您没有得到相应的授权！";
                }
//            }
        }

        if (StringUtils.isBlank(errorMsg)) {
            SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();
            UserVo userVo = PoToVoUtil.SysUserToVo(principal);
            map.put("Token", sessionid);
            map.put("login_user", userVo);
            return ServerResponse.serverResponseBySucess("登陆成功",map);
        } else {
            return ServerResponse.serverResponseByFail(101,errorMsg);
        }
    }
    /**
     * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
     */
    @GetMapping("/genCaptcha.do")
    public void genCaptcha(HttpServletResponse response) throws IOException {
        //设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, null);
        //将验证码放到HttpSession里面
        CodeCacheUtils.setKey(Constants.VALIDATE_CODE,verifyCode);
        LOGGER.info("本次生成的验证码为[" + verifyCode + "],已存放到HttpSession中");
        //设置输出的内容的类型为JPEG图像
        response.setContentType("image/jpeg");
        BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 116, 36, 5, true, new Color(30, 159, 255), null, null);
        //写给浏览器
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
    }

    @RequestMapping("/logout.do")
    public String logout(){
        return "forward:/logout";
    }
}
