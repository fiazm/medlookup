package com.example.medlookup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

public class DrugInfoLocalActivity extends Activity{
	
	final static String TAG = "DrugInfo";
	
	public String name ;
	public String usage ;
	public String effects;	
	public String filePath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_file_view);
		
		filePath = getIntent().getExtras().getString("path");
		Toast.makeText(this,filePath , Toast.LENGTH_SHORT).show();
		
		TextView drugName = (TextView)findViewById(R.id.drug_name);
		TextView drugUsage = (TextView)findViewById(R.id.drug_use);
		TextView sideEffects = (TextView)findViewById(R.id.side_effects);		
		
		File file = new File(filePath);
		
		
		StringBuilder text = new StringBuilder();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = br.readLine()) != null){
				text.append(line);
				text.append("\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		String startTag = "";
		String content = "";
		String endTag = "";
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
			
			parser.setInput(new StringReader(text.toString()));
			
			int eventType = parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				String tagName = parser.getName();
				switch(eventType){
				case XmlPullParser.START_TAG:
					startTag = parser.getName();
					break;
				case XmlPullParser.TEXT:
					content = parser.getText();
					break;
					
				case XmlPullParser.END_TAG:
					if(tagName.equalsIgnoreCase("name")){
						name = content;
					}else if(tagName.equalsIgnoreCase("usage")){
						usage = content;
					}else if(tagName.equalsIgnoreCase("sidefx")){
						effects = content;
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}			
		} catch (XmlPullParserException e) {
			
			e.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}

		
		drugName.setText(name);
		drugUsage.setText(usage);
		sideEffects.setText(effects);
				
	}
}
