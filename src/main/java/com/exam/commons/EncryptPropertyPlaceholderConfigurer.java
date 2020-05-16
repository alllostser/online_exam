package com.exam.commons;


import com.exam.utils.AESUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private String[] propertyNames = {
            "jdbc.password","master.jdbc.password", "slave.jdbc.password", "generator.jdbc.password", "master.redis.password"
    };
    //解密指定propertyName的加密属性值
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        for (String p : propertyNames) {
            if (p.equalsIgnoreCase(propertyName)) {
                byte[] bytes = AESUtil.parseHexStr2Byte(propertyValue);
                String s = new String(AESUtil.decrypt(bytes, "77test6658"));
                return s;
            }
        }
        return super.convertProperty(propertyName, propertyValue);
    }
}
