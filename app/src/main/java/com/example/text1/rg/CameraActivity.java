package com.example.text1.rg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.text1.AI;
import com.example.text1.Note;
import com.example.text1.Note_;
import com.example.text1.NotesAdapter;
import com.example.text1.ObjectBox;
import com.example.text1.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class CameraActivity extends AppCompatActivity {


    //用于切换手势的顺序的数字
    private int current = 0;

    private PreviewView previewView;
    private ImageView img_switch;
    private LinearLayout ll_picture_parent;
    private ImageView img_picture;
    private FocusView focus_view;
    private View view_mask;
    private RelativeLayout rl_result_picture;
    private ImageView img_picture_cancel;
    private ImageView img_picture_save;
    private RelativeLayout rl_start;
    private TextView tv_back;
    private ImageView img_take_photo;

    private ImageCapture imageCapture;
    private CameraControl mCameraControl;
    private ProcessCameraProvider cameraProvider;
    private CameraParam mCameraParam;
    private boolean front;

    //新加入的内容
    private LinearLayout loading_page;
    private TextView next_item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        mCameraParam = getIntent().getParcelableExtra(CameraConstant.CAMERA_PARAM_KEY);
        if (mCameraParam == null) {
            throw new IllegalArgumentException("CameraParam is null");
        }
        if (!Tools.checkPermission(this)) {
//            throw new NoPermissionException("需要有拍照权限和存储权限");
        }
        front = mCameraParam.isFront();
        initView();
        initData();
        setViewParam();
        intCamera();
    }

    private void setViewParam() {
        //是否显示切换按钮
        if (mCameraParam.isShowSwitch()) {
            img_switch.setVisibility(View.VISIBLE);
            if (mCameraParam.getSwitchSize() != -1 || mCameraParam.getSwitchLeft() != -1 || mCameraParam.getSwitchTop() != -1) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) img_switch.getLayoutParams();
                if (mCameraParam.getSwitchSize() != -1) {
                    layoutParams.width = layoutParams.height = mCameraParam.getSwitchSize();
                }
                if (mCameraParam.getSwitchLeft() != -1) {
                    layoutParams.leftMargin = mCameraParam.getSwitchLeft();
                }
                if (mCameraParam.getSwitchTop() != -1) {
                    layoutParams.topMargin = mCameraParam.getSwitchTop();
                }
                img_switch.setLayoutParams(layoutParams);
            }
            if (mCameraParam.getSwitchImgId() != -1) {
                img_switch.setImageResource(mCameraParam.getSwitchImgId());
            }
        } else {
            img_switch.setVisibility(View.GONE);
        }

        //是否显示裁剪框
        if (mCameraParam.isShowMask()) {
            view_mask.setVisibility(View.VISIBLE);
            if (mCameraParam.getMaskMarginLeftAndRight() != -1 || mCameraParam.getMaskMarginTop() != -1
                    || mCameraParam.getMaskRatioH() != -1) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view_mask.getLayoutParams();

                if (mCameraParam.getMaskMarginLeftAndRight() != -1) {
                    layoutParams.leftMargin = layoutParams.rightMargin = mCameraParam.getMaskMarginLeftAndRight();
                }

                if (mCameraParam.getMaskMarginTop() != -1) {
                    layoutParams.topMargin = mCameraParam.getMaskMarginTop();
                }

                if (mCameraParam.getMaskRatioH() != -1) {
                    Tools.reflectMaskRatio(view_mask, mCameraParam.getMaskRatioW(), mCameraParam.getMaskRatioH());
                }
                view_mask.setLayoutParams(layoutParams);
            }
            if (mCameraParam.getMaskImgId() != -1) {
                view_mask.setBackgroundResource(mCameraParam.getMaskImgId());
            }
        } else {
            view_mask.setVisibility(View.GONE);
        }

        if (mCameraParam.getBackText() != null) {
            tv_back.setText(mCameraParam.getBackText());
        }
        if (mCameraParam.getBackColor() != -1) {
            tv_back.setTextColor(mCameraParam.getBackColor());
        }
        if (mCameraParam.getBackSize() != -1) {
            tv_back.setTextSize(mCameraParam.getBackSize());
        }

        if (mCameraParam.getTakePhotoSize() != -1) {
            int size = mCameraParam.getTakePhotoSize();

            ViewGroup.LayoutParams pictureCancelParams = img_picture_cancel.getLayoutParams();
            pictureCancelParams.width = pictureCancelParams.height = size;
            img_picture_cancel.setLayoutParams(pictureCancelParams);

            ViewGroup.LayoutParams pictureSaveParams = img_picture_save.getLayoutParams();
            pictureSaveParams.width = pictureSaveParams.height = size;
            img_picture_save.setLayoutParams(pictureSaveParams);

            ViewGroup.LayoutParams takePhotoParams = img_take_photo.getLayoutParams();
            takePhotoParams.width = takePhotoParams.height = size;
            img_take_photo.setLayoutParams(takePhotoParams);
        }

        focus_view.setParam(mCameraParam.getFocusViewSize(), mCameraParam.getFocusViewColor(),
                mCameraParam.getFocusViewTime(), mCameraParam.getFocusViewStrokeSize(), mCameraParam.getCornerViewSize());


        if (mCameraParam.getCancelImgId() != -1) {
            img_picture_cancel.setImageResource(mCameraParam.getCancelImgId());
        }
        if (mCameraParam.getSaveImgId() != -1) {
            img_picture_save.setImageResource(mCameraParam.getSaveImgId());
        }
        if (mCameraParam.getTakePhotoImgId() != -1) {
            img_take_photo.setImageResource(mCameraParam.getTakePhotoImgId());
        }

        if (mCameraParam.getResultBottom() != -1) {
            ConstraintLayout.LayoutParams resultPictureParams = (ConstraintLayout.LayoutParams) rl_result_picture.getLayoutParams();
            resultPictureParams.bottomMargin = mCameraParam.getResultBottom();
            rl_result_picture.setLayoutParams(resultPictureParams);

            ConstraintLayout.LayoutParams startParams = (ConstraintLayout.LayoutParams) rl_start.getLayoutParams();
            startParams.bottomMargin = mCameraParam.getResultBottom();
            rl_start.setLayoutParams(startParams);
        }

        if (mCameraParam.getResultLeftAndRight() != -1) {
            RelativeLayout.LayoutParams pictureCancelParams = (RelativeLayout.LayoutParams) img_picture_cancel.getLayoutParams();
            pictureCancelParams.leftMargin = mCameraParam.getResultLeftAndRight();
            img_picture_cancel.setLayoutParams(pictureCancelParams);

            RelativeLayout.LayoutParams pictureSaveParams = (RelativeLayout.LayoutParams) img_picture_save.getLayoutParams();
            pictureSaveParams.rightMargin = mCameraParam.getResultLeftAndRight();
            img_picture_save.setLayoutParams(pictureSaveParams);
        }

        if (mCameraParam.getBackLeft() != -1) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_back.getLayoutParams();
            layoutParams.leftMargin = mCameraParam.getBackLeft();
            tv_back.setLayoutParams(layoutParams);
        }
        Tools.reflectPreviewRatio(previewView, Tools.aspectRatio(this));
    }

    private void initData() {

        //设置新的数据
        TextView gesture_name = findViewById(R.id.gesture_name);
        TextView gesture_explain = findViewById(R.id.gesture_explain);
        ImageView gesture = findViewById(R.id.gesture);

        Box<Note> notesBox;
        Query<Note> notesQuery;
        NotesAdapter notesAdapter;
        notesBox = ObjectBox.get().boxFor(Note.class);

        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
//        notesQuery = notesBox.query().order(Note_.name).build();
        notesQuery = notesBox.query(Note_.name.equal("家")).build();
        //notesQuery = notesBox.query().equal(Note_.name, "家").build();
        List<Note> Gesture = notesQuery.find();

        notesQuery = notesBox.query(Note_.name.equal("工作")).build();
        Gesture.add(notesQuery.find().get(0));
        notesQuery = notesBox.query(Note_.name.equal("人")).build();
        Gesture.add(notesQuery.find().get(0));
        notesQuery = notesBox.query(Note_.name.equal("你")).build();
        Gesture.add(notesQuery.find().get(0));
        notesQuery.close();
        gesture_name.setText(Gesture.get(current).getName());
        gesture.setImageDrawable(Gesture.get(current).getImage(true));
        gesture_explain.setText(Gesture.get(current).getExplain());

        current = (current + 1) % 4;

    }

    private void initView() {


        previewView = findViewById(R.id.previewView);
        img_switch = findViewById(R.id.img_switch);
        ll_picture_parent = findViewById(R.id.ll_picture_parent);
        img_picture = findViewById(R.id.img_picture);
        focus_view = findViewById(R.id.focus_view);
        view_mask = findViewById(R.id.view_mask);
        rl_result_picture = findViewById(R.id.rl_result_picture);
        img_picture_cancel = findViewById(R.id.img_picture_cancel);
        img_picture_save = findViewById(R.id.img_picture_save);
        rl_start = findViewById(R.id.rl_start);
        tv_back = findViewById(R.id.tv_back);
        img_take_photo = findViewById(R.id.img_take_photo);


        loading_page = findViewById(R.id.loading_page);
        next_item = findViewById(R.id.next_item);
        ImageView status = findViewById(R.id.status);
        status.setVisibility(View.GONE);
        status.setOnClickListener(v -> {
            status.setVisibility(View.GONE);
        });

        img_picture_save.setVisibility(View.VISIBLE);
        img_picture_cancel.setVisibility(View.VISIBLE);
        TextView count_down = findViewById(R.id.count_down);
        count_down.setVisibility(View.INVISIBLE);
        //切换相机
        img_switch.setOnClickListener(v -> {
            switchOrition();
            bindCameraUseCases();
        });

        //拍照成功然后点取消
        img_picture_cancel.setOnClickListener(v -> {
            img_picture.setImageBitmap(null);
            rl_start.setVisibility(View.VISIBLE);
            rl_result_picture.setVisibility(View.GONE);
            ll_picture_parent.setVisibility(View.GONE);
        });
        //拍照成功然后点保存
        img_picture_save.setOnClickListener(v -> {
            status.setVisibility(View.VISIBLE);
            status.setBackground(this.getDrawable(R.drawable.ic_pending));

            AtomicReference<Boolean> result = new AtomicReference<>();
            savePicture();

            Thread t1 = new Thread(() -> {
                try {
                    System.out.println("306");

                    File file = new File(mCameraParam.getPicturePath());
                    TextView gesture_name = findViewById(R.id.gesture_name);
                    AI ai = new AI((String) gesture_name.getText(),file);
                    ai.request_server();

                    result.set(ai.get_result());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            try {
                t1.start();
                t1.join();
            } catch (InterruptedException ex) {
                System.out.println("出现异常");
            }
            if (result.get()) {
                status.setBackground(this.getDrawable(R.drawable.ic_answer_true));
            } else {
                status.setBackground(this.getDrawable(R.drawable.ic_answer_false));
            }

        });
        //还没拍照就点取消
        tv_back.setOnClickListener(v -> {
            finish();
        });
        //点击拍照
        img_take_photo.setOnClickListener(v -> {
            System.out.println("OK1");
            savePictureAfterDelay(3);


            /*rl_start.setVisibility(View.GONE);
            rl_result_picture.setVisibility(View.VISIBLE);
            img_picture_save.setVisibility(View.VISIBLE);
            img_picture_cancel.setVisibility(View.VISIBLE);*/
        });
        //下一个 重新设置界面
        next_item.setOnClickListener(v -> {
            img_picture.setImageBitmap(null);
            rl_start.setVisibility(View.VISIBLE);
            rl_result_picture.setVisibility(View.GONE);
            ll_picture_parent.setVisibility(View.GONE);
            next_item.setVisibility(View.INVISIBLE);
            img_take_photo.setVisibility(View.VISIBLE);
            tv_back.setVisibility(View.VISIBLE);

            status.setVisibility(View.GONE);

            initView();
            initData();
            setViewParam();
            intCamera();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            autoFocus((int) event.getX(), (int) event.getY(), false);
        }
        return super.onTouchEvent(event);
    }

    private void switchOrition() {
        if (front) {
            front = false;
        } else {
            front = true;
        }
    }

    private void intCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (Exception e) {
                Log.d("wld________", e.toString());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        int screenAspectRatio = Tools.aspectRatio(this);
        int rotation = previewView.getDisplay() == null ? Surface.ROTATION_0 : previewView.getDisplay().getRotation();

        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build();

        imageCapture = new ImageCapture.Builder()
                //优化捕获速度，可能降低图片质量
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build();
        // 在重新绑定之前取消绑定用例
        cameraProvider.unbindAll();
        int cameraOrition = front ? CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK;
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(cameraOrition).build();
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        mCameraControl = camera.getCameraControl();
//        mCameraInfo = camera.getCameraInfo();

        int[] outLocation = Tools.getViewLocal(view_mask);
        autoFocus(outLocation[0] + (view_mask.getMeasuredWidth()) / 2, outLocation[1] + (view_mask.getMeasuredHeight()) / 2, true);
    }

    private void takePhoto(String photoFile) {
        // 保证相机可用
        if (imageCapture == null)
            return;

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(new File(photoFile)).build();

        //  设置图像捕获监听器，在拍照后触发
        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        rl_start.setVisibility(View.GONE);
                        rl_result_picture.setVisibility(View.VISIBLE);
                        ll_picture_parent.setVisibility(View.VISIBLE);
                        Bitmap bitmap = Tools.bitmapClip(CameraActivity.this, photoFile, front);
                        img_picture.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e("wld_____", "Photo capture failed: ${exc.message}", exception);
                    }
                });
    }

    private void savePicture() {
        Rect rect = null;
        if (mCameraParam.isShowMask()) {
            int[] outLocation = Tools.getViewLocal(view_mask);
            rect = new Rect(outLocation[0], outLocation[1],
                    view_mask.getMeasuredWidth(), view_mask.getMeasuredHeight());
        }
        Tools.saveBitmap(this, mCameraParam.getPictureTempPath(), mCameraParam.getPicturePath(), rect, front);
        Tools.deletTempFile(mCameraParam.getPictureTempPath());
        Toast.makeText(getBaseContext(), mCameraParam.getPicturePath(), Toast.LENGTH_LONG);
        Intent intent = new Intent();
        intent.putExtra(CameraConstant.PICTURE_PATH_KEY, mCameraParam.getPicturePath());
        setResult(RESULT_OK, intent);

        //显示进度
        /*        loading_page.setVisibility(View.VISIBLE);
         *//*        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return;
        }*//*
        loading_page.setVisibility(View.GONE);*/
        //显示结果及下一个
        img_picture_save.setVisibility(View.GONE);
        img_picture_cancel.setVisibility(View.GONE);
        rl_start.setVisibility(View.VISIBLE);
        img_take_photo.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.INVISIBLE);
        next_item.setVisibility(View.VISIBLE);
        System.out.println();

//        finish();
    }

    //https://developer.android.com/training/camerax/configuration
    private void autoFocus(int x, int y, boolean first) {
//        MeteringPointFactory factory = previewView.getMeteringPointFactory();
        MeteringPointFactory factory = new SurfaceOrientedMeteringPointFactory(x, y);
        MeteringPoint point = factory.createPoint(x, y);
        FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
//                .disableAutoCancel()
//                .addPoint(point2, FocusMeteringAction.FLAG_AE)
                // 3秒内自动调用取消对焦
                .setAutoCancelDuration(mCameraParam.getFocusViewTime(), TimeUnit.SECONDS)
                .build();
//        mCameraControl.cancelFocusAndMetering();
        ListenableFuture<FocusMeteringResult> future = mCameraControl.startFocusAndMetering(action);
        future.addListener(() -> {
            try {
                FocusMeteringResult result = future.get();
                if (result.isFocusSuccessful()) {
                    focus_view.showFocusView(x, y);
                    if (!first && mCameraParam.isShowFocusTips()) {
                        Toast mToast = Toast.makeText(getApplicationContext(), mCameraParam.getFocusSuccessTips(this), Toast.LENGTH_LONG);
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                        mToast.show();
                    }
                } else {
                    if (mCameraParam.isShowFocusTips()) {
                        Toast mToast = Toast.makeText(getApplicationContext(), mCameraParam.getFocusFailTips(this), Toast.LENGTH_LONG);
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                        mToast.show();
                    }
                    focus_view.hideFocusView();
                }
            } catch (Exception e) {
                e.printStackTrace();
                focus_view.hideFocusView();
            }
        }, ContextCompat.getMainExecutor(this));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraProvider.unbindAll();
//        if (cameraExecutor != null)
//            cameraExecutor.shutdown();
    }
    private final int HIDE=0;
    private final int PENDING=1;
    private final int TRUE=2;
    private final int FALSE=3;
    int pictureTimer = 0;
    // assign ID when we start a timed picture, used in makeDecrementTimerFunction callback. If the ID changes, the countdown will stop.
    int currentPictureID = 0;
    Handler handler = new Handler();

    private boolean showStatus(int status) {
        ImageView status_indicator = findViewById(R.id.status);
        if (status == HIDE) {
            //隐藏状态
            status_indicator.setVisibility(View.INVISIBLE);
        }
        if (status >= PENDING && status <= FALSE) {
            status_indicator.setVisibility(View.VISIBLE);
        }
        if (status == PENDING) {
            //等待状态
            status_indicator.setBackground(this.getDrawable(R.drawable.ic_pending));
            return true;
        } else if (status == TRUE) {

            //正确状态
            status_indicator.setBackground(this.getDrawable(R.drawable.ic_answer_true));
            return true;
        } else if (status == FALSE) {
            //错误状态
            status_indicator.setBackground(this.getDrawable(R.drawable.ic_answer_false));
            return true;
        } else {
            return false;
        }
    }
    void savePictureAfterDelay(int delay) {
        ImageView status_indicator = findViewById(R.id.status);
        status_indicator.setVisibility(View.GONE);
        pictureTimer = delay;
        updateTimerMessage();
        currentPictureID++;

        handler.postDelayed(makeDecrementTimerFunction(currentPictureID), 1000);
        //beepType = RAND.nextInt(numBeepTypes);

        //updateButtons(false);
    }
    void updateTimerMessage() {
//显示倒计时

        TextView count_down = findViewById(R.id.count_down);


        count_down.setText(String.valueOf(pictureTimer));
        count_down.setVisibility(View.VISIBLE);
        /*        String messageFormat = getString(R.string.timerCountdownMessageFormat);
        statusTextField.setText(String.format(messageFormat, pictureTimer));*/
    }
    Runnable makeDecrementTimerFunction(final int pictureID) {
        return new Runnable() {
            public void run() {decrementTimer(pictureID);}
        };
    }
    public void decrementTimer(final int pictureID) {
        TextView count_down = findViewById(R.id.count_down);
        if (pictureID!=this.currentPictureID) {
            return;
        }
        boolean takePicture = (pictureTimer==1);
        --pictureTimer;
        if (takePicture) {
            count_down.setVisibility(View.INVISIBLE);
            takePhoto(mCameraParam.getPictureTempPath());



            //playTimerBeep();
        }
        else if (pictureTimer>0) {
            updateTimerMessage();
            handler.postDelayed(makeDecrementTimerFunction(pictureID), 1000);
//            if (pictureTimer<3) playTimerBeep();
        }
    }
}
