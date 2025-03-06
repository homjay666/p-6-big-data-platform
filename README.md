# p-6-bigdata-data-platform

---

[toc]

## 一、概述

> [!TIP]
>
> **核心观点**：方向内求**深度**，领域内求**广度**
>
> **数据中台与大数据平台的关系**：大数据平台是数据中台技术基座，为数据中台提供核心能力

- 大数据平台层次介绍

<img src="https://homjay.oss-cn-shanghai.aliyuncs.com/img/2024/04/2024-04-2ca039b870ef3046fbd7658d9492726b-da3d981fa1.png" alt="">

- 大数据岗位介绍

<img src="https://homjay.oss-cn-shanghai.aliyuncs.com/img/2024/04/2024-04-c6bcc71615532b6dd09eeea12f361a4f-13a990df41.png" alt="">

- 大数据平台与数据中台的关系

<img src="https://homjay.oss-cn-shanghai.aliyuncs.com/img/2024/04/2024-04-ae4f5e9569d09f8afb83dc12757be5fb-fc47d6abf8.png" alt="">

| 分类       | 特点                                                         |
| ---------- | ------------------------------------------------------------ |
| 大数据平台 | - 提供计算、存储、调度等任务<br />- 统一数据标准，全局规划数据资产 |
| 数据中台   | - 业务数据化，数据业务化<br />- 构建核心数据资产，赋能业务实现数据业务化 |



## 二、阶段

<img src="https://homjay.oss-cn-shanghai.aliyuncs.com/img/2024/04/2024-04-dfe8df0537096cc6fde24e2cd04b3718-cf0ed3a13c.png" alt="">

### 阶段一:做集群的管理者

> [!TIP]
>
> **对应岗位**：引擎研发工程师













### 阶段二:做数据处理的执行者

> [!TIP]
>
> **对应岗位**：平台研发工程师













### 阶段三:做价值发现的引领者

> [!TIP]
>
> **对应岗位**：数据应用研发工程师















## 参考链接

| 资源名称 | 地址                                          |      |
| -------- | --------------------------------------------- | ---- |
| 慕课网   | https://coding.imooc.com/class/653.html       |      |
| 资源站   | https://www.ukoou.com/resource/1557/mk-dsj-qn |      |
|          |                                               |      |



## 技术栈

### Spring

> Spring 常用的注解。这些注解分为几个主要类别：

| 类别        | 注解                     | 用途                    |
| ----------- | ------------------------ | ----------------------- |
| 组件注解    | @Component               | 通用的组件标记          |
|             | @Service                 | 标记服务层组件          |
|             | @Repository              | 标记数据访问层组件      |
|             | @Controller              | 标记 Spring MVC 控制器  |
|             | @RestController          | RESTful Web 服务控制器  |
| 配置相关    | @Configuration           | 标记配置类              |
|             | @Bean                    | 声明一个 bean           |
|             | @ComponentScan           | 指定要扫描的包          |
|             | @PropertySource          | 指定属性文件位置        |
| 依赖注入    | @Autowired               | 自动装配依赖            |
|             | @Qualifier               | 指定注入 bean 的名称    |
|             | @Value                   | 注入属性值              |
|             | @Resource                | 按名称注入依赖          |
| AOP         | @Aspect                  | 声明切面                |
|             | @Before, @After, @Around | 定义通知                |
|             | @Pointcut                | 定义切点                |
| 事务管理    | @Transactional           | 声明事务边界            |
| Spring MVC  | @RequestMapping          | 映射 Web 请求           |
|             | @PathVariable            | 获取 URL 中的参数       |
|             | @RequestParam            | 获取请求参数            |
|             | @RequestBody             | 获取请求体              |
|             | @ResponseBody            | 将返回值序列化到响应体  |
| 验证        | @Valid                   | 触发验证                |
|             | @NotNull, @Size, 等      | 定义验证规则            |
| 条件注解    | @Conditional             | 条件化 bean 创建        |
|             | @Profile                 | 基于 profile 的条件创建 |
| Spring Boot | @SpringBootApplication   | 启动类注解              |
|             | @EnableAutoConfiguration | 启用自动配置            |
|             | @ConfigurationProperties | 绑定配置属性            |



### Mybatis

| 注解     | 用途                                    |
| -------- | --------------------------------------- |
| @Select  | 用于查询操作，定义 SELECT 语句          |
| @Insert  | 用于插入操作，定义 INSERT 语句          |
| @Update  | 用于更新操作，定义 UPDATE 语句          |
| @Delete  | 用于删除操作，定义 DELETE 语句          |
| @Mapper  | 标记该接口为 MyBatis 的映射器           |
| @Param   | 为 SQL 语句中的参数指定名称             |
| @Results | 用于映射结果集到 Java 对象              |
| @Result  | 在 @Results 注解中使用，映射单个字段    |
| @One     | 实现一对一关系映射                      |
| @Many    | 实现一对多关系映射                      |
| @Options | 提供附加的配置选项，如 useGeneratedKeys |

### Lombok

| 注解                | 用途                                                         |
| ------------------- | ------------------------------------------------------------ |
| @Getter             | 自动生成所有字段的 getter 方法                               |
| @Setter             | 自动生成所有字段的 setter 方法                               |
| @Data               | 组合注解，包括 @ToString, @EqualsAndHashCode, @Getter, @Setter 和 @RequiredArgsConstructor |
| @NoArgsConstructor  | 生成无参构造函数                                             |
| @AllArgsConstructor | 生成包含所有参数的构造函数                                   |
| @Builder            | 实现建造者模式                                               |
| @ToString           | 自动生成 toString() 方法                                     |
| @EqualsAndHashCode  | 自动生成 equals() 和 hashCode() 方法                         |
| @Slf4j              | 自动生成该类的 Logger 字段                                   |
| @NonNull            | 自动在方法或构造函数中对该参数进行空值检查                   |
| @Value              | 创建不可变类，所有字段默认为 private 和 final                |
| @SneakyThrows       | 自动捕获并重新抛出已检查的异常                               |





### Swagger

| 注解               | 用途                                            |
| ------------------ | ----------------------------------------------- |
| @Api               | 用于类上，表示这个类是 Swagger 资源             |
| @ApiOperation      | 用于方法上，描述一个 API 操作                   |
| @ApiParam          | 用于方法参数上，描述一个参数                    |
| @ApiModel          | 用于类上，描述一个用于请求或响应的对象模型      |
| @ApiModelProperty  | 用于模型类的属性上，描述模型属性                |
| @ApiResponse       | 描述一个 API 操作可能的响应                     |
| @ApiResponses      | 用于方法上，包含多个 @ApiResponse 注解          |
| @ApiIgnore         | 用于类、方法或参数上，指示 Swagger 忽略这个元素 |
| @ApiImplicitParam  | 描述一个未定义在方法参数中的参数                |
| @ApiImplicitParams | 用于方法上，包含多个 @ApiImplicitParam 注解     |

```java
@Api(tags = "用户管理")
@RestController
@RequestMapping("/users")
public class UserController {

    @ApiOperation(value = "获取用户信息", notes = "根据用户ID获取用户信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "成功获取用户信息"),
        @ApiResponse(code = 404, message = "用户不存在")
    })
    @GetMapping("/{id}")
    public User getUser(@ApiParam(value = "用户ID", required = true) @PathVariable Long id) {
        // 方法实现
    }

    @ApiOperation(value = "创建新用户")
    @PostMapping
    public User createUser(@ApiParam(value = "用户信息", required = true) @RequestBody @Valid UserDTO userDTO) {
        // 方法实现
    }
}

@ApiModel(description = "用户数据传输对象")
public class UserDTO {
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    // getter 和 setter
}
```





### Maven

### 命令

| 类别         | 参数                      | 描述                   |
| ------------ | ------------------------- | ---------------------- |
| 构建生命周期 | clean                     | 清理构建产生的文件     |
|              | compile                   | 编译源代码             |
|              | test                      | 运行测试               |
|              | package                   | 打包编译后的代码       |
|              | install                   | 安装包到本地仓库       |
|              | deploy                    | 部署到远程仓库         |
| 跳过阶段     | -DskipTests               | 跳过测试运行           |
|              | -Dmaven.test.skip=true    | 跳过测试编译和运行     |
|              | -Dmaven.javadoc.skip=true | 跳过 Javadoc 生成      |
| 项目信息     | -Dversion                 | 指定版本               |
|              | -DgroupId                 | 指定 groupId           |
|              | -DartifactId              | 指定 artifactId        |
| 日志和输出   | -X 或 --debug             | 开启调试输出           |
|              | -q 或 --quiet             | 安静模式，只输出错误   |
|              | -e                        | 显示详细错误信息       |
| 依赖管理     | dependency:tree           | 显示依赖树             |
|              | dependency:analyze        | 分析依赖               |
| 多模块项目   | -pl                       | 构建指定的模块         |
|              | -am                       | 同时构建依赖模块       |
| 配置文件     | -P                        | 激活指定的 profile     |
| 仓库相关     | -U                        | 强制检查更新           |
|              | -o                        | 离线模式               |
| 其他         | -T                        | 指定线程数             |
|              | -rf                       | 从指定模块重新开始构建 |

```SHELL
# 清理、编译和打包项目
mvn clean compile package

# 跳过测试并安装
mvn install -DskipTests

# 显示依赖树
mvn dependency:tree

# 构建特定模块及其依赖
mvn -pl module1 -am package

# 激活特定配置文件并部署
mvn -P production deploy
```

###  pom.xml

| 元素                       | 描述         | 示例                                                       |
| -------------------------- | ------------ | ---------------------------------------------------------- |
| `<project>`                | 根元素       | `<project xmlns="http://maven.apache.org/POM/4.0.0">`      |
| `<modelVersion>`           | POM 模型版本 | `<modelVersion>4.0.0</modelVersion>`                       |
| `<groupId>`                | 项目组 ID    | `<groupId>com.example</groupId>`                           |
| `<artifactId>`             | 项目 ID      | `<artifactId>my-project</artifactId>`                      |
| `<version>`                | 项目版本     | `<version>1.0-SNAPSHOT</version>`                          |
| `<packaging>`              | 打包类型     | `<packaging>jar</packaging>`                               |
| `<name>`                   | 项目名称     | `<name>My Project</name>`                                  |
| `<description>`            | 项目描述     | `<description>This is my project</description>`            |
| `<properties>`             | 自定义属性   | `<properties><java.version>11</java.version></properties>` |
| `<dependencies>`           | 项目依赖     | `<dependencies>...</dependencies>`                         |
| `<dependency>`             | 单个依赖     | `<dependency>...</dependency>`                             |
| `<build>`                  | 构建配置     | `<build>...</build>`                                       |
| `<plugins>`                | 插件列表     | `<plugins>...</plugins>`                                   |
| `<plugin>`                 | 单个插件     | `<plugin>...</plugin>`                                     |
| `<repositories>`           | 仓库配置     | `<repositories>...</repositories>`                         |
| `<profiles>`               | 构建配置文件 | `<profiles>...</profiles>`                                 |
| `<parent>`                 | 父 POM       | `<parent>...</parent>`                                     |
| `<modules>`                | 子模块列表   | `<modules>...</modules>`                                   |
| `<scm>`                    | 源代码管理   | `<scm>...</scm>`                                           |
| `<distributionManagement>` | 部署配置     | `<distributionManagement>...</distributionManagement>`     |

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>my-project</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>My Project</name>
  <description>This is my project</description>

  <properties>
    <java.version>11</java.version>
    <spring.version>5.3.10</spring.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>${spring.version}</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
          <source>${java.version}</source>
          <target>${java.version}</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

