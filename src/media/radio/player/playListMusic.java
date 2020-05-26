package media.radio.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class playListMusic extends Activity {
      ListView musiclist;
      Cursor musiccursor;
      int music_column_index;
      int count;
      MediaPlayer mMediaPlayer;
      MusicAdapter ea;
      
    @Override
  	protected void onResume(){
  		super.onResume();
  	    ea.notifyDataSetChanged();
  	    ea.notifyDataSetInvalidated();
  	    musiclist.invalidateViews();
  	}
      /** Called when the activity is first created. */
      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.playlistmusic);
            init_phone_music_grid();       
            
      }

      private void init_phone_music_grid() {
    	  ea = new MusicAdapter(this);
    	  	
            System.gc();
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
            String[] proj = { MediaStore.Audio.Media._ID,
            		MediaStore.Audio.Media.DATA,
            		MediaStore.Audio.Media.DISPLAY_NAME,
            		MediaStore.Audio.Media.SIZE ,
            		MediaStore.Audio.Media.DURATION,
            		MediaStore.MediaColumns.MIME_TYPE
            		};
            musiccursor = this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, selection, null, null);
            count = musiccursor.getCount();
            musiclist = (ListView) findViewById(R.id.playListMusic);
            musiclist.setAdapter(ea);

            musiclist.setOnItemClickListener(musicgridlistener);
            mMediaPlayer = new MediaPlayer();
      }

      private OnItemClickListener musicgridlistener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
                System.gc();
                  music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                  musiccursor.moveToPosition(position);
                  String filename = musiccursor.getString(music_column_index);
                  
                  Intent intent = new Intent(playListMusic.this, viewMusicList.class);
                  intent.putExtra("musik", filename);
                  startActivity(intent);
            }
      };

      public class MusicAdapter extends BaseAdapter {
          private LayoutInflater mInflater;
          
          public MusicAdapter(Context c) {
                mInflater = LayoutInflater.from(c);
          }

          public int getCount() {
                return count;
          }

          public Object getItem(int position) {
                return position;
          }

          public long getItemId(int position) {
                return position;
          }

          public View getView(int position, View convertView, ViewGroup parent) {
                System.gc();
                String nama = null;
                String size = null;
                String duration = null;
                String ext = null;
    		    
            	ViewHolder holder;
                if (convertView == null) {
                	convertView = mInflater.inflate(R.layout.row_lagu, null);
    				holder = new ViewHolder();
    				holder.txtNama = (TextView) convertView.findViewById(R.id.txtNama);
    				holder.txtSize = (TextView) convertView.findViewById(R.id.txtSize);
    				holder.txtDuration = (TextView) convertView.findViewById(R.id.txtDuration);
    				holder.txtExt = (TextView) convertView.findViewById(R.id.txtExt);
    				
                  convertView.setTag(holder);
                } else{
                	holder = (ViewHolder) convertView.getTag();
                }
                music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                musiccursor.moveToPosition(position);
                //nama = musiccursor.getString(music_column_index);
                nama = musiccursor.getString(music_column_index).substring(0, musiccursor.getString(music_column_index).lastIndexOf("."));
                
                final long SIZE = 1024;
                music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);                
                long sizeFile = musiccursor.getLong(music_column_index);
                
                double FileSize = sizeFile/SIZE;
                musiccursor.moveToPosition(position);
                
                if(FileSize > 1000){
              	  double FileSizeMB = (FileSize/1000);
              	  size = "Size : " + String.format("%.02f", FileSizeMB)+"MB";
                }else{
                size = "Size : " + FileSize+"KB";
                }
                
                final int HOUR = 60*60*1000;
                final int MINUTE = 60*1000;
                final int SECOND = 1000;
    		    
                music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                String time = musiccursor.getString(music_column_index);
                int durationMint = (Integer.parseInt(time)%HOUR)/MINUTE;
    		    int durationSec = (Integer.parseInt(time)%MINUTE)/SECOND;
    		    musiccursor.moveToPosition(position);
                duration = "Duration : " + String.format("%02d:%02d",durationMint,durationSec);
                                
                music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                musiccursor.moveToPosition(position);
                //nama = musiccursor.getString(music_column_index);
                ext = musiccursor.getString(music_column_index).substring(musiccursor.getString(music_column_index).lastIndexOf("."));
                String filenameArray[] = ext.split("\\.");
                String extension = filenameArray[filenameArray.length-1];
                
                
                holder.txtNama.setText(nama);
    			holder.txtSize.setText(size);
    			holder.txtDuration.setText(duration);
    			holder.txtExt.setText(extension);
    			

    			return convertView;
          }
          public class ViewHolder {
  			TextView txtNama;
  			TextView txtSize;
  			TextView txtDuration;
  			TextView txtExt;
  		}
    }
}