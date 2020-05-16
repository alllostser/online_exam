package com.exam.dao;

import com.exam.pojo.ExamRecord;
import com.exam.pojo.ExamRecordKey;

public interface ExamRecordMapper {
    int deleteByPrimaryKey(ExamRecordKey key);

    int insert(ExamRecord record);

    int insertSelective(ExamRecord record);

    ExamRecord selectByPrimaryKey(ExamRecordKey key);

    int updateByPrimaryKeySelective(ExamRecord record);

    int updateByPrimaryKeyWithBLOBs(ExamRecord record);

    int updateByPrimaryKey(ExamRecord record);
}