package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.Exam;



public interface ExamService {
    /**
     * 试卷列表
     * */

    ServerResponse findExamList(Exam exam, Integer pageNum, Integer pageSize, String orderBy);

}
