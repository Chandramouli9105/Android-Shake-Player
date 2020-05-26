package media.radio.player;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.hardware.SensorListener;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.view.View.OnTouchListener;


@SuppressWarnings("deprecation")
class Mp3Filter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return (name.endsWith(".mp3")||name.endsWith(".mid")||name.endsWith(".wav")||name.endsWith(".ogg")||name.endsWith(".flac")||name.endsWith(".ota")||name.endsWith(".imy")
				  ||name.endsWith(".xmf")||name.endsWith(".rtx"));
	}
}

@SuppressWarnings("deprecation")
public class MusicPlayer extends Activity implements SensorListener{
	 MediaPlayer mediaPlayer;
	 ImageButton buttonPlayPause, btnStop, btnNext, btnPrevious;
	 TextView durasi, currentDurasi;
	 
	 private int stateMediaPlayer;
	 private final int stateMP_NotStarter = 0;
	 private final int stateMP_Playing = 1;
	 private final int stateMP_Pausing = 2;
	 
	 String getMusic;
	 String pathFile;
	 
	 private SensorManager mSensorManager;	 
	 private long lastUpdateMusic = -1;
     private float xMusic, yMusic, zMusic;
     private float last_x_Music=0;
     private float last_y_Music=0;
     private float last_z_Music=0;
     private static final int SHAKE_THRESHOLD_MUSIC = 800;


	 private SeekBar seekBar;
	 private final Handler handler = new Handler();
	 private Utilities utils;
	 
	 private List<String> songs = new ArrayList<String>();
	 private int currentPosition;		
	 
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			setContentView(R.layout.musicplayer);
			 
	Intent i_get = getIntent();
	getMusic = i_get.getStringExtra("musik");
	pathFile = i_get.getStringExtra("path");		 
	
	buttonPlayPause = (ImageButton)findViewById(R.id.btnPlay);
	durasi = (TextView)findViewById(R.id.txtDuration);
	currentDurasi = (TextView)findViewById(R.id.txtCurrentDurasi);
	btnStop = (ImageButton)findViewById(R.id.btnStop);
	btnNext = (ImageButton)findViewById(R.id.btnNext);
	btnPrevious = (ImageButton)findViewById(R.id.btnPrev);


	mediaPlayer = new  MediaPlayer();
	utils = new Utilities();
	
    /*mSensorListener = new ShakeEvent();
    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mSensorManager.registerListener(mSensorListener,
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_UI);
    mSensorListener.setOnShakeListener(new ShakeEvent.OnShakeListener() {
      public void onShake() {
        //Toast.makeText(MusicPlayer.this, "Shake!", Toast.LENGTH_SHORT).show();
    	  onClick(buttonPlayPause);
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
	 
	 
	 public void updateSongList() {
			File home = new File(pathFile);
			File fileList[] = home.listFiles(new Mp3Filter());
			if (fileList != null) {
				for (File file : fileList ) {
					songs.add(file.getName());
				}
			}
		}
		 
		private void nextSong() {
		updateSongList();		
		if (++currentPosition >= songs.size()) {
			currentPosition = 0;
		} else {
			playSong(pathFile + songs.get(currentPosition));
			SeekBar();
		    startPlayProgressUpdater();
		     
		    buttonPlayPause.setImageResource(R.drawable.pause); 
		    stateMediaPlayer = stateMP_Playing;
			Toast.makeText(this, pathFile + songs.get(currentPosition), Toast.LENGTH_SHORT).show();
		}
		
			}

		private void prevSong() {
		updateSongList();
			if (currentPosition >= 1) {
				playSong(pathFile + songs.get(--currentPosition));
				SeekBar();
			    startPlayProgressUpdater();
			    
		    	buttonPlayPause.setImageResource(R.drawable.pause);
		    	stateMediaPlayer = stateMP_Playing;
		    	Toast.makeText(this, pathFile + songs.get(currentPosition), Toast.LENGTH_SHORT).show();
			} else {
				 playSong(pathFile + songs.get(currentPosition));
				 SeekBar();
			     startPlayProgressUpdater();
			     
			     buttonPlayPause.setImageResource(R.drawable.pause); 
			     stateMediaPlayer = stateMP_Playing;
			     Toast.makeText(this, pathFile + songs.get(currentPosition), Toast.LENGTH_SHORT).show();
			}
		}
		
		private String playSong(String file) {
			try {

				mediaPlayer.reset();
				//seekBar.setProgress(0);
				mediaPlayer.setDataSource(file);
				mediaPlayer.prepare();
				mediaPlayer.start();
			} catch (IOException e) {
				Log.e(getString(R.string.app_name), e.getMessage());
			}
			return file;
		}

	
	  public void SeekBar(){
		  final int HOUR = 60*60*1000;
		 	final int MINUTE = 60*1000;
		    final int SECOND = 1000;
		 	int duration;
		 	duration = mediaPlayer.getDuration();
		 	int durationMint = (duration%HOUR)/MINUTE;
		    int durationSec = (duration%MINUTE)/SECOND;
		    
		    seekBar = (SeekBar) findViewById(R.id.SeekBar);
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
	  
	  public void onClickNext(View v){
		    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 	findViewById(R.id.btnNext).startAnimation(shake);
		 	
		 	nextSong();
	  }
	  
	  public void onClickPrev(View v){
		    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 	findViewById(R.id.btnPrev).startAnimation(shake);
		 	
		 	prevSong();
	  }
	  
	  public void onClickStop(View v){
		  	Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		 	findViewById(R.id.btnStop).startAnimation(shake);
		 	
		 	if(stateMediaPlayer==stateMP_Playing){
		 		mediaPlayer.pause();
		 		buttonPlayPause.setImageResource(R.drawable.play);
		 		stateMediaPlayer = stateMP_Pausing;
		 	}else{
		  	mediaPlayer.reset();
			seekBar.setProgress(0);
	    	buttonPlayPause.setImageResource(R.drawable.play);
	    	mediaPlayer.start();
	    	}
	  }
	  
	  public void onClick(View v) {
		// TODO Auto-generated method stub
		  
		final String PATH_TO_FILE = getMusic;  
		  
	 	Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	 	findViewById(R.id.btnPlay).startAnimation(shake);

	    
			 switch(stateMediaPlayer){
			   /* case stateMP_Error:
			    mediaPlayer.pause();		        
				buttonPlayPause.setImageResource(R.drawable.play);
				stateMediaPlayer = stateMP_Pausing;
			     break;
			    */
			    case stateMP_NotStarter:
			    	 mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			    	  try {
			    	   mediaPlayer.setDataSource(PATH_TO_FILE);
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
			     SeekBar();
			     startPlayProgressUpdater();
			     buttonPlayPause.setImageResource(R.drawable.pause);
			     stateMediaPlayer = stateMP_Playing;
			     Toast.makeText(this, getMusic, Toast.LENGTH_SHORT).show();
			     break;
			     
			    case stateMP_Playing:
			     mediaPlayer.pause();
			     buttonPlayPause.setImageResource(R.drawable.play);
			     stateMediaPlayer = stateMP_Pausing;
			     break;
			    
			    case stateMP_Pausing:
			    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			    	  try {
			    	   mediaPlayer.setDataSource(PATH_TO_FILE);
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
			     SeekBar();
			     startPlayProgressUpdater();
			     
			     buttonPlayPause.setImageResource(R.drawable.pause); 
			     stateMediaPlayer = stateMP_Playing;
			     Toast.makeText(this, getMusic, Toast.LENGTH_SHORT).show();
			     break;
			    }
		}
	
	  public void startPlayProgressUpdater() {
	    	seekBar.setProgress(mediaPlayer.getCurrentPosition());

			if (mediaPlayer.isPlaying()) {
				Runnable notification = new Runnable() {
			        public void run() {
			        	startPlayProgressUpdater();
			        	   long totalDuration = mediaPlayer.getDuration();
						   long currentDuration = mediaPlayer.getCurrentPosition();
						   durasi.setText(""+utils.milliSecondsToTimer(totalDuration));
						   currentDurasi.setText(""+utils.milliSecondsToTimer(currentDuration));
					  
					}
			    };
			    handler.postDelayed(notification,100);
	    	}else{
	    		mediaPlayer.pause();
	    	}
	    } 

	    // This is event handler thumb moving event
	    private void seekChange(View v){
	    	if(mediaPlayer.isPlaying()){
		    	SeekBar sb = (SeekBar)v;
				mediaPlayer.seekTo(sb.getProgress());
			}
	    }

		public void onAccuracyChanged(int sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(int sensor, float[] values) {
			if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
	            long curTime = System.currentTimeMillis();
	            // only allow one update every 100ms.
	            if ((curTime - lastUpdateMusic) > 100) {
	            long diffTime = (curTime - lastUpdateMusic);
	            lastUpdateMusic = curTime;

	            xMusic = values[SensorManager.DATA_X];
	            yMusic = values[SensorManager.DATA_Y];
	            zMusic = values[SensorManager.DATA_Z];

	            if(Round(xMusic,4)>10.0000){
	                Log.d("sensor", "X Right axis: " + xMusic);
	                //Toast.makeText(this, "Right shake detected", Toast.LENGTH_SHORT).show();
	                onClickStop(btnStop);
	            }
	            else if(Round(xMusic,4)<-10.0000){
	                Log.d("sensor", "X Left axis: " + xMusic);
	                //Toast.makeText(this, "Left shake detected", Toast.LENGTH_SHORT).show();
	                onClick(buttonPlayPause);
	            }

	            float speed = Math.abs(xMusic+yMusic+zMusic - last_x_Music - last_y_Music - last_z_Music) / diffTime * 10000;

	            // Log.d("sensor", "diff: " + diffTime + " - speed: " + speed);
	            if (speed > SHAKE_THRESHOLD_MUSIC) {
	                //Log.d("sensor", "shake detected w/ speed: " + speed);
	                //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
	            }
	            last_x_Music = xMusic;
	            last_y_Music = yMusic;
	            last_z_Music = zMusic;
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