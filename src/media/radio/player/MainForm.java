package media.radio.player;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainForm extends TabActivity implements OnTabChangeListener{

	TabHost tabHost;
	Resources res;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablayout);
		
		res = getResources();
		tabHost = getTabHost();
		TabHost.TabSpec specBrowseFile;
		TabHost.TabSpec specplayListMusic;
		TabHost.TabSpec specplayListVideo;
		TabHost.TabSpec specAbout;
		
		tabHost.setOnTabChangedListener(this);
		
		specBrowseFile = tabHost.newTabSpec("tab1").
					setIndicator("Media Player",getResources().
					getDrawable(R.drawable.media_player)).setContent(new Intent().setClass(this, BrowseFiles.class));
		tabHost.addTab(specBrowseFile);

		specplayListMusic = tabHost.newTabSpec("tab2").
				setIndicator("Music Files",getResources().
				getDrawable(R.drawable.music_player)).setContent(new Intent().setClass(this, playListMusic.class));
		tabHost.addTab(specplayListMusic);
		
		specplayListVideo = tabHost.newTabSpec("tab3").
				setIndicator("Video Files",getResources().
				getDrawable(R.drawable.video_player)).setContent(new Intent().setClass(this, playListVideo.class));
		tabHost.addTab(specplayListVideo);
		
		specAbout = tabHost.newTabSpec("tab4").
					setIndicator("About",getResources().
					getDrawable(R.drawable.about)).setContent(new Intent().setClass(this, Tentang.class));
		tabHost.addTab(specAbout);


		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
			tabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tab_default));

        }
		
		tabHost.setCurrentTab(0);
		tabHost.getTabWidget().getChildAt(0).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tab_active));
		
	}
	
	public void onTabChanged(String tabId) {
		
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
        	tabHost.getTabWidget().getChildAt(i).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tab_default));
        } 
				
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tab_active));
	}
	
}
