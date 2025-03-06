import React, { useState, useEffect } from 'react';
import { Table, Button, Input, Space, Breadcrumb, message, Statistic, Card, Row, Col } from 'antd';
import { FolderOutlined, FileOutlined, ArrowUpOutlined } from '@ant-design/icons';
import { hdfsAPI } from '../api';

const HDFSPage = () => {
    const [currentPath, setCurrentPath] = useState('/');
    const [fileList, setFileList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [hdfsStatus, setHdfsStatus] = useState({});

    const fetchFileList = async (path) => {
        setLoading(true);
        try {
            const response = await hdfsAPI.listFiles(path);
            setFileList(response.data);
            setCurrentPath(path);
        } catch (error) {
            message.error('获取文件列表失败: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchHdfsStatus = async () => {
        try {
            const response = await hdfsAPI.getHdfsStatus();
            setHdfsStatus(response.data);
        } catch (error) {
            message.error('获取HDFS状态失败: ' + error.message);
        }
    };

    useEffect(() => {
        fetchFileList(currentPath);
        fetchHdfsStatus();
    }, []);

    const handleNavigate = (path) => {
        fetchFileList(path);
    };

    const handlePathChange = (e) => {
        if (e.key === 'Enter') {
            fetchFileList(e.target.value);
        }
    };

    const formatBytes = (bytes, decimals = 2) => {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const dm = decimals < 0 ? 0 : decimals;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    };

    const columns = [
        {
            title: '名称',
            dataIndex: 'fileName',
            key: 'fileName',
            render: (text, record) => (
                <Space>
                    {record.isDirectory ? (
                        <FolderOutlined style={{ color: '#1890ff' }} />
                    ) : (
                        <FileOutlined />
                    )}
                    {record.isDirectory ? (
                        <a onClick={() => handleNavigate(`${currentPath === '/' ? '' : currentPath}/${text}`)}>{text}</a>
                    ) : (
                        text
                    )}
                </Space>
            ),
        },
        {
            title: '大小',
            dataIndex: 'fileSize',
            key: 'fileSize',
            render: (text) => formatBytes(text),
        },
        {
            title: '权限',
            dataIndex: 'permission',
            key: 'permission',
        },
        {
            title: '修改时间',
            dataIndex: 'modificationTime',
            key: 'modificationTime',
            render: (text) => new Date(text).toLocaleString(),
        },
    ];

    const breadcrumbItems = [
        { title: '根目录', path: '/' },
        ...currentPath
            .split('/')
            .filter(Boolean)
            .map((dir, index, array) => ({
                title: dir,
                path: '/' + array.slice(0, index + 1).join('/'),
            })),
    ];

    return (
        <div>
            <h2>HDFS文件浏览</h2>

            <Row gutter={16} style={{ marginBottom: 20 }}>
                <Col span={8}>
                    <Card>
                        <Statistic
                            title="总容量"
                            value={formatBytes(hdfsStatus.capacity || 0)}
                            prefix={<ArrowUpOutlined />}
                        />
                    </Card>
                </Col>
                <Col span={8}>
                    <Card>
                        <Statistic
                            title="已使用"
                            value={formatBytes(hdfsStatus.used || 0)}
                            suffix={`(${
                                hdfsStatus.capacity
                                    ? ((hdfsStatus.used / hdfsStatus.capacity) * 100).toFixed(2)
                                    : 0
                            }%)`}
                        />
                    </Card>
                </Col>
                <Col span={8}>
                    <Card>
                        <Statistic
                            title="剩余空间"
                            value={formatBytes(hdfsStatus.remaining || 0)}
                        />
                    </Card>
                </Col>
            </Row>

            <div style={{ marginBottom: 16 }}>
                <Breadcrumb>
                    {breadcrumbItems.map((item, index) => (
                        <Breadcrumb.Item key={index}>
                            <a onClick={() => handleNavigate(item.path)}>{item.title}</a>
                        </Breadcrumb.Item>
                    ))}
                </Breadcrumb>
            </div>

            <Space style={{ marginBottom: 16 }} direction="vertical" style={{ width: '100%' }}>
                <Input
                    placeholder="HDFS路径"
                    value={currentPath}
                    onChange={(e) => setCurrentPath(e.target.value)}
                    onKeyPress={handlePathChange}
                    style={{ width: '100%' }}
                />
            </Space>

            <Table
                columns={columns}
                dataSource={fileList}
                rowKey="fileName"
                loading={loading}
            />
        </div>
    );
};

export default HDFSPage;