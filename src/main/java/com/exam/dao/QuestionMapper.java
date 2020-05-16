package com.exam.dao;

import com.exam.pojo.Question;

import java.util.List;

public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKey(Question record);
    /**
     * 根具条件获取试题列表
     * */
    List<Question> queryAll(Question question);

    /**
     * 添加试题
     * */
    int insert(Question question);
}