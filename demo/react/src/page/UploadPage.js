import React, { useState } from 'react';
import { Upload, Button, message, Input, Form, Card } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import { hdfsAPI } from '../api';

const UploadPage = () => {
    const [uploadPath, setUploadPath] = useState('/');
    const [fileList, setFileList] = useState([]);
    const [uploading, setUploading] = useState(false);

    const handleUpload = async () => {
        if (fileList.length === 0) {
            message.error('请选择至少一个文件');
            return;
        }

        setUploading(true);
        const file = fileList[0].originFileObj;

        try {
            const response = await hdfsAPI.uploadFile(file, uploadPath);
            message.success('文件上传成功');
            setFileList([]);
        } catch (error) {
            message.error('文件上传失败: ' + error.message);
        } finally {
            setUploading(false);
        }
    };

    const uploadProps = {
        beforeUpload: (file) => {
            setFileList([...fileList, { uid: file.uid, name: file.name, originFileObj: file }]);
            return false;
        },
        onRemove: (file) => {
            const index = fileList.findIndex((item) => item.uid === file.uid);
            const newFileList = [...fileList];
            newFileList.splice(index, 1);
            setFileList(newFileList);
        },
        fileList,
    };

    return (
        <div>
            <h2>上传文件到HDFS</h2>
            <Card style={{ maxWidth: 600, margin: '0 auto' }}>
                <Form layout="vertical">
                    <Form.Item label="HDFS目标路径">
                        <Input
                            placeholder="输入HDFS目标路径，例如: /user/hadoop/data"
                            value={uploadPath}
                            onChange={(e) => setUploadPath(e.target.value)}
                        />
                    </Form.Item>

                    <Form.Item label="选择文件">
                        <Upload {...uploadProps}>
                            <Button icon={<UploadOutlined />}>选择文件</Button>
                        </Upload>
                    </Form.Item>

                    <Form.Item>
                        <Button
                            type="primary"
                            onClick={handleUpload}
                            loading={uploading}
                            disabled={fileList.length === 0}
                        >
                            {uploading ? '上传中...' : '开始上传'}
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default UploadPage;
