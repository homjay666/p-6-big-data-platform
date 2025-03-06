#!/usr/bin
# -*- coding:utf-8 -*-


class Properties(object):
    def __init__(self, file_name):
        self.file_name = file_name
        self.properties = {}

    def get_properties(self):
        try:
            pro_file = open(self.file_name, 'Ur')
            for line in pro_file.readlines():
                line = line.strip().replace('\n', '')
                if line.find("#") != -1:
                    line = line[0:line.find('#')]
                if line.find('=') > 0:
                    strs = line.split('=')
                    strs[1] = line[len(strs[0]) + 1:]
                    self.properties[strs[0].strip()] = strs[1].strip()
        except Exception as e:
            raise e
        else:
            pro_file.close()
        return self.properties
