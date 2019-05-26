package com.xd.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xd.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BeanValidator {
    //全局校验工厂
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    //key对应字段，value对应错误信息
    public static <T> Map<String,String> validate(T t, Class... groups){
        Validator validator = validatorFactory.getValidator();
        //校验，违反约束的set集合
        Set<ConstraintViolation<T>> validateResult = validator.validate(t,groups);
        if(validateResult.isEmpty()){
            return Collections.emptyMap();
        }else{
            //有错
            LinkedHashMap errors = Maps.newLinkedHashMap(); //本质实现一样，谷歌guava写法
            Iterator iterator = validateResult.iterator();
            while(iterator.hasNext()){
                ConstraintViolation violation = (ConstraintViolation)iterator.next();
                errors.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            return errors;
        }
    }

    public static Map<String,String> validateList(Collection<?> collection){
        Preconditions.checkNotNull(collection); //guava
        Iterator iterator = collection.iterator();
        Map errors;
        do{
            if(!iterator.hasNext()){
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object,new Class[0]); //传入一个有零个元素的class数组，相当于null，但如果是null便利时会直接跳出，而这个不会
        }while (errors.isEmpty());
        return errors;
    }

    //任何校验均用这个方法，包装以上两个方法
    public  static Map<String,String> validteObject(Object first, Object... objects){
        if(objects != null && objects.length > 0){
            return validateList(Lists.asList(first,objects));
        }else{
            return validate(first,new Class[0]);
        }
    }

    public static void check(Object param) throws ParamException {
        Map<String,String> map = BeanValidator.validteObject(param);
        if(MapUtils.isNotEmpty(map)){
            throw new ParamException(map.toString());
        }
    }
}
