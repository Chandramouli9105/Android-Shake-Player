package media.radio.player;

import android.app.Activity;
import android.hardware.SensorListener;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;


@SuppressWarnings("deprecation")
public class GifPlayer extends Activity implements SensorListener{

	public GifView gifView;
	String getGif;

	 private int stateMediaPlayer;
	 private final int stateMP_NotStarter = 0;
	 private final int stateMP_Playing = 1;
	 private final int stateMP_Pausing = 2;
	 
	 ImageButton btnPlayPauseGif, btnStopGif;
	 
	 private SensorManager mSensorManager;
	 
	 private long lastUpdateGif = -1;
     private float xGif, yGif, zGif;
     private float last_x_Gif=0;
     private float last_y_Gif=0;
     private float last_z_Gif=0;
     private static final int SHAKE_THRESHOLD_Gif = 800;

	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gifplayer);

		gifView = (GifView) findViewById(R.id.gifView);
		btnPlayPauseGif = (ImageButton)findViewById(R.id.btnPlayGif);
		btnStopGif = (ImageButton)findViewById(R.id.btnStopGif);
		
		Intent i_get = getIntent();
		getGif = i_get.getStringExtra("gif");
		
		String stringPath = getGif;
		gifView.setGif(stringPath);
		
		/*mSensorListener = new ShakeEvent();
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener,
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_UI);
	    mSensorListener.setOnShakeListener(new ShakeEvent.OnShakeListener() {
	      public void onShake() {
	        //Toast.makeText(MusicPlayer.this, "Shake!", Toast.LENGTH_SHORT).show();
	    	  onClickGif(btnPlayPauseGif);
	      }
	    });
	    */
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    boolean accelSupported = mSensorManager.registerListener(this,
	        SensorManager.SENSOR_ACCELEROMETER,
	        SensorManager.SENSOR_DELAY_GAME);

	    if (!accelSupported) {
	        // on accelerometer on this device
	    	mSensorManager.unregisterListener(this,
	                SensorManager.SENSOR_ACCELEROMETER);
	    }
	    
	}
	
	@Override
	  protected void onResume() {
		 mSensorManager.registerListener(this,
			        SensorManager.SENSOR_ACCELEROMETER,
			        SensorManager.SENSOR_DELAY_GAME);
	    super.onResume();
	  }

	  @Override
	  protected void onStop() {
		  if (mSensorManager != null) {
		    	 mSensorManager.unregisterListener(this,
		                 SensorManager.SENSOR_ACCELEROMETER);
		    	 mSensorManager = null;
		         }
	    super.onStop();
	  }
	
	public void onClickStopGif(View v){
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	 	findViewById(R.id.btnStopGif).startAnimation(shake);
		
	 	gifView.stop();
	}
	
	public void onClickGif(View arg0) {
		// TODO Auto-generated method stub
		
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	 	findViewById(R.id.btnPlayGif).startAnimation(shake);
	 	
		 switch(stateMediaPlayer){
		    case stateMP_NotStarter:
		     gifView.play();
		     btnPlayPauseGif.setImageResource(R.drawable.pause);
		     stateMediaPlayer = stateMP_Playing;
		     break;
		    case stateMP_Playing:
		     gifView.pause();
		     btnPlayPauseGif.setImageResource(R.drawable.play);
		     stateMediaPlayer = stateMP_Pausing;
		     break;
		    case stateMP_Pausing:
		     gifView.play();
		     btnPlayPauseGif.setImageResource(R.drawable.pause);
		     stateMediaPlayer = stateMP_Playing;
		     break;
		    }
	}

	public void onAccuracyChanged(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(int sensor, float[] values) {
		if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdateGif) > 100) {
            long diffTime = (curTime - lastUpdateGif);
            lastUpdateGif = curTime;

            xGif = values[SensorManager.DATA_X];
            yGif = values[SensorManager.DATA_Y];
            zGif = values[SensorManager.DATA_Z];

            if(Round(xGif,4)>10.0000){
                Log.d("sensor", "X Right axis: " + xGif);
                //Toast.makeText(this, "Right shake detected", Toast.LENGTH_SHORT).show();
                onClickStopGif(btnStopGif);
            }
            else if(Round(xGif,4)<-10.0000){
                Log.d("sensor", "X Left axis: " + xGif);
                //Toast.makeText(this, "Left shake detected", Toast.LENGTH_SHORT).show();
                onClickGif(btnPlayPauseGif);
            }

            float speed = Math.abs(xGif+yGif+zGif - last_x_Gif - last_y_Gif - last_z_Gif) / diffTime * 10000;

            // Log.d("sensor", "diff: " + diffTime + " - speed: " + speed);
            if (speed > SHAKE_THRESHOLD_Gif) {
                //Log.d("sensor", "shake detected w/ speed: " + speed);
                //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
            }
            last_x_Gif = xGif;
            last_y_Gif = yGif;
            last_z_Gif = zGif;
            }
		}
	}
	
	public static float Round(float Rval, int Rpl) {
	    float p = (float)Math.pow(10,Rpl);
	    Rval = Rval * p;
	    float tmp = Math.round(Rval);
	    return (float)tmp/p;
	    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        //moveTaskToBack(true);
	    	killActivity();
	        return true;		        
	    }
	    return super.onKeyDown(keyCode, event);
	}

 public void killActivity(){
	 this.finish();
 }
 
}

