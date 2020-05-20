package com.exam.shiro.Filter;

import com.alibaba.fastjson.JSONObject;
import com.exam.commons.ServerResponse;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义logout过滤器，退出成功后返回json
 */
public class ShiroLogoutFilter extends LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        String redirectUrl = this.getRedirectUrl(request, response, subject);
        try {
            subject.logout();
        } catch (SessionException var6) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", var6);
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //这里是个坑，如果不设置的接受的访问源，那么前端都会报跨域错误，因为这里还没到corsConfig里面
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "0");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified,Authorization,Authentication,Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("XDomainRequestAllowed", "1");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        ServerResponse result =ServerResponse.serverResponseBySucess("退出成功");
        httpServletResponse.getWriter().write(JSONObject.toJSON(result).toString());
        return false; //true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
    }
}
