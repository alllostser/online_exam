package com.exam.commons;

public class ResponseCode {
    public static final String LOGIN_USER = "session_user";
    public enum Code{
        SUCCESS(200,"成功"),
        ERROR(100,"失败");
        private Integer code;
        private String message;

        Code(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    public enum UserEnum {
        ERRORS(101, "用户未登录");
        private Integer code;
        private String message;

        UserEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
