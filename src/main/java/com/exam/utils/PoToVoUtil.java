package com.exam.utils;

import com.exam.pojo.Exam;
import com.exam.pojo.Question;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.ExamVo;
import com.exam.pojo.vo.QuestionVo;
import com.exam.pojo.vo.UserVo;

import java.math.BigDecimal;
import java.util.Date;

public class PoToVoUtil {

    public static UserVo SysUserToVo(SysUser user){
        if (user == null){
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setLoginName(user.getLoginName());
        userVo.setNickName(user.getNickName());
        userVo.setTel(user.getTel());
        userVo.setEmail(user.getEmail());
        userVo.setIcon(user.getIcon());
        userVo.setLocked(user.getLocked());
        userVo.setCreateDate(TimeUtils.dateToStr(user.getCreateDate()));
        userVo.setUpdateDate(TimeUtils.dateToStr(user.getUpdateDate()));
        userVo.setUserType(user.getUserType()==1?"管理员":user.getUserType()==2?"教师":"考生");
        userVo.setCreateBy(user.getCreateBy());
        userVo.setUpdateBy(user.getUpdateBy());
        return userVo;
    }
    public static QuestionVo questionPoToVO(Question question){
        QuestionVo questionVo = new QuestionVo();
        questionVo.setAnalyse(question.getAnalyse());
        questionVo.setAnswer(question.getAnswer());
        questionVo.setCreateBy(question.getCreateBy());
        questionVo.setCreateDate(TimeUtils.dateToStr(question.getCreateDate(),"yyyy-MM-dd"));
        questionVo.setScore(question.getScore());
        questionVo.setId(question.getId());
        questionVo.setOptionA(question.getOptionA());
        questionVo.setOptionB(question.getOptionB());
        questionVo.setOptionC(question.getOptionC());
        questionVo.setOptionD(question.getOptionD());
        questionVo.setTitle(question.getTitle());
        questionVo.setType(question.getType());
        questionVo.setUpdateBy(question.getUpdateBy());
        questionVo.setUpdateDate(question.getUpdateDate()!=null?TimeUtils.dateToStr(question.getUpdateDate(),"yyyy-MM-dd"):null);
        return questionVo;
    }
    public static ExamVo examPoToVo(Exam exam){
        ExamVo examVo = new ExamVo();
        examVo.setExamId(exam.getExamId());
        examVo.setExamName(exam.getExamName());
        examVo.setExamStartDate(exam.getExamStartDate());
        examVo.setExamLastTime(exam.getExamLastTime());
        examVo.setCreateBy(exam.getCreateBy());
        examVo.setUpdateBy(exam.getUpdateBy());
        examVo.setScore(exam.getScore());
        return examVo;
    }
}
