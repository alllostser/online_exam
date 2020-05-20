package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.ExamStudent;

import java.util.List;

public interface ExamStudentService {
    /**
     * 插入数据
     * */
    ServerResponse insert(ExamStudent examStudent);

    /**
     * 查询考生id集合
     * */
    List<Integer> findStudentIdsByExamIds(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);

    /**
     * 通过examId和studentId查询数据
     * */
    ExamStudent selectByExamIdAndStuId(Integer examId, Integer id);
}
