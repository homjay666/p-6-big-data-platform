<dependencies>
    <!-- Presto JDBC驱动 -->
    <dependency>
        <groupId>com.facebook.presto</groupId>
        <artifactId>presto-jdbc</artifactId>
        <version>0.273</version>
    </dependency>
    
    <!-- 用于处理CSV的库 (可选) -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-csv</artifactId>
        <version>1.9.0</version>
    </dependency>
    
    <!-- 日志库 (可选) -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.32</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.10</version>
    </dependency>
</dependencies>


import java.sql.*;
import java.util.*;
import java.io.*;

public class PrestoHiveQueryExample {

    private String prestoUrl;
    private Properties properties;

    /**
     * 初始化Presto连接
     * @param host Presto服务器地址
     * @param port Presto端口（默认8080）
     * @param catalog 目录名（如hive）
     * @param schema 模式名（如default）
     * @param user 用户名
     */
    public PrestoHiveQueryExample(String host, int port, String catalog, String schema, String user) {
        // 构建Presto JDBC URL
        this.prestoUrl = String.format("jdbc:presto://%s:%d/%s/%s", host, port, catalog, schema);
        
        // 设置连接属性
        this.properties = new Properties();
        this.properties.setProperty("user", user);
        
        // 可选：添加其他连接属性
        // this.properties.setProperty("password", "password"); // 如果需要认证
        // this.properties.setProperty("SSL", "true"); // 启用SSL
        // this.properties.setProperty("SSLVerification", "FULL"); // SSL验证模式
    }

    /**
     * 执行SQL查询并返回结果
     * @param sql SQL查询语句
     * @return 查询结果列表
     */
    public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(prestoUrl, properties);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // 获取列名
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            
            // 处理结果集
            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }
        }
        
        return resultList;
    }

    /**
     * 执行分页查询
     * @param sql 基础SQL查询（不包含LIMIT和OFFSET）
     * @param pageSize 每页记录数
     * @param pageNum 页码（从1开始）
     * @return 分页结果
     */
    public List<Map<String, Object>> executePageQuery(String sql, int pageSize, int pageNum) throws SQLException {
        int offset = (pageNum - 1) * pageSize;
        String pagedSql = sql + " LIMIT " + pageSize + " OFFSET " + offset;
        return executeQuery(pagedSql);
    }

    /**
     * 获取查询结果的总记录数
     * @param sql 原始SQL查询
     * @return 总记录数
     */
    public long getQueryCount(String sql) throws SQLException {
        // 构造COUNT查询
        String countSql = "SELECT COUNT(*) AS total_count FROM (" + sql + ") AS query_count";
        
        List<Map<String, Object>> result = executeQuery(countSql);
        if (!result.isEmpty()) {
            return ((Number) result.get(0).get("total_count")).longValue();
        }
        
        return 0;
    }

    /**
     * 将查询结果导出到CSV文件
     * @param sql SQL查询
     * @param outputFile 输出文件路径
     */
    public void exportToCSV(String sql, String outputFile) throws SQLException, IOException {
        List<Map<String, Object>> results = executeQuery(sql);
        
        if (results.isEmpty()) {
            System.out.println("无数据可导出");
            return;
        }
        
        try (FileWriter fileWriter = new FileWriter(outputFile);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            
            // 写入CSV头
            List<String> headers = new ArrayList<>(results.get(0).keySet());
            bufferedWriter.write(String.join(",", headers));
            bufferedWriter.newLine();
            
            // 写入数据行
            for (Map<String, Object> row : results) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    Object value = row.get(header);
                    values.add(value == null ? "" : value.toString().replace(",", "\\,"));
                }
                bufferedWriter.write(String.join(",", values));
                bufferedWriter.newLine();
            }
            
            System.out.println("数据已成功导出到 " + outputFile);
        }
    }

    /**
     * 执行EXPLAIN查询，获取查询计划
     * @param sql SQL查询
     * @return 查询计划
     */
    public String getQueryPlan(String sql) throws SQLException {
        String explainSql = "EXPLAIN " + sql;
        
        StringBuilder planBuilder = new StringBuilder();
        
        try (Connection connection = DriverManager.getConnection(prestoUrl, properties);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(explainSql)) {
            
            while (resultSet.next()) {
                planBuilder.append(resultSet.getString(1)).append("\n");
            }
        }
        
        return planBuilder.toString();
    }

    /**
     * 监控查询执行
     * @param sql SQL查询
     * @return 查询ID和执行时间（毫秒）
     */
    public Map<String, Object> executeQueryWithStats(String sql) throws SQLException {
        long startTime = System.currentTimeMillis();
        String queryId = null;
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(prestoUrl, properties);
             Statement statement = connection.createStatement()) {
            
            // 启用查询进度追踪
            statement.execute("SET SESSION query_max_execution_time = '30m'");
            
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                // 尝试获取查询ID（Presto JDBC驱动特有方法）
                try {
                    if (resultSet.isWrapperFor(com.facebook.presto.jdbc.PrestoResultSet.class)) {
                        com.facebook.presto.jdbc.PrestoResultSet prestoResultSet = 
                            resultSet.unwrap(com.facebook.presto.jdbc.PrestoResultSet.class);
                        queryId = prestoResultSet.getQueryId();
                    }
                } catch (Exception e) {
                    // 可能不支持或无法获取，忽略异常
                }
                
                // 处理结果
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    results.add(row);
                }
            }
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("queryId", queryId);
        stats.put("executionTimeMs", executionTime);
        stats.put("resultCount", results.size());
        stats.put("results", results);
        
        return stats;
    }

    /**
     * 取消正在执行的查询
     * @param queryId 查询ID
     */
    public void cancelQuery(String queryId) throws SQLException {
        if (queryId == null || queryId.isEmpty()) {
            throw new IllegalArgumentException("查询ID不能为空");
        }
        
        try (Connection connection = DriverManager.getConnection(prestoUrl, properties);
             Statement statement = connection.createStatement()) {
            
            statement.execute("CALL system.runtime.kill_query('" + queryId + "', 'User cancelled')");
            System.out.println("查询 " + queryId + " 已被取消");
        }
    }

    /**
     * 示例用法
     */
    public static void main(String[] args) {
        try {
            // 创建查询客户端
            PrestoHiveQueryExample client = new PrestoHiveQueryExample(
                "presto-coordinator.example.com", 8080, "hive", "default", "user");
            
            // 基本查询示例
            String sql = "SELECT * FROM sales_data WHERE year = 2023";
            System.out.println("执行基本查询:");
            List<Map<String, Object>> results = client.executeQuery(sql);
            printResults(results, 10);
            
            // 分页查询示例
            System.out.println("\n执行分页查询 (第2页，每页5条):");
            List<Map<String, Object>> pagedResults = client.executePageQuery(sql, 5, 2);
            printResults(pagedResults, 5);
            
            // 查询总数
            long count = client.getQueryCount(sql);
            System.out.println("\n总记录数: " + count);
            
            // 导出到CSV
            client.exportToCSV(sql, "sales_data_2023.csv");
            
            // 获取查询计划
            System.out.println("\n查询计划:");
            String plan = client.getQueryPlan(sql);
            System.out.println(plan);
            
            // 执行查询并获取统计信息
            System.out.println("\n带统计信息的查询执行:");
            Map<String, Object> statsResult = client.executeQueryWithStats(
                "SELECT region, SUM(amount) AS total FROM sales_data GROUP BY region");
            System.out.println("查询ID: " + statsResult.get("queryId"));
            System.out.println("执行时间: " + statsResult.get("executionTimeMs") + "ms");
            System.out.println("结果数量: " + statsResult.get("resultCount"));
            
            // 可以添加取消查询的示例，但通常需要在另一个线程中执行
            // client.cancelQuery((String) statsResult.get("queryId"));
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 打印结果集
     * @param results 结果列表
     * @param maxRows 最大显示行数
     */
    private static void printResults(List<Map<String, Object>> results, int maxRows) {
        if (results.isEmpty()) {
            System.out.println("无数据");
            return;
        }
        
        // 打印表头
        Map<String, Object> firstRow = results.get(0);
        for (String column : firstRow.keySet()) {
            System.out.print(column + "\t");
        }
        System.out.println();
        
        // 打印分隔线
        for (int i = 0; i < firstRow.size() * 8; i++) {
            System.out.print("-");
        }
        System.out.println();
        
        // 打印数据行
        int rowCount = Math.min(results.size(), maxRows);
        for (int i = 0; i < rowCount; i++) {
            Map<String, Object> row = results.get(i);
            for (Object value : row.values()) {
                System.out.print((value == null ? "NULL" : value) + "\t");
            }
            System.out.println();
        }
        
        // 显示是否有更多行
        if (results.size() > maxRows) {
            System.out.println("... 还有 " + (results.size() - maxRows) + " 行 ...");
        }
    }
}