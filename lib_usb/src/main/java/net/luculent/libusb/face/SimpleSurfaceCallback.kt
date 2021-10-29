package net.luculent.libusb.face

import android.view.Surface
import com.serenegiant.widget.CameraViewInterface

/**
 *
 * @Description:     surfaceview空回调
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/29 10:02
 */
abstract class SimpleSurfaceCallback : CameraViewInterface.Callback {
    override fun onSurfaceCreated(view: CameraViewInterface?, surface: Surface?) {
    }

    override fun onSurfaceChanged(
        view: CameraViewInterface?,
        surface: Surface?,
        width: Int,
        height: Int
    ) {
    }

    override fun onSurfaceDestroy(view: CameraViewInterface?, surface: Surface?) {
    }
}