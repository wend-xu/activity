package com.learn.activitisample2.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;

@Component
public class BaseTools {
    public Map<String,Object> outputEntityInfo(Object entity) throws IllegalAccessException{
        System.out.println(">>>>>>>>>>>>>>>>>> PARSE START >>>>>>>>>>>>>>>>");
        long startTime = new Date().getTime();
        System.out.println("START TIMR :"+startTime);
        Map<String,Object> map = new HashMap<>();

        Class<?> entityClass = entity.getClass();
        String entityType = entityClass.getTypeName();
        Field[] declaredFields = entityClass.getDeclaredFields();

        for(Field declareField:declaredFields){
            boolean flag = declareField.isAccessible();
            declareField.setAccessible(true);
            String name = declareField.getName();
            Object o = declareField.get(entity);
            map.put(name,o);
            declareField.setAccessible(flag);
        }

        StringBuilder sb = new StringBuilder();
        map.forEach((key,value)->{
            sb.append(MessageFormat.format("KEY: {0}  ;\tVALUE: {1} \n",key,value));
        });
        String result = MessageFormat.format("ENTITY TYPE： {0} \nOUT PUT RESULT IS: \n{1}", entityType, sb.toString());
        System.out.println(result);

        long endTime = new Date().getTime();
        long excuteTime = endTime - startTime;
        System.out.println(MessageFormat.format("END TIME：{0} ； USE: {1} ms",endTime+"",excuteTime));
        System.out.println(">>>>>>>>>>>>>>>>>> PARSE END >>>>>>>>>>>>>>>>>>");
        return map;
    }

    public Map<String,Object> outputEntityInfo2(Object entity){
        Map<String,Object> result = null;
        try {
            result = outputEntityInfo(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
}
