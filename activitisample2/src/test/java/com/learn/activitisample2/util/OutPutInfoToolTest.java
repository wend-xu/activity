package com.learn.activitisample2.util;

import com.learn.activitisample2.entity.Dept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OutPutInfoToolTest {
    @Autowired
    BaseTools baseTools;

    @Test
    public void entityToMapTest() throws IllegalAccessException{
        Dept dept = new Dept("12345678","234567");
        baseTools.outputEntityInfo(dept);
    }
}