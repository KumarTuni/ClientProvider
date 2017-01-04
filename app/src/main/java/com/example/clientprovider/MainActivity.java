package com.example.clientprovider;

import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
	
	ListView listView;
	LinearLayout linearLayout;
	
	Cursor cursor;
	
	public static final String FILES = "video";
	public static final String PROVIDER_AUTHORITY = "com.example.provider.FileProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_AUTHORITY + "/");
	public static final Uri FILES_URI = Uri.parse("content://"
			+ PROVIDER_AUTHORITY + "/" + FILES  + "/");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView) findViewById(R.id.listView1);
		
		final String[] from = { "_id", "fileName" };
		    
		final int[] to = { R.id.textViewID, R.id.textFileName };
		
		cursor = getContentResolver().query(FILES_URI, null,
				null, null, null);
		SimpleCursorAdapter sAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row, cursor, from, to); 
		listView.setAdapter(sAdapter);
		listView.setOnItemClickListener(new ListListener());
		    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class ListListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			/*Uri uri = ContentUris.withAppendedId(FILES_URI, id);
			cursor = getContentResolver().query(uri, null,
					null, null, null);*/
			
			cursor.moveToPosition(position);
			String name = cursor.getString(cursor.getColumnIndex("fileName"));
			Uri movieUri = Uri.parse(FILES_URI + name);
			Log.i("DEBUG:", "path: " + movieUri);
	       
		    String type = getMimeType(name);

			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			//intent.setDataAndType(movieUri, "video/*");
			intent.setDataAndType(movieUri, type);
			startActivity(intent);
		}
		
	}
	
	public static String getMimeType(String uri)
    {
        String extension = uri.substring(uri.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
        return mimeType;
    }
		
}
