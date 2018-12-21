package com.yuzhi.controller;

import com.yuzhi.service.HownetDllService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 控制转发层
 * @author Hunter
 *
 */

@RestController
@RequestMapping(value="/hownet")
public class HownetDllController {
    //业务层注入
    @Autowired
    private HownetDllService hownetDllService;
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(HownetDllController.class);
    /**
     * 事件主体抽取
     */
//    @SuppressWarnings("unchecked")
    @RequestMapping(value="/eventAbstract",method = RequestMethod.POST)
    public List<Object> eventAbstract(String articleID, String article) {
        // 调用dll接口获得分词数据，返回分析结果
        List<Object> finalResult = hownetDllService.eventAbstract(articleID,article);
        return finalResult;
    }
    /**
     * 健康测试
     * @return
     */
    @RequestMapping(value="/healthTest",method = RequestMethod.POST)
    public String keepAlived(){
        String response=hownetDllService.keepAlived();
        logger.info("---->>>hownet健康状态："+response);
        return response;
    }

}
