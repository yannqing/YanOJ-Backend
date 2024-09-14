package com.yannqing.yanoj;

import com.yannqing.yanoj.judge.codesandbox.CodeSandBox;
import com.yannqing.yanoj.judge.codesandbox.CodeSandBoxFactory;
import com.yannqing.yanoj.judge.codesandbox.CodeSandboxProxy;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yannqing.yanoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 主类测试
 *
 */
@SpringBootTest
class MainApplicationTests {

//    @Value("${codesandbox.type:example}")
//    private String type;

//    @Test
//    void contextLoads() {
//        CodeSandBox codeSandbox = CodeSandBoxFactory.newInstance(type);
//        codeSandbox = new CodeSandboxProxy(codeSandbox);
//        String code = "int main() { }";
//        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
//        List<String> inputList = Arrays.asList("1 2", "3 4");
//        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
//                .code(code)
//                .language(language)
//                .inputList(inputList)
//                .build();
//        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
//        System.out.println(executeCodeResponse);
//    }


}
