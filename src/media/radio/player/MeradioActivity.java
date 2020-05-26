package media.radio.player;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;


public class MeradioActivity extends Activity{
		
	 TextView shake;
	 private SensorManager mSensorManager;
	 private ShakeEvent mSensorListener;
	 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		shake=(TextView)findViewById(R.id.shake);
		//new CekKoneksi().execute();
		mSensorListener = new ShakeEvent();
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener,
	        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_UI);
	    mSensorListener.setOnShakeListener(new ShakeEvent.OnShakeListener() {
	      public void onShake() {
	    	  new CekKoneksi().execute();
	      }
	    });
	    
	}
	 
	 @Override
		public void onConfigurationChanged(final Configuration newConfig)
		{
		    // Ignore orientation change to keep activity from restarting
		    super.onConfigurationChanged(newConfig);
		}
		
		public class CekKoneksi extends AsyncTask<Void, Void, Void> {
			ProgressDialog dialog;
			
			@Override
			 protected void onPreExecute() {
			  // TODO Auto-generated method stub
				 dialog= ProgressDialog.show(MeradioActivity.this, "", 
		                 "Loading...", true);
			 }

			 @Override
			 protected Void doInBackground(Void... params) {
			  // TODO Auto-generated method stub
				 move();
				 return null;
			  
			 }

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				dialog.dismiss();				
			}
		}
	 
	public void move(){        
		Intent intent = new Intent(MeradioActivity.this, MainForm.class);
		startActivity(intent);
	}


	 @Override
		  protected void onResume() {
		    super.onResume();
		    mSensorManager.registerListener(mSensorListener,
		        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		        SensorManager.SENSOR_DELAY_UI);
		  }

		  @Override
		  protected void onStop() {
		    mSensorManager.unregisterListener(mSensorListener);
		    super.onStop();
		  }
		  
		  @Override
	      protected void onDestroy() {
	            // TODO Auto-generated method stub
	            super.onDestroy();
	            /**
	             * it show massage on LogCat when Activity Destroys
	             */
	            Log.i("Login Activity", "Destroy");
	            killActivity();
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
		        finish();

		 }		
}
