package com.example.medlookup;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FileListActivity extends Activity implements OnItemClickListener{
	
	public ArrayList<String> rListFiles = new ArrayList<String>();
	
	public ListView fileView ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);
		fileView = (ListView)findViewById(R.id.list);
		
		String sdPath = Environment.getExternalStorageDirectory().toString() +  "/MedData/";
		
		File files = new File(sdPath);
		File[] list = files.listFiles();
		
		int itemCount = list.length;
		String[] strList = new String[itemCount];
		
		for(int i =0; i < itemCount ; i++){
			String absPath = list[i].getAbsolutePath();
			rListFiles.add(absPath);
			strList[i] = absPath;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strList);
		fileView.setAdapter(adapter);
		
		fileView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG)
				.show();
				Intent i = new Intent();
				i.setClass(getApplicationContext(), DrugInfoLocalActivity.class);
				i.putExtra("path", rListFiles.get(position));
				startActivity(i);
			}
			
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}


}
