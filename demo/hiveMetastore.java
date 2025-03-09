<dependencies>
    <!-- Hive Metastore 客户端 -->
    <dependency>
        <groupId>org.apache.hive</groupId>
        <artifactId>hive-metastore</artifactId>
        <version>3.1.2</version>
    </dependency>
    
    <!-- Hadoop Common -->
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-common</artifactId>
        <version>3.1.1</version>
    </dependency>
    
    <!-- Hive Exec (可能需要，取决于操作) -->
    <dependency>
        <groupId>org.apache.hive</groupId>
        <artifactId>hive-exec</artifactId>
        <version>3.1.2</version>
    </dependency>
</dependencies>


import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.*;
import org.apache.thrift.TException;

import java.util.List;

public class HiveMetastoreExample {

    private HiveMetaStoreClient client;

    public HiveMetastoreExample() {
        try {
            // 创建Hive配置
            HiveConf hiveConf = new HiveConf();
            
            // 设置metastore地址
            hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, "thrift://metastore-host:9083");
            
            // 可选：设置其他配置
            // hiveConf.setVar(HiveConf.ConfVars.METASTORE_USE_THRIFT_SASL, "true"); // 启用Kerberos认证
            // hiveConf.setVar(HiveConf.ConfVars.METASTORE_KERBEROS_PRINCIPAL, "hive/_HOST@EXAMPLE.COM");
            
            // 创建客户端连接
            client = new HiveMetaStoreClient(hiveConf);
            System.out.println("成功连接到Hive Metastore");
        } catch (MetaException e) {
            System.err.println("连接Hive Metastore失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有数据库列表
     */
    public List<String> getAllDatabases() throws TException {
        return client.getAllDatabases();
    }

    /**
     * 获取指定数据库中的所有表
     */
    public List<String> getAllTables(String dbName) throws TException {
        return client.getAllTables(dbName);
    }

    /**
     * 获取表的详细信息
     */
    public Table getTable(String dbName, String tableName) throws TException {
        return client.getTable(dbName, tableName);
    }

    /**
     * 获取表的列信息
     */
    public List<FieldSchema> getTableColumns(String dbName, String tableName) throws TException {
        return client.getSchema(dbName, tableName);
    }

    /**
     * 获取表的分区信息
     */
    public List<Partition> getTablePartitions(String dbName, String tableName) throws TException {
        return client.listPartitions(dbName, tableName, (short) -1);
    }

    /**
     * 查询表的存储位置
     */
    public String getTableLocation(String dbName, String tableName) throws TException {
        Table table = client.getTable(dbName, tableName);
        return table.getSd().getLocation();
    }

    /**
     * 打印表的元数据信息
     */
    public void printTableMetadata(String dbName, String tableName) throws TException {
        Table table = getTable(dbName, tableName);
        System.out.println("===== 表信息 =====");
        System.out.println("表名: " + table.getTableName());
        System.out.println("数据库: " + table.getDbName());
        System.out.println("创建时间: " + table.getCreateTime());
        System.out.println("所有者: " + table.getOwner());
        System.out.println("表类型: " + table.getTableType());
        System.out.println("存储位置: " + table.getSd().getLocation());
        System.out.println("输入格式: " + table.getSd().getInputFormat());
        System.out.println("输出格式: " + table.getSd().getOutputFormat());

        System.out.println("\n===== 列信息 =====");
        List<FieldSchema> columns = table.getSd().getCols();
        for (FieldSchema column : columns) {
            System.out.println(column.getName() + " (" + column.getType() + "): " + column.getComment());
        }

        System.out.println("\n===== 分区键 =====");
        List<FieldSchema> partitionKeys = table.getPartitionKeys();
        if (partitionKeys != null && !partitionKeys.isEmpty()) {
            for (FieldSchema partKey : partitionKeys) {
                System.out.println(partKey.getName() + " (" + partKey.getType() + ")");
            }
        } else {
            System.out.println("该表没有分区");
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (client != null) {
            client.close();
            System.out.println("已关闭Hive Metastore连接");
        }
    }

    public static void main(String[] args) {
        HiveMetastoreExample example = new HiveMetastoreExample();
        
        try {
            // 获取所有数据库
            List<String> databases = example.getAllDatabases();
            System.out.println("数据库列表: " + databases);
            
            // 选择一个数据库查看所有表
            String dbName = "default";
            List<String> tables = example.getAllTables(dbName);
            System.out.println(dbName + "数据库中的表: " + tables);
            
            // 如果有表，查看第一个表的详细信息
            if (!tables.isEmpty()) {
                String tableName = tables.get(0);
                example.printTableMetadata(dbName, tableName);
                
                // 查询分区信息
                List<Partition> partitions = example.getTablePartitions(dbName, tableName);
                System.out.println("\n===== 分区信息 =====");
                if (partitions != null && !partitions.isEmpty()) {
                    System.out.println("分区数量: " + partitions.size());
                    for (int i = 0; i < Math.min(5, partitions.size()); i++) {
                        Partition p = partitions.get(i);
                        System.out.println("分区" + (i+1) + ": " + p.getValues() + 
                                          ", 位置: " + p.getSd().getLocation());
                    }
                    if (partitions.size() > 5) {
                        System.out.println("... 更多分区省略 ...");
                    }
                }
            }
        } catch (TException e) {
            System.err.println("操作Hive Metastore时出错: " + e.getMessage());
            e.printStackTrace();
        } finally {
            example.close();
        }
    }
}