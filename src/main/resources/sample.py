# Copyright (C) 2005-2016 IP2Location.com
# All Rights Reserved
#
# This library is free software: you can redistribute it and/or
# modify it under the terms of the MIT license

import os
import sys
import IP2Location

database = IP2Location.IP2Location(sys.argv[2])
rec = database.get_all(sys.argv[1])
print(rec.country_long)
