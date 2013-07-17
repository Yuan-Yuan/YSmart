'''
Created on Jul 15, 2013

@author: fathi
'''

from ysmart.backend import correlation, ystree
from ysmart.frontend.sql2xml import toXml
from ysmart.spark import translator
import os.path
import unittest

class BaseTestCase(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

class Test(BaseTestCase):

    def setUp(self):
        homedir = os.getenv('HOME')
        print homedir
        self.spark_home = os.getenv('SPARK_HOME', os.path.join(homedir, 'workspace/spark'))
        self.dest_dir = os.path.join(self.spark_home, 'examples/src/main/scala/spark/examples')

    def tearDown(self):
        pass


    def test_1(self):
        self._test_scaffold('tests/ysmart/test/unit_tests/tpch_select_nation.sql'
                                  , 'tests/ysmart/test/tpch_test/tpch.schema')

    def test_ssb1_1(self):
        self._test_scaffold('tests/ysmart/test/unit_tests/tpch_select_nation.sql'
                                  , 'tests/ysmart/test/tpch_test/tpch.schema')

    def _test_scaffold(self, input_file_path, schema_file_path):
#         config.turn_on_correlation = True
#         config.advanced_agg = True

        with open(input_file_path) as quey_file, open(schema_file_path) as schema_file:
            xml_str = toXml(quey_file)
            schema_str = schema_file.read()
        tree_node = ystree.ysmart_tree_gen(xml_str, schema_str)
        tree_node = correlation.ysmart_correlation(tree_node)
        
        file_name, file_extionsion = os.path.splitext(os.path.basename(input_file_path))
        self.assertEqual(file_extionsion, '.sql', '{query_file} does not end with .sql'.format(query_file=input_file_path))
        job_name = file_name.title()
        dest_file_path = os.path.join(self.dest_dir, job_name + '.scala')
        code = translator.spark_code(tree_node, job_name)
        with open(dest_file_path, 'w') as job_file:
            job_file.write(code)
            job_file.flush()

if __name__ == "__main__":
    # import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
