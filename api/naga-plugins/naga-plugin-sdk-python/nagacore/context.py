#!/usr/bin
# -*- coding:utf-8 -*-
import logging
import re
import os
try:
  from naga import NagaApi
  import pyarrow as pa
  from properties import Properties
  from setting import *
except ImportError:
  pass
import json
import six

try:
  from pyspark import SparkContext
  from pyspark.sql import SparkSession
except ImportError:
  pass


def singleton(cls, *args, **kw):
  instances = {}

  def _singleton(*args, **kw):
    if cls not in instances:
      instances[cls] = cls(*args, **kw)
    return instances[cls]

  return _singleton


@singleton
class Context(object):
  def __init__(self, local_debug=False):
    self._client = None
    self.schedule_id = None
    self._init_log()
    # 获取runtime信息
    self._fill_runtime_args()
    # 获取naga平台信息
    properties = Properties('{0}/runtime.properties'.format(self.job_name)).get_properties()
    rpc_server = properties.get('api.server')
    rpc_port = properties.get('api.server.port')
    logging.info('rpcServer={},rpcPort={}'.format(
        rpc_server, rpc_port))
    if rpc_server and rpc_port:
      self._naga_api = NagaApi(rpc_server, rpc_port)
      job_config = self._naga_api.get_job_config(
          self.task_name, self.job_name, self.flow_exec_id)
      if job_config:
        self.team = job_config['team']
        self.config = job_config
        self._naga_api.save_job_runtime_config(self.task_name,
                                                 self.job_name,
                                                 self.flow_exec_id,
                                                 self.config)
      else:
        logging.info('get job config failed!')
    self.hadoop_home = os.getenv('HADOOP_HOME')
    if self.hadoop_home is None:
      self.hadoop_home = '/soft/home/hadoop-2.8.5'
    logging.info('HADOOP_HOME:{}'.format(self.hadoop_home))

  def get_file_system(self, host="hdfs://ns", port=0, driver='libhdfs'):
    if self._client is None:
      self._client = pa.hdfs.connect(host=host, port=port, user=self.team, driver=driver)
    return self._client

  def _init_log(self):
    logging.basicConfig(level=logging.INFO,
                        format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                        datefmt='%a, %d %b %Y %H:%M:%S')

  def _fill_runtime_args(self):
    if self._is_spark_job():
      spark = SparkSession.builder.master(
          'yarn').enableHiveSupport().getOrCreate()
      spark_config = spark.sparkContext.getConf()
      java_options = spark_config.get('spark.executor.extraJavaOptions')
      logging.info('getJavaOptions spark.executor.extraJavaOptions={}'.format(
          java_options))
      self._set_java_options_sys_property(java_options)

    props_file = None
    for parent,dirnames,filenames in os.walk('./'):
      for filename in filenames:
        print filename
        if 'props' in filename:
          props_file = filename
          break
      break

    properties = Properties(props_file).get_properties()

    self.flow_exec_id = properties.get("azkaban.flow.execid")
    self.runtime_job_id = properties.get("azkaban.job.id")
    self.job_name = properties.get("azkaban.job.id")
    self.task_name = properties.get("azkaban.flow.projectname")
    logging.info(
        "getEnv result:[azkaban.flowid = {},azkaban.execid = {}, \
        azkaban.jobid = {},azkaban.jobname = {},azkaban.projectname = {}]".format(
            self.flow_name, self.flow_exec_id, self.runtime_job_id,
            self.job_name, self.task_name))

  def _set_java_options_sys_property(self, java_options):
    if java_options is None:
      raise Exception('spark.driver.extraJavaOptions is None')
    extra_java_options = java_options.replace('-D', '').replace('"', '')
    pairs = extra_java_options.split(' ')
    for pair in pairs:
      logging.info('pair={}'.format(pair))
      ss = pair.split('=')
      os.environ[ss[0]] = ss[1]

  def _is_spark_job(self):
    p_path = os.getcwd()
    logging.info('parentPath:{}'.format(p_path))
    file_list = os.listdir(p_path)
    for file in file_list:
      if file.find('spark') != -1:
        return True
    return False

  def save_table_info(self, ds_name, db_name, table_name, table_desc=''):
    if self.local_debug:
      return
    self._naga_api.save_table_info(self.task_name, ds_name, db_name,
                                     table_name, table_desc)

  def output_values(self, output_values):
    if self.local_debug:
      return
    if not isinstance(output_values, dict):
      raise Exception("output_values must be type of dict!")
    self._naga_api.save_job_output_param(self.task_name, self.job_name,
                                           self.flow_exec_id,
                                           output_values)
