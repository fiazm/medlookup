package com.example.medlookup;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	final static String TAG = "MedLookup";
	
	private EditText compoundText;
	private Button btnCollectInfo;
	private Button btnLocalData;
	
	public String medCompound = "";
	public String whyPrescribed = "";
	public String commonSideEffects = "";
	public ArrayList<String> sideEffects = new ArrayList<String>();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		compoundText = (EditText)findViewById(R.id.active_compound);
		
		btnCollectInfo = (Button)findViewById(R.id.collect_data_button);
		btnCollectInfo.setOnClickListener(this);
		
		btnLocalData = (Button)findViewById(R.id.local_data_button);
		btnLocalData.setOnClickListener(this);
	}
	
	public String drugPageLink = null;
	public void collectData(String med){
		String url = "http://vsearch.nlm.nih.gov/vivisimo/cgi-bin/query-meta?v%3Aproject=medlineplus&query="+med;
		try {
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla")
					.followRedirects(true)
					.header("Referer","Android Med Lookup App")
					.timeout(0)
					.get();
			
			String title = doc.title();
			System.out.println("title: " + title);
			
			Elements links = doc.select("span.url");
			
			for(Element link : links){
				System.out.println("url: " + link.text());
				if(link.text().contains("medlineplus/druginfo/meds")){
					drugPageLink = "http://"+link.text();
				}
			}
			
			if(drugPageLink != null){
				doc = Jsoup.connect(drugPageLink)
						.userAgent("Mozilla")
						.followRedirects(true)
						.header("Referer","Android Med Lookup App")
						.timeout(0)
						.get();
				
				System.out.println(doc.html());
				
				Elements whys = doc.select("a[name=why] + div.hblock.group + p");
				
				for(Element why : whys){
					Log.i(TAG,why.text());
					whyPrescribed = why.text();
				}
				
				
				Elements sidefx = doc.select("a[name=side-effects] + div.hblock.group + h3 + ul li");
				int c = 0;
				for(Element li : sidefx){
					c++;
					Log.i(TAG,"Children: " + li.text());
					sideEffects.add(li.text());
					commonSideEffects = commonSideEffects + li.text() + "\n\n";
				}
				
			}else{
				Toast.makeText(MainActivity.this, "Sorry, could not find the medicatioitemn.", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.collect_data_button:
			final String med = compoundText.getText().toString();
			final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,"Gathering data", "Please Wait...");
			if(med.length() > 1){
				try{
					new Thread(){
						public void run(){
							
							collectData(med);
							progressDialog.dismiss();
							Intent i = new Intent();
							i.setClass(MainActivity.this,DrugInfoActivity.class);
							i.putExtra("name" ,med);
							i.putExtra("usage",whyPrescribed);
							i.putExtra("sideEffects", commonSideEffects);
							startActivity(i);
						}
						
					}.start();
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				Toast.makeText(MainActivity.this, "Please enter a valid drug name", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.local_data_button:
				Intent i = new Intent();
				i.setClass(MainActivity.this, FileListActivity.class);
				startActivity(i);
			break;
		}
		
		
	}




}
