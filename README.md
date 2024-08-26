# YanOJ 判题系统

> 作者：[yannqing](https://github.com/yannqing)
> 仅分享于 [彦青](https://yannqing.com)


> 用于在线测评编程题目代码的系统，能够根据用户提交的代码，出题人预先设置的题目输入和输出用例，进行编译代码、运行代码、判断代码



## 企业项目开发流程

1. 项目介绍、项目调研、需求分析
2. 核心业务流程
3. 项目要做的功能（功能模块）
4. 技术选型（技术预研）
5. 项目初始化
6. 项目开发
7. 测试
8. 优化
9. 代码提交、代码审核
10. 产品验收
11. 上线



## 实现核心

1. 权限校验

是否可以提交代码

2. **代码沙箱**
    1. 用户代码藏毒：木马文件，修改系统权限
    2. 沙箱：隔离的，安全的环境，用户的代码不会影响到沙箱之外的系统运行
    3. 资源分配：系统2G，不能被用户疯狂占用资源
3. 判题规则
    1. 题目用例的比对，结果的验证
4. 任务调度
    1. 服务器资源有限，用户要排队，按顺序依次判题，不能拒绝用户



## 核心业务流程

判题服务：获取题目信息、预计的输入输出结果，返回给主业务后端：用户的答案是否正确

代码沙箱：只负责运行代码，给出结果，不管什么结果是正确的。

**实现了解耦**



## 功能

1. 题目模块
    1. 创建题目（管理员）
    2. 删除题目（管理员）
    3. 修改题目（管理员）
    4. 搜索题目（用户）
    5. 在线做题
    6. 提交题目代码
2. 用户模块
    1. 注册
    2. 登录
3. 判题模块
    1. 提交判题（结果是否正确与错误）
    2. 错误处理（内存溢出、安全性、超时）
    3. **自主实现** 代码沙箱（安全沙箱）
    4. 开放接口（提供一个独立的新服务）

## 项目扩展思路

1. 支持多种语言
2. Remote Judge
3. 完善的评测功能：普通测评、特殊测评、交互测评、在线自测、子任务分组评测、文件
4. 统计分析用户判题记录
5. 权限校验

## 技术选型

前端：Vue3、Arco Design 组件库、手撸项目模板、在线代码编辑器、在线文档浏览

Java 进程控制、Java 安全管理器、部分 JVM 知识点

虚拟机（云服务器）、Docker（代码沙箱实现）

Spring Cloud 微服务 、消息队列、多种设计模式











## 后端开发

基本流程：

1. 题目模块
2. 题目提交模块
3. 判题逻辑
4. 代码沙箱实现
5. 安全优化
6. 微服务优化









### 代码沙箱实现

> ps：只负责接受代码和输入，返回编译运行的结果，不负责判题（可以作为独立的项目 / 服务，提供给其他的需要执行代码的项目去使用）

先定义执行代码的接口。后面如果有其他的代码沙箱实现，直接实现接口即可。



定义三种代码沙箱的实现：

1. 示例代码沙箱
2. 远程代码沙箱
3. 第三方代码沙箱



#### 沙箱优化（工厂模式）

使用静态工厂模式，根据用户传入的字符串参数（沙箱类别），来生成对应的代码沙箱实现类

```java
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
```



#### 沙箱优化（代理模式）

比如：我们需要在调用代码沙箱前，输出请求参数日志；在代码沙箱调用后，输出响应结果日志，便于管理员去分析。

每个代码沙箱类都写一遍 log.info？难道每次调用代码沙箱前后都执行 log？

使用代理模式，提供一个 Proxy，来增强代码沙箱的能力（代理模式的作用就是增强能力）

```java
@Slf4j
public class CodeSandboxProxy implements CodeSandBox {

    private final CodeSandBox codeSandbox;


    public CodeSandboxProxy(CodeSandBox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
```

使用方式：

```java
CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
codeSandbox = new CodeSandboxProxy(codeSandbox);
```





### 判题服务

> ps：用户提交题目后。由判题服务接收到题目信息，用户提交信息。然后交给代码沙箱去判断

1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）

2）如果题目提交状态不为等待中，就不用重复执行了

3）更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态

4）调用沙箱，获取到执行结果

5）根据沙箱的执行结果，设置题目的判题状态和信息

#### 策略模式优化

针对不同的情况，定义独立的策略。便于分别修改策略和维护。而不是把所有的判题逻辑、if ... else ... 代码全部混在一起写。

1）定义判题策略接口，让代码更加通用化：

2）定义判题上下文对象，用于定义在策略中传递的参数（可以理解为一种 DTO）：

3）实现默认判题策略，先把 judgeService 中的代码搬运过来

4）再新增一种判题策略，通过 if ... else ... 的方式选择使用哪种策略

但是，如果选择某种判题策略的过程比较复杂，如果都写在调用判题服务的代码中，代码会越来越复杂，会有大量 if ... else ...，所以建议单独编写一个判断策略的类。

5）定义 JudgeManager，目的是尽量简化对判题功能的调用，让调用方写最少的代码、调用最简单。对于判题策略的选取，也是在 JudgeManager 里处理的。



### 判题思路总结

整体步骤：

1. 用户提交题目接口（参数：代码，编程语言，对应的题目id）
2. 对用户提交的的参数进行**合法校验**
    1. 编程语言是否存在（调用枚举类的遍历方法）
    2. 传入的题目id是否存在这个题目
    3. 将题目提交的数据存入`question_submit`表
3. 开始**异步**判题（进入判题服务，ps：这里的判题服务是单独的一个服务）**参数**：题目提交id（`questionSubmitId`）
4. 对于一系列参数（题目提交id，题目，题目提交的状态等）进行**合法校验**
    1. `question_submit`表中是否存在刚刚用户提交的数据（根据`questionSubmitId`查询）
    2. 题目是否存在（在`question_submit`表中，根据`questionId`字段查询`question`表）
    3. 判断用户提交的代码的状态，如果不是**等待中**则就不用继续判题了，直接抛异常
    4. 修改题目的提交状态为**判题中**
5. 调用**代码沙箱**（代码沙箱只负责执行代码，然后返回结果，不负责判题）执行代码
    1. 从配置文件中读取需要使用的**代码沙箱类型**
    2. 使用**工厂模式**，根据不同的代码沙箱类型，来返回不同的代码沙箱实例，初始化**代理模式**中的代码沙箱私有参数
    3. 获取代码沙箱需要的参数（`question`表中的**输入用例**，用户提交的**代码**，**编程语言**，在`question_submit`表）
    4. 传入参数，在代码沙箱中执行代码（在**代理模式**中执行，**方便在所有的代码沙箱执行前后输出日志**）
    5. 返回代码的执行结果（**输出结果集合**，**接口信息**，**执行状态**，**判题信息**：程序执行信息，消耗内存，消耗时间。）
6. 针对代码沙箱返回的结果，开始进行判题（结果是否正确，是否超时，内存溢出等等）
    1. 因为不同的代码，判题逻辑可能有细微的区别，所以使用**策略模式**定义多个判题策略
    2. 使用 `JudgeManager` 根据不同的编程语言选择不同的策略，并执行判题（进入具体的策略类，参数：**判题信息**：程序执行信息，消耗内存，消耗时间。**题目输入用例**，**代码沙箱输出结果集合**，**题目信息**，**题目提交信息**，**输入与输出对应集合**）
    3. 判断沙箱执行的结果输出数量是否和预期输出数量相等，不相等直接返回**答案错误**
    4. 依次判断每一项输出和预期输出是否相等，不相等直接返回**答案错误**
    5. 判断题目限制（是否超时，是否内存溢出等）
    6. 返回结果
7. 修改数据库中的判题结果（修改  `judgeInfo` ，`status` ）





## docker 容器





### 容器安全

1. 超时控制
2. 内存资源
3. 网络资源
4. 权限管理

#### 权限

Docker 容器已经做了系统层面的隔离，比较安全，但不能保证绝对安全。

1）结合 Java 安全管理器和其他策略去使用

2）限制用户不能向 root 根目录写文件：

```java
java复制代码CreateContainerResponse createContainerResponse = containerCmd
        .withHostConfig(hostConfig)
        .withNetworkDisabled(true)
        .withReadonlyRootfs(true)
```

3）Linux 自带的一些安全管理措施，比如 seccomp（Secure Computing Mode）是一个用于 Linux 内核的安全功能，它允许你限制进程可以执行的系统调用，从而减少潜在的攻击面和提高容器的安全性。通过配置 seccomp，你可以控制容器内进程可以使用的系统调用类型和参数。

示例 seccomp 配置文件 profile.json：

```json
json复制代码{
  "defaultAction": "SCMP_ACT_ALLOW",
  "syscalls": [
    {
      "name": "write",
      "action": "SCMP_ACT_ALLOW"
    },
    {
      "name": "read",
      "action": "SCMP_ACT_ALLOW"
    }
  ]
}
```

在 hostConfig 中开启安全机制：

```java
java复制代码String profileConfig = ResourceUtil.readUtf8Str("profile.json");
hostConfig.withSecurityOpts(Arrays.asList("seccomp=" + profileConfig));
```

