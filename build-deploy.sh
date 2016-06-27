#!/bin/bash
./build.sh
rsync --del -R -Pzrlt target/dependency target/classes config/*.dev.* config/*.prd.* *.md \
  mobile/www socmedmon.sh ceefour@luna3.bippo.co.id:socmedmon/
