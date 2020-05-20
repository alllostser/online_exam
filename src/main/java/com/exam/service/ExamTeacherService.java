package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.ExamTeacher;

import java.util.List;

public interface ExamTeacherService {
    ServerResponse insert(ExamTeacher examTeacher);

    List<Integer> selectByExamId(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);
}
