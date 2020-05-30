package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.Notice;

public interface NoticeService {
    /**
     * 批量获取notice集合
     * @param notice
     * @return
     */
    TableDataInfo findNoticeList(Notice notice, Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 新增公告
     *
     * @param notice 实例对象
     * @return 实例对象
     */
    ServerResponse insert(Notice notice);

    /**
     * 获取公告根据id
     * @param id
     * @return
     */
    ServerResponse queryById(Integer id);

    /**
     * 更新公告
     * @param notice
     * @return
     */
    ServerResponse update(Notice notice);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ServerResponse delete(String ids);
}
