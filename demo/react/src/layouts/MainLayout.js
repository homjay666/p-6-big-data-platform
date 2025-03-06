import React, { useState } from 'react';
import { Layout, Menu, Typography } from 'antd';
import {
    UploadOutlined,
    DatabaseOutlined,
    CodeOutlined,
    DashboardOutlined,
    FolderOutlined,
} from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';

const { Header, Sider, Content } = Layout;
const { Title } = Typography;

const MainLayout = ({ children }) => {
    const [collapsed, setCollapsed] = useState(false);
    const location = useLocation();

    const menuItems = [
        {
            key: '/',
            icon: <DashboardOutlined />,
            label: <Link to="/">集群概览</Link>,
        },
        {
            key: '/hdfs',
            icon: <FolderOutlined />,
            label: <Link to="/hdfs">HDFS文件管理</Link>,
        },
        {
            key: '/hive',
            icon: <DatabaseOutlined />,
            label: <Link to="/hive">Hive查询</Link>,
        },
        {
            key: '/spark',
            icon: <CodeOutlined />,
            label: <Link to="/spark">Spark作业</Link>,
        },
        {
            key: '/upload',
            icon: <UploadOutlined />,
            label: <Link to="/upload">文件上传</Link>,
        },
    ];

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed}>
                <div className="logo" style={{ height: 32, margin: 16, background: 'rgba(255, 255, 255, 0.3)' }} />
                <Menu
                    theme="dark"
                    selectedKeys={[location.pathname]}
                    mode="inline"
                    items={menuItems}
                />
            </Sider>
            <Layout className="site-layout">
                <Header className="site-layout-background" style={{ padding: 0, background: '#fff' }}>
                    <Title level={3} style={{ margin: '0 16px' }}>
                        Hadoop集群管理系统
                    </Title>
                </Header>
                <Content style={{ margin: '16px' }}>
                    <div className="site-layout-background" style={{ padding: 24, minHeight: 360, background: '#fff' }}>
                        {children}
                    </div>
                </Content>
            </Layout>
        </Layout>
    );
};

export default MainLayout;