#!/system/bin/sh
echo 1 > /sys/kernel/uevent_helper
/data/local/tmp/busybox mknod /dev/sutmp b 7 200
/data/local/tmp/busybox losetup /dev/sutmp /data/local/tmp/su.img
/data/local/tmp/busybox mount /dev/sutmp /system/xbin/
/system/bin/chown root:shell /system/xbin
/system/bin/chcon u:object_r:system_file:s0 /system/xbin
/system/xbin/su -d
