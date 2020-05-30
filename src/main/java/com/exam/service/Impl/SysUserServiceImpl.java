package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.dao.SysUserMapper;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.UserVo;
import com.exam.service.SysUserService;
import com.exam.utils.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    SysUserMapper sysUserMapper;

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse addUser(SysUser user) {
        //step1:1非空判断
        if (user.getLoginName() == null || "".equals(user.getLoginName())) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USERNAME_NOT_EMPTY.getStatus(), Consts.StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        if (StringUtils.isBlank(user.getPassword())) {//如果未设密码则使用默认密码
            user.setPassword(Constants.DEFAULT_PASSWORD);
        }
        if (StringUtils.isBlank(user.getNickName())){//如果未设置昵称则自动生产昵称
            user.setNickName(RandomNameUtils.getRandomJianHan(6));
        }
        SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        user.setCreateBy(loginUser.getId());
        //step2:操作数据库
        int result = sysUserMapper.insertSelective(user);
        if (result <= 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        //清除缓存
        GuavaCacheUtils.setKey("count", "null");
        return ServerResponse.serverResponseBySucess(Consts.StatusEnum.UPDATE_SUCCESS.getDesc());
    }

    /**
     * 根据id查找用户
     *
     * @param id
     * @return
     */
    @Override
//    @RequiresRoles({"teacher"})
    public ServerResponse findUserById(Integer id) {
        //step:1参数非空判断
        if (id == null || id < 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //step2：查询数据库
        SysUser user = sysUserMapper.findUserById(id);
        if (user == null) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USERID_NOT_EXISTS.getStatus(), Consts.StatusEnum.USERID_NOT_EXISTS.getDesc());
        }
        UserVo userVo = PoToVoUtil.SysUserToVo(user);
        return ServerResponse.serverResponseBySucess(userVo);
    }

    /**
     * 更新用户信息
     *
     * @param sysUser
     * @return
     */
    @Override
    public ServerResponse updateUser(SysUser sysUser) {
        //获取登录用户信息
        SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //step1:非空判断
        if (sysUser.getId() == null) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        if (loginUser.getUserType()!=1){
            if (!sysUser.getId().equals(loginUser.getId())){
                throw new UnauthorizedException("无权限修改他人信息！");
            }
        }
        sysUser.setUpdateBy(loginUser.getId());
        //操作数据库
        int result = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (result <= 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(Consts.StatusEnum.UPDATE_SUCCESS.getDesc());
    }

    /**
     * 用户列表搜索+动态排序
     *
     * @param userType
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    @RequiresRoles({"teacher"})
    public TableDataInfo userList(Integer userType, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
//        统计记录条数，适应前端分页
        if (GuavaCacheUtils.getKey("count")==null){
            int count = sysUserMapper.selectAllCount();
            GuavaCacheUtils.setKey("count", Integer.toString(count));
        }
        //step1:判读是否传递了userType和keyword
        if (userType == null && (keyword == null || "".equals(keyword))) {
            //前端没有传递userType和keyword,向前端返回空的数据
            //PageHelper
            PageHelper.startPage(pageNum, pageSize);
            if (orderBy != null && !"".equals(orderBy)) {
                if (orderBy.contains("&")){
                    //filedname&desc/filedname&asc
                    String[] orderbys = orderBy.split("&");
                    PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
                }
            }
            List<SysUser> usertList = sysUserMapper.selectAll();
            List<UserVo> userVos = new ArrayList<>();
            for (SysUser user : usertList) {
                if (user!=null){
                    UserVo userVo = PoToVoUtil.SysUserToVo(user);
                    userVos.add(userVo);
                }
            }
//            PageInfo pageInfo = new PageInfo(userVos);
            return TableDataInfo.ResponseBySucess("", Long.valueOf(GuavaCacheUtils.getKey("count")), userVos);
        }
        //step2:判断userType是否传递
//        if (userType != -1 || userType!= null) {//传递了userType
//            //查询userTypeq权限的所有用户
//            List<SysUser> sysUsers = sysUserMapper.selectByUserTypeAndkeyword(userType,keyword);
//        }
        //step3:判断keyword是否传递
        if (keyword != null && !"".equals(keyword)) {
            keyword = "%" + keyword + "%";
        }
        //step4：执行查询
        //写在查询前
        PageHelper.startPage(pageNum, pageSize);
        if (orderBy != null && !"".equals(orderBy)) {
            //filedname_desc/filedname_asc
            String[] orderbys = orderBy.split("_");
            PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
        }
        List<SysUser> sysUsers = sysUserMapper.selectByUserTypeAndkeyword(userType, keyword);
        //转化vo对象
        List<UserVo> userVos = new ArrayList<>();
        for (SysUser user : sysUsers) {
            UserVo userVo = PoToVoUtil.SysUserToVo(user);
            userVos.add(userVo);
        }
        //构建分页模型
//        PageInfo pageInfo = new PageInfo(userVos);
        //step5：返回结果
        if (userVos.size() <= 0) {
            return TableDataInfo.ResponseByFail(404,"无查找结果");
        }
        return TableDataInfo.ResponseBySucess("", (long) Long.valueOf(GuavaCacheUtils.getKey("count")), userVos);
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse deleteSysUserByIds(String ids) {
        //非空判断
        if (StringUtils.isEmpty(ids)) {
            return ServerResponse.serverResponseByFail(0, "没有选择任何用户");
        }
        //查数据库
        Integer[] id = Convert.toIntArray(ids);
//        SysUser user = sysUserMapper.selectByPrimaryKey(id);
//        if (user==null){
//            return ServerResponse.serverResponseByFail(0,"该用户已被删除");
//        }
        //执行删除操作
        int row = sysUserMapper.deleteByPrimaryKey(id);
        if (row<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        //清除缓存
        GuavaCacheUtils.setKey("count", "null");
        return ServerResponse.serverResponseBySucess(row);//返回受影响行数
    }

    /**
     * 用户登录查找-shiro调用
     * */
    @Override
    public SysUser findSysUserByLoginName(String username) {
        SysUser sysUser = sysUserMapper.selectByUsernameAndPassword2(username);
        return sysUser;
    }

    /**
     * 根据id查找教师集合
     * */
    @Override
    public List<SysUser> findUserByIds(List<Integer> teacherids){
        List<SysUser> teachers = sysUserMapper.findUserByIds(teacherids);
        return teachers;
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param loginName
     * @return
     */
    @Override
    public ServerResponse changePass(String oldPassword, String newPassword, String loginName) {
        SysUser user = findSysUserByLoginName(loginName);

        if (!user.getPassword().equals(oldPassword)){
            return ServerResponse.serverResponseByFail(-100,"密码错误，请核对后再次尝试！");
        }
        int result = sysUserMapper.changePass(user.getId(),newPassword);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(Consts.StatusEnum.UPDATE_SUCCESS.getDesc(),result);
    }
}
