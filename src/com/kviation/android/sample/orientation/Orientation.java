
package com.kviation.android.sample.orientation;

import cc.storymaker.guidedcam.NotificationController;
import cc.storymaker.guidedcam.NotificationPlugin;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Context;

public class Orientation extends NotificationPlugin implements SensorEventListener {

//  public interface Listener {
//    void onOrientationChanged(float pitch, float roll);
//  }

  private static final int SENSOR_DELAY_MICROS = 50 * 1000; // 50ms

  private final SensorManager mSensorManager;
  private final Sensor mRotationSensor;
  private final WindowManager mWindowManager;

  private int mLastAccuracy;
//  private Listener mListener;
  Context mContext;
  AttitudeIndicator mView;
  
  
  public Orientation(Context context, NotificationController controller, View view) {
    super(context, controller, view);
	mContext = context;
	mView = (AttitudeIndicator) view;
	    
    mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
    mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    
    // Can be null if the sensor hardware is not available
    mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    Log.e("" + mRotationSensor);
  }

  public void start() {
//    if (mListener == listener) {
//      return;
//    }
//    mListener = listener;
    if (mRotationSensor == null) {
      Log.w("Rotation vector sensor not available; will not provide orientation data.");
      return;
    }
    mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY_MICROS);
  }

  public void stop() {
    mSensorManager.unregisterListener(this);
//    mListener = null;
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    if (mLastAccuracy != accuracy) {
      mLastAccuracy = accuracy;
    }
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
//    if (mListener == null) {
//      return;
//    }
    if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
      return;
    }
    if (event.sensor == mRotationSensor) {
      updateOrientation(event.values);
    }
  }

  private void updateOrientation(float[] rotationVector) {
    float[] rotationMatrix = new float[9];
    SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

    // By default, remap the axes as if the front of the
    // device screen was the instrument panel.
    int worldAxisForDeviceAxisX = SensorManager.AXIS_X;
    int worldAxisForDeviceAxisY = SensorManager.AXIS_Z;

    // Adjust the rotation matrix for the device orientation
    int screenRotation = mWindowManager.getDefaultDisplay().getRotation();
    if (screenRotation == Surface.ROTATION_0) {
      worldAxisForDeviceAxisX = SensorManager.AXIS_X;
      worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
    } else if (screenRotation == Surface.ROTATION_90) {
      worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
      worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
    } else if (screenRotation == Surface.ROTATION_180) {
      worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
      worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
    } else if (screenRotation == Surface.ROTATION_270) {
      worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
      worldAxisForDeviceAxisY = SensorManager.AXIS_X;
    }

    float[] adjustedRotationMatrix = new float[9];
    SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
        worldAxisForDeviceAxisY, adjustedRotationMatrix);

    // Transform rotation matrix into azimuth/pitch/roll
    float[] orientation = new float[3];
    SensorManager.getOrientation(adjustedRotationMatrix, orientation);

    // Convert radians to degrees
    float pitch = orientation[1] * -57;
    float roll = orientation[2] * -57;

//    mListener.onOrientationChanged(pitch, roll);
    mView.setAttitude(pitch, roll);
  }
}
