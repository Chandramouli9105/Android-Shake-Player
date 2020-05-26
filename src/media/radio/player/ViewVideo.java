package media.radio.player;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewVideo extends Activity {
      private String filename;
      VideoView vv;
      MediaController mc;

      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);  
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN); 
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
			setContentView(R.layout.viewvideo);
			
            System.gc();
            Intent i = getIntent();
            filename = i.getStringExtra("video");
            
            vv = (VideoView) findViewById(R.id.VideoView);
            mc = new MediaController(this);
            vv.setMediaController(mc);
            mc.setAnchorView(vv);
            vv.setMediaController(mc);
            vv.setVideoPath(filename);
            vv.requestFocus();
            vv.start();       
      }
} 