package com.mytechia.robobo.framework.robobo_sensing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.sensing.accel.IAccelerationListener;
import com.mytechia.robobo.framework.sensing.accel.IAccelerationModule;
import com.mytechia.robobo.framework.sensing.brightness.IBrightnessListener;
import com.mytechia.robobo.framework.sensing.brightness.IBrightnessModule;
import com.mytechia.robobo.framework.sensing.orientation.IOrientationListener;
import com.mytechia.robobo.framework.sensing.orientation.IOrientationModule;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;

public class MainActivity extends AppCompatActivity implements IOrientationListener, IBrightnessListener, IAccelerationListener {

    private TextView textView;
    private RoboboServiceHelper roboboHelper;
    private RoboboManager roboboManager;
    private IOrientationModule orientationModule;
    private IBrightnessModule brightnessModule;
    private IAccelerationModule accelerationModule;
    private String TAG = "MainActivity";

    private ProgressBar brightnessBar;
    private ProgressBar yawBar;
    private ProgressBar pitchBar;
    private ProgressBar rollBar;

    private float maxval = (float) 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.rollBar = (ProgressBar) findViewById(R.id.rollBar);
        this.yawBar = (ProgressBar) findViewById(R.id.yawBar);
        this.pitchBar = (ProgressBar) findViewById(R.id.pitchBar);

        this.brightnessBar = (ProgressBar) findViewById(R.id.brightnessBar);
        rollBar.setMax(360);
        pitchBar.setMax(360);
        yawBar.setMax(360);
        rollBar.setProgress(128);
        brightnessBar.setMax(50000);
//        this.textureView = (TextureView) findViewById(R.id.textureView);
        roboboHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
            @Override
            public void onRoboboManagerStarted(RoboboManager robobo) {

                //the robobo service and manager have been started up
                roboboManager = robobo;


                //dismiss the wait dialog


                //start the "custom" robobo application
                startRoboboApplication();

            }

            @Override
            public void onError(Exception errorMsg) {

                final String error = errorMsg.getLocalizedMessage();


            }

        });

        //start & bind the Robobo service
        Bundle options = new Bundle();
        roboboHelper.bindRoboboService(options);
    }
    private void startRoboboApplication() {

        try {
            orientationModule = roboboManager.getModuleInstance(IOrientationModule.class);
            brightnessModule = roboboManager.getModuleInstance(IBrightnessModule.class);
            accelerationModule = roboboManager.getModuleInstance(IAccelerationModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
        brightnessModule.suscribe(this);
        orientationModule.suscribe(this);
        accelerationModule.suscribe(this);
    }

    @Override
    public void onOrientationChanged(final float yaw, final float pitch,final float roll) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG,"Yaw: " + yaw + " Pitch: " + pitch + " Roll: " + roll);

                yawBar.setProgress(Math.round(yaw+180));
                pitchBar.setProgress(Math.round(pitch+180));
                rollBar.setProgress(Math.round(roll+180));

            }
        });





    }

    @Override
    public void onBrightness(final float value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG,"Yaw: " + yaw + " Pitch: " + pitch + " Roll: " + roll);

                if (value> maxval){
                    brightnessBar.setMax(Math.round(value));
                    maxval = value;
                }
                //Log.d(TAG,"Brightness value "+ value);
               brightnessBar.setProgress(Math.round(value));

            }
        });
    }

    @Override
    public void onBrightnessChanged() {
        Log.d(TAG,"Changed!");
    }

    @Override
    public void onAccelerationChange() {
        Log.d(TAG,"ACCELCHANGED");
    }

    @Override
    public void onAcceleration(final double xaccel, final double yaccel, final double zaccel) {

    }

    @Override
    public void onCalibrationAngle(double angle) {
        Log.w(TAG,angle+"");
    }
}
