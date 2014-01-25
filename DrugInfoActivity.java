package com.example.medlookup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DrugInfoActivity extends Activity implements OnClickListener{
	
	public Button btnSave ;
	public String name ;
	public String usage ;
	public String effects;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drug_info);
		
		TextView drugName = (TextView)findViewById(R.id.drug_name);
		TextView drugUsage = (TextView)findViewById(R.id.drug_use);
		TextView sideEffects = (TextView)findViewById(R.id.side_effects);
		
		btnSave = (Button)findViewById(R.id.save_button);
		btnSave.setOnClickListener(this);
		
		Bundle bndl = getIntent().getExtras();
		name = bndl.getString("name");
		usage = bndl.getString("usage");
		effects = bndl.getString("sideEffects");
		
		drugName.setText(name);
		drugUsage.setText(usage);
		sideEffects.setText(effects);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.save_button:
			final ProgressDialog progDialog = ProgressDialog.show(DrugInfoActivity.this, "Saving data", "Saving Data to SD Card.");
			try{
				new Thread(){
					public void run(){
						saveData();
						progDialog.dismiss();
					}
				}.start();
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
		}
		
	}
	
	public void saveData(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try{
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "data");
			serializer.startTag("","name");
			serializer.text(name);
			serializer.endTag("", "name");
			serializer.startTag("", "usage");
			serializer.text(usage);
			serializer.endTag("", "usage");
			serializer.startTag("", "sidefx");
			serializer.text(effects);
			serializer.endTag("", "sidefx");
			serializer.endTag("","data");
			serializer.endDocument();
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		

		String text = writer.toString();
		
		String sdPath = Environment.getExternalStorageDirectory().toString() +  "/MedData";
		
		File dataDir = new File(sdPath);
		Log.i("MedData",dataDir.getAbsolutePath());
		
		if(!dataDir.mkdirs()){
			Log.i("MedLookup","problem creating directory");
		}else{
			Log.i("MedLookup","created directory");
		}
		
		File saveFile = new File(sdPath ,"/"+name+".xml");
		
		try {
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			
			Log.i("MedLookup","Writing data to : " + saveFile.getAbsolutePath());
			FileWriter fWriter = new FileWriter(saveFile.getAbsolutePath());
			fWriter.write(text);
			fWriter.close();
			Log.i("MedLookup","XML: " + text);
			/*
			BufferedWriter output = new BufferedWriter(new FileWriter(saveFile));
			
			output.write(text);
			output.close();
			*/
			System.out.println("File was written.");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	
		
	}
	

}
