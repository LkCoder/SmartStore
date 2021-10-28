#ifndef    _MARIO_USB_H
#define   _MARIO_USB_H
#ifdef __cplusplus
extern "C" {
#endif

int openMarioUsb(int VID,int PID);
int ioctlMarioUsb(int addr,int cmd ,char *arg ,int length);
int readMarioUsbData(int addr,int cmd ,char *arg ,int length);
int closeMarioUsb();

#ifdef __cplusplus
}
#endif

#endif


