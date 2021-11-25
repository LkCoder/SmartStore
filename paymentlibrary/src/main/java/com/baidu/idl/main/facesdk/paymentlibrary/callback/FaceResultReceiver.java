package com.baidu.idl.main.facesdk.paymentlibrary.callback;

import com.baidu.idl.main.facesdk.paymentlibrary.model.User;

/**
 * @Description: 人脸识别结果接收器
 * @Author: yanlei.xia
 * @CreateDate: 2021/11/25 10:41
 */
public interface FaceResultReceiver {
    void onReceive(User user);
}
