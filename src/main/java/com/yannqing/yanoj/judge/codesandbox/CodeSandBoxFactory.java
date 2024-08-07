package com.yannqing.yanoj.judge.codesandbox;

import com.yannqing.yanoj.judge.codesandbox.impl.ExampleCodeSandBox;
import com.yannqing.yanoj.judge.codesandbox.impl.RemoteCodeSandBox;
import com.yannqing.yanoj.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * @description: 代码沙箱工厂（根据字符串参数创建对应的代码沙箱实例）
 * @author: yannqing
 * @create: 2024-08-06 17:47
 * @from: <更多资料：yannqing.com>
 **/
public class CodeSandBoxFactory {
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "remote": {
                return new RemoteCodeSandBox();
            }
            case "thirdParty": {
                return new ThirdPartyCodeSandBox();
            }
            default: {
                return new ExampleCodeSandBox();
            }
        }
    }
}
