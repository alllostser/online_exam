package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.dao.ExamTeacherMapper;
import com.exam.pojo.ExamTeacher;
import com.exam.service.ExamTeacherService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExamTeacherServiceImpl implements ExamTeacherService {
    @Resource
    private ExamTeacherMapper examTeacherMapper;
    @Override
    public ServerResponse insert(ExamTeacher examTeacher) {
        if (examTeacher !=null){
            int insert = examTeacherMapper.insert(examTeacher);
            if (insert<=0){
                return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
            }
            return ServerResponse.serverResponseBySucess(insert);
        }
        return ServerResponse.serverResponseByFail(1,"参数不能为空");
    }

    @Override
    public List<Integer> selectByExamId(Integer examId) {
        if (examId != null){
            List<Integer> teacherIds = examTeacherMapper.selectByExamId(examId);
            return teacherIds;
        }
        return null;
    }

    /**
     * 通过试卷id删除数据
     * @param examId
     * @return
     */
    @Override
    public int deleteByExamId(Integer examId) {
        int result = examTeacherMapper.deleteByExamId(examId);
        return result;
    }
}
