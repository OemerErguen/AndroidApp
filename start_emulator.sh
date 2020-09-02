#!/bin/bash
# customize this script before execution!
export QT_SCALE_FACTOR=0.5 # fix emulator on 4k displays on linux

cd /home/worker/Android/Sdk/emulator

#./emulator @Nexus_S_API_23
./emulator @Nexus_6_API_27_2
#./emulator @Nexus_6_API_28
