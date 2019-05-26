package com.xd.controller;

import com.xd.common.ApplicationContextHelper;
import com.xd.common.JsonData;
import com.xd.dao.SysAclModuleMapper;
import com.xd.exception.ParamException;
import com.xd.exception.PermissionException;
import com.xd.model.SysAclModule;
import com.xd.param.TestVo;
import com.xd.util.BeanValidator;
import com.xd.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
//exploited测试用，另一个发布用

@Controller
@RequestMapping("/test")
@Slf4j  //不用自己定义logger
public class TestController {

    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello(){
        log.info("hello");
        throw new RuntimeException("test exception");
//        return JsonData.success("hello,permission");
    }

    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo) throws ParamException {
        log.info("validate");
        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }

    @RequestMapping("/validate2.json")
    @ResponseBody
    public JsonData validate2(TestVo vo) throws ParamException {
        log.info("validate");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.objToString(module));
        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }
}
