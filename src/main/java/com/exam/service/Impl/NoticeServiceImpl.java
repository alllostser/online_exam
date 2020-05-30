package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.dao.NoticeMapper;
import com.exam.pojo.Notice;
import com.exam.pojo.SysUser;
import com.exam.service.NoticeService;
import com.exam.utils.Convert;
import com.exam.utils.GuavaCacheUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName NoticeServiceImpl
 * @Description //公告service层
 * @Author GuXinYu
 * @Date 2020/5/30 1:00
 * @Version 1.0
 **/
@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
     private NoticeMapper noticeMapper;
    /**
     * 批量获取notice集合
     * @param notice
     * @return
     */
    @Override
    public TableDataInfo findNoticeList(Notice notice, Integer pageNum, Integer pageSize, String orderBy) {
        if (GuavaCacheUtils.getKey("noticeCount")==null){//如果缓存为空
            Long count = noticeMapper.getCountAll();
            GuavaCacheUtils.setKey("noticeCount",Long.toString(count));//存入缓存
        }
        PageHelper.startPage(pageNum, pageSize);
        if (orderBy != null && !"".equals(orderBy)) {
            if (orderBy.contains("&")) {
                //filedname&desc/filedname&asc
                String[] orderbys = orderBy.split("&");
                PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
            }
        }
        List<Notice> notices = noticeMapper.queryAll(notice);
        return TableDataInfo.ResponseBySucess("",Long.valueOf(GuavaCacheUtils.getKey("noticeCount")),notices);
    }
    /**
     * 新增数据
     * @param notice 实例对象
     * @return 实例对象
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse insert(Notice notice) {
//        清除缓存
        GuavaCacheUtils.setKey("noticeCount","null");
        //非空判断
        if (notice == null){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();
        notice.setCreateBy(principal.getId());
        int result = noticeMapper.insert(notice);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess("添加成功",result);
    }

    /**
     * 获取公告根据id
     * @param id
     * @return
     */
    @Override
    public ServerResponse queryById(Integer id) {
        if (id ==null){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Notice notice = noticeMapper.queryById(id);
        if (notice==null){
            return ServerResponse.serverResponseByFail(404,"查无结果");
        }
        return ServerResponse.serverResponseBySucess(notice);
    }

    /**
     * 更新公告
     * @param notice
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse update(Notice notice) {
        if (notice.getNoticeId()==null){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        int result = noticeMapper.update(notice);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess("更新成功",result);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Override
    public ServerResponse delete(String ids) {
        //清除缓存
        GuavaCacheUtils.setKey("noticeCount","null");
        if (StringUtils.isBlank(ids)){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Integer[] integers = Convert.toIntArray(ids);
        int result = noticeMapper.delete(integers);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess("删除成功",result);
    }
}
