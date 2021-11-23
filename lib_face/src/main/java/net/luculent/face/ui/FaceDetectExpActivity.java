package net.luculent.face.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.baidu.idl.face.platform.FaceStatusNewEnum;
import com.baidu.idl.face.platform.model.ImageInfo;

import net.luculent.face.FaceCallBack;
import net.luculent.face.FaceLogger;
import net.luculent.face.FaceManager;
import net.luculent.face.api.FaceException;
import net.luculent.face.api.response.FaceUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceDetectExpActivity extends FaceDetectActivity implements TimeoutDialog.OnTimeoutDialogClickListener, FaceCallBack<FaceUser> {

    private TimeoutDialog mTimeoutDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FaceManager.registerFaceVerifyCallBack(this);
    }

    @Override
    protected int displayOrientation(Context context) {
        int videoDirection = FaceManager.getFaceConfig().getQualityConfig().getVideoDirection();
        if (videoDirection == -1) {
            return super.displayOrientation(context);
        }
        return videoDirection;
    }

    @Override
    public void onDetectCompletion(FaceStatusNewEnum status, String message,
                                   HashMap<String, ImageInfo> base64ImageCropMap,
                                   HashMap<String, ImageInfo> base64ImageSrcMap) {
        super.onDetectCompletion(status, message, base64ImageCropMap, base64ImageSrcMap);
        if (status == FaceStatusNewEnum.OK && mIsCompletion) {
            // 获取最优图片
            getBestImage(base64ImageSrcMap);
        } else if (status == FaceStatusNewEnum.DetectRemindCodeTimeout) {
            if (mViewBg != null) {
                mViewBg.setVisibility(View.VISIBLE);
            }
            showMessageDialog();
        }
    }

    /**
     * 获取最优图片
     *
     * @param imageSrcMap 原图集合
     */
    private void getBestImage(HashMap<String, ImageInfo> imageSrcMap) {
        String faceBase64 = null;
        // 将原图集合中的图片按照质量降序排序，最终选取质量最优的一张原图图片
        if (imageSrcMap != null && imageSrcMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list2 = new ArrayList<>(imageSrcMap.entrySet());
            Collections.sort(list2, new Comparator<Map.Entry<String, ImageInfo>>() {

                @Override
                public int compare(Map.Entry<String, ImageInfo> o1,
                                   Map.Entry<String, ImageInfo> o2) {
                    String[] key1 = o1.getKey().split("_");
                    String score1 = key1[2];
                    String[] key2 = o2.getKey().split("_");
                    String score2 = key2[2];
                    // 降序排序
                    return Float.valueOf(score2).compareTo(Float.valueOf(score1));
                }
            });
            faceBase64 = list2.get(0).getValue().getBase64();
        }
//        FaceManager.verify(faceBase64);
    }

    private void showMessageDialog() {
        mTimeoutDialog = new TimeoutDialog(this);
        mTimeoutDialog.setDialogListener(this);
        mTimeoutDialog.setCanceledOnTouchOutside(false);
        mTimeoutDialog.setCancelable(false);
        mTimeoutDialog.show();
        onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FaceManager.unregisterFaceVerifyCallBack(this);
    }

    @Override
    public void onRecollect() {
        if (mTimeoutDialog != null) {
            mTimeoutDialog.dismiss();
        }
        if (mViewBg != null) {
            mViewBg.setVisibility(View.GONE);
        }
        onResume();
    }

    @Override
    public void onReturn() {
        if (mTimeoutDialog != null) {
            mTimeoutDialog.dismiss();
        }
        finish();
    }

    @Override
    public void onSuccess(FaceUser result) {
        finish();
    }

    @Override
    public void onError(@NotNull FaceException exception) {
        FaceLogger.e("face verify error= " + exception.toString());
        finish();
    }
}
