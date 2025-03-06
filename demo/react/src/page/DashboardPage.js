import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Statistic, Table, Progress, Spin, Button } from 'antd';
import {
    DashboardOutlined,
    CloudOutlined,
    AreaChartOutlined,
    ReloadOutlined
} from '@ant-design/icons';
import { clusterAPI, hdfsAPI } from '../api';

const DashboardPage = () => {
    const [clusterInfo, setClusterInfo] = useState({});
    const [hdfsInfo, setHdfsInfo] = useState({});
    const [nodes, setNodes] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchClusterInfo();
    }, []);

    const fetchClusterInfo = async () => {
        setLoading(true);
        try {
            const [yarnResponse, hdfsResponse, nodesResponse] = await Promise.all([
                clusterAPI.getYarnStatus(),
                hdfsAPI.getHdfsStatus(),
                clusterAPI.getClusterNodes()
            ]);

            setClusterInfo(yarnResponse.data);
            setHdfsInfo(hdfsResponse.data);

            if (nodesResponse.data.nodes && nodesResponse.data.nodes.node) {
                setNodes(nodesResponse.data.nodes.node);
            }
        } catch (error) {
            console.error('获取集群信息失败:', error);
        } finally {
            setLoading(false);
        }
    };

    const formatBytes = (bytes, decimals = 2) => {
        if (!bytes) return '0 Bytes';
        const k = 1024;
        const dm = decimals < 0 ? 0 : decimals;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    };

    const nodeColumns = [
        {
            title: '节点ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '状态',
            dataIndex: 'state',
            key: 'state',
            render: (state) => (
                <span style={{ color: state === 'RUNNING' ? 'green' : 'red' }}>
          {state}
        </span>
            ),
        },
        {
            title: '主机名',
            dataIndex: 'nodeHostName',
            key: 'nodeHostName',
        },
        {
            title: '机架',
            dataIndex: 'rack',
            key: 'rack',
        },
        {
            title: '内存使用',
            key: 'memory',
            render: (_, record) => (
                <Progress
                    percent={Math.round((record.usedMemoryMB / record.availMemoryMB) * 100)}
                    size="small"
                />
            ),
        },
        {
            title: 'CPU使用',
            key: 'vCores',
            render: (_, record) => (
                <Progress
                    percent={Math.round((record.usedVirtualCores / record.availableVirtualCores) * 100)}
                    size="small"
                />
            ),
        },
    ];

    return (
        <div>
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
                <h2>集群概览</h2>
                <Button
                    icon={<ReloadOutlined />}
                    onClick={fetchClusterInfo}
                    loading={loading}
                >
                    刷新
                </Button>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', margin: '50px 0' }}>
                    <Spin size="large" tip="加载集群信息..." />
                </div>
            ) : (
                <>
                    <Row gutter={16} style={{ marginBottom: 16 }}>
                        <Col span={8}>
                            <Card>
                                <Statistic
                                    title="集群节点数"
                                    value={nodes.length}
                                    prefix={<CloudOutlined />}
                                />
                            </Card>
                        </Col>
                        <Col span={8}>
                            <Card>
                                <Statistic
                                    title="应用程序数量"
                                    value={clusterInfo.appsSubmitted || 0}
                                    prefix={<AreaChartOutlined />}
                                />
                            </Card>
                        </Col>
                        <Col span={8}>
                            <Card>
                                <Statistic
                                    title="集群总内存"
                                    value={formatBytes((clusterInfo.totalMB || 0) * 1024 * 1024)}
                                    prefix={<DashboardOutlined />}
                                />
                            </Card>
                        </Col>
                    </Row>

                    <Row gutter={16} style={{ marginBottom: 16 }}>
                        <Col span={12}>
                            <Card title="HDFS存储" bordered={false}>
                                <Row>
                                    <Col span={12}>
                                        <Statistic
                                            title="总容量"
                                            value={formatBytes(hdfsInfo.capacity || 0)}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Statistic
                                            title="已使用"
                                            value={formatBytes(hdfsInfo.used || 0)}
                                        />
                                    </Col>
                                </Row>
                                <div style={{ marginTop: 16 }}>
                                    <Progress
                                        percent={Math.round((hdfsInfo.used / hdfsInfo.capacity) * 100) || 0}
                                        status="active"
                                    />
                                </div>
                            </Card>
                        </Col>
                        <Col span={12}>
                            <Card title="YARN资源" bordered={false}>
                                <Row>
                                    <Col span={12}>
                                        <Statistic
                                            title="总内存"
                                            value={formatBytes((clusterInfo.totalMB || 0) * 1024 * 1024)}
                                        />
                                    </Col>
                                    <Col span={12}>
                                        <Statistic
                                            title="总CPU核心"
                                            value={clusterInfo.totalVirtualCores || 0}
                                        />
                                    </Col>
                                </Row>
                                <div style={{ marginTop: 16 }}>
                                    <Progress
                                        percent={Math.round((clusterInfo.allocatedMB / clusterInfo.totalMB) * 100) || 0}
                                        status="active"
                                    />
                                </div>
                            </Card>
                        </Col>
                    </Row>

                    <Card title="集群节点" style={{ marginBottom: 16 }}>
                        <Table
                            columns={nodeColumns}
                            dataSource={nodes}
                            rowKey="id"
                            pagination={{ pageSize: 5 }}
                        />
                    </Card>
                </>
            )}
        </div>
    );
};

export default DashboardPage;