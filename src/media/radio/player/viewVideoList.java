package media.radio.player;

import java.io.IOException;
import media.radio.player.R;
import android.app.Activity;
import android.hardware.SensorListener;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;


@SuppressWarnings("deprecation")
public class viewVideoList extends Activity implements SurfaceHolder.Callback,  SensorListener{
	
	MediaPlayer mediaPlayer;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean pausing = false;
	String getVideo, getPath;
	
	 private int stateMediaPlayer;
	 private final int stateMP_NotStarter = 0;
	 private final int stateMP_Playing = 1;
	 private final int stateMP_Pausing = 2;
	 
	 private SensorManager mSensorManager;	 
	 private long lastUpdateVideo = -1;
     private float xVideo, yVideo, zVideo;
     private float last_x_Video=0;
     private float last_y_Video=0;
     private float last_z_Video=0;
     private static final int SHAKE_THRESHOLD_VIDEO = 800;

	 ImageButton btnPlayVideo, btnStopVideo, btnPrevVideo, btnNextVideo, btnFullVideo;
	 TextView durasi, currentDurasi;
	 
	 private SeekBar seekBar;
	 private final Handler handler = new Handler();
	 private Utilities utils;
	 
	/** Called when the activity is first created. */
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.viewvideolist);
	     
	    btnPlayVideo = (ImageButton)findViewById(R.id.btnPlayVideo);
	    durasi = (TextView)findViewById(R.id.txtDurationVideo);
	    btnStopVideo = (ImageButton)findViewById(R.id.btnStopVideo);
	    currentDurasi = (TextView)findViewById(R.id.txtCurrentDurasiVideo);
	    btnFullVideo = (ImageButton)findViewById(R.id.fullScreenVideo);
	    
	     getWindow().setFormat(PixelFormat.UNKNOWN);
	     surfaceView = (SurfaceView)findViewById(R.id.surfaceview);
	     surfaceHolder = surfaceView.getHolder();
	     surfaceHolder.addCallback(this);
	     surfaceHolder.setFixedSize(176, 144);
	     surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	     mediaPlayer = new MediaPlayer();
	     utils = new Utilities();
	     
	    Intent i_get = getIntent();
	 	getVideo = i_get.getStringExtra("video");
 
	    /* 
	 	mSensorListener = new ShakeEvent();
	     mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	     mSensorManager.registerListener(mSensorListener,
	         mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	         SensorManager.SENSOR_DELAY_UI);


	     mSensorListener.setOnShakeListener(new ShakeEvent.OnShakeListener() {

	       public void onShake() {
	         //Toast.makeText(VideoPlayer.this, "Shake!", Toast.LENGTH_SHORT).show();
	    	   onClickVideo(btnPlayVideo);
	       }
	     });    */
	 	
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
	 
	 
	 public void seekBar(){
		 
		 final int HOUR = 60*60*1000;
		 	final int MINUTE = 60*1000;
		    final int SECOND = 1000;
		 	int duration;
		 	duration = mediaPlayer.getDuration();
		 	int durationMint = (duration%HOUR)/MINUTE;
		    int durationSec = (duration%MINUTE)/SECOND;
		    
		 seekBar = (SeekBar) findViewById(R.id.SeekBarVideo);
	     seekBar.setMax(mediaPlayer.getDuration());
	     seekBar.setOnTouchListener(new OnTouchListener() {
	 		public boolean onTouch(View v, MotionEvent arg1) {
	 			// TODO Auto-generated method stub
	 			seekChange(v);
	 			return false;
	 		}
	 	});
	     durasi.setText(String.format("%02d:%02d",durationMint,durationSec));
		 
	 }
	 public void startPlayProgressUpdater() {
	    	seekBar.setProgress(mediaPlayer.getCurrentPosition());

			if (mediaPlayer.isPlaying()) {
				Runnable notification = new Runnable() {
			        public void run() {
			        	startPlayProgressUpdater();
			        	 long totalDuration = mediaPlayer.getDuration();
						   long currentDuration = mediaPlayer.getCurrentPosition();
						  
						   // Displaying Total Duration time
						   durasi.setText(""+utils.milliSecondsToTimer(totalDuration));
						   // Displaying time completed playing
						   currentDurasi.setText(""+utils.milliSecondsToTimer(currentDuration));
					}
			    };
			    handler.postDelayed(notification,1000);
	    	}else{
	    		mediaPlayer.pause();
	    		//buttonPlayStop.setText(getString(R.string.play_str));
	    		//seekBar.setProgress(0);
	    	}
	    } 

	    // This is event handler thumb moving event
	    private void seekChange(View v){
	    	if(mediaPlayer.isPlaying()){
		    	SeekBar sb = (SeekBar)v;
				mediaPlayer.seekTo(sb.getProgress());
			}
	    }
	 
	 public void onClickFullVideo(View v){
		 Intent intent = new Intent(viewVideoList.this, ViewVideo.class);
         intent.putExtra("video", getVideo);
         startActivity(intent);
	 }   
	    
	 public void onClickStopVideo(View v){
		 	Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 	findViewById(R.id.btnStopVideo).startAnimation(shake);
		  
		 	mediaPlayer.reset();
			seekBar.setProgress(0);
	    	btnPlayVideo.setImageResource(R.drawable.play);
	    	mediaPlayer.start();
	 }   
	    
	    
	 public void onClickVideo(View v) {
		 
	  final String stringPath = getVideo;	   
	  Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	  findViewById(R.id.btnPlayVideo).startAnimation(shake);
	    
   		 switch(stateMediaPlayer){
			    case stateMP_NotStarter:
			    	  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			    	  mediaPlayer.setDisplay(surfaceHolder);

			    	  try {
			    	   mediaPlayer.setDataSource(stringPath);
			    	   mediaPlayer.prepare();
			    	  } catch (IllegalArgumentException e) {
			    	   // TODO Auto-generated catch block
			    	   e.printStackTrace();
			    	  } catch (IllegalStateException e) {
			    	   // TODO Auto-generated catch block
			    	   e.printStackTrace();
			    	  } catch (IOException e) {
			    	   // TODO Auto-generated catch block
			    	   e.printStackTrace();
			    	  }
			     mediaPlayer.start();
			     seekBar();
			     startPlayProgressUpdater();
			     btnPlayVideo.setImageResource(R.drawable.pause);
			     stateMediaPlayer = stateMP_Playing;
			     Toast.makeText(this, getVideo, Toast.LENGTH_SHORT).show();
			     break;
			    case stateMP_Playing:
			     mediaPlayer.pause();
			     btnPlayVideo.setImageResource(R.drawable.play);
			     stateMediaPlayer = stateMP_Pausing;
			     break;
			    case stateMP_Pausing:
			    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			    	  mediaPlayer.setDisplay(surfaceHolder);

			    	  try {
			    	   mediaPlayer.setDataSource(stringPath);
			    	   mediaPlayer.prepare();
			    	  } catch (IllegalArgumentException e) {
			    	   // TODO Auto-generated catch block
			    	   e.printStackTrace();
			    	  } catch (IllegalStateException e) {
			    	   // TODO Auto-generated catch block
			    	   e.printStackTrace();
			    	  } catch (IOException e) {
			    	   // TODO Auto-generated catch block
			    	   e.printStackTrace();
			    	  }
			     mediaPlayer.start();
			     seekBar();
			     startPlayProgressUpdater();
			     btnPlayVideo.setImageResource(R.drawable.pause);
			     stateMediaPlayer = stateMP_Playing;
			     Toast.makeText(this, getVideo, Toast.LENGTH_SHORT).show();
			     break;
			    
			    }
   	 }
	 
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(int sensor, float[] values) {
		if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdateVideo) > 100) {
            long diffTime = (curTime - lastUpdateVideo);
            lastUpdateVideo = curTime;

            xVideo = values[SensorManager.DATA_X];
            yVideo = values[SensorManager.DATA_Y];
            zVideo = values[SensorManager.DATA_Z];

            if(Round(xVideo,4)>10.0000){
                Log.d("sensor", "X Right axis: " + xVideo);
                //Toast.makeText(this, "Right shake detected", Toast.LENGTH_SHORT).show();
                onClickStopVideo(btnStopVideo);
            }
            else if(Round(xVideo,4)<-10.0000){
                Log.d("sensor", "X Left axis: " + xVideo);
                //Toast.makeText(this, "Left shake detected", Toast.LENGTH_SHORT).show();
                onClickVideo(btnPlayVideo);
            }

            float speed = Math.abs(xVideo+yVideo+zVideo - last_x_Video - last_y_Video - last_z_Video) / diffTime * 10000;

            // Log.d("sensor", "diff: " + diffTime + " - speed: " + speed);
            if (speed > SHAKE_THRESHOLD_VIDEO) {
                //Log.d("sensor", "shake detected w/ speed: " + speed);
                //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
            }
            last_x_Video = xVideo;
            last_y_Video = yVideo;
            last_z_Video = zVideo;
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
