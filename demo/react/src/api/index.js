import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// HDFS相关API
export const hdfsAPI = {
  uploadFile: (file, path) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('path', path);
    return api.post('/hdfs/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
  listFiles: (path) => api.get(`/hdfs/list?path=${encodeURIComponent(path)}`),
  getHdfsStatus: () => api.get('/hdfs/status'),
};

// Hive相关API
export const hiveAPI = {
  executeQuery: (sql) => api.post('/hive/query', { sql }),
  showTables: () => api.get('/hive/tables'),
  describeTable: (tableName) => api.get(`/hive/describe/${tableName}`),
};

// Spark相关API
export const sparkAPI = {
  submitJob: (jarPath, mainClass, args) => api.post('/spark/submit', {
    jarPath,
    mainClass,
    args,
  }),
  getStatus: () => api.get('/spark/status'),
};

// 集群监控相关API
export const clusterAPI = {
  getYarnStatus: () => api.get('/cluster/yarn'),
  getClusterNodes: () => api.get('/cluster/nodes'),
};

export default {
  hdfsAPI,
  hiveAPI,
  sparkAPI,
  clusterAPI,
};