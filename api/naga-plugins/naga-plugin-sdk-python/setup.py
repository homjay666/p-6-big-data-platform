#!/usr/bin
# -*- coding=utf-8 -*-
import os
from setuptools import setup

try:
    from setuptools import setup
except ImportError:
    from distutils.core import setup

setup(name='nagacore',
      version='1.0.0',
      description='Python SDK for naga plugin core',
      url='http://www.imooc.com',
      maintainer='jixin',
      maintainer_email='jixin.life@foxmail.com',
      license='BSD',
      keywords='naga',
      packages=['nagacore'],
      install_requires=['requests >= 2.14.2', 'pyarrow >= 0.8.0'],
      zip_safe=False)
