/*
 * Copyright: Zhong Xin Chuang Zhan Technology Co., Limited 
 * ÉîÛÚÊÐÖÚöÎ´´Õ¹¿Æ¼¼ÓÐÏÞ¹«Ë¾
 * Author: cys
 * version: 1.0 
*/
#ifndef ZXCZCAMERAAPI_H_
#define ZXCZCAMERAAPI_H_

/*Warn: when flash read and write, stream must stop.*/

/**************** zxcz error code ****************/
#define ZXCZ_SUCCESS        1	//success
#define ZXCZ_FAIL           0	//fail
#define ZXCZ_NOT_SUPPORT   -1	//not Support
#define ZXCZ_NO_DEV        -2	//no device
#define ZXCZ_OUT_RANGE     -3	//out of range

#define ZXCZ_VSYNC_ISCLOSED 2	//vsync is closed, AE/AWB not work
#define ZXCZ_VSYNC_ISOPENED 3	//vsync is opened, AE/AWB work
/*************************************************/


enum VSYNC_STATUE
{
	VSYNC_CLOSE = 0,	//vsync is closed, AE/AWB not work
	VSYNC_OPEN  = 1		//vsync is opened, AE/AWB work
};

/*
Summary:    init camera, prepare resource.
Parameters: 
    mDHandle: Camera handle
    nFwSize:  Camera fw size
              1:64K; 2:128K
            
Return: 1: success
        0: fail
       -1: not Support
       -2: no device
Warn: init before camera stream on. exp: camera open/connect
*/
int zxczInitCamera(uvc_device_handle_t *mDHandle, int nFwSize);

/*
Summary:    uninit camera.
Return: 1: success
        0: fail
*/
int zxczUnInitCamera( void );

/*
Summary:    flash chip erase
Return: 
    true:	success
    false:  fail
Warn: flash erase, stream must stop.
*/
bool zxczFlashChipErase( void );

/*
Summary:    flash block erase
Parameters: 
    addr:   block address
Return: 
    true:	success
    false:  fail
Warn: flash erase, stream must stop.
*/
bool zxczFlashBlockErase( unsigned int addr );

/*
Summary:    burn firmware to flash
Parameters: 
    filePath: Firmware file path
    chipEaese: 
        true: enable flash chip erase
        false: flash erase 0~128K

Return: 
    RET_SSF_FAIL		-1	//set source file fail
    RET_LOAD_ERR		-2	//load source data error
    RET_INIT_ERR		-3	//burn init fail
    RET_CHECK_EN_ERR	-4	//flash check error
    RET_ERASE_ERR		-5	//burn erase error
    RET_CHECK_ERR		-6	//burn check error
    RET_PROGRAM_ERR		-7	//burn program error
    RET_VERIFY_ERR		-8	//burn verify error
    RET_END_PROCESS_ERR	-9	//burn end process error
    RET_NOT_FIND_DEV    -10 //no device
    RET_SUCCESS			0	//burn ok
Warn: when burn flash, stream must stop.
*/
int zxczBurnToFlash(char* filePath, bool chipErase);

/*
Summary:    Burn firmware to flash, erase and program. Base on zxczBurnToFlash streamline the upgrade process
            remove burn check\verify\. 
Parameters: 
    filePath: Firmware file path
    chipEaese: 
        true:  flash chip erase
        false: flash erase 0~128K

Return: 
    RET_SSF_FAIL		-1	//set source file fail
    RET_LOAD_ERR		-2	//load source data error
    RET_INIT_ERR		-3	//burn init fail
    RET_ERASE_ERR		-5	//burn erase error
    RET_PROGRAM_ERR		-7	//burn program error
    RET_END_PROCESS_ERR	-9	//burn end process error
    RET_NOT_FIND_DEV    -10 //no device
    RET_SUCCESS			0	//burn ok
Warn: not check and verify, only erase and program 
      when burn flash, stream must stop.
*/
int zxczBurnToFlashStreamline(char* filePath, bool chipErase);

/*
Summary:    check burn process
Return: 
	BURN_NOT_START      -1 //not start
	BURN_PREPARE        0  //start to prepare resource
	BURN_INIT           1  //prepare ok, start to init
	BURN_ERASE          2  //init ok, start to erase
	BURN_CHECK          3  //erase ok, start to check flash
	BURN_PROGRAM        4  //check flash ok, start to program
	BURN_VERIFY         5  //program ok, start to Verify
	BURN_END_PROCESS    6  //Verify ok, start to end process
	BURN_END            7  //end
Warn: when flash read and write, stream must stop.
*/
int zxczBurnProcess();

/*
Summary: restart current camera
Return: 
	1: success
*/
int zxczRstCamera();

/*
Summary: read OV9712 AE Gain.
Parameters: 
    pOv9712AeData:   point of Ae data
    pOv9712GainData: point of Gain data
Return:  
    1: success
    0: fail
Warn: when flash read and write, stream must stop.
*/
int zxczReadOv9712AeGain( unsigned int *pOv9712AeData, 
                          unsigned int *pOv9712GainData );

/*
Summary: read OV5695 AE Gain.
Parameters: 
    pOv5695AeData:   point of Ae data
    pOv5695GainData: point of Gain data
Return:  
    1: success
    0: fail
Warn: when flash read and write, stream must stop.
*/
int zxczReadOv5695AeGain( unsigned int *pOv5695AeData, 
                          unsigned int *pOv5695GainData );

/*
Summary: read Ar0331 AE Gain.
Parameters: 
    pAr0331AeData:    point of Ae data
    pAr0331AGainData: point of A gain data
    pAr0331DGainData: point of D gain data
Return:  
    1: success
    0: fail
Warn: when flash read and write, stream must stop.
*/
int zxczReadAr0331AeGain( unsigned int *pAr0331AeData, 
                          unsigned int *pAr0331AGainData, 
                          unsigned int *pAr0331DGainData );

/*
Summary: read Ov8856 AE Gain.
Parameters: 
    pOv8856AeData:    point of Ae data
    pOv8856GainData:  point of gain data
Return:  
    1: success
    0: fail
Warn: when flash read and write, stream must stop.
*/
int zxczReadOv8856AeGain( unsigned int *pOv8856AeData, unsigned int *pOv8856GainData);

/*
Summary: read Ar0230 AE Gain.
Parameters: 
    pAr0230AeData:    point of Ae data
    pAr0230AGainData: point of A gain data
    pAr0230DGainData: point of D gain data
Return:  
    1: success
    0: fail
Warn: when flash read and write, stream must stop.
*/
int zxczReadAr0230AeGain( unsigned int *pAr0230AeData, 
                          unsigned int *pAr0230AGainData, 
                          unsigned int *pAr0230DGainData );
                          
/*
Summary: read IMX323 AE Gain.
Parameters: 
    pIMX323AeData:    point of Ae data
    pIMX323GainData:  point of gain data
Return:  
    1: success
    0: fail
Warn: when flash read and write, stream must stop.
*/
int zxczReadIMX323AeGain( unsigned int *pIMX323AeData, unsigned int *pIMX323GainData);

/*
Summary: 
    read i2c data. 
    close vsync -> read i2c -> open vsync.
Parameters: 
    slaveId:  i2c slave id 7bit address mode.
    i2cAddr:  point of Gain data.
    addrLen:  address length 
                  1: 1byte mode; 
                  2: 2byte mode.
    pI2cData: i2c read data buffer, buffer size larger then dataLen.
    dataLen:  data length.
                  1: 1byte mode; 
                  2: 2byte mode.
Return:  
    1: success.
    0: fail.
Warn: when read and write cmos sensor i2c, must stream on.
*/
int zxczI2cReadEx( unsigned char slaveId, 
                   unsigned int  i2cAddr, 
                   unsigned char addrLen, 
                   unsigned char *pI2cData, 
                   unsigned char dataLen );

/*
Summary: 
    write data. 
    close vsync -> write i2c -> open vsync.
Parameters: 
    slaveId:  i2c slave id 7bit address mode.
    i2cAddr:  point of Gain data.
    addrLen:  address length 
                  1: 1byte mode; 
                  2: 2byte mode.
    data:     i2c write data, 0~65535.
    dataLen:  data length.
                  1: 1byte mode; 
                  2: 2byte mode.
Return:  
    1: success.
    0: fail.
Warn: when read and write cmos sensor i2c, must stream on.
*/
int zxczI2cWriteEx( unsigned char slaveId, 
                    unsigned int  i2cAddr, 
                    unsigned char addrLen, 
                    unsigned int  data, 
                    unsigned char dataLen );

/*
Summary:    Read flash data
Parameters: 
    nLen      : Read flash length, between 1 to 4096
    nAddr     : Read flash address, flash can be read address between 0x1f000 to 0x1ffff
    pFlashData: flash data
Return: 1: success
        0: fail
       -1: not Support
       -2: no device
       -3: address out of range
       
Note: nAddr + nAddr: between 0x1f000 to 0x80000
Warn: when flash read and write, stream must stop.
*/
int zxczFlashRead( unsigned long nLen, 
                   unsigned int  nAddr, 
                   unsigned char *pFlashData );

/*
Summary:    write flash data
Parameters: 
   nLen      : write flash length, between 1 to 4096
   nAddr     : write flash address, flash can be write address between 0x1f000 to 0x7ffff
   pFlashData: flash data
Return: 1: success
       0: fail
      -1: not Support
      -2: no device
      -3: address out of range
      
Note: nAddr + nAddr: between 0x1f000 to 0x80000
Warn: flash操作请勿开启视频流，建议读取一个sector拼接好然后再写入
Warn: 将会擦除一个 sector，请保持数据在一个sector范围内
*/
int zxczFlashWrite( unsigned long nLen, 
                    unsigned int  nAddr, 
                    unsigned char *pFlashData );

/*
Summary:    Read I2 12 Byte sn 
Parameters: 
    pSn: 12 Byte SN
Return: 1: success
        0: fail
       -1: not Support
       -2: no device
       -3: address out of range
       
Warn: when Sn read, stream must stop.
*/
int zxczReadI2Sn(unsigned char *pSn);

/*
Summary:    Read dsp reg data
Parameters: 
    nAddr    : dsp reg address
    *pRegData: reg data
Return: 1: success
        0: fail
       -2: no device
*/
int zxczDspRegRead(unsigned short nAddr, unsigned char *pRegData);

/*
Summary:    write dsp reg data
Parameters: 
    nAddr   : dsp reg address
    regValue: reg data
Return: 1: success
        0: fail
       -2: no device
*/
int zxczDspRegWrite(unsigned short nAddr, unsigned char regValue);

/*
Summary:	get fw version
Parameters: 
	pProduct: fw version, buffer size need 56BYTE
	nFwVersionRegulation: Version number rules: 0-old; 1-new(flash layout).
Return: 1: success
		0: fail
	   -2: no device
Warn: get from flash, stream must stop.
*/
int zxczGetFWVersion(char *pFwVersion, int nFwVersionRegulation);

/*
Summary:    get product name
Parameters: 
    pProduct: product name, buffer size need 32BYTE
Return: 1: success
        0: fail
       -2: no device
Warn: get from flash, stream must stop.
*/
int zxczGetProduct(unsigned char *pProduct);

/*
Summary:    get manufacturer name
Parameters: 
    pProduct: manufacturer name, buffer size need 32BYTE
Return: 1: success
        0: fail
       -2: no device
Warn: get from flash, stream must stop.
*/
int zxczGetManufacturer(unsigned char *pManufacturer);

/*
Summary:    get serial number
Parameters: 
    pProduct: serial number, buffer size need 32BYTE
Return: 1: success
        0: fail
       -2: no device
Warn: get from flash, stream must stop.
*/
int zxczGetSerialNumber(unsigned char *pSerialNumber);

/*
Summary:    get interface name
Parameters: 
    pProduct: interface name, buffer size need 32BYTE
Return: 1: success
        0: fail
       -2: no device
Warn: get from flash, stream must stop.
*/
int zxczGetInterface(unsigned char *pInterface);

#endif /* ZXCZCAMERAAPI_H_ */
