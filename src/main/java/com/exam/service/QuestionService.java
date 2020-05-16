package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.Question;

public interface QuestionService {
    /**
     * 获取试题列表
     * */
    ServerResponse findQuestionList(Question question, Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 添加试题
     * */
    ServerResponse insert(Question question);
}
