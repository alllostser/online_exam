package com.exam.commons;


import com.alibaba.fastjson.JSONObject;
import com.exam.pojo.SysUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletInteceptor implements HandlerInterceptor   {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        SysUser user = (SysUser) request.getSession().getAttribute(Consts.StatusEnum.LOGIN_USER);
        if (user == null){
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(ServerResponse.serverResponseByFail(101,"请先登录")));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
