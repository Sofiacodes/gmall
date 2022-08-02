package com.example.logger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wxample.gmall.common.constants.GmallConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController   // Controller + ResponseBody
@Slf4j
public class LogJsonController {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @PostMapping("/log")
    public String log(@RequestParam("logString") String logString){

        /**
         *  日志级别 trace debug info warn error fatal
         *      log.debug(logString);
         *      log.error(logString);
         *      System.out.println(logString);   //打印到控制台
         */


        JSONObject jsonObject = JSON.parseObject(logString);
        jsonObject.put("ts",System.currentTimeMillis());
        log.info(logString);
        //区分出事件类型
        if ("startup".equals(jsonObject.get("type"))){
            kafkaTemplate.send(GmallConstant.KAFKA_STARTUP,jsonObject.toJSONString());
        }else{
            kafkaTemplate.send(GmallConstant.KAFKA_EVENT,jsonObject.toJSONString());
        }

        return "sucess";
    }

}
