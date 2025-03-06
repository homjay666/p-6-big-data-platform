import React, { useState, useEffect } from 'react';
import { Input, Button, Table, Tabs, message, Select, Spin, Card } from 'antd';
import { hiveAPI } from '../api';

const { TabPane } = Tabs;
const { TextArea } = Input;
const { Option } = Select;

const HivePage = () => {
    const [sql, setSql] = useState('');
    const [queryResult, setQueryResult] = useState([]);
    const [columns, setColumns] = useState([]);
    const [loading, setLoading] = useState(false);
    const [tables, setTables] = useState([]);
    const [selectedTable, setSelectedTable] = useState(null);
    const [tableSchema, setTableSchema] = useState([]);
    const [loadingSchema, setLoadingSchema] = useState(false);

    useEffect(() => {
        fetchTables();
    }, []);

    const fetchTables = async () => {
        try {
            const response = await hiveAPI.showTables();
            setTables(response.data);
        } catch (error) {
            message.error('获取表列表失败: ' + error.message);
        }
    };

    const handleExecuteQuery = async () => {
        if (!sql.trim()) {
            message.error('请输入SQL语句');
            return;
        }

        setLoading(true);
        try {
            const response = await hiveAPI.executeQuery(sql);
            const result = response.data.data;

            if (result.length > 0) {
                const columnKeys = Object.keys(result[0]);
                const tableColumns = columnKeys.map(key => ({
                    title: key,
                    dataIndex: key,
                    key: key,
                    ellipsis: true,
                }));

                setColumns(tableColumns);
                setQueryResult(result);
                message.success('查询执行成功');
            } else {
                setColumns([]);
                setQueryResult([]);
                message.success('查询执行成功，但没有返回结果');
            }
        } catch (error) {
            message.error('查询执行失败: ' + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

    const handleSelectTable = async (tableName) => {
        setSelectedTable(tableName);
        setLoadingSchema(true);

        try {
            const response = await hiveAPI.describeTable(tableName);
            setTableSchema(response.data);
        } catch (error) {
            message.error('获取表结构失败: ' + error.message);
        } finally {
            setLoadingSchema(false);
        }
    };

    const schemaColumns = [
        {
            title: '列名',
            dataIndex: 'col_name',
            key: 'col_name',
        },
        {
            title: '数据类型',
            dataIndex: 'data_type',
            key: 'data_type',
        },
        {
            title: '注释',
            dataIndex: 'comment',
            key: 'comment',
        },
    ];

    return (
        <div>
            <h2>Hive SQL查询</h2>

            <Tabs defaultActiveKey="1">
                <TabPane tab="SQL查询" key="1">
                    <Card>
                        <div style={{ marginBottom: 16 }}>
                            <TextArea
                                rows={6}
                                value={sql}
                                onChange={(e) => setSql(e.target.value)}
                                placeholder="输入Hive SQL查询，例如: SELECT * FROM employees LIMIT 10;"
                            />
                        </div>

                        <div style={{ marginBottom: 16 }}>
                            <Button type="primary" onClick={handleExecuteQuery} loading={loading}>
                                执行查询
                            </Button>
                        </div>

                        {loading ? (
                            <div style={{ textAlign: 'center', margin: '20px 0' }}>
                                <Spin tip="查询执行中..." />
                            </div>
                        ) : (
                            <Table
                                columns={columns}
                                dataSource={queryResult}
                                scroll={{ x: true }}
                                bordered
                                pagination={{ pageSize: 10 }}
                            />
                        )}
                    </Card>
                </TabPane>

                <TabPane tab="表结构浏览" key="2">
                    <Card>
                        <div style={{ marginBottom: 16 }}>
                            <Select
                                style={{ width: 300 }}
                                placeholder="选择表"
                                onChange={handleSelectTable}
                                value={selectedTable}
                            >
                                {tables.map((table) => (
                                    <Option key={table} value={table}>
                                        {table}
                                    </Option>
                                ))}
                            </Select>
                        </div>

                        {loadingSchema ? (
                            <div style={{ textAlign: 'center', margin: '20px 0' }}>
                                <Spin tip="加载表结构..." />
                            </div>
                        ) : (
                            selectedTable && (
                                <Table
                                    columns={schemaColumns}
                                    dataSource={tableSchema}
                                    rowKey="col_name"
                                    pagination={false}
                                    bordered
                                />
                            )
                        )}
                    </Card>
                </TabPane>
            </Tabs>
        </div>
    );
};

export default HivePage;