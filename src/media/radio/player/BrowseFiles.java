package media.radio.player;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import media.radio.player.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.AdapterView.OnItemClickListener;

/** Class Must extends with Dialog */
/** Implement onClickListener to dismiss dialog when OK Button is pressed */
public class BrowseFiles extends Activity {
	String pathTeks="";
	String tempFile="";
	
	private List<String> item = null;
	private List<String> path = null;
	private String root="/sdcard";
	
	ListView listBrowse;
	StringBuilder text;
	BufferedReader br;
	File file;
	
	AlertDialog show;
	
	String pathFile="";
	String pathUtama;
	
	FilesAdapter ea;
	
	File[] files;
    static String[] namaFiles, sizeFiles, extFiles;
	
	@Override
  	protected void onResume(){
  		super.onResume();
  	    ea.notifyDataSetChanged();
  	    ea.notifyDataSetInvalidated();
  	    listBrowse.invalidateViews();
  	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.browse);
		listBrowse = (ListView) findViewById(R.id.listBrowse);
		ea = new FilesAdapter(this);
		
		getDir(root);
		
		listBrowse.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
		    	file = new File(path.get(position));
		    	  if (file.isDirectory())
		    	  {
		    	   if(file.canRead()){
		    	    getDir(path.get(position));
		    	   }
		    	   else
		    	   {
		    	    noRead();
		    	   }
		    	  }
		    	  else
		    	  {
		    		  try {
		    			  tempFile = file.getName();
		    			  pathFile=pathTeks+"/"+tempFile;
		    			  pathUtama = pathTeks +"/";
		    			  
		    			  if(tempFile.endsWith(".mp3")||tempFile.endsWith(".mid")||tempFile.endsWith(".wav")||tempFile.endsWith(".ogg")||tempFile.endsWith(".flac")||tempFile.endsWith(".ota")||tempFile.endsWith(".imy")
		    					  ||tempFile.endsWith(".xmf")||tempFile.endsWith(".rtx")){
		    				  Intent intent = new Intent(BrowseFiles.this,MusicPlayer.class);
			                  intent.putExtra("musik", pathFile);
			                  intent.putExtra("path", pathUtama);
			                  startActivity(intent);
		    			  }else if(tempFile.endsWith(".3gp")||tempFile.endsWith(".mp4")||tempFile.endsWith(".webm")||tempFile.endsWith(".mkv")){
		    				  Intent intent = new Intent(BrowseFiles.this,VideoPlayer.class);
			                  intent.putExtra("video", pathFile);
			                  intent.putExtra("path", pathUtama);
			                  startActivity(intent);
		    			  }else if(tempFile.endsWith(".png")||tempFile.endsWith(".jpg")||tempFile.endsWith(".jpeg")||tempFile.endsWith(".bmp")){
		    				  Intent intent = new Intent(BrowseFiles.this,ImagePlayer.class);
			                  intent.putExtra("image", pathFile);
			                  intent.putExtra("path", pathUtama);
			                  startActivity(intent);
		    			  }else if(tempFile.endsWith(".gif")){
		    				  Intent intent = new Intent(BrowseFiles.this,GifPlayer.class);
			                  intent.putExtra("gif", pathFile);
			                  intent.putExtra("path", pathUtama);
			                  startActivity(intent);
		    			  }else{
		    				  noMedia();
		    			  	}
		    			  	    			  
			    		  }
			                catch (Exception e) {
			              }
		    	  		}
		    	  	}         
			  });
	}
	
	public class FilesAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        
        public FilesAdapter(Context c) {
              mInflater = LayoutInflater.from(c);
        }
        public int getCount() {
              return files.length;
        }
        public Object getItem(int position) {
              return position;
        }
        public long getItemId(int position) {
              return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            System.gc();
          	ViewHolder holder;
              if (convertView == null) {
              	convertView = mInflater.inflate(R.layout.row_files, null);
  				holder = new ViewHolder();
  				holder.txtNama = (TextView) convertView.findViewById(R.id.txtNamaFiles);
  				holder.txtTipe = (TextView) convertView.findViewById(R.id.txtTipeFiles);
  				holder.txtExt = (TextView) convertView.findViewById(R.id.txtExtFiles);
                convertView.setTag(holder);
              } else{
              	holder = (ViewHolder) convertView.getTag();
              }
              
            namaFiles = new String[item.size()];
            sizeFiles = new String[item.size()];
            extFiles = new String[item.size()];
      		
      		for(int i=0;i<item.size();i++){
      			String row = item.get(i);
      			namaFiles[i] = row.toString();
      			sizeFiles[i] = row.toString();
      			extFiles[i] = row.toString();
      		}
      		 		
      		String extFile = null;     
      		int idx = namaFiles[position].lastIndexOf("/");
      		String direktori = namaFiles[position].substring(idx+1, namaFiles[position].length());
      		
      		String tipeFile ="";
      		

      		if(!direktori.equals("")){
      			if(tipeFile.equals("Directory")){
      				int index = namaFiles[position].lastIndexOf(".");
              		String nama = namaFiles[position].substring(0, index);
              		namaFiles[position] = nama;
      			}          		
      		}
      		
      		if(direktori.equals("")||namaFiles[position].equals("/sdcard")||namaFiles[position].equals("../")){
      			extFile="<dir>";
      			tipeFile = "Directory";
      			
      		}else{
      			String filenameArray[] = extFiles[position].split("\\.");
          		extFile = filenameArray[filenameArray.length-1];
          		
          		if(extFile.equalsIgnoreCase("mp3")||extFile.equalsIgnoreCase("mid")||extFile.equalsIgnoreCase("wav")||extFile.equalsIgnoreCase("ogg")||extFile.equalsIgnoreCase("flac")||extFile.equalsIgnoreCase("ota")||extFile.equalsIgnoreCase("imy")){
          			tipeFile="Music File";
          		}else if(extFile.equalsIgnoreCase("3gp")||extFile.equalsIgnoreCase("mp4")||extFile.equalsIgnoreCase("webm")||extFile.equalsIgnoreCase("mkv")){
          			tipeFile="Video File";
          		}else if(extFile.equalsIgnoreCase("png")||extFile.equalsIgnoreCase("jpg")||extFile.equalsIgnoreCase("jpeg")||extFile.equalsIgnoreCase("bmp")){
          			tipeFile="Image File";
          		}else if(extFile.equalsIgnoreCase("gif")){
          			tipeFile="Animation File";
          		}else{
          			tipeFile="Not Media File";
          		}
      		}
      		

      		
              holder.txtNama.setText(namaFiles[position]);
  			  holder.txtTipe.setText(tipeFile);
  			  holder.txtExt.setText(extFile);
  			  
  			return convertView;
        }
        public class ViewHolder {
			TextView txtNama;
			TextView txtTipe;
			TextView txtExt;
		}
  }
	
	
	public void getDir(String dirPath)
    {
	 pathTeks = dirPath;
     item = new ArrayList<String>();
     path = new ArrayList<String>();
     
     File f = new File(dirPath);
     files = f.listFiles();
     
     if(!dirPath.equals(root))
     {
      item.add(root);
      path.add(root);
      
      item.add("../");
      path.add(f.getParent());           
     }
     
     for(int i=0; i < files.length; i++)
     {
       File file = files[i];
       path.add(file.getPath());
       if(file.isDirectory())
       {
        item.add(file.getName() + "/");
       }
       else
       {
        item.add(file.getName());
       }
     }
     
     /*ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
     listBrowse.setAdapter(fileList);*/
     
     listBrowse.setAdapter(ea);
     
    }
	public void noRead(){
		show = new AlertDialog.Builder(this)
	    .setIcon(R.drawable.shake)
	    .setTitle("[" + file.getName() + "] this Direktori can't be read!")
	    .setPositiveButton("OK", 
	      new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	       }
	      }).show();
	}
	
	public void noMedia(){
		show = new AlertDialog.Builder(this)
	    .setIcon(R.drawable.shake)
	    .setTitle("[" + file.getName() + "] this is not media player file!")
	    .setPositiveButton("OK", 
	      new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	       }
	      }).show();
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
