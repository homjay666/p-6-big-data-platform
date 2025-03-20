# 后端

---

[TOC]





## 最佳实践

### 与第三方服务通信的典型方式

> [!CAUTION]
>
> ###### 最佳实践建议
>
> 1. **生产环境核心系统**：优先使用原生SDK方式，获得最佳性能和安全性
> 2. **跨平台或微服务场景**：考虑REST API方式，牺牲部分性能换取更好的兼容性
> 3. **运维或临时任务**：命令行方式简单直接，但不建议用于关键业务系统

#### 命令行调用方式

> **原理**：通过Shell执行命令与外部服务交互
>
> **适合场景**：==适用于临时脚本或运维工具==

**优缺点**：

- ✅ 实现简单，无需额外依赖
- ❌ 性能较差，需创建子进程
- ❌ 安全隐患（命令注入风险）
- ❌ 错误处理复杂
- ❌ 依赖HDFS客户端安装

```java
public List<String> listHdfsFilesViaShell() throws Exception {
    List<String> fileNames = new ArrayList<>();
    
    ProcessBuilder pb = new ProcessBuilder("hdfs", "dfs", "-ls", "/tmp");
    Process process = pb.start();
    
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("Found") && !line.trim().isEmpty()) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 8) {
                    fileNames.add(parts[parts.length - 1]);
                }
            }
        }
    }
    
    int exitCode = process.waitFor();
    if (exitCode != 0) {
        throw new RuntimeException("Command execution failed: " + exitCode);
    }
    
    return fileNames;
}
```

#### :100: ​原生SDK方式

> **原理**：使用专门的SDK/客户端库直接通信
>
> **适合场景**：==在企业生产环境中，大多数情况下首选使用方法==

**优缺点**：

- ✅ 性能最佳，直接通过API调用
- ✅ 错误处理完善
- ✅ 支持Kerberos安全认证
- ✅ 获取详细的文件元数据
- ✅ 企业级应用首选
- ❌ 需要导入Hadoop依赖
- ❌ 配置相对复杂

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public List<String> listHdfsFilesViaAPI() {
    List<String> fileNames = new ArrayList<>();
    Logger logger = LoggerFactory.getLogger(getClass());
    
    try {
        // 加载配置
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://namenode:8020");
        
        // Kerberos认证（如需要）
        if (conf.getBoolean("hadoop.security.authentication.enabled", false)) {
            conf.set("hadoop.security.authentication", "kerberos");
            UserGroupInformation.setConfiguration(conf);
            UserGroupInformation.loginUserFromKeytab(
                "hdfs_user@EXAMPLE.COM", "/path/to/hdfs_user.keytab");
        }
        
        // 获取文件系统
        try (FileSystem fs = FileSystem.get(conf)) {
            // 读取目录内容
            RemoteIterator<LocatedFileStatus> fileIterator = 
                fs.listFiles(new Path("/tmp"), false);
            
            while (fileIterator.hasNext()) {
                LocatedFileStatus status = fileIterator.next();
                fileNames.add(status.getPath().getName());
                logger.debug("Found file: {}", status.getPath().getName());
            }
        }
    } catch (IOException e) {
        logger.error("Error accessing HDFS", e);
        throw new RuntimeException("Failed to list HDFS files", e);
    }
    
    return fileNames;
}
```



#### REST API 接口方式

> **原理**：利用服务提供的REST API进行通信
>
> **适用场景**：==适用于微服务架构或非Java应用==

**优缺点**：

- ✅ 支持跨平台和跨语言访问
- ✅ 适合微服务架构
- ✅ 不需要HDFS客户端
- ❌ 性能较差（HTTP开销）
- ❌ 安全配置复杂

```java
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

public List<String> listHdfsFilesViaWebHDFS() throws Exception {
    List<String> fileNames = new ArrayList<>();
    String webhdfsUrl = "http://namenode:50070/webhdfs/v1/tmp?op=LISTSTATUS";
    
    URL url = new URL(webhdfsUrl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    
    int responseCode = conn.getResponseCode();
    if (responseCode == 200) {
        String response = IOUtils.toString(conn.getInputStream(), "UTF-8");
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject fileStatuses = jsonResponse.getJSONObject("FileStatuses");
        JSONArray fileStatusArray = fileStatuses.getJSONArray("FileStatus");
        
        for (int i = 0; i < fileStatusArray.length(); i++) {
            JSONObject fileStatus = fileStatusArray.getJSONObject(i);
            fileNames.add(fileStatus.getString("pathSuffix"));
        }
    } else {
        throw new RuntimeException("HTTP error: " + responseCode);
    }
    
    return fileNames;
}
```

#### RPC通信

> **原理**：直接调用远程服务器上的方法
>
> **代表技术**：gRPC、Dubbo、Thrift 
>
> **适用场景**：微服务内部高性能通信、同构系统

**优缺点**：

- ✅ 性能极高、强类型接口定义
- ✅ 支持多语言、IDL定义清晰
- ❌ 耦合度相对较高、需要专用客户端

```java
// gRPC客户端示例
public Response callService(Request request) {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress("service.example.com", 9090)
        .usePlaintext()
        .build();
    
    try {
        ServiceGrpc.ServiceBlockingStub stub = ServiceGrpc.newBlockingStub(channel);
        return stub.process(request);
    } finally {
        channel.shutdown();
    }
}
```


#### WebSocket实时通信

> **原理**：建立持久连接进行双向通信 
>
> **适用场景**：实时应用、聊天、推送、监控

**优缺点**：

- ✅ 低延迟、双向通信
- ✅ 减少连接开销、实时性强
- ❌ 连接维护成本高、长连接管理复杂

```java
// WebSocket客户端示例
@ClientEndpoint
public class WebSocketClient {
    private Session session;
    
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }
    
    @OnMessage
    public void onMessage(String message) {
        System.out.println("收到消息: " + message);
    }
    
    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
    
    public void connect() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI("ws://example.com/socket"));
    }
}
```


#### 消息队列/中间件通信

> **原理**：通过消息队列中间件异步传递数据 
>
> **适用场景**：系统解耦、异步处理、削峰填谷

**优缺点**：

- ✅ 系统解耦、异步通信、高吞吐量
- ✅ 提高系统弹性和可用性
- ❌ 增加系统复杂度、一致性保证难度增加

```java
// 订单服务
public void createOrder(Order order) {
    orderRepository.save(order);
    rabbitTemplate.convertAndSend("order:processing", order);
}

// 订单处理服务
@Service
public class OrderProcessor {
    @RabbitListener(queues = "order:processing")
    public void processOrder(Order order) {
        // 检查库存
        boolean stockAvailable = inventoryService.checkAndReserveStock(order);
        if (stockAvailable) {
            paymentService.processPayment(order);
            // 只有一个服务处理此消息，实现任务分派
        }
    }
}
```



#### 发布订阅模式

> **原理**：发布方发送消息到主题，订阅方接收主题消息 
>
> **适用场景**：事件驱动架构、多系统协同

**优缺点**：

- ✅ 低耦合、动态消息路由
- ✅ 支持一对多通信模式
- ❌ 消息可靠性挑战、故障追踪复杂

```java
// 告警发送端
public void sendAlert(AlertLevel level, String message, String system) {
    AlertEvent alert = new AlertEvent(level, message, system, System.currentTimeMillis());
    kafkaTemplate.send("system:alerts", objectMapper.writeValueAsString(alert));
}

// 多种订阅者处理同一告警
public class EmailAlertHandler {
    @KafkaListener(topics = "system:alerts")
    public void processAlert(String alertJson) {
        AlertEvent alert = objectMapper.readValue(alertJson, AlertEvent.class);
        if (alert.getLevel() == AlertLevel.CRITICAL) {
            emailService.sendUrgentEmail(alert);
        }
    }
}

public class SMSAlertHandler {
    @KafkaListener(topics = "system:alerts")
    public void processAlert(String alertJson) {
        AlertEvent alert = objectMapper.readValue(alertJson, AlertEvent.class);
        if (alert.getLevel() == AlertLevel.CRITICAL) {
            smsService.sendEmergencySMS(alert);
        }
    }
}
```

==消息队列/中间件通信 和 发布订阅模式 核心区别==

| 特性       | 发布订阅模式 (Pub/Sub)         | 消息队列 (Message Queue)   |
| ---------- | ------------------------------ | -------------------------- |
| 消息分发   | 一对多(广播)                   | 通常是点对点               |
| 消费方式   | 多个订阅者接收相同消息         | 单个消费者处理每条消息     |
| 消息持久性 | 传统实现中通常是瞬态的         | 通常持久化存储直到处理     |
| 消费确认   | 通常没有ACK机制                | 有确认(ACK)机制确保处理    |
| 负载均衡   | 不支持(每个订阅者收到全部消息) | 支持(消息分散给多个消费者) |



### 与第三方服务通信典型应用场景

==命令行调用方式场景==

| 场景           | 场景描述                                                |
| -------------- | ------------------------------------------------------- |
| 系统运维自动化 | 运维脚本通过SSH远程执行服务器命令进行批量部署或配置更新 |
| 数据导入导出   | 通过调用数据库命令行工具`mysql`执行数据备份或迁移操作   |
| 定时任务执行   | 通过cron服务调用命令行工具执行周期性数据处理任务        |

==原生SDK方式场景==

| 场景           | 场景描述                                           |
| -------------- | -------------------------------------------------- |
| 云服务资源管理 | 使用AWS/阿里云SDK创建、配置和管理云计算资源        |
| 支付系统集成   | 使用支付平台SDK集成第三方支付功能到应用中          |
| 数据分析处理   | 使用大数据处理框架SDK(如Hadoop、Spark)进行数据分析 |

==REST API 接口方式场景==

| 场景           | 场景描述                                        |
| -------------- | ----------------------------------------------- |
| 跨平台应用集成 | 电商平台前端与后端系统之间的数据交互            |
| 第三方服务整合 | 集成第三方地图服务API提供位置搜索和路线规划功能 |
| 微服务间通信   | 微服务架构中不同服务之间的状态同步和数据交换    |

==RPC通信场景==

| 场景           | 场景描述                               |
| -------------- | -------------------------------------- |
| 微服务内部调用 | 同一业务域内多个微服务之间的高性能调用 |
| 分布式事务处理 | 跨服务的事务协调和状态同步             |
| 高性能服务调用 | 数据密集型应用中的计算密集型服务调用   |

==WebSocket实时通信场景==

| 场景           | 场景描述                                 |
| -------------- | ---------------------------------------- |
| 实时协作应用   | 在线文档编辑器中多用户同时编辑的内容同步 |
| 实时通讯应用   | 在线聊天应用中的消息即时推送和状态更新   |
| 监控数据可视化 | 运维监控系统实时数据推送和展示           |

==发布订阅模式 (Pub/Sub)场景==

| 场景           | 场景描述                                                   |
| -------------- | ---------------------------------------------------------- |
| 实时数据更新   | 股票交易平台向所有关注特定股票的客户端推送价格变化         |
| 系统监控告警   | 监控系统检测到异常时向所有运维人员发送告警                 |
| 多系统数据同步 | 电商平台产品信息变更后同步到搜索引擎、推荐系统、移动应用等 |

==消息队列场景==

| 场景               | 场景描述                                                 |
| ------------------ | -------------------------------------------------------- |
| 任务处理与工作队列 | 电商平台下单后异步处理订单履行、库存更新、发票生成等任务 |
| 流量削峰填谷       | 抢购活动时处理大量订单请求                               |
| 可靠的异步处理     | 用户注册后发送欢迎邮件、触发积分发放、初始化用户画像     |
|                    |                                                          |

