package com.mytechia.robobo.framework.robobo_sensing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.sensing.orientation.IOrientationListener;
import com.mytechia.robobo.framework.sensing.orientation.IOrientationModule;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;

public class MainActivity extends AppCompatActivity implements IOrientationListener {

    private TextView textView;
    private RoboboServiceHelper roboboHelper;
    private RoboboManager roboboManager;
    private IOrientationModule orientationModule;
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.textView = (TextView) findViewById(R.id.textView);


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
            public void onError(String errorMsg) {

                final String error = errorMsg;


            }

        });

        //start & bind the Robobo service
        Bundle options = new Bundle();
        roboboHelper.bindRoboboService(options);
    }
    private void startRoboboApplication() {

        try {
            orientationModule = roboboManager.getModuleInstance(IOrientationModule.class);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
        orientationModule.suscribe(this);
    }

    @Override
    public void onOrientationChanged(final float yaw, final float pitch,final float roll) {


                    Log.d(TAG,"Yaw: " + yaw + " Pitch: " + pitch + " Roll: " + roll);


    }
}
