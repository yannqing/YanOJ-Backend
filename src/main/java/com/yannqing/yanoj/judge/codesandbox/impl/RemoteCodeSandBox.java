package com.yannqing.yanoj.judge.codesandbox.impl;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yannqing.yanoj.common.ErrorCode;
import com.yannqing.yanoj.exception.BusinessException;
import com.yannqing.yanoj.judge.codesandbox.CodeSandBox;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 远程代码沙箱（实际调用接口的代码沙箱）
 * @author: yannqing
 * @create: 2024-08-06 17:37
 * @from: <更多资料：yannqing.com>
 **/
public class RemoteCodeSandBox implements CodeSandBox {

    @Value("${codesandbox.url:http://localhost:8080/executeCode}")
    private String codeSandBoxUrl;

    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");

//        String url = codeSandBoxUrl;
        String url = "http://localhost:8091/executeCode";

        String json = JSONUtil.toJsonStr(executeCodeRequest);

        System.out.println("Request URL: " + url);
        System.out.println("Request Body: " + json);

        RestTemplate restTemplate = new RestTemplate();

        String urlx = "http://sandbox:8080/test";
        String res = null;
        try {
            res = restTemplate.getForObject(urlx, String.class);
        } catch (RestClientException e) {
            System.out.println("error:" + e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("-------------------------> " + res);


        String responseStr = null;
        try {
            responseStr = HttpUtil
                    .createPost(codeSandBoxUrl)
                    .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                    .body(json)
                    .execute()
                    .body();
        } catch (HttpException e) {
            System.out.println("==================> " + e.getMessage());
            throw new RuntimeException(e);
        }

        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "execute code remoteCodeSandbox error, message = {}" + responseStr);
        }
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
        System.out.println("executeCodeResponse: " + executeCodeResponse);
        return executeCodeResponse;
    }
}
