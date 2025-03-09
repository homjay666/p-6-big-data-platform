# 大数据管理平台

---

[toc]

## 概述

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

- 大数据平台开发阶段

<img src="https://homjay.oss-cn-shanghai.aliyuncs.com/img/2024/04/2024-04-dfe8df0537096cc6fde24e2cd04b3718-cf0ed3a13c.png" alt="">

| 分类       | 特点                                                         |
| ---------- | ------------------------------------------------------------ |
| 大数据平台 | - 提供计算、存储、调度等任务<br />- 统一数据标准，全局规划数据资产 |
| 数据中台   | - 业务数据化，数据业务化<br />- 构建核心数据资产，赋能业务实现数据业务化 |





## 功能模块

### 前端

| 功能名称          | 实现方法                                                   | 技术栈                                          |
| ----------------- | ---------------------------------------------------------- | ----------------------------------------------- |
| 组件化UI框架      | 基于组件化思想构建可复用的界面元素，适应大数据平台复杂布局 | React, Ant Design, Styled-components, LESS      |
| 响应式布局        | 实现不同设备自适应的界面，支持大屏与移动端访问             | CSS Grid, Flexbox, Media Queries, Tailwind CSS  |
| 数据可视化图表    | 集成专业图表库展示数据分析结果，支持交互式操作             | ECharts, AntV (G2Plot/G6), D3.js                |
| 实时数据展示      | 使用WebSocket实现集群状态、任务进度等数据的实时更新        | WebSocket API, RxJS, ahooks                     |
| 状态管理          | 统一管理复杂应用状态，处理组件间通信和数据流               | Redux Toolkit, Context API, Dva                 |
| API集成与数据获取 | 封装后端API调用，处理异步请求和缓存策略                    | Axios, React Query, Umi Request                 |
| 路由与权限控制    | 实现多级路由系统，基于用户权限动态生成菜单和可访问页面     | React Router, Ant Design Pro 权限模型, RBAC     |
| 表单管理与验证    | 构建复杂配置表单，支持动态字段和多步骤向导                 | Ant Design Form, React Hook Form, Yup, formily  |
| 大数据表格优化    | 处理大量数据展示，支持虚拟滚动、分页和复杂筛选             | Ant Design Table, React-Window, Ag-Grid         |
| 代码编辑器集成    | 提供SQL、Python等语言的在线编辑功能                        | Monaco Editor (VS Code核心), Ace Editor         |
| 拖拽式设计器      | 实现工作流、ETL过程可视化编排                              | React DnD, react-flow, X6 (AntV)                |
| 主题与国际化      | 支持明暗主题切换和多语言界面                               | i18next, Ant Design主题系统, CSS变量            |
| 前端性能优化      | 实现代码分割、懒加载和缓存策略，优化大型应用性能           | Webpack/Vite, React.lazy, Suspense, Web Workers |
| 前端构建与部署    | 配置自动化构建流程，支持多环境部署                         | Webpack, Docker, Jenkins, Nginx                 |
| 微前端架构        | 支持多团队协作开发，模块独立部署和运行                     | Qiankun, Module Federation, 微应用加载器        |
| 用户行为分析      | 收集用户操作数据，优化产品体验                             | 埋点系统, 百度统计/友盟, 腾讯前端监控           |

### 后端

| 功能名称                  | 实现方法                                                     | 技术栈                                                       |
| ------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 任务调度系统              | 提供可视化工作流编排，支持定时、依赖触发和事件触发机制       | XXL-Job/DolphinScheduler, Quartz, DAG调度引擎, 分布式锁(Redis) |
| 数据血缘分析              | 解析SQL和作业依赖关系，生成数据流转图谱，支持表/字段级血缘追踪 | Apache Atlas, Hive SQL解析器, AntV G6图分析, 元数据索引      |
| 数据集成工具              | 可视化配置数据源间的ETL流程，支持增量同步和调度策略          | DataX, Sqoop, Kettle, 自定义数据源连接器, 数据同步调度       |
| 可视化与报表              | 基于查询结果构建交互式图表与仪表盘，支持多维分析             | ECharts, DataV, FineReport, Superset集成, 报表模板引擎       |
| 操作审计日志              | 记录所有平台操作行为，提供多维度审计查询和安全分析           | ELK Stack, Canal, 日志采集器, 行为分析引擎, 安全告警         |
| 数据安全与脱敏            | 在数据查询和导出时自动识别敏感信息并进行脱敏处理             | 敏感信息识别引擎, 正则匹配库, 加密算法, 数据水印, 访问控制   |
| Web后端语言SpringBoot     | 使用SpringBoot框架构建RESTful API，集成各种大数据组件客户端  | SpringBoot, Maven, Spring MVC, Spring Security, MySQL        |
| HDFS文件上传与下载        | 通过Hadoop客户端API或WebHDFS REST API实现文件系统操作，提供断点续传功能 | Hadoop客户端, WebHDFS REST API, Apache Commons FileUpload, 异步文件处理 |
| Hadoop集群参数查看        | 调用Hadoop管理接口获取配置参数，支持关键指标可视化           | Hadoop REST API, JMX监控, HttpClient, ECharts                |
| Yarn任务关闭              | 通过ResourceManager API发送任务终止命令，支持批量操作和权限控制 | Yarn REST API, ResourceManager客户端, 任务队列管理           |
| Hive元数据查看            | 访问Hive Metastore获取表结构、分区等元数据信息，集中管理表结构、分区信息，提供数据字典和元数据检索功能 | Hive MetaStore API, JDBC连接, 数据字典管理，Hive Hook        |
| Hive数据SQL查询（Presto） | 集成Presto引擎执行SQL查询，支持结果分页、导出和可视化        | Presto REST API, SQL解析器, 数据分页组件, 查询历史记录       |
| Spark任务提交             | 通过Livy服务或Spark REST API提交各类Spark作业，支持参数配置和资源分配 | Apache Livy, Spark REST API, 作业参数模板, 资源调度管理      |
| Spark作业查看             | 调用Spark History Server和Yarn APIs获取作业状态、日志和资源使用情况 | Spark History Server API, Spark监控指标, 实时日志流, 阿里云日志服务 |



## 参考链接

| 资源名称 | 地址                                          |      |
| -------- | --------------------------------------------- | ---- |
| 慕课网   | https://coding.imooc.com/class/653.html       |      |
| 资源站   | https://www.ukoou.com/resource/1557/mk-dsj-qn |      |
|          |                                               |      |



