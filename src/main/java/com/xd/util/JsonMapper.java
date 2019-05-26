package com.xd.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

@Slf4j
//可以将一个类转化为json对象，也可以把json转换为系统中的类对象
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();
    //变量初始化
    static{
        // config
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);//不认识的字段的处理，不抛异常
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);//对象为空，不抛异常
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);//排除掉为空的字段
    }

    //核心转换方法
    //对象--->json
    public static <T> String objToString(T src){
        if(src == null){
            return null;
        }
        try{
            return src instanceof String ? (String)src : objectMapper.writeValueAsString(src);
        }catch (Exception e){
            log.warn("parse object to String exception, error{}",e);
            return null;    //返回空，不抛异常
        }
    }

    public static <T> T stringToObj(String src, TypeReference<T> typeReference){
        if(src == null || typeReference == null){
            return null;
        }
        try{
            return (T) ((typeReference.getType()).equals(String.class) ? src : objectMapper.readValue(src,typeReference));
        }catch (Exception e){
            log.warn("parse String to Object exception, String{}, TypeReference<T>{}, error:{}",src,typeReference.getType(),e);
            return null;
        }
    }
}
