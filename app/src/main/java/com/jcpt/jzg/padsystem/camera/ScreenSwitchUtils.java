package com.jcpt.jzg.padsystem.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jcpt.jzg.padsystem.utils.LogUtil;

import static android.R.attr.orientation;

/**
 * Created by libo on 2017/1/9.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class ScreenSwitchUtils {

    public static final int ORIENTATION_PORTRAIT = 90;
    public static final int ORIENTATION_LANDSCAPE = 0;

    public static  int orientationDigree180 = 180;
    public static int orientationDigree270 = 270;

    public static int screenOrientation = ORIENTATION_LANDSCAPE;

    private static final String TAG = ScreenSwitchUtils.class.getSimpleName();

    private volatile static ScreenSwitchUtils mInstance;

    private Activity mActivity;

    // 是否是竖屏
    private boolean isPortrait = true;

    private SensorManager sm;
    private OrientationSensorListener listener;
    private Sensor sensor;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 888:
                    int orientation = msg.arg1;
                    if (orientation > 45 && orientation < 135) {
                        Log.e("test", " 45 - - 135 切换"); //180

                    } else if (orientation > 135 && orientation < 225) {
                        Log.e("test", "135 - - 225 切换");//270
                    } else if (orientation > 225 && orientation < 315) {
                        if (isPortrait) {
                            Log.e("test", "切换成横屏");
                            screenOrientation = ORIENTATION_LANDSCAPE;
//                            mActivity.setRequestedOrientation(0);
                            isPortrait = false;
                        }
                    } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                        if (!isPortrait) {
                            Log.e("test","切换成竖屏");
                            screenOrientation = ORIENTATION_PORTRAIT;
//                            mActivity.setRequestedOrientation(1);
                            isPortrait = true;
                        }
                    }
                    break;
                default:
                    break;
            }

        };
    };

    /** 返回ScreenSwitchUtils单例 **/
    public static ScreenSwitchUtils init(Context context) {
        if (mInstance == null) {
            synchronized (ScreenSwitchUtils.class) {
                if (mInstance == null) {
                    mInstance = new ScreenSwitchUtils(context);
                }
            }
        }
        return mInstance;
    }

    private ScreenSwitchUtils(Context context) {
        // 注册重力感应器,监听屏幕旋转
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(mHandler);
    }

    /** 开始监听 */
    public void start(Activity activity) {
        mActivity = activity;
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /** 停止监听 */
    public void stop() {
        sm.unregisterListener(listener);
    }

    /**
     * 手动横竖屏切换方向
     */
    public void toggleScreen() {
        sm.unregisterListener(listener);
        if (isPortrait) {
            isPortrait = false;
            // 切换成横屏
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            isPortrait = true;
            // 切换成竖屏
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public boolean isPortrait(){
        return this.isPortrait;
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener implements SensorEventListener {

        float[] accelerometerValues=new float[3];
        float[] magneticFieldValues=new float[3];

        private Handler rotateHandler;

        public OrientationSensorListener(Handler handler) {
            rotateHandler = handler;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magneticFieldValues=sensorEvent.values;
            }
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = sensorEvent.values;
            }

            float[]values=new float[3];
            float[] R= new float[9];
            SensorManager.getRotationMatrix(R,null,accelerometerValues,magneticFieldValues);
            SensorManager.getOrientation(R,values);
            // 要经过一次数据格式的转换，转换为度  
            values[0]=(float)Math.toDegrees(values[0]);
            LogUtil.e(TAG,"values[0] : "+values[0]);

            if (rotateHandler != null) {
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }
    }
}

