package com.scandiumsc.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AboutUs extends Activity 
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aboutus);
    
		int app_code = 0;
		String app_ver = "";
		try 
		{
			app_code = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
			app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    
		TextView companyContactInfo = (TextView) findViewById(R.id.companyContactInfo);
		companyContactInfo.setText("Company Contact Info");
	
		TextView physicalAddress = (TextView) findViewById(R.id.physicalAddress);
		physicalAddress.setText("Physical Address");
	
		TextView addressOne = (TextView) findViewById(R.id.addressOne);
		addressOne.setText("Jl. KH Mas Mansyur No 3 ");
		
		TextView addressTwo = (TextView) findViewById(R.id.addressTwo);
		addressTwo.setText("Jakarta 10240 ");
		
		TextView addressThree = (TextView) findViewById(R.id.addressThree);
		addressThree.setText("Indonesia");
	    
	    TextView twitter = ((TextView) findViewById(R.id.twitter));
	    twitter.setText(" @rifairizqy");
	    twitter.setOnClickListener(onClick);
	    
	    TextView facebook = ((TextView) findViewById(R.id.facebook));
	    facebook.setText(" http://www.facebook.com/rifairizqy");
	    facebook.setOnClickListener(onClick);
	    
		Log.v("RouteBuswayActivity","app_code = " + app_code);
		Log.v("RouteBuswayActivity","app_ver = " + app_ver);
		TextView versionCode = (TextView) findViewById(R.id.versionCode);
		versionCode.setText("Version Code : " + app_code);
		
		TextView versionName = (TextView) findViewById(R.id.versionName);
		versionName.setText("Version Name : " + app_ver);
	    
	    TextView scandium = ((TextView) findViewById(R.id.scandium));
	    scandium.setText("http://www.scandiumsc.com");
	    scandium.setOnClickListener(onClick);
	}
	
	View.OnClickListener onClick = new View.OnClickListener() 
	{
		public void onClick(View v)
		{
	    	if(v.getId() == R.id.scandium)
	    		goToUrl("http://www.scandiumsc.com");
	    	else if(v.getId() == R.id.twitter)
	    		goToUrl("http://twitter.com/rifairizqy");
	    	else if(v.getId() == R.id.facebook)
	    		goToUrl("http://www.facebook.com/rifairizqy");
		}
	};
  
	private void goToUrl (String url) {
		Uri uriUrl = Uri.parse(url);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);
	}
}