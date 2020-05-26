package media.radio.player;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class playListVideo extends Activity {
      private Cursor videocursor;
      private int video_column_index;
      ListView videolist;
      int count;
      VideoAdapter ea;
      

      @Override
    	protected void onResume(){
    		super.onResume();
    	    ea.notifyDataSetChanged();
    	    ea.notifyDataSetInvalidated();
    	    videolist.invalidateViews();
    	}
      
      /** Called when the activity is first created. */
      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.playlistvideo);
            init_phone_video_grid();
      }

      private void init_phone_video_grid() {
    	  ea = new VideoAdapter(this);
            System.gc();
            String[] proj = { MediaStore.Video.Media._ID,MediaStore.Video.Media.DATA,MediaStore.Video.Media.DISPLAY_NAME,
            		MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Video.VideoColumns._ID, MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.MIME_TYPE};
            videocursor = this.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);
            count = videocursor.getCount();
            videolist = (ListView) findViewById(R.id.playListVideo);
            videolist.setAdapter(ea);
            videolist.setOnItemClickListener(videogridlistener);
      }

      private OnItemClickListener videogridlistener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
                 System.gc();
                  video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                  videocursor.moveToPosition(position);
                  String filename = videocursor.getString(video_column_index);
                  
                  Intent intent = new Intent(playListVideo.this, viewVideoList.class);
                  intent.putExtra("video", filename);
                  startActivity(intent);
            }
      };

      public class VideoAdapter extends BaseAdapter {
    	  private LayoutInflater mInflater;

            public VideoAdapter(Context c) {
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
                 String namaVideo = null;
                 String sizeVideo = null;
                 String durationVideo = null;
                 String extVideo = null;
                 
                 ViewHolder holder;
                  if (convertView == null) {
                	convertView = mInflater.inflate(R.layout.row_video, null);
      				holder = new ViewHolder();
      				holder.txtNamaVideo = (TextView) convertView.findViewById(R.id.txtNamaVideo);
      				holder.txtSizeVideo = (TextView) convertView.findViewById(R.id.txtSizeVideo);
      				holder.txtDurationVideo = (TextView) convertView.findViewById(R.id.txtDurationVideo);
      				holder.ImageVideo = (ImageView) convertView.findViewById(R.id.imgVideo);
      				holder.txtExtVideo = (TextView) convertView.findViewById(R.id.txtExtVideo);
                    convertView.setTag(holder);
                	
                  } else{
                	  holder = (ViewHolder) convertView.getTag();
                  }
                  video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                  videocursor.moveToPosition(position);
                  //namaVideo = videocursor.getString(video_column_index);
                  namaVideo = videocursor.getString(video_column_index).substring(0, videocursor.getString(video_column_index).lastIndexOf("."));
                  
                  final long SIZE = 1024;
                  video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);                
                  long sizeFile = videocursor.getLong(video_column_index);
                  
                  double FileSize = sizeFile/SIZE;
                  videocursor.moveToPosition(position);
                  
                  if(FileSize > 1000){
                	  double FileSizeMB = (FileSize/1000);
                	  sizeVideo = "Size : " + String.format("%.02f", FileSizeMB) +"MB";
                  }else{
                  sizeVideo = "Size : " + String.format("%.02f", FileSize)+"KB";
                  }
                  
                  final int HOUR = 60*60*1000;
                  final int MINUTE = 60*1000;
                  final int SECOND = 1000;
      		    
                  video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                  String time = videocursor.getString(video_column_index);
                  int durationMint = (Integer.parseInt(time)%HOUR)/MINUTE;
      		      int durationSec = (Integer.parseInt(time)%MINUTE)/SECOND;
      		      videocursor.moveToPosition(position);
                  durationVideo = "Duration : " + String.format("%02d:%02d",durationMint,durationSec);
                  
                  
                  video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID);
                  ContentResolver crThumb = getContentResolver();
                  Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(crThumb, videocursor.getLong(video_column_index),
                              MediaStore.Video.Thumbnails.MICRO_KIND,
                              (BitmapFactory.Options) null );          

                  video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                  videocursor.moveToPosition(position);
                  //nama = musiccursor.getString(music_column_index);
                  extVideo = videocursor.getString(video_column_index).substring(videocursor.getString(video_column_index).lastIndexOf("."));
                  String filenameArray[] = extVideo.split("\\.");
                  String extension = filenameArray[filenameArray.length-1];
                  
                  holder.txtNamaVideo.setText(namaVideo);
      			  holder.txtSizeVideo.setText(sizeVideo);
      			  holder.txtDurationVideo.setText(durationVideo);
      			  holder.ImageVideo.setImageBitmap(bitmap);
      			  holder.txtExtVideo.setText(extension);
      			  return convertView;
            }
            
            public class ViewHolder {
      			TextView txtNamaVideo;
      			TextView txtSizeVideo;
      			TextView txtDurationVideo;
      			ImageView ImageVideo;
      			TextView txtExtVideo;
      			
      		}
      }
}