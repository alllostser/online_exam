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
        userVo.setDelFlag(user.getDelFlag());
        userVo.setUserType(user.getUserType());
        return userVo;
    }
    public static QuestionVo questionPoToVO(Question question){
        QuestionVo questionVo = new QuestionVo();
        questionVo.setAnalyse(question.getAnalyse());
        questionVo.setAnswer(question.getAnswer());
        questionVo.setCreateBy(question.getCreateBy());
        questionVo.setCreateDate(TimeUtils.dateToStr(question.getCreateDate()));
        questionVo.setScore(question.getScore());
        questionVo.setId(question.getId());
        questionVo.setOptionA(question.getOptionA());
        questionVo.setOptionB(question.getOptionB());
        questionVo.setOptionC(question.getOptionC());
        questionVo.setOptionD(question.getOptionD());
        questionVo.setTitle(question.getTitle());
        questionVo.setType(question.getType());
        questionVo.setUpdateBy(question.getUpdateBy());
        questionVo.setUpdateDate(TimeUtils.dateToStr(question.getUpdateDate()));
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
