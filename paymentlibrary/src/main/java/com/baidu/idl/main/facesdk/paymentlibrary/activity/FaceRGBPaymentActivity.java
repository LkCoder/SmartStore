package com.baidu.idl.main.facesdk.paymentlibrary.activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;
import com.baidu.idl.main.facesdk.paymentlibrary.R;
import com.baidu.idl.main.facesdk.paymentlibrary.callback.CameraDataCallback;
import com.baidu.idl.main.facesdk.paymentlibrary.callback.FaceDetectCallBack;
import com.baidu.idl.main.facesdk.paymentlibrary.callback.FaceResultReceiver;
import com.baidu.idl.main.facesdk.paymentlibrary.manager.FaceSDKManager;
import com.baidu.idl.main.facesdk.paymentlibrary.model.LivenessModel;
import com.baidu.idl.main.facesdk.paymentlibrary.model.SingleBaseConfig;
import com.baidu.idl.main.facesdk.paymentlibrary.model.User;
import com.baidu.idl.main.facesdk.paymentlibrary.paymentcamera.AutoTexturePreviewView;
import com.baidu.idl.main.facesdk.paymentlibrary.paymentcamera.CameraPreviewManager;
import com.baidu.idl.main.facesdk.paymentlibrary.utils.BitmapUtils;
import com.baidu.idl.main.facesdk.paymentlibrary.utils.DensityUtils;
import com.baidu.idl.main.facesdk.paymentlibrary.utils.FaceOnDrawTexturViewUtil;
import com.baidu.idl.main.facesdk.paymentlibrary.utils.FileUtils;


public class FaceRGBPaymentActivity extends BaseActivity implements View.OnClickListener {

    // 图片越大，性能消耗越大，也可以选择640*480， 1280*720
    private static final int PREFER_WIDTH = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();
    private static final int PERFER_HEIGH = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();
    private Context mContext;
    private AutoTexturePreviewView mAutoCameraPreviewView;
    private RelativeLayout relativeLayout;
    private int mLiveType;
    private boolean isTime = true;
    private long searshTime;
    private boolean isNeedCamera = false;
    // 包含适配屏幕后后的人脸的x坐标，y坐标，和width
    private float[] pointXY = new float[4];

    private TextView preToastText;
    private RelativeLayout progressLayout;
    private ImageView progressBarView;
    private RelativeLayout payHintRl;
    private boolean mIsOnClick = false;
    private ImageView isMaskImage;
    private ImageView detectRegImageItem;
    private ImageView isCheckImageView;
    private TextView detectRegTxt;
    private boolean count = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        isNeedCamera = FaceSDKManager.initModelSuccess;
        setContentView(R.layout.activity_face_rgb_paymentlibrary);
        initView();
        // 屏幕的宽
        int displayWidth = DensityUtils.getDisplayWidth(mContext);
        // 屏幕的高
        int displayHeight = DensityUtils.getDisplayHeight(mContext);
        // 当屏幕的宽大于屏幕宽时
        if (displayHeight < displayWidth) {
            // 获取高
            int height = displayHeight;
            // 获取宽
            int width = (int) (displayHeight * ((9.0f / 16.0f)));
            // 设置布局的宽和高
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            // 设置布局居中
            params.gravity = Gravity.CENTER;
            relativeLayout.setLayoutParams(params);
        }
        int size = displayWidth / 3 * 2 + 100;
        progressBarView.getLayoutParams().width = size;
        progressBarView.getLayoutParams().height = size;
    }

    /**
     * View
     */
    private void initView() {
        // 获取整个布局
        relativeLayout = findViewById(R.id.all_relative);
        // 单目摄像头RGB 图像预览
        mAutoCameraPreviewView = findViewById(R.id.auto_camera_preview_view);
        mAutoCameraPreviewView.isDraw = true;

        // 返回
        ImageView mButReturn = findViewById(R.id.btn_back);
        mButReturn.setOnClickListener(this);

        preToastText = findViewById(R.id.pre_toast_text);
        progressLayout = findViewById(R.id.progress_layout);
        progressBarView = findViewById(R.id.progress_bar_view);
        // 预览模式下提示
        payHintRl = findViewById(R.id.pay_hintRl);
        detectRegImageItem = findViewById(R.id.detect_reg_image_item);
        isMaskImage = findViewById(R.id.is_mask_image);
        isCheckImageView = findViewById(R.id.is_check_image_view);
        detectRegTxt = findViewById(R.id.detect_reg_txt);

        mLiveType = SingleBaseConfig.getBaseConfig().getType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTestOpenDebugRegisterFunction();
    }

    private void startTestOpenDebugRegisterFunction() {
        if (SingleBaseConfig.getBaseConfig().getRBGCameraId() != -1) {
            CameraPreviewManager.getInstance().setCameraFacing(SingleBaseConfig.getBaseConfig().getRBGCameraId());
        } else {
            CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_FACING_FRONT);
        }
        CameraPreviewManager.getInstance().startPreview(mContext, mAutoCameraPreviewView,
                PREFER_WIDTH, PERFER_HEIGH, new CameraDataCallback() {
                    @Override
                    public void onGetCameraData(byte[] data, Camera camera, int width, int height) {
                        // 摄像头预览数据进行人脸检测
                        if (isNeedCamera) {
                            FaceSDKManager.getInstance().onDetectCheck(data, null, null,
                                    height, width, mLiveType, new FaceDetectCallBack() {
                                        @Override
                                        public void onFaceDetectCallback(LivenessModel livenessModel) {
                                            checkCloseDebugResult(livenessModel);
                                        }

                                        @Override
                                        public void onTip(int code, String msg) {
                                            preToastText.setText(msg);
                                        }

                                        @Override
                                        public void onFaceDetectDarwCallback(LivenessModel livenessModel) {

                                        }
                                    });
                        }
                    }
                });
    }

    // ***************预览模式结果输出*************
    private void checkCloseDebugResult(final LivenessModel livenessModel) {
        // 当未检测到人脸UI显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (livenessModel == null || livenessModel.getFaceInfo() == null) {
                    if (isTime) {
                        isTime = false;
                        searshTime = System.currentTimeMillis();
                    }
                    long endSearchTime = System.currentTimeMillis() - searshTime;
                    if (endSearchTime < 5000) {
                        preToastText.setTextColor(Color.parseColor("#FFFFFF"));
                        progressBarView.setImageResource(R.mipmap.ic_loading_grey);
                    } else {
                        payHint(null);
                    }
                    return;
                }

                isTime = true;
                pointXY[0] = livenessModel.getFaceInfo().centerX;
                pointXY[1] = livenessModel.getFaceInfo().centerY;
                pointXY[2] = livenessModel.getFaceInfo().width;
                pointXY[3] = livenessModel.getFaceInfo().width;
                FaceOnDrawTexturViewUtil.converttPointXY(pointXY, mAutoCameraPreviewView,
                        livenessModel.getBdFaceImageInstance(), livenessModel.getFaceInfo().width);
                float leftLimitX = AutoTexturePreviewView.circleX - AutoTexturePreviewView.circleRadius;
                float rightLimitX = AutoTexturePreviewView.circleX + AutoTexturePreviewView.circleRadius;
                float topLimitY = AutoTexturePreviewView.circleY - AutoTexturePreviewView.circleRadius;
                float bottomLimitY = AutoTexturePreviewView.circleY + AutoTexturePreviewView.circleRadius;
                if (pointXY[0] - pointXY[2] / 2 < leftLimitX
                        || pointXY[0] + pointXY[2] / 2 > rightLimitX
                        || pointXY[1] - pointXY[3] / 2 < topLimitY
                        || pointXY[1] + pointXY[3] / 2 > bottomLimitY) {
                    preToastText.setTextColor(Color.parseColor("#FFFFFF"));
                    progressBarView.setImageResource(R.mipmap.ic_loading_grey);
                    return;
                }
                preToastText.setTextColor(Color.parseColor("#FFFFFF"));
                preToastText.setText("正在识别中...");
                progressBarView.setImageResource(R.mipmap.ic_loading_blue);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count) {
                            count = false;
                            payHint(livenessModel);
                        }
                    }
                }, 2 * 500);  // 延迟1秒执行
            }
        });
    }


    @Override
    public void onClick(View v) {
        // 返回
        int id = v.getId();
        if (id == R.id.btn_back) {
            if (mIsOnClick) {
                progressLayout.setVisibility(View.VISIBLE);
                payHintRl.setVisibility(View.GONE);
                preToastText.setTextColor(Color.parseColor("#FFFFFF"));
                preToastText.setText("保持面部在取景框内");
                progressBarView.setImageResource(R.mipmap.ic_loading_grey);
                isNeedCamera = true;
                count = true;
                mIsOnClick = false;
            } else {
                if (!FaceSDKManager.initModelSuccess) {
                    Toast.makeText(mContext, "SDK正在加载模型，请稍后再试",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                finish();
            }
        }
    }


    private void payHint(final LivenessModel livenessModel) {
        if (livenessModel == null) {
            isMaskImage.setImageResource(R.mipmap.ic_mask_fail);
            isCheckImageView.setImageResource(R.mipmap.ic_icon_fail_sweat);
            detectRegTxt.setTextColor(Color.parseColor("#FECD33"));
            detectRegTxt.setText("识别超时");
            progressLayout.setVisibility(View.GONE);
            payHintRl.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //   todo somthing here
                    progressLayout.setVisibility(View.VISIBLE);
                    payHintRl.setVisibility(View.GONE);
                    isTime = true;
                }
            }, 3 * 1000);  // 延迟3秒执行
            return;
        }
        if (livenessModel.getUser() == null) {
            // todo: 失败展示
            BDFaceImageInstance bdFaceImageInstance = livenessModel.getBdFaceImageInstance();
            Bitmap instaceBmp = BitmapUtils.getInstaceBmp(bdFaceImageInstance);
            isMaskImage.setImageResource(R.mipmap.ic_mask_fail);
            isCheckImageView.setImageResource(R.mipmap.ic_icon_fail_sweat);
            detectRegImageItem.setImageBitmap(instaceBmp);
            detectRegTxt.setTextColor(Color.parseColor("#FECD33"));
            detectRegTxt.setText("抱歉未能认出您");
            progressLayout.setVisibility(View.GONE);
            payHintRl.setVisibility(View.VISIBLE);
            isNeedCamera = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //   todo somthing here
                    progressLayout.setVisibility(View.VISIBLE);
                    payHintRl.setVisibility(View.GONE);
                    isNeedCamera = true;
                    count = true;
                }
            }, 3 * 1000);  // 延迟3秒执行

        }
        if (livenessModel.getUser() != null) {
            // todo: 成功展示kk
            payHintRl.setVisibility(View.VISIBLE);
            String absolutePath = FileUtils.getBatchImportSuccessDirectory()
                    + "/" + livenessModel.getUser().getImageName();
            Bitmap userBitmap = BitmapFactory.decodeFile(absolutePath);
            detectRegImageItem.setImageBitmap(userBitmap);
            isMaskImage.setImageResource(R.mipmap.ic_mask_success);
            isCheckImageView.setImageResource(R.mipmap.ic_icon_success_star);
            detectRegTxt.setTextColor(Color.parseColor("#00BAF2"));
            detectRegTxt.setText(livenessModel.getUser().getUserName() + " 识别成功");
            isNeedCamera = false;
            mIsOnClick = true;
            detectRegTxt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleDetectUser(livenessModel.getUser());
                }
            }, 2000);
        }
    }

    private void handleDetectUser(User user) {
        FaceResultReceiver resultReceiver = FaceSDKManager.getInstance().getFaceResultReceiver();
        if (resultReceiver != null) {
            resultReceiver.onReceive(user);
        }
        finish();
    }
}
