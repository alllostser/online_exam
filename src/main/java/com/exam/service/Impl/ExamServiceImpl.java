package com.exam.service.Impl;

import com.exam.commons.ServerResponse;
import com.exam.dao.ExamMapper;
import com.exam.pojo.Exam;
import com.exam.service.ExamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Resource
    private ExamMapper examMapper;

    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse findExamList(Exam exam, Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        if (orderBy != null && !"".equals(orderBy)) {
            if (orderBy.contains("&")){
                //filedname&desc/filedname&asc
                String[] orderbys = orderBy.split("&");
                PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
            }
        }
        List<Exam> exams = examMapper.queryAll(exam);
        if (exams == null || exams.size()<=0){
            return ServerResponse.serverResponseByFail(0,"没有找到任何试卷信息");
        }
        PageInfo pageInfo = new PageInfo(exams);
        return ServerResponse.serverResponseBySucess(pageInfo);
    }
}
