package com.exam.shiro.sessionManager;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义的sessionmanager
 */
public class CustomSessionManager extends DefaultWebSessionManager {
    private static final Logger log = LoggerFactory.getLogger(DefaultSessionManager.class);

    /**
     * 头信息中具有sessionid
     * 指定sessionId获取方法
     * */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头Authorization中的数据
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(id)){
            //如果没有携带数据，则生成新的sessionid
            return super.getSessionId(request,response);

        }else {
                //返回sessionid
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
                return id;
        }
    }

    @Override
    protected Session retrieveSession(SessionKey sessionKey) {
        Serializable sessionId = this.getSessionId(sessionKey);
        if (sessionId == null) {
            log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a session could not be found.", sessionKey);
            return null;
        } else {
            Session s = null;
            try {
                s = this.retrieveSessionFromDataSource(sessionId);
            } catch (UnknownSessionException e) {
                log.debug("There is no session with id [" + sessionId + "]");
            }
            if (s == null) {
                String msg = "Could not find session with ID [" + sessionId + "]";
                log.debug(msg);
                return null;
            } else {
                return s;
            }
        }
    }
}
