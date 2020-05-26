package media.radio.player;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;

public class Tentang extends Activity{
		
	TextView[] txtJudul, txtGaris, txtKonten;
	LinearLayout lytBantuan;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tentang);
		
		lytBantuan = (LinearLayout) findViewById(R.id.lytBantuan);
		bantuanShake();
	}
	
	public void bantuanShake(){
		try{
    		InputStream in = getResources().openRawResource(R.raw.shake);
    		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		  DocumentBuilder db = dbf.newDocumentBuilder();
    		  Document doc = db.parse(in,null);
    		  doc.getDocumentElement();
    		  
    		  NodeList nodeInformasi = doc.getElementsByTagName("informasi");
    	
    		  
    		  txtJudul = new TextView[nodeInformasi.getLength()];
    		  txtGaris = new TextView[nodeInformasi.getLength()];
    		  txtKonten = new TextView[nodeInformasi.getLength()];
    		  
    		  for (int i = 0; i < nodeInformasi.getLength(); i++) {

    		Node tentangInformasi = nodeInformasi.item(i);

    		
    		if (tentangInformasi.getNodeType() == Node.ELEMENT_NODE) {
    			
    		    
    			Element judulElmnt = (Element) tentangInformasi;
    			NodeList judulElmntLst = judulElmnt.getElementsByTagName("judul");
    		    Element jdlElmnt = (Element) judulElmntLst.item(0);
    		    NodeList judul = jdlElmnt.getChildNodes();
    		      
    		    txtJudul[i] = new TextView(this);
    		    txtJudul[i].setText(((Node) judul.item(0)).getNodeValue());
    		    txtJudul[i].setTextSize(16);
    			txtJudul[i].setPadding(5, 5, 5, 5);
    			txtJudul[i].setTypeface(Typeface.DEFAULT_BOLD);
    			txtJudul[i].setTextColor(Color.RED);
    		    txtJudul[i].setLayoutParams(new LayoutParams(
    	                LayoutParams.FILL_PARENT,
    	                LayoutParams.WRAP_CONTENT));
    		    lytBantuan.addView(txtJudul[i]);
    		    
    		    txtGaris[i] = new TextView(this);
    			txtGaris[i].setLayoutParams(new LayoutParams(
    					LayoutParams.FILL_PARENT,1));
    			
    			txtGaris[i].setBackgroundColor(Color.WHITE);
    		    lytBantuan.addView(txtGaris[i]);
    			
    		    Element penjElmnt = (Element) tentangInformasi;
    			NodeList penjElmntLst = penjElmnt.getElementsByTagName("penjelasan");
    		    Element penElmnt = (Element) penjElmntLst.item(0);
    		    NodeList penjelasan = penElmnt.getChildNodes();
    		      
        		txtKonten[i] = new TextView(this);
    		    txtKonten[i].setText(((Node) penjelasan.item(0)).getNodeValue());
    		    txtKonten[i].setTextColor(Color.WHITE);
    		    txtKonten[i].setPadding(5, 5, 5, 5);
    		    txtKonten[i].setTextSize(14);
    		    txtKonten[i].setLayoutParams(new LayoutParams(
    	                LayoutParams.FILL_PARENT,
    	                LayoutParams.WRAP_CONTENT));
    		    lytBantuan.addView(txtKonten[i]);
    		    
    		}
    		  }	
    		}catch(final Exception e){
    			e.getMessage();
    	}
	}
}