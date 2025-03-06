#!/usr/bin
# -*- coding:utf-8 -*-
import requests
import json
import logging
try:
    from setting import *
except ImportError:
    pass


class NagaApi(object):
    """
        naga api model for get config from api server by http
    """

    def __init__(self, host, port):
        self._base_url = 'http://{0}:{1}'.format(host, port)

    def get_job_config(self, task_name, job_name, exec_id='-1'):
        if task_name is None or job_name is None:
            logging.error(
                'get_job_config [task_name:{0},job_name:{1}] error,params can not be none'.format(task_name, job_name))
            return None
        data = {
            'taskName': task_name,
            'jobName': job_name,
            'execId': exec_id
        }
        url = '{0}{1}'.format(self._base_url, "/naga/v1/param/job")
        r = requests.get(url=url, params=data)
        if r.status_code == requests.codes.ok:
            return r.json()
        else:
            logging.error('naga api server return code:{0}'.format(r.status_code))
            return None

    def save_job_runtime_config(self, task_name, job_name, exec_id, job_config):
        if task_name is None or exec_id is None or job_name is None or not isinstance(job_config, dict):
            logging.error(
                'save_job_runtime_config [task_name:{0},exec_id:{1},job_name:{2},job_config:{3}] error,params error'.format(
                    task_name, exec_id, job_name, job_config))
            return None
        url = '{0}{1}'.format(self._base_url,"/naga/v1/param/runtime")
        headers = {'Content-type': 'application/json'}
        params = {
            "taskName":task_name,
            "jobName":job_name,
            "execId":exec_id,
            "config":job_config
        }
        r = requests.post(url=url, data=json.dumps(params), headers=headers)
        if r.content.find('success') == -1:
            raise Exception('save_job_runtime_config state failed!')

    def get_job_output_param(self, task_name, job_name, exec_id):
        if task_name is None or exec_id is None or job_name is None:
            logging.error(
                'get_job_output_param [task_name:{0},exec_id:{1},job_name:{2}] error,params error'.format(
                    task_name, exec_id, job_name))
            return None
        url = '{0}{1}'.format(self._base_url, "/naga/v1/param/output/job")
        data = {
            'taskName': task_name,
            'jobName': job_name,
            'execId': exec_id
        }
        r = requests.get(url=url, params=data)
        if r.status_code == requests.codes.ok:
            return r.json()
        else:
            logging.error('naga api server return code:{0}'.format(r.status_code))
            return None

    def save_job_output_param(self, task_name, job_name, exec_id, params):
        if task_name is None or exec_id is None or job_name is None or not isinstance(params, dict):
            logging.error(
                'get_job_output_param [task_name:{0},exec_id:{1},job_name:{2},params:{3}] error,params error'.format(
                    task_name, exec_id, job_name, params))
            return None
        url = '{0}{1}'.format(self._base_url,"/naga/v1/param/output")
        headers = {'Content-type': 'application/json'}
        data = {
            "taskName":task_name,
            "jobName":job_name,
            "execId":exec_id,
            "config":params
        }
        r = requests.post(url=url, data=json.dumps(data), headers=headers)
        if r.content.find('success') == -1:
            raise Exception('save job output state failed!')

    def get_task_output_params(self, task_name, exec_id):
        if task_name is None or exec_id is None:
            logging.error(
                'get_task_output_param [task_name:{0},exec_id:{1}] error,params error'.format(
                    task_name, exec_id))
            return None
        url = '{0}{1}'.format(self._base_url, "/naga/v1/param/output/task")
        data = {
            "taskName":task_name,
            "execId":exec_id,
        }
        r = requests.get(url=url,params=data)
        if r.status_code == requests.codes.ok:
            return r.json()
        else:
            logging.error('naga api server return code:{0},error:{1},url:{2}'.format(r.status_code, r.content, url))
            return None

    def save_table_info(self, task_name, ds_name, db_name, table_name, table_schema=""):
        if task_name is None or ds_name is None or db_name is None or table_name is None:
            logging.error(
                'save_table_info [task_name:{0},ds_name:{1},db_name:{2},table_name:{3}] error,params error'.format(
                    task_name, ds_name, db_name, table_name))
            return None
        url = '{0}{1}'.format(self._base_url, "/naga/v1/param/output/bloodline")
        data = {
            'taskName': task_name,
            'dsName': ds_name,
            'dbName': db_name,
            'tableName': table_name,
            'tableSchema': table_schema
        }
        r = requests.get(url=url, params=data)
        if r.content.find('success') == -1:
            logging.error("data = {0}".format(data))
            logging.error(r.content)
            # raise Exception('save_table_info state failed!')


if __name__ == '__main__':
    api = NagaApi('127.0.0.1', '9042')
    api.get_job_config("testtask","testjob",-1)
