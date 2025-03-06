import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Card, Table, Tabs, message, List, Tag, Spin } from 'antd';
import { sparkAPI, hdfsAPI } from '../api';

const { TabPane } = Tabs;

const SparkPage = () => {
    const [form] = Form.useForm();
    const [submitting, setSubmitting] = useState(false);
    const [sparkStatus, setSparkStatus] = useState({});
    const [hdfsFiles, setHdfsFiles] = useState([]);
    const [loadingFiles, setLoadingFiles] = useState(false);
    const [jarPath, setJarPath] = useState('');

    useEffect(() => {
        fetchSparkStatus();
        fetchHdfsJars();
    }, []);

    const fetchSparkStatus = async () => {
        try {
            const response = await sparkAPI.getStatus();
            setSparkStatus(response.data);
        } catch (error) {
            message.error('获取Spark状态失败: ' + error.message);
        }
    };

    const fetchHdfsJars = async () => {
        setLoadingFiles(true);
        try {
            // 可以设置固定的JAR目录，例如 /apps/spark/jars
            const response = await hdfsAPI.listFiles('/apps/spark/jars');
            setHdfsFiles(response.data.filter(file => file.fileName.endsWith('.jar')));
        } catch (error) {
            message.error('获取HDFS JAR文件失败: ' + error.message);
        } finally {
            setLoadingFiles(false);
        }
    };

    const handleSubmit = async (values) => {
        setSubmitting(true);
        try {
            // 将参数拆分为数组
            const args = values.args ? values.args.split(' ') : [];

            const response = await sparkAPI.submitJob(
                values.jarPath,
                values.mainClass,
                args
            );

            message.success('Spark作业提交成功');
            form.resetFields();
            fetchSparkStatus();
        } catch (error) {
            message.error('提交Spark作业失败: ' + error.message);
        } finally {
            setSubmitting(false);
        }
    };

    const handleSelectJar = (fileName) => {
        const jarFullPath = `/apps/spark/jars/${fileName}`;
        setJarPath(jarFullPath);
        form.setFieldsValue({ jarPath: jarFullPath });
    };

    return (
        <div>
            <h2>Spark作业管理</h2>

            <Tabs defaultActiveKey="1">
                <TabPane tab="提交Spark作业" key="1">
                    <Card>
                        <Form
                            form={form}
                            layout="vertical"
                            onFinish={handleSubmit}
                        >
                            <Form.Item
                                name="jarPath"
                                label="JAR文件路径 (HDFS)"
                                rules={[{ required: true, message: '请输入JAR文件路径' }]}
                            >
                                <Input placeholder="例如: /apps/spark/jars/example.jar" value={jarPath} />
                            </Form.Item>

                            <Form.Item
                                name="mainClass"
                                label="主类名称"
                                rules={[{ required: true, message: '请输入主类名称' }]}
                            >
                                <Input placeholder="例如: org.example.SparkWordCount" />
                            </Form.Item>

                            <Form.Item
                                name="args"
                                label="参数 (以空格分隔)"
                            >
                                <Input placeholder="例如: hdfs:///input hdfs:///output" />
                            </Form.Item>

                            <Form.Item>
                                <Button type="primary" htmlType="submit" loading={submitting}>
                                    提交作业
                                </Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </TabPane>

                <TabPane tab="HDFS JAR文件" key="2">
                    <Card>
                        {loadingFiles ? (
                            <Spin tip="加载JAR文件..." />
                        ) : (
                            <List
                                bordered
                                dataSource={hdfsFiles}
                                renderItem={item => (
                                    <List.Item
                                        actions={[
                                            <Button type="link" onClick={() => handleSelectJar(item.fileName)}>
                                                选择
                                            </Button>
                                        ]}
                                    >
                                        {item.fileName}
                                    </List.Item>
                                )}
                            />
                        )}
                    </Card>
                </TabPane>

                <TabPane tab="Spark状态" key="3">
                    <Card>
                        <div style={{ marginBottom: 20 }}>
                            <Button onClick={fetchSparkStatus} style={{ marginRight: 10 }}>
                                刷新
                            </Button>
                        </div>

                        <List bordered>
                            <List.Item>
                                <span>活跃的应用程序数量:</span>
                                <Tag color="green">{sparkStatus.activeApplications || 0}</Tag>
                            </List.Item>
                            <List.Item>
                                <span>已完成的应用程序数量:</span>
                                <Tag color="blue">{sparkStatus.completedApplications || 0}</Tag>
                            </List.Item>
                        </List>
                    </Card>
                </TabPane>
            </Tabs>
        </div>
    );
};

export default SparkPage;