import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import DashboardPage from './pages/DashboardPage';
import HDFSPage from './pages/HDFSPage';
import HivePage from './pages/HivePage';
import SparkPage from './pages/SparkPage';
import UploadPage from './pages/UploadPage';
import 'antd/dist/antd.css';

const App = () => {
    return (
        <Router>
            <MainLayout>
                <Routes>
                    <Route path="/" element={<DashboardPage />} />
                    <Route path="/hdfs" element={<HDFSPage />} />
                    <Route path="/hive" element={<HivePage />} />
                    <Route path="/spark" element={<SparkPage />} />
                    <Route path="/upload" element={<UploadPage />} />
                </Routes>
            </MainLayout>
        </Router>
    );
};

export default App;