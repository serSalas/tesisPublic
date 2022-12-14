/*
 * usbswitcher.c, by @joystick and @rpaleari
 *
 * Search for an attached USB Samsung device and switch it to a specific USB
 * configuration (#2). Requires libusb [1] (use version 0.1).
 *
 * References:
 * [1] http://libusb.org/
 *
 * Updated to support LG and HTC
 * NOTE: run twice if it does not succeed the first time
 * Dec 21, 2017
 * root@davejingtian.org
 */

#include <assert.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>

#include "usb.h"

/* This VID/PID pair is OK for all the Samsung phones we tested. However, you
   may need to tune them for other device models. */
#define SAMSUNG_VENDOR_ID  	0x04e8
#define SAMSUNG_PRODUCT_ID 	0x6860
#define LG_VENDOR_ID		0x1004
#define LG_PRODUCT_ID		0x633e
//#define LG_PRODUCT_ID		0x62ce
#define HTC_VENDOR_ID		0x0bb4
#define HTC_PRODUCT_ID		0x0f64

static int VENDOR_ID;
static int PRODUCT_ID;
extern char *optarg;



void _log(char tag, const char *fmt, ...) {
  char msg[1024];

  va_list ap;
  va_start(ap, fmt);
  vsnprintf(msg, sizeof(msg), fmt, ap);
  va_end(ap);

  fprintf(stderr, "[%c] %s\n", tag, msg);
}

#define error(fmt, ...) _log('!', fmt, ## __VA_ARGS__)
#define info(fmt, ...) _log('*', fmt, ## __VA_ARGS__)

struct usb_device *find_device() {
  struct usb_bus *bus;
  struct usb_device *dev;
  for (bus=usb_busses; bus != NULL; bus=bus->next) {
    for (dev=bus->devices; dev; dev=dev->next) {
      if ((dev->descriptor.idVendor == VENDOR_ID)) {
//&&
//	  (dev->descriptor.idProduct == PRODUCT_ID)) {
	//printf("hello, device found with vendor id\n");
	return dev;
      } else {
      //  printf("vendor/product id mismatch\n");
      }
    }
  }
  return NULL;
}

int main(int argc, char **argv) {
  int r;
  struct usb_device *dev;
  usb_dev_handle *udev;
  int c, option_index = 0;

  /* Process the arguments */
  VENDOR_ID = LG_VENDOR_ID;
  PRODUCT_ID = LG_PRODUCT_ID;

  usb_init();
  usb_find_busses();
  usb_find_devices();

  dev = find_device();
  if (!dev) {
    error("Device not found");
    exit(-1);
  }
  //info("Device found, %d configuration(s)", dev->descriptor.bNumConfigurations);

  if (dev->descriptor.bNumConfigurations == 1) {
    //info("Device has only 1 configuration, no need to switch");
    return 0;
  }

  udev = usb_open(dev);
  if (!udev) {
    error("Can't open device");
    exit(-1);
  }
 // info("Device opened, Switching to configuration #2");

  usb_reset(udev);
  //sleep(5);
  r = usb_set_configuration(udev, 2);
  if (r != 0) {
    error("Configuration switch failed");
    usb_close(udev);
    exit(-1);
  }

  //info("Configuration switched!");
  usb_reset(udev);
  usb_close(udev);

  return 0;
}
