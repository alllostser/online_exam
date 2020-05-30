package com.exam.dao;

import com.exam.pojo.Notice;

import java.util.List;

public interface NoticeMapper {
    /**
     * 获取总记录条数
     * */
    Long getCountAll();


    /**
     * 通过实体作为筛选条件查询
     * @param notice 实例对象
     * @return 对象列表
     */
    List<Notice> queryAll(Notice notice);

    /**
     * 新增公告
     *
     * @param notice 实例对象
     * @return 影响行数
     */
    int insert(Notice notice);

    /**
     * 获取公告根据id
     * @param id
     * @return
     */
    Notice queryById(Integer id);

    /**
     * 更新公告
     * @param notice
     * @return
     */
    int update(Notice notice);

    /**
     * 批量删除
     * @param integers
     * @return
     */
    int delete(Integer[] integers);
}