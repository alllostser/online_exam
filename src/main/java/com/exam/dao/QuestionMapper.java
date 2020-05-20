package com.exam.dao;

import com.exam.pojo.Question;

import java.util.List;

public interface QuestionMapper {
    /**
     * 删除试题
     * */
    int deleteByPrimaryKey(Integer id);

    /**
     * 根具条件获取试题列表
     * */
    List<Question> queryAll(Question question);

    /**
     * 添加试题
     * */
    int insert(Question question);

    /**
     * 修改试题
     * */
    int updateQuestion(Question question);

    /**
     * 批量删除
     *
     * @param ids 需要删除的id的集合
     * @return 受影响的行数
     */
    int deleteByIds(Integer[] ids);

    /**
     *通过id查找一条试题
     * */
    Question selectOneById(Integer id);

    /**
     * 通过问题id查询试题集合
     * */
    List<Question> findQuestionListByExamId(List<Integer> questionIds);
}