package net.luculent.face.ui

import android.app.Activity
import android.content.BroadcastReceiver
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.media.AudioManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import com.baidu.idl.face.platform.*
import com.baidu.idl.face.platform.manager.TimeManager
import com.baidu.idl.face.platform.model.FaceExtInfo
import com.baidu.idl.face.platform.model.ImageInfo
import com.baidu.idl.face.platform.stat.Ast
import com.baidu.idl.face.platform.ui.FaceSDKResSettings
import com.baidu.idl.face.platform.ui.utils.BrightnessUtils
import com.baidu.idl.face.platform.ui.utils.VolumeUtils
import com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView
import com.baidu.idl.face.platform.utils.DensityUtils
import net.luculent.face.FaceManager
import net.luculent.face.R
import java.util.*

/**
 *
 * @Description:     活体检测基类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/19 15:42
 */
abstract class FaceLivenessBaseActivity : Activity(), ILivenessStrategyCallback,
    ILivenessViewCallback, VolumeUtils.VolumeCallback {

    var mFrameLayout: FrameLayout? = null
    @JvmField
    var mFaceDetectRoundView: FaceDetectRoundView? = null
    var mRelativeAddImageView: RelativeLayout? = null
    var mSoundView: ImageView? = null

    @JvmField
    var mViewBg: View? = null

    var mImageAnim: ImageView? = null
    private var mAnimationDrawable: AnimationDrawable? = null
    private var mLivenessType: LivenessTypeEnum? = null

    // 人脸信息
    private lateinit var mFaceConfig: FaceConfig

    @JvmField
    var mILivenessStrategy: ILivenessStrategy? = null

    @JvmField
    var mIsCompletion = false

    // 监听系统音量广播
    var mVolumeReceiver: BroadcastReceiver? = null

    @JvmField
    var mDisplayWidth = 0

    @JvmField
    var mDisplayHeight = 0

    @Volatile
    protected var mIsEnableSound = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindows()
        setContentView(getLayoutId())
        initFaceModule()
        initView()
        initListener()
    }

    open fun initWindows() {
        val currentBright = BrightnessUtils.getScreenBrightness(this)
        BrightnessUtils.setBrightness(this, currentBright + 100)
        // 设置为无标题(去掉Android自带的标题栏)，(全屏功能与此无关)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置为全屏模式
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val dm = DisplayMetrics()
        val display = this.windowManager.defaultDisplay
        display.getMetrics(dm)
        mDisplayWidth = dm.widthPixels
        mDisplayHeight = dm.heightPixels
    }

    open fun getLayoutId(): Int {
        return R.layout.activity_face_liveness_v3100
    }

    open fun initFaceModule() {
        FaceSDKResSettings.initializeResId()
        mFaceConfig = FaceSDKManager.getInstance().faceConfig
        val am = getSystemService(AUDIO_SERVICE) as AudioManager
        val vol = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        mIsEnableSound = if (vol > 0) mFaceConfig.isSound else false
    }

    open fun initView() {
        mFrameLayout = findViewById(R.id.liveness_surface_layout)
        mFaceDetectRoundView = findViewById(R.id.liveness_face_round)
        mRelativeAddImageView = findViewById(R.id.relative_add_image_view)
        mViewBg = findViewById(R.id.view_live_bg)
        mSoundView?.setImageResource(if (mIsEnableSound) R.mipmap.icon_titlebar_voice2 else R.drawable.collect_image_voice_selector)
        mFaceDetectRoundView?.setIsActiveLive(true)

        val surfaceRatio = FaceManager.getFaceConfig().qualityConfig.surfaceRatio
        val cameraFL = FrameLayout.LayoutParams(
            (mDisplayWidth * surfaceRatio).toInt(),
            (mDisplayHeight * surfaceRatio).toInt(),
            Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        )
        val faceSurface = getFaceSurface()
        faceSurface.layoutParams = cameraFL
        mFrameLayout?.addView(faceSurface)
        addImageView()
    }

    open fun initListener() {
        findViewById<View>(R.id.liveness_close)?.setOnClickListener { onBackPressed() }
        mSoundView?.setOnClickListener {
            mIsEnableSound = !mIsEnableSound
            mSoundView?.setImageResource(if (mIsEnableSound) R.mipmap.icon_titlebar_voice2 else R.drawable.collect_image_voice_selector)
            mILivenessStrategy?.setLivenessStrategySoundEnable(mIsEnableSound)
        }
    }

    abstract fun getFaceSurface(): View

    /**
     * 动态加载ImageView
     */
    private fun addImageView() {
        mFaceDetectRoundView?.post {
            mImageAnim = ImageView(this)
            val layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.height = DensityUtils.dip2px(this, 110f) // 设置图片的高度
            layoutParams.width = DensityUtils.dip2px(this, 87f) // 设置图片的宽度
            val halfHeight = (mFaceDetectRoundView!!.height / 2).toFloat()
            layoutParams.setMargins(
                0, (halfHeight - halfHeight * FaceDetectRoundView.HEIGHT_RATIO).toInt()
                        - layoutParams.height / 2, 0, 0
            )
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            mImageAnim?.layoutParams = layoutParams
            mImageAnim?.scaleType = ImageView.ScaleType.FIT_XY // 使图片充满控件大小
            mRelativeAddImageView?.addView(mImageAnim)
        }
    }

    open fun detectFace(previewRect: Rect, detectRect: Rect, degree: Int, data: ByteArray) {
        if (mIsCompletion) {
            return
        }
        if (mILivenessStrategy == null) {
            mILivenessStrategy = FaceSDKManager.getInstance().getLivenessStrategyModule(this)
                .apply {
                    setPreviewDegree(degree)
                    setLivenessStrategySoundEnable(mIsEnableSound)
                    setLivenessStrategyConfig(
                        mFaceConfig.livenessTypeList,
                        previewRect,
                        detectRect,
                        this@FaceLivenessBaseActivity
                    )
                }
        }
        mILivenessStrategy?.livenessStrategy(data)
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
        mVolumeReceiver = VolumeUtils.registerVolumeReceiver(this, this)
        mFaceDetectRoundView?.apply {
            setTipTopText("请将脸移入取景框")
            postDelayed({
                startPreview()
            }, 100)
        }
    }

    override fun onPause() {
        mILivenessStrategy?.reset()
        VolumeUtils.unRegisterVolumeReceiver(this, mVolumeReceiver)
        mVolumeReceiver = null
        mFaceDetectRoundView?.setProcessCount(
            0,
            mFaceConfig.livenessTypeList.size
        )
        super.onPause()
        stopPreview()
        mIsCompletion = false
    }

    abstract fun startPreview()
    abstract fun stopPreview()

    override fun volumeChanged() {
        try {
            val am = this.getSystemService(AUDIO_SERVICE) as AudioManager
            val cv = am.getStreamVolume(AudioManager.STREAM_MUSIC)
            mIsEnableSound = cv > 0
            mSoundView?.setImageResource(if (mIsEnableSound) R.mipmap.icon_titlebar_voice2 else R.mipmap.icon_titlebar_voice1)
            mILivenessStrategy?.setLivenessStrategySoundEnable(mIsEnableSound)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onLivenessCompletion(
        status: FaceStatusNewEnum, message: String?,
        base64ImageCropMap: HashMap<String, ImageInfo>?,
        base64ImageSrcMap: HashMap<String, ImageInfo>?, currentLivenessCount: Int
    ) {
        if (mIsCompletion) {
            return
        }
        onRefreshView(status, message, currentLivenessCount)
        if (status == FaceStatusNewEnum.OK) {
            mIsCompletion = true
        }
        // 打点
        Ast.getInstance().faceHit("liveness")
    }

    private fun onRefreshView(
        status: FaceStatusNewEnum,
        message: String?,
        currentLivenessCount: Int
    ) {
        when (status) {
            FaceStatusNewEnum.OK, FaceStatusNewEnum.FaceLivenessActionComplete, FaceStatusNewEnum.DetectRemindCodeTooClose, FaceStatusNewEnum.DetectRemindCodeTooFar, FaceStatusNewEnum.DetectRemindCodeBeyondPreviewFrame, FaceStatusNewEnum.DetectRemindCodeNoFaceDetected -> {
                mFaceDetectRoundView?.apply {
                    setTipTopText(message)
                    setTipSecondText("")
                    setProcessCount(currentLivenessCount, mFaceConfig.livenessTypeList.size)
                }
                stopAnim()
            }
            FaceStatusNewEnum.FaceLivenessActionTypeLiveEye, FaceStatusNewEnum.FaceLivenessActionTypeLiveMouth, FaceStatusNewEnum.FaceLivenessActionTypeLivePitchUp, FaceStatusNewEnum.FaceLivenessActionTypeLivePitchDown, FaceStatusNewEnum.FaceLivenessActionTypeLiveYawLeft, FaceStatusNewEnum.FaceLivenessActionTypeLiveYawRight, FaceStatusNewEnum.FaceLivenessActionTypeLiveYaw -> {
                mFaceDetectRoundView?.apply {
                    setTipTopText(message)
                    setTipSecondText("")
                    setProcessCount(currentLivenessCount, mFaceConfig.livenessTypeList.size)
                }
            }
            FaceStatusNewEnum.DetectRemindCodePitchOutofUpRange, FaceStatusNewEnum.DetectRemindCodePitchOutofDownRange, FaceStatusNewEnum.DetectRemindCodeYawOutofLeftRange, FaceStatusNewEnum.DetectRemindCodeYawOutofRightRange -> {
                mFaceDetectRoundView?.apply {
                    setTipTopText("请保持正脸")
                    setTipSecondText(message)
                    setProcessCount(currentLivenessCount, mFaceConfig.livenessTypeList.size)
                }
            }
            FaceStatusNewEnum.FaceLivenessActionCodeTimeout -> {
                mFaceDetectRoundView?.setProcessCount(
                    currentLivenessCount,
                    mFaceConfig.livenessTypeList.size
                )
                // 帧动画开启
                if (mRelativeAddImageView?.visibility == View.INVISIBLE) {
                    mRelativeAddImageView?.visibility = View.VISIBLE
                }
                loadAnimSource()
                // 监听帧动画时间
                mAnimationDrawable?.let {
                    var duration = 0
                    var i = 0
                    while (i < it.numberOfFrames) {
                        // 计算动画播放的时间
                        duration += it.getDuration(i)
                        i++
                    }
                    TimeManager.getInstance().activeAnimTime = duration
                }
            }
            else -> {
                mFaceDetectRoundView?.apply {
                    setTipTopText("请保持正脸")
                    setTipSecondText(message)
                    setProcessCount(currentLivenessCount, mFaceConfig.livenessTypeList.size)
                }
            }
        }
    }

    override fun setCurrentLiveType(liveType: LivenessTypeEnum) {
        mLivenessType = liveType
    }

    override fun viewReset() {
        mFaceDetectRoundView?.setProcessCount(0, 1)
    }

    override fun animStop() {
        stopAnim()
    }

    override fun setFaceInfo(faceInfo: FaceExtInfo?) {
        // TODO：传递FaceInfo信息，便于调试画人脸检测框和人脸检测区域（使用时，将注释放开）
        // if (mFaceDetectRoundView != null) {
        //     mFaceDetectRoundView.setFaceInfo(faceInfo);
        // }
    }

    // 加载动画
    private fun loadAnimSource() {
        if (mLivenessType != null) {
            when (mLivenessType) {
                LivenessTypeEnum.Eye -> mImageAnim?.setBackgroundResource(R.drawable.anim_eye)
                LivenessTypeEnum.HeadLeft -> mImageAnim?.setBackgroundResource(R.drawable.anim_left)
                LivenessTypeEnum.HeadRight -> mImageAnim?.setBackgroundResource(R.drawable.anim_right)
                LivenessTypeEnum.HeadDown -> mImageAnim?.setBackgroundResource(R.drawable.anim_down)
                LivenessTypeEnum.HeadUp -> mImageAnim?.setBackgroundResource(R.drawable.anim_up)
                LivenessTypeEnum.Mouth -> mImageAnim?.setBackgroundResource(R.drawable.anim_mouth)
                else -> {
                }
            }
            mAnimationDrawable = mImageAnim?.background as AnimationDrawable
            mAnimationDrawable?.start()
        }
    }

    private fun stopAnim() {
        mAnimationDrawable?.stop()
        mAnimationDrawable = null
        if (mRelativeAddImageView?.visibility == View.VISIBLE) {
            mRelativeAddImageView?.visibility = View.INVISIBLE
        }
    }
}