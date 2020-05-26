package media.radio.player;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


@SuppressWarnings("deprecation")
class ImageFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return (name.endsWith(".png")||name.endsWith(".jpg")||name.endsWith(".jpeg")||name.endsWith(".bmp"));
	}
}

@SuppressWarnings("deprecation")
public class ImagePlayer extends Activity implements SensorListener {
	
	String getImage, getPath;
	ImageButton prevImage, nextImage;
	
	 private List<String> image = new ArrayList<String>();
	 private int currentPosition;	

	 private SensorManager mSensorManager;
	 
	 private long lastUpdateImage = -1;
     private float xImage, yImage, zImage;
     private float last_x_Image=0;
     private float last_y_Image=0;
     private float last_z_Image=0;
     private static final int SHAKE_THRESHOLD_Image = 800;
     
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.imageplayer);
	        
	        prevImage = (ImageButton)findViewById(R.id.btnPrevImage);
	        nextImage = (ImageButton)findViewById(R.id.btnNextImage);
	        
	        
	        Intent i_get = getIntent();
		 	getImage = i_get.getStringExtra("image");
	        getPath = i_get.getStringExtra("path");
		 	
	        final String stringPath = getImage;
	        
	        ImageView image = (ImageView) findViewById(R.id.imagePlayer);
	        image.setVisibility(ImageView.VISIBLE);
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        Bitmap bitmap = BitmapFactory.decodeFile(stringPath, options);
	        image.setImageBitmap(bitmap);
	        
	        image.setOnTouchListener(new TouchImage());  
	        
	        Toast.makeText(this, getImage, Toast.LENGTH_SHORT).show();
	        
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
	  
	 public void updateImageList() {
			File home = new File(getPath);
			File fileList[] = home.listFiles(new ImageFilter());
			if (fileList != null) {
				for (File file : fileList ) {
					image.add(file.getName());
				}
			}
		}
		 
	 
	public void onClickNextImg(View v){
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		findViewById(R.id.btnNextImage).startAnimation(shake);
		nextImage();
		
	} 
	
	public void onClickPrevImg(View v){
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		findViewById(R.id.btnPrevImage).startAnimation(shake);
		prevImage();
		
	}
		private void nextImage() {
		updateImageList();
		if (++currentPosition >= image.size()) {
			currentPosition = 0;
		} else {
			playImage(getPath + image.get(currentPosition));
			Toast.makeText(this, getPath + image.get(currentPosition), Toast.LENGTH_SHORT).show();
		}
		
			}
		
		private String playImage(String file) {
			ImageView image = (ImageView) findViewById(R.id.imagePlayer);
			image.setVisibility(ImageView.VISIBLE);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(file, options);
			image.setImageBitmap(bitmap);
			return file;
		}
		
		private void prevImage() {
			updateImageList();
				if (currentPosition >= 1) {
					playImage(getPath + image.get(--currentPosition));
					Toast.makeText(this, getPath + image.get(currentPosition), Toast.LENGTH_SHORT).show();
				} else {
					playImage(getPath + image.get(currentPosition));
					Toast.makeText(this, getImage, Toast.LENGTH_SHORT).show();
				}
				
			}

		public void onAccuracyChanged(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(int sensor, float[] values) {
			if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
	            long curTime = System.currentTimeMillis();
	            // only allow one update every 100ms.
	            if ((curTime - lastUpdateImage) > 100) {
	            long diffTime = (curTime - lastUpdateImage);
	            lastUpdateImage = curTime;

	            xImage = values[SensorManager.DATA_X];
	            yImage = values[SensorManager.DATA_Y];
	            zImage = values[SensorManager.DATA_Z];

	            if(Round(xImage,4)>10.0000){
	                Log.d("sensor", "X Right axis: " + xImage);
	                //Toast.makeText(this, "Right shake detected", Toast.LENGTH_SHORT).show();
	                    	onClickNextImg(nextImage);
	            }
	            else if(Round(xImage,4)<-10.0000){
	                Log.d("sensor", "X Left axis: " + xImage);
	                //Toast.makeText(this, "Left shake detected", Toast.LENGTH_SHORT).show();
	                    	onClickPrevImg(prevImage);
	            }

	            float speed = Math.abs(xImage+yImage+zImage - last_x_Image - last_y_Image - last_z_Image) / diffTime * 10000;
	            // Log.d("sensor", "diff: " + diffTime + " - speed: " + speed);
	            if (speed > SHAKE_THRESHOLD_Image) {
	                //Log.d("sensor", "shake detected w/ speed: " + speed);
	                //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
	            }  
	            last_x_Image = xImage;
	            last_y_Image = yImage;
	            last_z_Image = zImage;
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
