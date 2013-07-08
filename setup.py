#!/usr/bin/env python
"""
   Copyright (c) 2012 The Ohio State University.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
"""
import os
import sys

from distutils.core import setup
from distutils.sysconfig import get_python_lib

# Dynamically calculate the version based on django.VERSION.
version = 'v13.09'

setup(
    name='YSmart',
    version=version,
    url='http://ysmart.cse.ohio-state.edu/',
    author='Rubao Lee and others',
    author_email='liru@cse.ohio-state.edu',
    description=('YSmart is a correlation aware SQL-to-MapReduce translator'),
    license='Apache License 2.0',
    packages=['antlr', 'ysmart.frontend', 'ysmart.backend'],
    package_dir={'antlr': 'SQL2XML/antlr3', 'ysmart.frontend': 'SQL2XML', 'ysmart.backend': 'XML2MapReduce', 'ysmart.test': 'test '},
    scripts=['translate.py'],
)