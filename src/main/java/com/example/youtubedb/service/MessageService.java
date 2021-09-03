package com.example.youtubedb.service;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class MessageService {

//    @Value("${coolsms.devHee.apikey}")
    private String apiKey = "NCSPKDQXASHQOBBK";

//    @Value("${coolsms.devHee.apisecret}")
    private String apiSecret = "DDE4POPKWR09KGRFYYKJYCVUVI99PNQ7";

//    @Value("${coolsms.devHee.fromnumber}")
    private String fromNumber = "01086231917";

    public void sendMessage(String toNumber, String randomNumber) {

        log.info("test중 1");
        Message coolsms = new Message(apiKey, apiSecret);

        log.info("test중 2");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", toNumber);
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("text", "문자가면 연락좀");
        params.put("kakaoOptions", null);
//                "[grabMe] 인증번호 "+randomNumber+" 를 입력하세요.");
        params.put("app_version", "test app 1.2"); // application name and version
        log.info("test중 3");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

}