package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.dao.ExamStudentMapper;
import com.exam.pojo.ExamStudent;
import com.exam.service.ExamStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 试卷考生关联表service层
 * */
@Service
public class ExamStudentServiceImpl implements ExamStudentService {
    @Resource
    private ExamStudentMapper examStudentMapper;
    /**
     * 插入数据
     * */
    @Override
    public ServerResponse insert(ExamStudent examStudent) {
        if (examStudent == null){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //操作数据库
        int insert = examStudentMapper.insert(examStudent);
        if (insert<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(insert);
    }

    /**
     * 查询考生id集合
     * */
    @Override
    public List<Integer> findStudentIdsByExamIds(Integer examId) {
        List<Integer> studentIds = examStudentMapper.findStudentIdsByExamIds(examId);
        return studentIds;
    }

    /**
     * 通过试卷id删除数据
     * */
    @Override
    public int deleteByExamId(Integer examId) {
        int result = examStudentMapper.deleteByExamId(examId);
        return result;
    }

    @Override
    public ExamStudent selectByExamIdAndStuId(Integer examId, Integer id) {
        ExamStudent examStudent = examStudentMapper.selectByExamIdAndStuId(examId,id);
        return examStudent;
    }
}
