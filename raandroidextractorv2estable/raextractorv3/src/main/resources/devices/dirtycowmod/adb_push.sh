#!/system/bin/sh

adb push src/main/resources/devices/dirtycowmod/exploit.sh /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/adb_remove.sh /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/run-as-dirtycow /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/run-as-dirtycow64 /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/patch-init /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/libsupol.so /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/supolicy /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/busybox /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/readelf /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/su.img /data/local/tmp
adb push src/main/resources/devices/dirtycowmod/pwn.sh /data/local/tmp
adb shell chmod 777 /data/local/tmp/exploit.sh
adb shell chmod 755 /data/local/tmp/pwn.sh 
adb shell chmod 755 /data/local/tmp/busybox
adb shell chmod 755 /data/local/tmp/readelf
adb shell chmod 755 /data/local/tmp/patch-init
adb shell chmod 755 /data/local/tmp/supolicy
adb shell chmod 755 /data/local/tmp/libsupol.so
adb shell chmod 755 /data/local/tmp/run-as-dirtycow
adb shell chmod 755 /data/local/tmp/su.img
adb shell chmod 755 /data/local/tmp/run-as-dirtycow64
