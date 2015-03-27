package com.scandiumsc.android;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

import org.w3c.dom.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class BuswayMaps extends Activity
{
	private static final String jakarta= "Jakarta";
	
	private Cursor cursorAllHalte=null;
	private Cursor cursorAllRoute=null;
	private BuswayHelper helper=null;
	private RouteBean routeWalking = null;
  	private RouteBean routeOri = null; 
  	private RouteBean routeDest = null;
  	private LocationManager locMgr;
  	private Location location;
  
  	private List<BuswayBean> allHalte;
  	private List<BuswayBean> allRoute;
  	private List<String> edgeKey;
  	private Map<String, Edge> edge;
  	private TreeMap<Integer, BuswayBean> treeVertex;
  	private Stack<Integer> path;
  
  	private double latMyLocation;
  	private double lonMyLocation;
  	private double latMyDestination;
  	private double lonMyDestination;
  
  	private BigDecimal bigDis;
  
  	private int oriHalteId;
  	private int destHalteId;
  	private int treeVertexSizeWithoutDestination;
  
  	private boolean indonesiaLanguage;
  	private boolean getDirectionList;
  	private boolean ICSversion;
  
  	private String oriLatLongHalte;
  	private LatLng oriLatLongHalteValue;
  	private String destLatLongHalte;
  	private LatLng destLatLongHalteValue;
  	private String oldDestination;
  	private String message;
  
  	// language
  	private String destination;
  	private String pleaseInsert;
  	private String btnCancel;
  	private String btnSearch;
  	private String distanceTo;
  	private String byWalking;
  	private String is;
  	private String from;
  	private String switchTo;
  	private String thenWalkToHalte;
  	private String walkTo; 
  	private String at;
  	private String getOff;
  	private String stop;
  	private String showMap;
  	private String takeDirectionTo;
  
  	private String reset;
  	private String connectionError;
  	private String recordNotFound;
  	private String destOutOfRange;
  
  	private String msgConnectionNotAvailable;
  	private String msgOutOfRange;
  	private String msgShowYourDestination;
  	private String msgGPS;
  	private String msgPleaseRefreshYourPosition;
  
  	private String btnYes;
  	private String btnNo;
  
  	private GoogleMap mMap;
  	private List<Marker> markers;
  	private LatLng latlngMyLocation;
  	private GoogleMapV2Direction md;

	private Criteria criteria;

	private Object provider;
  	
	@Override
	public void onCreate(Bundle savedInstanceState) 
  	{
  		super.onCreate(savedInstanceState);
	    setContentView(R.layout.busway_map);
	    
	    md = new GoogleMapV2Direction();
	    mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    mMap.setMyLocationEnabled(true);
	    
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 

	    // 	searchDatabase
	    cursorAllRoute = getHelper().getAllRoute();
	    cursorAllRoute.moveToFirst();
	    
	    cursorAllHalte = getHelper().getAllHalte();
	    cursorAllHalte.moveToFirst();
	    // end searchDatabase
    
	    allHalte = new ArrayList<BuswayBean>();
	    allRoute = new ArrayList<BuswayBean>();
	    treeVertex = new TreeMap<Integer, BuswayBean>();
	    edge = new TreeMap<String, Edge>();
	    edgeKey = new Vector<String>();
	    path = new Stack<Integer>();
	    bigDis = new BigDecimal(0);
	    indonesiaLanguage = false;
	    getDirectionList = false;
	    ICSversion = false;
	    
	    markers = new ArrayList<Marker>();
	    LatLng latlngHalteBusway;
	    
	    if(Locale.getDefault().getLanguage().equals("in"))
	    	indonesiaLanguage = true;
	    
	    if(indonesiaLanguage)
	    	convertToIndoLanguage();
	    else
	    	convertToEnglishLanguage();
	    
	    Log.v("BuswayMaps","My locale = " + Locale.getDefault().getLanguage() + " | indonesiaLanguage = " + indonesiaLanguage);	
	    
	    if(cursorAllHalte != null && cursorAllHalte.getCount() > 0) 
	    {
	    	for (int i = 0; i < cursorAllHalte.getCount(); i++) 
	    	{
	    		BuswayBean item = new BuswayBean();
	
		      	item.setId(cursorAllHalte.getInt(cursorAllHalte.getColumnIndex("_id")));
		      	item.setName(cursorAllHalte.getString(cursorAllHalte.getColumnIndex("name")));
		        item.setLatitude(cursorAllHalte.getDouble(cursorAllHalte.getColumnIndex("lat")));
		        item.setLongitude(cursorAllHalte.getDouble(cursorAllHalte.getColumnIndex("lon")));
		        item.setLine(cursorAllHalte.getDouble(cursorAllHalte.getColumnIndex("line")));
		        item.setPole(cursorAllHalte.getDouble(cursorAllHalte.getColumnIndex("pole")));
	
		        allHalte.add(item);
		        cursorAllHalte.moveToNext();
	    	}
	
	    	// adding marker from database allHalte to map
	    	for (int i = 0; i < allHalte.size(); i++)
	    	{
	    		if(!treeVertex.containsKey(allHalte.get(i).getId()))
	    		{ 
	    			latlngHalteBusway = new LatLng(allHalte.get(i).getLatitude(), allHalte.get(i).getLongitude());
	    			Marker marker = mMap.addMarker(new MarkerOptions()
	    					.position(latlngHalteBusway)
	    					.title(allHalte.get(i).getName())
	    					.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
	    					.anchor(0.5f, 1));
	    			markers.add(marker);
	    			treeVertex.put(allHalte.get(i).getId(), allHalte.get(i));
	    		}
	    	}
	    	// end adding marker to map
	    }
    
	    if(cursorAllRoute != null && cursorAllRoute.getCount() > 0) 
	    {
	    	for (int i = 0; i < cursorAllRoute.getCount(); i++) 
	    	{
	    		BuswayBean item = new BuswayBean();
	
	    		item.setId(cursorAllRoute.getInt(cursorAllRoute.getColumnIndex("_id")));
	    		item.setNameId(cursorAllRoute.getInt(cursorAllRoute.getColumnIndex("home_id")));
	    		item.setNeighbourId(cursorAllRoute.getInt(cursorAllRoute.getColumnIndex("neighbour_id")));
			      
	    		allRoute.add(item);
	    		cursorAllRoute.moveToNext();
		    }
	    	
		    for (int i = 0; i < allRoute.size(); i++)
		    	addEdge(new BuswayBean(allRoute.get(i).getNameId()), new BuswayBean(allRoute.get(i).getNeighbourId()), false);
	    }
	    
	    refreshMyPosition();
	    
	    // put id 0 --> menandakan halte terakhir
	    BuswayBean item = new BuswayBean();
	  	item.setId(0);
	  	item.setName("null");
	    item.setLatitude(0);
	    item.setLongitude(0);
	    item.setLine(0);
	    item.setPole(0);
	    
	    treeVertex.put(item.getId(), item);
	    treeVertexSizeWithoutDestination = treeVertex.size();
	    
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	    	ICSversion = true;
	    
	    Log.v("BuswayMaps","allHalte size = " + allHalte.size());
	    Log.v("BuswayMaps","treeVertexSizeWithoutDestination = " + treeVertexSizeWithoutDestination);
	    Log.v("BuswayMaps","edge size = " + edge.size());
	    Log.v("BuswayMaps","version 4.0 ++ = " + ICSversion);
  	}
  
  	private BuswayHelper getHelper() 
  	{
  		if(helper == null) 
  			helper = new BuswayHelper(BuswayMaps.this);
  		return(helper);
  	}
  
  	@Override
  	public boolean onCreateOptionsMenu(Menu menu) 
  	{
  		if(ICSversion)
  			new MenuInflater(this).inflate(R.menu.detail_option_ics, menu);
  		else
  			new MenuInflater(this).inflate(R.menu.detail_option, menu);
  	
  		return(super.onCreateOptionsMenu(menu));
  	}
  
  	@Override
  	public boolean onOptionsItemSelected(MenuItem item)
  	{
//  		if(item.getItemId() == R.id.refresh) // refresh location
//  		{ 
//  			Log.v("BuswayMaps","My locale = " + Locale.getDefault().getLanguage() + " | indonesiaLanguage = " + indonesiaLanguage);	
//  			deleteOldDirectionAndDestination();
//  			refreshMyPosition();
//  			Toast.makeText(this, "refresh !", Toast.LENGTH_SHORT).show();
//  			return(true);
//  		}
//  		else 
  		if(item.getItemId() == R.id.search) // search location
  		{ 
  			showPopUp();
  			return(true);
  		}
  		else if(item.getItemId() == R.id.directionList) // draw location
  		{ 
  			if(message != null)
  				showPopUpMessage(message);
  			else
  				Toast.makeText(this, msgShowYourDestination, Toast.LENGTH_SHORT).show();

  			return(true);
  		}
  		else if(item.getItemId() == R.id.reset) // reset
  		{
  			deleteOldDirectionAndDestination();
  			refreshMyPosition();
  			Toast.makeText(this, reset, Toast.LENGTH_SHORT).show();
  			return(true);
  		}
  		else if(item.getItemId() == R.id.aboutus) // search location
  		{ 
  			startActivity(new Intent(BuswayMaps.this, AboutUs.class)); // pindah page about us
  			return(true);
  		}

  		return(super.onOptionsItemSelected(item));
  	}
  
  	private void refreshMyPosition() 
  	{
  		Log.e("BuswayMaps","----------------------------------");
  		Log.v("BuswayMaps",latMyLocation+" "+lonMyLocation + " --> My Position before refresh");
	  
  		if(!haveNetworkConnection()) {
  			buildAlertMessage(msgConnectionNotAvailable, false);
  			return;
  		}
  	
//  		for(int i = 0; i < myMapView.getOverlays().size(); i++)
////  		for(int i = 0; i < markers.size(); i++)
//  		{
//  			Overlay tmpOverlay = myMapView.getOverlays().get(i);
//  			BuswayOverlay buswayOverLay = (BuswayOverlay) tmpOverlay;
//    	
//  			if(buswayOverLay.item.getSnippet().equalsIgnoreCase(myLocation)) 
//  			{
//  				myMapView.getOverlays().remove(i);
//  				myMapView.invalidate();
//  			}
//  		}
  	
  		locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

  		if(locMgr == null) 
  		{
  			Log.e("BuswayMaps","Error: Location Manager is null");
  			Toast.makeText(BuswayMaps.this, connectionError, Toast.LENGTH_SHORT).show();
  			return;
  		}

  		if(!locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER )) 
  			buildAlertMessage(msgGPS, true);
  		else
  		{
  			location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
  			if(location == null)
  				location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
  	        
  			if(location == null) {
  				Log.e("BuswayMaps","Refresh Position");
  	  			Toast.makeText(BuswayMaps.this, msgPleaseRefreshYourPosition, Toast.LENGTH_SHORT).show();
  	  			return;
  			}
  			else if(location.getLatitude() != 0 && location.getLongitude() != 0) 
  			{
//  				LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
//  	            LatLng topright = bounds.northeast;
//  	            double toprgt_latitude = topright.latitude;
//  	            double toprgt_longitude = topright.longitude;	
//
//  	            Log.d("top lat", "" + toprgt_latitude);//getting zero values.
//  	            Log.d("top lon", "" + toprgt_longitude);//getting zero values.
//  	            
//  	            VisibleRegion vr = mMap.getProjection().getVisibleRegion();
//  	            double left = vr.latLngBounds.southwest.longitude;
//  	            double top = vr.latLngBounds.northeast.latitude;
//  	            double right = vr.latLngBounds.northeast.longitude;
//  	            double bottom = vr.latLngBounds.southwest.latitude;
//  	            
//  	            Log.d("left", "" + left);//getting zero values.
//  	            Log.d("top	", "" + top);//getting zero values.
//  	          	Log.d("right", "" + right);//getting zero values.
//  	        	Log.d("bottom", "" + bottom);//getting zero values.
  	        
  				latMyLocation = location.getLatitude();
  				lonMyLocation = location.getLongitude();
  				Log.v("BuswayMaps",latMyLocation+" "+lonMyLocation + " --> My Position after refresh from gps");
  				Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
  				
  				try
  				{
  					Log.v("BuswayMaps","try address");
  					List<Address> addresses = gcd.getFromLocation(latMyLocation, lonMyLocation, 1);;
  					Log.v("BuswayMaps","addresses size = " + addresses.size());
  					if(addresses != null && addresses.size() > 0) 
  					{
  						Address address = addresses.get(0);
  						Log.v("BuswayMaps",address.getAddressLine(0) + ", " + address.getAdminArea());
  						int jkt = address.getAdminArea().indexOf(jakarta);

  						if(jkt >= 0)
  						{
  							//  adding my location
//  							geoMyPosition = new GeoPoint((int)(latMyPosition * 1E6), (int)(lonMyPosition * 1E6));
  							
//  							myMapView.getController().setZoom(17);
//  							myMapView.getController().animateTo(geoMyPosition);
//  							myMapView.getController().setCenter(geoMyPosition);
//  							myMapView.setBuiltInZoomControls(true);
//	            
//  							myMapView.getOverlays().add(new BuswayOverlay(markerMyPosition, geoMyPosition, myLocation));
//  							myMapView.invalidate();
  							
  							latlngMyLocation = new LatLng(latMyLocation, lonMyLocation);
  							mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngMyLocation, 17));
//  							mMap.addMarker(new MarkerOptions().position(latlngMyLocation).title("My Location"));

						  // end adding my location
  						}
  						else
  						{
  							buildAlertMessage(msgOutOfRange, false);
  							return;
  						}
  					}
  					else
  					{
  						buildAlertMessage(msgOutOfRange, false);
  						return;
  					}
  				}
  				catch (IOException e)
  				{
  					Toast.makeText(BuswayMaps.this, msgPleaseRefreshYourPosition, Toast.LENGTH_SHORT).show();
  					Log.e("BuswayMaps", "Impossible to connect to Geocoder", e);
  					e.printStackTrace();
  				}
  			} 
  		}
  		Log.e("BuswayMaps","----------------------------------");
  	}
  
  	private void showPopUp() 
  	{  
  		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
  		final EditText txtDestination = new EditText(this);  
  		if (txtDestination.length() > 0) {
  			txtDestination.setText(null);
  		}
  		txtDestination.setHint(pleaseInsert);  
  		txtDestination.setSingleLine();
    
  		helpBuilder.setTitle(destination); 
  		helpBuilder.setView(txtDestination);        
  		helpBuilder.setIcon(R.drawable.ic_menu_search);
  		helpBuilder.setPositiveButton(btnSearch, new DialogInterface.OnClickListener()
  		{  
  			public void onClick(DialogInterface dialog, int which) 
  			{  
  				if(txtDestination.getText() != null && txtDestination.getText().length() > 0)
  					changeMap(txtDestination.getText().toString() + ", Jakarta"); // append Jakarta 
  				else
  					Toast.makeText(BuswayMaps.this, pleaseInsert, Toast.LENGTH_SHORT).show();
  			}  
  		});
    
  		helpBuilder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() 
  		{  
  			@Override  
  			public void onClick(DialogInterface dialog, int which) {  
  				dialog.cancel();
  			}  
  		});  
    
  		AlertDialog helpDialog = helpBuilder.create();
  		helpDialog.show();  
  	}
  
  	private void showPopUpMessage(String message) 
  	{  
  		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
  		DistanceAlgorithm holder=new DistanceAlgorithm();
  		double distance = holder.DistanceBetweenPlaces(latMyLocation, lonMyLocation, latMyDestination, lonMyDestination);
		
  		bigDis = new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);
  		helpBuilder.setMessage(message);      
  		helpBuilder.setPositiveButton(showMap, new DialogInterface.OnClickListener()
  		{  
  			public void onClick(DialogInterface dialog, int which) 
  			{  
  				if(!getDirectionList)
  				{
  					if(bigDis.compareTo(new BigDecimal(1.5)) <= 0 || oriHalteId == destHalteId)
  					{
//  						RouteOverlay routeOverlayWalking = new RouteOverlay(routeWalking, Color.RED);
//  						myMapView.getOverlays().add(routeOverlayWalking);
  					}
  					else
				  	{	 
//  						getDirectionBusway(path); // marker direction busway route (b --> c)
//  						RouteOverlay routeOverlayOri = new RouteOverlay(routeOri, Color.RED);
//  						myMapView.getOverlays().add(routeOverlayOri);
//  						RouteOverlay routeOverlayDest = new RouteOverlay(routeDest, Color.RED);
//  						myMapView.getOverlays().add(routeOverlayDest);
				  	}
    		 
//  				myMapView.getController().animateTo(geoMyPosition);
//  				myMapView.getController().setZoom(17);
//				  	myMapView.setBuiltInZoomControls(true);
//				  	myMapView.displayZoomControls(true);
//				  	myMapView.invalidate();
  					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngMyLocation, 17));
				  	getDirectionList = true;
  				}
  			}  
  		});
    
  		helpBuilder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() 
  		{  
  			@Override  
  			public void onClick(DialogInterface dialog, int which) {  
  				dialog.cancel();
  			}  
  		});  
    
  		AlertDialog helpDialog = helpBuilder.create();  
  		helpDialog.show();  
  	}
  
  	public void changeMap(String destination)
  	{
//  		GeoPoint DestinationLocation = null;
  		LatLng DestinationLocation = null;
  		DistanceAlgorithm holder=new DistanceAlgorithm();
  	
  		try
  		{
  			Log.v("BuswayMaps","My Old destination = " + oldDestination + " | My New destination = " + destination);
  			path = new Stack<Integer>();
  			Geocoder g = new Geocoder(this, Locale.getDefault());
  			java.util.List<android.location.Address> result=g.getFromLocationName(destination, 1);
 			
  			if(result.size() > 0)
  			{
  				deleteOldDirectionAndDestination();

  				oldDestination = destination;
  				latMyDestination = result.get(0).getLatitude();
  				lonMyDestination = result.get(0).getLongitude();
 				
  				Log.v("BuswayMaps","country = " + String.valueOf(result.get(0).getCountryName()));
  				Log.v("BuswayMaps",latMyDestination+" , "+lonMyDestination  + "--> my destination latlong");
//  				DestinationLocation = new GeoPoint((int) (latMyDestination * 1E6), (int) (lonMyDestination * 1E6));
  				DestinationLocation = new LatLng(latMyDestination, lonMyDestination);
  				double distance = holder.DistanceBetweenPlaces(latMyLocation, lonMyLocation, latMyDestination, lonMyDestination);
  				bigDis = new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);

  				if(bigDis.compareTo(new BigDecimal(30)) < 0)
  				{
	    			mMap.addMarker(new MarkerOptions().position(DestinationLocation).title(destination)
	    					.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
	    					.anchor(0.5f, 1));
  					getDistance(destination, bigDis);
  				}
  				else
  				{
  					Toast.makeText(BuswayMaps.this, destOutOfRange, Toast.LENGTH_SHORT).show();
  					Log.e("BuswayMaps",destOutOfRange);
  				}
  			}            
  			else
  			{
  				Toast.makeText(BuswayMaps.this, recordNotFound, Toast.LENGTH_SHORT).show();
  				Log.e("BuswayMaps",recordNotFound);
  				return;
  			}
  		}
  		catch(IOException io)
 		{
 			Toast.makeText(BuswayMaps.this, connectionError, Toast.LENGTH_SHORT).show();
 			Log.e("BuswayMaps",connectionError);
 		}
 	}
  
  	private void getDistance(String destination, final BigDecimal bigDis) 
  	{
  		final String myPosition = latMyLocation+","+lonMyLocation;
  		final LatLng fromPosition = new LatLng(latMyLocation, lonMyLocation);
  		final String myDestination = latMyDestination+","+lonMyDestination;
  		final LatLng toPosition = new LatLng(latMyDestination, lonMyDestination);

  		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		
  		TreeMap<Integer, BuswayBean> duration = new TreeMap<Integer, BuswayBean>();
  		List<Integer> halteList = new Vector<Integer>();
  		Stack<Integer> pathTmp = new Stack<Integer>();
  		String oriHalteName = null;
  		String destHalteName = null;
  		String messageTmp = null;
  		int halteNext = 0;
  		boolean line = false;
		
  		oriHalteId = getBuswayStopID(latMyLocation, lonMyLocation);
  		oriLatLongHalte = treeVertex.get(oriHalteId).getLatitude()+","+treeVertex.get(oriHalteId).getLongitude();
  		oriLatLongHalteValue = new LatLng(treeVertex.get(oriHalteId).getLatitude(), treeVertex.get(oriHalteId).getLongitude());
  		
  		oriHalteName = treeVertex.get(oriHalteId).getName();
		
  		BuswayBean newest = new BuswayBean();
  		newest.setId(treeVertexSizeWithoutDestination + 1);
  		newest.setName(destination);
  		newest.setLatitude(latMyDestination);
  		newest.setLongitude(lonMyDestination);
  		newest.setLine(1000);
  		newest.setPole(1000);
  		treeVertex.put(newest.getId(), newest); // adding destination as vertex
  	
  		halteList = getListBuswayStopID(latMyDestination, lonMyDestination); // 2 halte terdekat
  	
  		for(int i = 0; i < halteList.size(); i++)
  		{
  			BuswayBean item = new BuswayBean();
  			int tmp = halteList.get(i);
  			halteNext = halteNext + 1;
  		
  			item.setId(tmp);
  			item.setNameId(allRoute.get(tmp).getNameId());
  			item.setNeighbourId(newest.getId());
  			String srcPlace = treeVertex.get(tmp).getLatitude()+","+treeVertex.get(tmp).getLongitude();
    	
  			RouteBean route = getDirectionsData(srcPlace, myDestination);
  			if(route == null) 
  			{
  				Toast.makeText(BuswayMaps.this, connectionError, Toast.LENGTH_SHORT).show();
  				return;
  			}
  	
  			duration.put(route.getDurationSecond(), item);
  		
  			Log.v("BuswayMaps","(" + tmp + ") " 
				  + treeVertex.get(tmp).getName() + " , line = " + treeVertex.get(tmp).getLine() 
				  + " --> " + route.getDurationSecond() + " Second | " + route.getLength() + " Meter");
  			if((halteNext < halteList.size() && (treeVertex.get(tmp).getLine() == treeVertex.get(halteList.get(halteNext)).getLine()))
				  || route.getLength() > 1000 || route.getLength() < 200)
  			{
  				line = true;
  			}
  		
  			addEdge(item, newest, true);
  		}

  		Log.v("BuswayMaps","line = " + line);
  		if(line)
  		{
  			int minDuration = Collections.min(duration.keySet());
  			BuswayBean bus = duration.remove(minDuration);
  			Log.e("BuswayMaps","(" + bus.getId() + ") " + treeVertex.get(bus.getId()).getName() + " --> " + minDuration + " Second");
  			destHalteId = bus.getId();
  			if(oriHalteId != destHalteId)
  				path = getRoute(oriHalteId, bus.getId()); // algoritma
  		}
  		else
  		{
  			path = getRoute(oriHalteId, newest.getId()); // algoritma
  			path.remove(path.firstElement()); // remove vertex destination
  			destHalteId = path.firstElement();
  		}

	 	destLatLongHalte = treeVertex.get(destHalteId).getLatitude()+","+treeVertex.get(destHalteId).getLongitude();
	 	destLatLongHalteValue = new LatLng(treeVertex.get(destHalteId).getLatitude(), treeVertex.get(destHalteId).getLongitude());
	 	destHalteName = treeVertex.get(destHalteId).getName();
		
	 	Log.v("BuswayMaps","(" + oriHalteId + ") " + oriHalteName + " --> (" + destHalteId + ") " + destHalteName + " [" + bigDis + " KM]");
		
	 	if(bigDis.compareTo(new BigDecimal(1.5)) <= 0 || oriHalteId == destHalteId)
	 	{	
	 		routeWalking = getDirectionsData(myPosition, myDestination); // by walking
	 		if(routeWalking == null) {
	 			Toast.makeText(BuswayMaps.this, connectionError, Toast.LENGTH_SHORT).show();
	 			return;
	 		}
	 		message = distanceTo + " '" + destination + "' " + is + " " + routeWalking.getTxtLength() + 
	 				" (" + routeWalking.getTxtDuration() + ") \n" + byWalking;
	 	}
	 	else
	 	{
	 		routeOri = getDirectionsData(myPosition, oriLatLongHalte); // a --> b
	 		routeDest = getDirectionsData(destLatLongHalte, myDestination); // c --> d
	 		if(routeOri == null || routeDest == null) {
	 			Toast.makeText(BuswayMaps.this, connectionError, Toast.LENGTH_SHORT).show();
	 			return;
	 		}
	   
	 		pathTmp.addAll(path);
	 		int number = 0;
	 		if(routeOri.getLength() == 0)
	 		{
	 			messageTmp = "1. " + from + " '" + oriHalteName + "' \n";
	 			number = 1;
	 		}
		  	else
		  	{
		  		if(routeOri.getLength() < 1000)
		  			messageTmp = "1. " + walkTo + " '" + oriHalteName + "' (" + routeOri.getTxtLength() + ", " 
		  					+ routeOri.getTxtDuration() + ") \n";
		  		else
		  			messageTmp = "1. " + distanceTo + " '" + oriHalteName + "' (" + routeOri.getTxtLength() 
		  					+ ", " + routeOri.getTxtDuration() + ") \n";
	   		
		  		messageTmp = messageTmp + "2. " + from + " '" + oriHalteName + "' \n";
		  		number = 2;
		  	}
	   
	 		String transit = getMessage(pathTmp, destHalteName, destination, number);
	 		messageTmp = messageTmp + transit;
	   	
	 		message = distanceTo + " '" + destination + "' " + is + " " + bigDis 
	 				+ " km (" + path.size() + " Busway " + stop + ") \n" + messageTmp;
	 	}
		
	 	helpBuilder.setMessage(message); 
	 	getDirectionList = false;
	  
	 	helpBuilder.setPositiveButton(showMap, new DialogInterface.OnClickListener()
	 	{  
	 		public void onClick(DialogInterface dialog, int which) 
	 		{ 
	 			if(bigDis.compareTo(new BigDecimal(1.5)) <= 0 || oriHalteId == destHalteId)
	 			{
	 				getDirectionMap(fromPosition, toPosition);
	 			}
	 			else
			  	{	 
	 				getDirectionBusway(path); // marker busway route (b --> c)
	 				getDirectionMap(fromPosition, oriLatLongHalteValue);
	 				getDirectionMap(destLatLongHalteValue, toPosition);
			  	}
	 			
	 			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngMyLocation, 17));
	 			getDirectionList = true;
	 		}  
	 	});
		
	 	helpBuilder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() 
	 	{  
	 		@Override  
	 		public void onClick(DialogInterface dialog, int which) {  
	 			dialog.cancel();
	 		}	  
	 	});  
		
	 	AlertDialog helpDialog = helpBuilder.create();
	 	helpDialog.show();  
  	}
  
  	private int getBuswayStopID(double lat, double lng) 
  	{
  		DistanceAlgorithm holder = new DistanceAlgorithm();
  		TreeMap<BigDecimal, BuswayBean> distanceFromMyPosition = new TreeMap<BigDecimal, BuswayBean>();
  		BuswayBean bus = new BuswayBean();
  		BigDecimal minDistance = null;
  		
  		for(int i=0; i < allHalte.size(); i++ )
  		{
  			BuswayBean temp = new BuswayBean();
  			temp = allHalte.get(i);
  			double distance = holder.DistanceBetweenPlaces(lat, lng, temp.getLatitude(), temp.getLongitude());
  			BigDecimal bigDis = new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);
		  	distanceFromMyPosition.put(bigDis, temp);
  		}
  	
  		minDistance = Collections.min(distanceFromMyPosition.keySet());
  		bus = distanceFromMyPosition.get(minDistance);
  	
  		return bus.getId();
  	}
  
  	private List<Integer> getListBuswayStopID(double lat, double lng) 
  	{
  		DistanceAlgorithm holder = new DistanceAlgorithm();
  		TreeMap<BigDecimal, BuswayBean> distanceFromMyPosition = new TreeMap<BigDecimal, BuswayBean>();
  		List<Integer> halteList = new Vector<Integer>();
  		BuswayBean bus = new BuswayBean();
  		BigDecimal minDistance = null;
  	
  		for(int i=0; i < allHalte.size(); i++ )
  		{
  			BuswayBean temp = new BuswayBean();
  			temp = allHalte.get(i);
  			double distance = holder.DistanceBetweenPlaces(lat, lng, temp.getLatitude(), temp.getLongitude());  		
  			BigDecimal bigDis = new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);
		  	distanceFromMyPosition.put(bigDis, temp);
  		}
  	
  		minDistance = Collections.min(distanceFromMyPosition.keySet());
  		bus = distanceFromMyPosition.remove(minDistance);
  		halteList.add(bus.getId());
  	
  		minDistance = Collections.min(distanceFromMyPosition.keySet());
  		bus = distanceFromMyPosition.remove(minDistance);
  		halteList.add(bus.getId());
  	
  		return halteList;
  	}
  
  	// algoritma
  	private Stack<Integer> getRoute(int oriHalteId, int destHalteId) 
  	{ 
  		Log.v("BuswayMaps","----Begin getRoute(algoritma)----");
  		boolean done = false;
  		Queue<BuswayRoute> priorityQueue = new LinkedList<BuswayRoute>();
  		TreeMap<Integer, Integer> frontAndPredecessor = new TreeMap<Integer, Integer>();
  		List<Integer> visited = new Vector<Integer>();
  		Stack<Integer> path = new Stack<Integer>();
  		Stack<Integer> pathTmp = new Stack<Integer>();
  		BuswayRoute br = new BuswayRoute();
  		int nextNeighbourId = 0;
  		int frontVertexId = 0;
  		int vertexId = 0;
		
  		br.setNextNeighbourId(oriHalteId);
  		br.setFrontVertexId(oriHalteId);
  		br.setCost(0);
		
  		priorityQueue.add(br);
		
  		visited.add(br.getNextNeighbourId()); // mark as visited
		
  		while(!done && !priorityQueue.isEmpty())
  		{
//			Log.v("BuswayMaps","--Begin While--");
//			Log.v("BuswayMaps","----priorityQueue value----");
//			for(BuswayRoute e : priorityQueue)
//			{
//				Log.v("BuswayMaps",
//						"(" + e.getNextNeighbourId() + ") " + treeVertex.get(e.getNextNeighbourId()).getName() 
//						+ " | " + e.getCost() 
//						+ " | (" + e.getFrontVertexId() + ") " + treeVertex.get(e.getFrontVertexId()).getName());
//			}
//			Log.v("BuswayMaps","---------------------------");
			
  			br = priorityQueue.remove();
  			frontVertexId = br.getNextNeighbourId();
			
  			if(frontVertexId > 0)
  			{
//				Log.v("BuswayMaps","remove ("+ br.getFrontVertexId() +") " + treeVertex.get(br.getFrontVertexId()).getName());
//				Log.v("BuswayMaps","frontVertexId = ("+ frontVertexId +") " + treeVertex.get(frontVertexId).getName());
  				for(Edge e : edge.values())
  				{
  					if(e.getVertexBegin().getId() == frontVertexId)
  					{
  						nextNeighbourId = e.getVertexEnd().getId();
//						Log.v("BuswayMaps","("+ frontVertexId +") "+ treeVertex.get(frontVertexId).getName()
//								+ " has neighbour (" + nextNeighbourId + ") " + treeVertex.get(nextNeighbourId).getName());
  						if(!visited.contains(nextNeighbourId) || nextNeighbourId == 0)
  						{
//							Log.v("BuswayMaps","and ("+ nextNeighbourId +") "+ treeVertex.get(nextNeighbourId).getName() + " not visited");
  							priorityQueue.add(addShortestPath(visited, frontVertexId, br, e).clone());
  							visited.add(nextNeighbourId); // mark as visited
  							frontAndPredecessor.put(nextNeighbourId, frontVertexId);
  						}
  					}
					
  					if(nextNeighbourId == destHalteId)
  					{
//						Log.v("BuswayMaps","-----DONE TRUE-----");
  						done = true;
  						priorityQueue.add(br.clone());
  						break;
  					}
  				}
  			}
			
  			br.clean();
//			Log.v("BuswayMaps","--End While--");
  		}
		
  		path.push(destHalteId);
  		vertexId = destHalteId;
		
  		while(hasPredecessor(vertexId, frontAndPredecessor))
  		{
  			vertexId = predecessorOfVertex(vertexId, frontAndPredecessor);
  			path.push(vertexId);
			
  			if(vertexId == oriHalteId)
  				break;
  		}
		
  		pathTmp.addAll(path);
		
  		while(!pathTmp.isEmpty())
  		{	
  			int pop = pathTmp.pop();
  			Log.v("BuswayMaps","halte = (" + pop + ") "+ treeVertex.get(pop).getName());
  		}
		
	 	Log.v("BuswayMaps","----End getRoute(algoritma)----");
	  	return path;
  	}
  
  	private String getMessage(Stack<Integer> path, String destHalteName, String destination, int number) 
  	{	 
  		Log.v("BuswayMaps","--Begin getMessage Transit--");
  		Stack<Integer> pathOld = new Stack<Integer>();
  		List<Integer> pathList = new Vector<Integer>();
  		String message = "";
  		int halteOriID = 0;
  		int halteDestID = 0;
  		int next = 0;
		
  		pathOld.addAll(path);
  		while(!pathOld.isEmpty())
  			pathList.add(pathOld.pop());
  	
  		pathOld.addAll(path);
		
  		while(!path.isEmpty())
	  	{
  			halteOriID = path.pop();
  		
  			if(!path.isEmpty())
  			{
  				halteDestID = path.peek();
  			
  				BuswayBean halteA = new BuswayBean();
  				BuswayBean halteB = new BuswayBean();
					
  				halteA = treeVertex.get(halteOriID);
  				halteB = treeVertex.get(halteDestID);
    		
  				if((halteA.getLine() != halteB.getLine() && halteA.getLine() > 0 && halteB.getLine() > 0)  // pindah koridor jalan kaki
  						|| (halteA.getId() == 9 && halteB.getId() == 71) // dukuh atas 1 --> dukuh atas 2
  						|| (halteA.getId() == 71 && halteB.getId() == 9) // dukuh atas 2 --> dukuh atas 1
  						|| (halteA.getId() == 53 && halteB.getId() == 142) // grogol 1 --> grogol 2
						|| (halteA.getId() == 142 && halteB.getId() == 53) // grogol 2 --> grogol 1
  						|| (halteA.getId() == 87 && halteB.getId() == 193) // pasar jatinegara --> stasiun jatinegara 2
  						|| (halteA.getId() == 193 && halteB.getId() == 87) // stasiun jatinegara 2 --> pasar jatinegara
  						|| (halteA.getId() == 174 && halteB.getId() == 192) // stasiun jatinegara --> flyover jatinegara
						|| (halteA.getId() == 192 && halteB.getId() == 174) // flyover jatinegara --> staiusn jatinegara
  					|| (halteA.getId() == 165 && halteB.getId() == 203) // SUNTER KELAPA GADING --> SUNTER BOULEVARD BARAT
					|| (halteA.getId() == 203 && halteB.getId() == 165)) // SUNTER BOULEVARD BARAT --> SUNTER KELAPA GADING
  				{
  					message = message + (number = number +1) + ". " + getOff + " '" 
									+ halteA.getName() + "' " + thenWalkToHalte + " '" + halteB.getName() +"' \n";
					
  					if(halteA.getId() == 9 && halteB.getId() == 71) // dukuh atas 1 --> dukuh atas 2
  					{
  						if(pathList.contains(70))  // HALIMUN
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
  						else if(pathList.contains(105))  // SETIABUDI UTARA
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'RAGUNAN' \n";	
  					}
  					else if(halteA.getId() == 71 && halteB.getId() == 9) // dukuh atas 2 --> dukuh atas 1 
  					{
  						if(pathList.contains(10)) // TOSARI
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KOTA' \n";
  						else if(pathList.contains(8))  // SETIABUDI
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'BLOK M' \n";
  					}
  					else if(halteA.getId() == 53 && halteB.getId() == 142) // grogol 1 --> grogol 2
  					{
  						if(pathList.contains(141)) // LATUMENTEN
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
  						else if(pathList.contains(143))  // S PARMAN CENTRAL PARK
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PINANG RANTI' \n";
  					}
  					else if(halteA.getId() == 142 && halteB.getId() == 53) // grogol 2 --> grogol 1
  					{
  						if(pathList.contains(54)) // RS SUMBER WARAS
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'HARMONI' \n";
  						else if(pathList.contains(135)) // KEDOYA GREEN GARDEN
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'LEBAK BULUS' \n";
  						else if(pathList.contains(52))  // JELAMBAR
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KALIDERES' \n";
  					}
  					else if(halteA.getId() == 6 && halteB.getId() == 148) // BENHIL --> SEMANGGI
				  	{
  						if(pathList.contains(149)) // GATOT SUBROTO LIPI
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PINANG RANTI' \n";
  						else if(pathList.contains(147))  // SENAYAN JCC
						  	message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
				  	}
  					else if(halteA.getId() == 148 && halteB.getId() == 6) // SEMANGGI --> BENHIL
  					{
  						if(pathList.contains(5)) // POLDA METRO
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'BLOK M' \n";
  						else if(pathList.contains(7))  // KARET
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KOTA' \n";
  					}
  					else if(halteA.getId() == 99 && halteB.getId() == 151) // KUNINGAN TIMUR --> KUNINGAN BARAT
  					{
  						if(pathList.contains(152)) // TEGAL PARANG
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PINANG RANTI' \n";
  						else if(pathList.contains(150))  // GATSU JAMSOSTEK
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
  					}
  					else if(halteA.getId() == 151 && halteB.getId() == 99) // KUNINGAN BARAT --> KUNINGAN TIMUR
  					{
  						if(pathList.contains(100)) // PATRA
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'DUKUH ATAS 2' \n";
  						else if(pathList.contains(98))  // MAMPANG PRAPATAN
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'RAGUNAN' \n";
  					}
					else if(halteA.getId() == 83 && halteB.getId() == 67) // MATRAMAN 1 --> MATRAMAN 2
					{
						if(pathList.contains(66)) // PASAR GENJING
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
						else if(pathList.contains(68))  // MANGGARAI
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'DUKUH ATAS 2' \n";
					}
					else if(halteA.getId() == 67 && halteB.getId() == 83) // MATRAMAN 2 --> MATRAMAN 1
					{
						if(pathList.contains(84)) // TEGALAN
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";
						else if(pathList.contains(82))  // SALEMBA CAROLUS
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'ANCOL' \n";
					}
					else if(halteA.getId() == 171 && halteB.getId() == 63) // PRAMUKA BPKP 2 --> PRAMUKA BPKP
					{
						if(pathList.contains(62)) // UNJ
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
						else if(pathList.contains(64))  // PRAMUKA LIA
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'DUKUH ATAS 2' \n";
					}
					else if(halteA.getId() == 63 && halteB.getId() == 171) // PRAMUKA BPKP --> PRAMUKA BPKP 2
					{
						if(pathList.contains(170)) // RAWASARI
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'TANJUNG PRIOK' \n";
						else if(pathList.contains(172))  // RAWAMANGUN
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PGC' \n";
					}
					else if(halteA.getId() == 26 && halteB.getId() == 167) // CEMPAKA TIMUR --> CEMPAKA MAS 2
					{
						if(pathList.contains(166)) // YOS SUDARSO KODAMAR
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'TANJUNG PRIOK' \n";
						else if(pathList.contains(168))  // CEMPAKA PUTIH
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PGC' \n";
					}
					else if(halteA.getId() == 167 && halteB.getId() == 26) // CEMPAKA MAS 2 --> CEMPAKA TIMUR
					{
						if(pathList.contains(25)) // PENDONGKELAN
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
						else if(pathList.contains(27))  // RS ISLAM
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'HARMONI' \n";
					}
					else if(halteA.getId() == 32 && halteB.getId() == 78) // SENEN --> SENEN SENTRAL
					{
						if(pathList.contains(77)) // BUDI UTOMO
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'ANCOL' \n";
						else if(pathList.contains(79))  // PAL PUTIH
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";
					}
					else if(halteA.getId() == 78 && halteB.getId() == 32) // SENEN SENTRAL --> SENEN
					{
						if(pathList.contains(31)) // GALUR
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
						else if(pathList.contains(33))  // ATRIUM SENEN
							message = message + (number = number +1) + ". " + takeDirectionTo + " 'HARMONI' \n";
					}
					else if(halteA.getId() == 87 && halteB.getId() == 193) // pasar jatinegara --> stasiun jatinegara 2
  					{
  						if(pathList.contains(192))  // flyover jatinegara
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGEBANG' \n";
  						else if(pathList.contains(194))  // JATINEGARA RS. PREMIER
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";	
  					}
  					else if(halteA.getId() == 193 && halteB.getId() == 87) // stasiun jatinegara 2 --> pasar jatinegara 
  					{
  						if(pathList.contains(88)) // KAMPUNG MELAYU
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";
  					}
  					
  					else if(halteA.getId() == 174 && halteB.getId() == 192) // stasiun jatinegara --> flyover jatinegara
  					{
  						if(pathList.contains(191))  // PASAR ENJO
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGEBANG' \n";
  						else if(pathList.contains(193))  // STASIUN JATINEGARA
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";	
  					}
  					else if(halteA.getId() == 192 && halteB.getId() == 174) // flyover jatinegara --> stasiun jatinegara 
  					{
  						if(pathList.contains(173)) // BEA CUKAI
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'TANJUNG PRIOK' \n";
  						else if(pathList.contains(175))  // PEDATI
  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PGC' \n";
  					}
//  					else if(halteA.getId() == 165 && halteB.getId() == 203) // SUNTER KELAPA GADING --> SUNTER BOULEVARD BARAT 
//  					{
//  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
//  					}
//  					else if(halteA.getId() == 203 && halteB.getId() == 165) // SUNTER BOULEVARD BARAT --> SUNTER KELAPA GADING 
//  					{
//  						if(pathList.contains(164)) // PLUMPANG PERTAMINA
//  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'TANJUNG PRIOK' \n";
//  						else if(pathList.contains(166))  // YOS SUDARSO KODAMAR
//  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PGC' \n";
//  					}
  				}
				else if(halteB.getLine() == 0)
				{					
					int index = 0;
					for(int i = 0; i < pathList.size(); i++)
					{
						index = index + 1;
						if(pathList.get(i) == halteOriID)
						{
							next = index;
							next = next + 1;

							if(next < pathList.size())
							{
								int halteC = pathList.get(next);
			  				
								if(halteA.getId() == 13 && halteB.getId() == 14 && halteC == 40) // BI - MONAS - BALAIKOTA
								{
									message = message + (number = number +1) + ". " + switchTo + " koridor 2 " + at + " '" + halteB.getName() + "' \n";
									message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
			  				}
			  				else if(halteA.getId() == 14 && halteB.getId() == 15 && halteC == 39) // MONAS - HARMONI - PECENONGAN
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 3 at '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PASAR BARU' \n";
			  				}
			  				else if((halteA.getId() == 14 || halteA.getId() == 16) 
			  						&& halteB.getId() == 15 && halteC == 54) // MONAS/SAWAH BESAR - HARMONI - RS SUMBER WARAS
			  				{
			  					if(pathOld.contains(135)) // KEDOYA GREEN GARDEN
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 8 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'LEBAK BULUS' \n";
			  					}
			  					else
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 3 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'KALIDERES' \n";
			  					}
			  				}
			  				else if(halteA.getId() == 16 && halteB.getId() == 15 && halteC == 39) // SAWAH BESAR - HARMONI - PECENONGAN
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 3 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PASAR BARU' \n";
			  				}
			  				else if(halteA.getId() == 16 && halteB.getId() == 15 && halteC == 14) // SAWAH BESAR - HARMONI - MONAS
			  				{
			  					if(pathOld.contains(40)) // BALAIKOTA
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 2 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
			  					}
			  				}
			  				else if(halteA.getId() == 54 && halteB.getId() == 15 && halteC == 16) // RS SUMBER WARAS - HARMONI - SAWAH BESAR
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 1 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'KOTA' \n";
			  				}
			  				else if(halteA.getId() == 54 && halteB.getId() == 15 && halteC == 39) // RS SUMBER WARAS - HARMONI - PECENONGAN 
			  				{
			  					if(pathOld.contains(135) && pathOld.contains(39)) // KEDOYA && PECENONGAN dari lebak bulus
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 2 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'PASAR BARU' \n";
			  					}
			  				}
			  				else if((halteA.getId() == 54 || halteA.getId() == 39) 
			  						&& halteB.getId() == 15 && halteC == 14) // RS SUMBER WARAS/PECENONGAN - HARMONI - MONAS
			  				{
			  					if(pathOld.contains(40)) // BALAIKOTA
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 2 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
			  					}
			  					else
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 1 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'BLOK M' \n";
			  					}
			  				}
			  				else if(halteA.getId() == 39 && halteB.getId() == 15 && halteC == 16) // PECENONGAN - HARMONI - SAWAH BESAR
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 1 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'KOTA' \n";
			  				}
			  				else if(halteA.getId() == 39 && halteB.getId() == 15 && halteC == 54) // PECENONGAN - HARMONI - RS SUMBER WARAS
			  				{
			  					if(pathOld.contains(135)) // KEDOYA GREEN GARDEN
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 8 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'LEBAK BULUS' \n";
			  					}
			  				}
			  				else if(halteA.getId() == 50 && halteB.getId() == 51 && halteC == 135) // TAMAN KOTA - INDOSIAR - KEDOYA GREEN GARDEN
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 8 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'LEBAK BULUS' \n";
			  				}
			  				else if(halteA.getId() == 135 && halteB.getId() == 51 && halteC == 50) // KEDOYA GREEN GARDEN - INDOSIAR - TAMAN KOTA
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 3 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'KALIDERES' \n";
			  				}
			  				else if(halteA.getId() == 106 && halteB.getId() == 70 && halteC == 69) // LATUHARHARI - HALIMUN - PASAR RUMPUT
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 4 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PULOGADUNG' \n";
			  				}
			  				else if(halteA.getId() == 70 && halteB.getId() == 71 && halteC == 105) // HALIMUN - DUKUH ATAS 2 - SETIABUDI UTARA
			  				{
			  					if(pathOld.contains(69)) // PASAR RUMPUT
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 6 at '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'RAGUNAN' \n";
			  					}
			  				}
			  				else if(halteA.getId() == 157 && halteB.getId() == 116 && halteC == 117) // CAWANG CILIWUNG - BNN - CAWANG OTISTA
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 7 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";
			  				}
			  				else if(halteA.getId() == 117 && halteB.getId() == 116 && halteC == 157) // CAWANG OTISTA - BNN - CAWANG CILIWUNG
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 9 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
			  				}
			  				else if(halteA.getId() == 178 && halteB.getId() == 115 && halteC == 116) // CAWANG SUTOYO - CAWANG UKI - BNN
			  				{
			  					if(pathOld.contains(157)) // CAWANG CILIWUNG
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 9 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
			  					}
			  					else
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 7 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";
			  					}
			  				}
			  				else if(halteA.getId() == 114 && halteB.getId() == 113 && halteC == 112) // BKN - PGC CILILITAN - PS. KRAMAT JATI
			  				{
			  					if(pathOld.contains(178) && pathOld.contains(111)) // CAWANG SUTOYO && PS. INDUK
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 7 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG RAMBUTAN' \n";
			  					}
			  					else if(pathOld.contains(178) && pathOld.contains(158)) // CAWANG SUTOYO && GARUDA TMN MINI
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 9 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'PINANG RANTI' \n";
			  					}
			  					else if(pathOld.contains(157) && pathOld.contains(111)) // CAWANG CILIWUNG && PS. INDUK
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 7 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG RAMBUTAN' \n";
			  					}
			  					else if(pathOld.contains(117) && pathOld.contains(158)) // CAWANG OTISTA && GARUDA TMN MINI
			  					{
			  						message = message + (number = number +1) + ". " + switchTo + " koridor 9 " + at + " '" + halteB.getName() + "' \n";
			  						message = message + (number = number +1) + ". " + takeDirectionTo + " 'PINANG RANTI' \n";
			  					}
			  				}
			  				else if(halteA.getId() == 114 && halteB.getId() == 113 && halteC == 179) // BKN - PGC CILILITAN - PGC
			  				{
		  						if(pathOld.contains(157) || pathOld.contains(117)) // CAWANG CILIWUNG/CAWANG OTISTA
		  						{
		  							message = message + (number = number +1) + ". " + switchTo + " koridor 10 " + at + " '" + halteB.getName() + "' \n";
		  							message = message + (number = number +1) + ". " + takeDirectionTo + " 'PGC' \n";
		  						}
			  				}
			  				else if(halteA.getId() == 158 && halteB.getId() == 112 && halteC == 111) // GARUDA TMN MINI - PS. KRAMAT JATI - PS. INDUK
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 7 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG RAMBUTAN' \n";
			  				}
			  				else if(halteA.getId() == 111 && halteB.getId() == 112 && halteC == 158) // PS. INDUK - PS. KRAMAT JATI - GARUDA TMN MINI
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 9 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PINANG RANTI' \n";
			  				}
			  				else if(halteA.getId() == 116 && halteB.getId() == 115 && halteC == 178) // BNN - CAWANG UKI - CAWANG SUTOYO
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 10 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'TANJUNG PRIOK' \n";
			  				}
			  				else if(halteA.getId() == 164 && halteB.getId() == 165 && halteC == 203) // PLUMPANG PERTAMINA - SUNTER KLP GAING - SUNTER BOULEVARD BARAT
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 12 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
			  				}
			  				else if(halteA.getId() == 166 && halteB.getId() == 165 && halteC == 203) // YOS SUDARSO KODAMAR - SUNTER KLP GAING - SUNTER BOULEVARD BARAT
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 12 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
			  				}
			  				else if(halteA.getId() == 203 && halteB.getId() == 165 && halteC == 166) // SUNTER BOULEVARD BARAT - SUNTER KLP GAING - YOS SUDARSO KODAMAR 
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 12 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PGC' \n";
			  				}
			  				else if(halteA.getId() == 199 && halteB.getId() == 75 && halteC == 76) // KEMAYORAN LANDAS PACU TIMUR - JEMBATAN MERAH - PASAR BARU TIMUR 
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 5 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'KAMPUNG MELAYU' \n";
			  				}
								
			  				else if(halteA.getId() == 74 && halteB.getId() == 75 && halteC == 199) // GUNUNG SAHARI MANGGA DUA  - JEMBATAN MERAH -KEMAYORAN LANDAS PACU TIMUR
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 12 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'TANJUNG PRIOK' \n";
			  				}
			  				else if(halteA.getId() == 197 && halteB.getId() == 20 && halteC == 19) // PANGERAN JAYAKARTA - KOTA - GLODOK
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 1 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'BLOK M' \n";
			  				}
			  				else if(halteA.getId() == 196 && halteB.getId() == 20 && halteC == 19) // PANGERAN JAYAKARTA - KOTA - GLODOK
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 1 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'BLOK M' \n";
			  				}
			  				else if(halteA.getId() == 19 && halteB.getId() == 20 && halteC == 197) // GLODOK - KOTA - PANGERAN JAYAKARTA 
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 12 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'TANJUNG PRIOK' \n";
			  				}
			  				else if(halteA.getId() == 19 && halteB.getId() == 20 && halteC == 196) // GLODOK - KOTA - KALI BESAR BARAt 
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 12 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PLUIT' \n";
			  				}
			  				else if(halteA.getId() == 195 && halteB.getId() == 137 && halteC == 138) // BANDENGAN PEKOJAN - PENJARINGAN - JEMBATAN TIGA 
			  				{
			  					message = message + (number = number +1) + ". " + switchTo + " koridor 9 " + at + " '" + halteB.getName() + "' \n";
			  					message = message + (number = number +1) + ". " + takeDirectionTo + " 'PINANG RANTI' \n";
			  				}
			  				break;
			  			}
			  		}
						
					}
				}
  		}
  	}
  	
  	message = message + (number = number +1) + ". " + getOff + " '" + destHalteName + "' \n";
  	if(routeDest.getLength() > 0)
  	{
  		if(routeDest.getLength() < 1000)
  			message = message + (number = number +1) + ". " + walkTo + " '" + destination + "' (" 
  					+ routeDest.getTxtLength() + ", " + routeDest.getTxtDuration() + ") \n";
  		else
  			message = message + (number = number +1) + ". " + distanceTo + " '" + destination + "' (" 
    				+ routeDest.getTxtLength() + ", " + routeDest.getTxtDuration() + ") \n";
  	}
  	Log.e("BuswayMaps",message);
  	Log.v("BuswayMaps","--end getMessage--");
		return message;
  }
  
  private void getDirectionBusway(Stack<Integer> path) 
  { 
  	Log.v("BuswayMaps","--getDirectionBusway--");
  	int halteOriID = 0;
  	int halteDestID = 0;
  	PolylineOptions line;
  	
  	while(!path.isEmpty())
  	{
  		halteOriID = path.pop();
  		
  		if(!path.isEmpty())
  		{
  			halteDestID = path.peek();
  			
			BuswayBean a = new BuswayBean();
			BuswayBean b = new BuswayBean();

			a = treeVertex.get(halteOriID);
			b = treeVertex.get(halteDestID);
				
//    		GeoPoint startGP=new GeoPoint((int)(a.getLatitude() * 1E6), (int)(a.getLongitude() * 1E6));
    		
    		if((a.getLine() != b.getLine() && a.getLine() > 0 && b.getLine() > 0)  // pindah koridor jalan kaki
    					|| (a.getId() == 9 && b.getId() == 71) // dukuh atas 1 --> dukuh atas 2
						|| (a.getId() == 71 && b.getId() == 9) // dukuh atas 2 --> dukuh atas 1
						|| (a.getId() == 53 && b.getId() == 142) // grogol 1 --> grogol 2
						|| (a.getId() == 142 && b.getId() == 53) // grogol 2 --> grogol 1
						|| (a.getId() == 87 && b.getId() == 193) // pasar jatinegara --> stasiun jatinegara 2
  						|| (a.getId() == 193 && b.getId() == 87) // stasiun jatinegara 2 --> pasar jatinegara
  						|| (a.getId() == 174 && b.getId() == 192) // stasiun jatinegara --> flyover jatinegara
						|| (a.getId() == 192 && b.getId() == 174) // flyover jatinegara --> staiusn jatinegara
						|| (a.getId() == 165 && b.getId() == 203) // SUNTER KELAPA GADING --> SUNTER BOULEVARD BARAT
						|| (a.getId() == 203 && b.getId() == 165)) // SUNTER BOULEVARD BARAT --> SUNTER KELAPA GADIN 
    		{
    			line=new PolylineOptions().add(new LatLng(a.getLatitude(), a.getLongitude()),
    				                                new LatLng(b.getLatitude(), b.getLongitude()))
    				                           .width(10).color(Color.RED);

    			mMap.addPolyline(line);
    		}
    		else
    		{
    			line=new PolylineOptions().add(new LatLng(a.getLatitude(), a.getLongitude()),
                        new LatLng(b.getLatitude(), b.getLongitude()))
                   .width(10).color(Color.GREEN);

    			mMap.addPolyline(line);
    		}
		}
  	}
  	
  	Log.v("BuswayMaps","--end getDirectionBusway--");
  }

  class DistanceAlgorithm
  {
  	double RADIO = 6367;
    private DistanceAlgorithm() { }

    public double DistanceBetweenPlaces(double lat1, double lon1, double lat2, double lon2)
    {
    	double dlat = Math.toRadians(lat2 - lat1);
      double dlon = Math.toRadians(lon2 - lon1);
      double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
      		Math.pow(Math.sin(dlon / 2), 2);
      
      double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      return angle * RADIO;
    }
  }
  
  /*
   * boolean newest
   * - true, adding c1 to D and c2 to D
   * - false, original edge from database
   */
  private void addEdge(BuswayBean vertexBegin, BuswayBean vertexEnd, boolean newest)
  {
//  	Log.v("BuswayMaps","----Begin addEdge----");
		Edge ed = new Edge();
		BuswayBean v = new BuswayBean();
		StringBuffer sb = new StringBuffer(2);
		List<Edge> tmpEdge = new Vector<Edge>(32);
		
		ed.setVertexBegin(vertexBegin);
		ed.setVertexEnd(vertexEnd);
		
		sb.append(vertexBegin.getId()).append(" - ").append(vertexEnd.getId());
		
		if(newest)
			edgeKey.add(sb.toString());
		
		edge.put(sb.toString(), ed);
		tmpEdge.add(ed);
		
//		Log.v("BuswayMaps","success addEdge " + sb.toString());
		sb.delete(0, sb.length());
		
		if(treeVertex.containsKey(vertexBegin.getId()))
		{
			v = treeVertex.remove(vertexBegin.getId());
			v.getEdge().addAll(tmpEdge);
			treeVertex.put(vertexBegin.getId(), v);
//			Log.v("BuswayMaps",v.getName() + " (" + v.getId() + ")"+ " has size edge = " + v.getEdge().size());
		}
		
//		Log.v("BuswayMaps","----end addEdge----");
  }
  
  private BuswayRoute addShortestPath(List<Integer> visited, int frontVertex, BuswayRoute bRoute, Edge e)
	{
  	BuswayRoute buswayRouteTmp = new BuswayRoute();
		int nextNeighbour = 0;
		double weightOfEdgeToNeighbour = 0;
		double nextCost = 0;
		
		if(e.getVertexBegin().getId() == frontVertex)
		{
			nextNeighbour = e.getVertexEnd().getId();
			weightOfEdgeToNeighbour = 1;
			
			if(!visited.contains(nextNeighbour))
			{
				nextCost =  bRoute.getCost() + weightOfEdgeToNeighbour;
				
				buswayRouteTmp.setNextNeighbourId(nextNeighbour);
				buswayRouteTmp.setCost(nextCost);
				buswayRouteTmp.setFrontVertexId(frontVertex);
				
//				Log.v("BuswayMaps","adding priorityQueue = (" 
//						+ buswayRouteTmp.getNextNeighbourId() + ") " + treeVertex.get(buswayRouteTmp.getNextNeighbourId()).getName() 
//						+ " | " + buswayRouteTmp.getCost() + " | (" 
//						+ buswayRouteTmp.getFrontVertexId() + ") " + treeVertex.get(buswayRouteTmp.getFrontVertexId()).getName());
			}
		}
		
		return buswayRouteTmp;
	}
  
  private boolean hasPredecessor(int vertex, TreeMap<Integer, Integer> frontAndPredecessor)
	{
		boolean valid = false;
		if(frontAndPredecessor.containsKey(vertex));
			valid = true;
			
		return valid;
	}
  
  private int predecessorOfVertex(int vertexId, TreeMap<Integer, Integer> frontAndPredecessor)
  {
		if(frontAndPredecessor.containsKey(vertexId));
			vertexId = frontAndPredecessor.get(vertexId);
		
		return vertexId;
  }
  
  private void deleteOldDirectionAndDestination()
  {
  		bigDis = new BigDecimal(0);
	  	latMyDestination = 0.0d;
	  	lonMyDestination = 0.0d;
	  	message = null;
	  	getDirectionList = false;
	  	
	  	int treeVertexSizeBeforeDelete = treeVertex.size();
	
		if(oldDestination != null)
		{
			mMap.clear();
			LatLng latlngHalteBusway;
			
	    	for (int i = 0; i < allHalte.size(); i++)
	    	{ 
				latlngHalteBusway = new LatLng(allHalte.get(i).getLatitude(), allHalte.get(i).getLongitude());
				mMap.addMarker(new MarkerOptions()
	            	.position(latlngHalteBusway)
	            	.title(allHalte.get(i).getName())
	            	.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
	            	.anchor(0.5f, 1));
	    	}
	    	
			for(int i = 0; i < edgeKey.size(); i++) {
				edge.remove(edgeKey.get(i));
			}
			
			for(int i = treeVertexSizeWithoutDestination; i < treeVertexSizeBeforeDelete; i++) {
				treeVertex.remove(treeVertexSizeWithoutDestination);
			}
	
			
			oldDestination = null;
			edgeKey.clear();
			edgeKey = new Vector<String>();
		}
  }
  
  // popup message
  private void buildAlertMessage(final String msg, final boolean alertGPS) 
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(msg);
    builder.setCancelable(false);
    
    builder.setPositiveButton(btnYes, new DialogInterface.OnClickListener() 
    {
    	public void onClick(final DialogInterface dialog, final int id) 
    	{
    		if(alertGPS)
    			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    		else if(msg.equalsIgnoreCase(msgConnectionNotAvailable))
    		{
    			startActivity(new Intent(BuswayMaps.this, RouteBuswayActivity.class)); // pindah page
    		}
    		else
    			dialog.cancel();
    	}
    });
    
    if(alertGPS)
    {
    	builder.setNegativeButton(btnNo, new DialogInterface.OnClickListener() 
    	{
    		public void onClick(final DialogInterface dialog, final int id) 
    		{
    			startActivity(new Intent(BuswayMaps.this, RouteBuswayActivity.class)); // pindah page
    		}
    	});
    }
    
    AlertDialog alert = builder.create();
    alert.show();
  }
  
  private boolean haveNetworkConnection() 
  {
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
    for (NetworkInfo ni : netInfo) {
        if(ni.getTypeName().equalsIgnoreCase("WIFI"))
            if(ni.isConnected())
                haveConnectedWifi = true;
        if(ni.getTypeName().equalsIgnoreCase("MOBILE"))
            if(ni.isConnected())
                haveConnectedMobile = true;
    }
    return haveConnectedWifi || haveConnectedMobile;
  }
  
  protected boolean isRouteDisplayed() {
    return false;
  }

  @Override
  public void onPause() {
  	if(helper != null)
  		helper.close();
  	
  	super.onPause();
  }
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
    
    if(helper != null)
    	helper.close();
  }
  
  private void convertToIndoLanguage() 
  {
  	destination = "Tujuan";
  	pleaseInsert = "Silahkan, masukan tujuan anda..";
  	btnCancel = "Batal";
  	btnSearch = "Cari";
  	distanceTo = "Jarak ke";
  	byWalking = "Jarak terlalu dekat, cukup berjalan kaki..";
  	is = "adalah";
  	from = "Naik dari halte";
  	switchTo = "Transit ke";
  	thenWalkToHalte = "kemudian berjalan ke halte";
  	walkTo = "Silahkan, jalan menuju";
  	at = "di";
  	getOff = "Turun di halte";
  	stop = "Halte";
  	showMap = "Lihat Map";
  	takeDirectionTo = "Ambil jurusan ke";
  	msgShowYourDestination = "Masukan tujuan anda";
  	reset = "Hapus !";
  	connectionError = "Koneksi Terputus !";
  	destOutOfRange = "Tujuan anda diluar jangkauan Busway, aplikasi ini hanya untuk daerah Jakarta";
  	msgConnectionNotAvailable = "Koneksi Tidak Tersedia";
  	msgOutOfRange = "Lokasi anda diluar jangkauan, aplikasi ini hanya untuk daerah Jakarta";
  	recordNotFound = "Data tidak ditemukan";
  	msgGPS = "GPS Anda sepertinya dinonaktifkan, apakah Anda ingin mengaktifkannya?";
  	msgPleaseRefreshYourPosition = "Silahkan, perbarui posisi anda..";
  	btnYes = "Ya";
  	btnNo = "Tidak";
  }
  
  private void convertToEnglishLanguage() 
  {
  	destination = "Destination";
  	pleaseInsert = "Please, insert your destination..";
  	btnCancel = "Cancel";
  	btnSearch = "Search";
  	distanceTo = "Distance to";
  	byWalking = "Please, by walking..";
  	is = "is";
  	from = "From";
  	switchTo = "Switch to";
  	thenWalkToHalte = "then walk to";
  	walkTo = "Please, walk to";
  	at = "at";
  	getOff = "Get off";
  	stop = "stops";
  	showMap = "Show Map";
  	takeDirectionTo = "Take direction to";
  	msgShowYourDestination = "Show your destination";
  	reset = "Reset !";
  	connectionError = "Connection Error !";
  	destOutOfRange = "Your destination is out of range, because this application only work in Jakarta";
  	msgConnectionNotAvailable = "Connection not available";
  	msgOutOfRange = "Your position is out of range, because this application only work in Jakarta";
  	recordNotFound = "Record Not Found";
  	msgGPS = "Your GPS seems to be disabled, do you want to enable it?";
  	msgPleaseRefreshYourPosition = "Please, refresh your position..";
  	btnYes = "Yes";
  	btnNo = "No";
  }
  
  private RouteBean getDirectionsData(String srcPlace, String destPlace) 
  {
	  Parser parser;
	  RouteBean r = null;
	  try
	  {
		  final String urlString = "http://maps.googleapis.com/maps/api/directions/json?origin=" + srcPlace 
    			+ "&destination=" + destPlace + "&sensor=true&mode=walking";
		  Log.e("BuswayMaps",urlString);
		  parser = new GoogleParser(urlString.toString());
		  r =  parser.parse();
	  }
	  catch (Exception e)
	  {
		  Toast.makeText(BuswayMaps.this, connectionError, Toast.LENGTH_SHORT).show();
		  e.printStackTrace();
	  }
    
	  return r;
  }
	
  class RouteOverlay extends Overlay 
  {
	  private final List<GeoPoint> routePoints;
	  private int colour;
	  private static final int ALPHA = 120;
	  private static final float STROKE = 4.5f;
	  private final Path path;
	  private final Point p;
	  private final Paint paint;

	  /**
	   * 	Public constructor.
	   * 	@param route Route object representing the route.
	   * 	@param defaultColour default colour to draw route in.
	   */

	  public RouteOverlay(final RouteBean route, final int defaultColour) 
	  {
		  super();
		  routePoints = route.getPoints();
		  colour = defaultColour;
		  path = new Path();
		  p = new Point();
		  paint = new Paint();
	  }

	  @Override
	  public final void draw(final Canvas c, final MapView mv, final boolean shadow) 
	  {
		  super.draw(c, mv, shadow);

		  paint.setColor(colour);
		  paint.setAlpha(ALPHA);
		  paint.setAntiAlias(true);
		  paint.setStrokeWidth(STROKE);
		  paint.setStyle(Paint.Style.STROKE);

		  redrawPath(mv);
		  c.drawPath(path, paint);
	  }

	  /**
	   * Set the colour to draw this route's overlay with.
	   * @param c  Int representing colour.
	   */
	  public final void setColour(final int c) {
            colour = c;
	  }

	  /**
	   * Clear the route overlay.
	   */
	  public final void clear() {
            routePoints.clear();
	  }

	  /**
	   * Recalculate the path accounting for changes to
	   * the projection and routePoints.
	   * @param mv MapView the path is drawn to.
	   */
	  private void redrawPath(final MapView mv) 
	  {
		  final Projection prj = mv.getProjection();
		  path.rewind();
		  final Iterator<GeoPoint> it = routePoints.iterator();
		  prj.toPixels(it.next(), p);
		  path.moveTo(p.x, p.y);
		  while (it.hasNext()) 
		  {
			  prj.toPixels(it.next(), p);
			  path.lineTo(p.x, p.y);
		  }
		  
		  path.setLastPoint(p.x, p.y);
	  }
  }
  
  private void getDirectionMap(LatLng from, LatLng to) {
		LatLng fromto[] = { from, to };
		new LongOperation().execute(fromto);
  }
  
  private class LongOperation extends AsyncTask<LatLng, Void, Document> {

		@Override
		protected Document doInBackground(LatLng... params) {
			Document doc = md.getDocument(params[0], params[1],
					GoogleMapV2Direction.MODE_WALKING);
			return doc;
		}

		@Override
		protected void onPostExecute(Document result) {
			setResult(result);

		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	public void setResult(Document doc) 
	{
		int duration = md.getDurationValue(doc);
		String distance = md.getDistanceText(doc);
		String start_address = md.getStartAddress(doc);
		String copy_right = md.getCopyRights(doc);

		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(3).color(
				Color.RED);

		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}

		mMap.addPolyline(rectLine);
	}
} 