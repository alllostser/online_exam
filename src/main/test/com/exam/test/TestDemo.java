package com.exam.test;

import com.exam.dao.SysUserMapper;
import com.exam.pojo.SysUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:ApplicationContext.xml")
public class TestDemo {
    @Resource
    private SysUserMapper dao;

    @Test
    public void text1(){
        SysUser sysUser = dao.selectByPrimaryKey(1);
        System.out.println(sysUser);
    }
    @Test
    public void text2(){
      int a  = 1,b=2,c;
     c= (a+ ++b>3?++a:b);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }
    @Test
    public void text3(){
        List al = new ArrayList();
        al.add(1);
        print(al);
        System.out.println(al);
    }
    public void  print(List al){
        al.add(2);
        al=new ArrayList();
        al.add(3);
        al.add(4);
    }
}
