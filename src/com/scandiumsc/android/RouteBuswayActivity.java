package com.scandiumsc.android;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class RouteBuswayActivity extends FragmentActivity 
{
	BuswayHelper helper=null;
	Cursor model=null;
	
	//the ViewSwitcher
	private ViewSwitcher switcher;
	private static final int REFRESH_SCREEN = 1;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		switcher = (ViewSwitcher) findViewById(R.id.profileSwitcher);
		startScan();

		Button btnShowmap=(Button) findViewById(R.id.showmap);
		btnShowmap.setOnClickListener(onMap);
	}

	public void startScan() {
		new Thread() {
			public void run() {					
				try{			
					// This is just a tmp sleep so that we can emulate something loading
					model = getHelper().getAllHalte();
					if(model.getCount() <= 0) 
					{
						Log.v("RouteBuswayActivity","--INSERT DATABASE--");
						
						/**
						 * ket :(POLE)
						 * 0 = halte hanya lewat
						 * 1 = halte exit
						 * 2 = halte transit
						 */
						inputAllHalte(); // DARI KORIDOR 1 SAMPAI 12
					
						inputRuteKoridor1BlokMKota();
						inputRuteKoridor2PulogadungHarmoni();
						inputRuteKoridor3KalideresPasarbaru();
						inputRuteKoridor4PulogadungDukuhatas2();
						inputRuteKoridor5AncolKampungmelayu();
						inputRuteKoridor6Dukuhatas2Ragunan();
						inputRuteKoridor7KampungmelayuKampungrambutan();
						inputRuteKoridor8LebakbulusHarmoni();
						inputRuteKoridor9PluitPinangranti();
						inputRuteKoridor10TanjungpriokPGC();
						inputRuteKoridor11WalikotajaktimKampungmelayu();
						inputRuteKoridor12PluitTanjungPriok();
						
						inputRuteTransit();
					}
					// Use this handler so than you can update the UI from a thread
					hRefresh.sendEmptyMessage(REFRESH_SCREEN);
				}catch(Exception e){
				}
			}
		}.start();
	}
	
	// Refresh handler, necessary for updating the UI in a/the thread
	Handler hRefresh = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case REFRESH_SCREEN:
				switcher.showNext();
				// To go back to the first view, use switcher.showPrevious()
				break;
			default:
				break;
			}
		}
	};
		
//	class GetTask extends AsyncTask<Object, Void, String> 
//	{
//	    Context context;
//
//	    GetTask(Context context) {
//	    	Log.v("RouteBuswayActivity","GetTask1");
//	        this.context = context;
//	    }
//
//	    @Override
//	    protected void onPreExecute() {
//	        super.onPreExecute();
//	        
//	        Log.v("RouteBuswayActivity","GetTask2");
//	        mDialog = new ProgressDialog(RouteBuswayActivity.this);
//	        mDialog.setMessage("Please wait...");
//	        mDialog.show();
//	    }
//
//	    @Override
//	    protected String doInBackground(Object... params) 
//	    {
//	        // here you can get the details from db or web and fetch it..
//			Log.v("RouteBuswayActivity","--INSERT DATABASE--");
//			
//			/**
//			 * ket :(POLE)
//			 * 0 = halte hanya lewat
//			 * 1 = halte exit
//			 * 2 = halte transit
//			 */
//			inputAllHalte(); // DARI KORIDOR 1 SAMPAI 11
//		
//			inputRuteKoridor1BlokMKota();
//			inputRuteKoridor2PulogadungHarmoni();
//			inputRuteKoridor3KalideresPasarbaru();
//			inputRuteKoridor4PulogadungDukuhatas2();
//			inputRuteKoridor5AncolKampungmelayu();
//			inputRuteKoridor6Dukuhatas2Ragunan();
//			inputRuteKoridor7KampungmelayuKampungrambutan();
//			inputRuteKoridor8LebakbulusHarmoni();
//			inputRuteKoridor9PluitPinangranti();
//			inputRuteKoridor10TanjungpriokPGC();
//			inputRuteKoridor11WalikotajaktimKampungmelayu();
//			inputRuteKoridor12PluitTanjungPriok();
//			
//			inputRuteTransit();
//			return null;
//	    }
//
//	    @Override
//	    protected void onPostExecute(String result) {
//	    	Log.v("RouteBuswayActivity","GetTask3");
//	        super.onPostExecute(result);
//
//	        mDialog.dismiss();
//	    }
//	}
	
	
  private View.OnClickListener onMap=new View.OnClickListener() 
  {
	  @Override
	  public void onClick(View v)
	  {
		  startActivity(new Intent(RouteBuswayActivity.this, BuswayMaps.class)); // pindah page
	  }
  };

  private BuswayHelper getHelper() 
  {
    if (helper==null) 
      helper=new BuswayHelper(RouteBuswayActivity.this);
    
    return(helper);
  }
  
	private void inputAllHalte()
	{
		/*
		 * ket : KORIDOR
		 * 0 = Hanya lewat
		 * 1 = exit
		 * 2 = transit
		 */
		// koridor 1 , rute blok m - kota
		Log.v("RouteBuswayActivity","--KORIDOR 1--");
		helper.insertHalte("BLOK M", -6.24349, 106.80061, 1, 1); // exit
		helper.insertHalte("MASJID AGUNG", -6.235034,	106.798418, 1, 0);
		helper.insertHalte("BUNDARAN SENAYAN", -6.227496, 106.801435, 1, 0);
		helper.insertHalte("GELORA BUNG KARNO", -6.224217, 106.805785, 1, 0);
		helper.insertHalte("POLDA METRO JAYA", -6.221535,	106.809633, 1, 0);
		helper.insertHalte("BENDUNGAN HILIR", -6.217058, 106.815320, 1, 2); // transit
		helper.insertHalte("KARET", -6.214562, 106.818425, 1, 0);
		helper.insertHalte("SETIABUDI", -6.210257, 106.821055, 1, 0);
		helper.insertHalte("DUKUH ATAS 1", -6.20585, 106.82222, 1, 2); // transit
		helper.insertHalte("TOSARI", -6.198650, 106.823134, 1, 0);
		helper.insertHalte("BUNDARAN HI", -6.193468, 106.822961, 1, 0);
		helper.insertHalte("SARINAH", -6.18797, 106.82297, 1, 0);
		helper.insertHalte("BANK INDONESIA", -6.182719, 106.822929, 1, 0);
		helper.insertHalte("MONAS", -6.175542, 106.822764, 0, 2); // transit 
		helper.insertHalte("HARMONI SENTRAL", -6.16581, 106.82048, 0, 2); // transit
		helper.insertHalte("SAWAH BESAR", -6.160279, 106.819165, 1, 0);
		helper.insertHalte("MANGGA BESAR", -6.151947, 106.817305, 1, 0);
		helper.insertHalte("OLIMO", -6.149136, 106.816626, 1, 0);
		helper.insertHalte("GLODOK", -6.144697, 106.815455, 1, 0);
		helper.insertHalte("KOTA", -6.13779, 106.81368, 0, 2); // transit

		// koridor 2 , rute PULOGADUNG KORIDOR 2- HARMONI
		Log.v("RouteBuswayActivity","--KORIDOR 2--");
		helper.insertHalte("PULOGADUNG", -6.18327, 106.90888, 0, 1); // exit
//		helper.insertHalte("PULOGADUNG KORIDOR 2", -6.18327, 106.90888, 2, 1); // exit
		helper.insertHalte("BERMIS", -6.17814, 106.89851, 2, 0);
		helper.insertHalte("PULOMAS", -6.17471, 106.89273, 2, 0);
		helper.insertHalte("ASMI", -6.17175, 106.88875, 2, 0);
		helper.insertHalte("PENDONGKELAN", -6.16800, 106.88256, 2, 0);
		helper.insertHalte("CEMPAKA TIMUR", -6.16649, 106.87598, 2, 2); // transit
		helper.insertHalte("RS ISLAM", -6.1686, 106.87026, 2, 0);
		helper.insertHalte("CEMPAKA TENGAH", -6.17036, 106.86645, 2, 0);
		helper.insertHalte("PASAR CEMPAKA PUTIH", -6.17231, 106.86226, 2, 0);
		helper.insertHalte("RAWA SELATAN", -6.17389, 106.85777, 2, 0);
		helper.insertHalte("GALUR", -6.17437, 106.85463, 2, 0);
		helper.insertHalte("SENEN", -6.17821, 106.84240, 2, 2); // transit
		helper.insertHalte("ATRIUM SENEN", -6.1772, 106.84091, 2, 0);
		helper.insertHalte("RSPAD", -6.17555, 106.83675, 2, 0);
		helper.insertHalte("DEPLU", -6.1735, 106.8347, 2, 0);
		helper.insertHalte("GAMBIR 1", -6.17426, 106.8303, 2, 0);
		helper.insertHalte("ISTIQLAL", -6.17220, 106.83098, 2, 0);
		helper.insertHalte("JUANDA", -6.16805, 106.83071, 0, 2); // transit
		helper.insertHalte("PECENONGAN", -6.16768, 106.82815, 0, 2); // transit
		helper.insertHalte("BALAIKOTA", -6.18036, 106.82875, 2, 0);
		helper.insertHalte("GAMBIR 2", -6.17944, 106.83126, 2, 0);
		helper.insertHalte("KWITANG", -6.18098, 106.8385, 2, 0); //

		// koridor 3 , rute KALIDERES - PASAR BARU
		Log.v("RouteBuswayActivity","--KORIDOR 3--");
		helper.insertHalte("KALIDERES", -6.15469, 106.70565, 3, 1); // exit
		helper.insertHalte("PESAKIH", -6.15477, 106.71525, 3, 0);
		helper.insertHalte("SUMUR BOR", -6.15303, 106.71917, 3, 0);
		helper.insertHalte("RAWA BUAYA", -6.153933, 106.726333, 3, 0);
		helper.insertHalte("JEMBATAN BARU", -6.15485, 106.73053, 3, 0);
		helper.insertHalte("DISPENDA", -6.15457, 106.73804, 3, 0);
		helper.insertHalte("JEMBATAN GANTUNG", -6.15547, 106.74926, 3, 0);
		helper.insertHalte("TAMAN KOTA", -6.15717, 106.75791, 3, 0);
		helper.insertHalte("INDOSIAR", -6.16347, 106.77536, 0, 2); // transit
		helper.insertHalte("JELAMBAR", -6.16653, 106.78645, 0, 2); // transit
		helper.insertHalte("GROGOL 1", -6.16676, 106.79060, 0, 2); // transit
		helper.insertHalte("RS SUMBER WARAS", -6.16625, 106.79714, 0, 2); // transit
		helper.insertHalte("PASAR BARU", -6.16596, 106.83488, 3, 0); //
		
		// koridor 4 , rute PULOGADUNG KORIDOR 4 - DUKUH ATAS 2
		Log.v("RouteBuswayActivity","--KORIDOR 4--");
//		helper.insertHalte("PULOGADUNG KORIDOR 4", -6.18327, 106.90888, "4", 1); // exit
		helper.insertHalte("PASAR PULOGADUNG", -6.18745, 106.9059, 4, 0);
		helper.insertHalte("TU GAS", -6.1923, 106.90488, 4, 0);
		helper.insertHalte("LAYUR", -6.19349, 106.89902, 4, 0);
		helper.insertHalte("PEMUDA RAWAMANGUN", -6.19346, 106.89167, 4, 0);
		helper.insertHalte("VELODROME", -6.19341, 106.88818, 4, 0);
		helper.insertHalte("SUNAN GIRI", -6.19323, 106.88367, 4, 0);
		helper.insertHalte("UNJ", -6.19286, 106.8802, 4, 0);
		helper.insertHalte("PRAMUKA BPKP", -6.19202, 106.87325, 4, 2); // transit
		helper.insertHalte("PRAMUKA LIA", -6.19218, 106.86857, 4, 0);
		helper.insertHalte("UTAN KAYU", -6.19264, 106.86463, 4, 0);
		helper.insertHalte("PASAR GENJING", -6.19432, 106.86095, 4, 0);
		helper.insertHalte("MATRAMAN 2", -6.19901, 106.85431, 4, 2); // transit
		helper.insertHalte("MANGGARAI", -6.20893, 106.84752, 4, 0);
		helper.insertHalte("PASAR RUMPUT", -6.20709, 106.84104, 4, 0);
		helper.insertHalte("HALIMUN", -6.20511, 106.8334, 0, 2); // transit
		helper.insertHalte("DUKUH ATAS 2", -6.20422, 106.82316, 0, 2); // transit

		// koridor 5 , rute ANCOL - KAMPUNG MELAYU
		Log.v("RouteBuswayActivity","--KORIDOR 5--");
		helper.insertHalte("ANCOL", -6.12756, 106.83040, 5, 1); // exit
		helper.insertHalte("PADEMANGAN", -6.13366, 106.83165, 5, 0);
		helper.insertHalte("GUNUNG SAHARI MANGGA DUA", -6.1372, 106.83247, 0, 2);
		helper.insertHalte("JEMBATAN MERAH", -6.14878, 106.83467, 0, 2);
		helper.insertHalte("PASAR BARU TIMUR", -6.16245, 106.83818, 5, 0);
		helper.insertHalte("BUDI UTOMO", -6.16599, 106.83903, 5, 0);
		helper.insertHalte("SENEN SENTRAL", -6.17793, 106.84209, 5, 2); // transit
		helper.insertHalte("PAL PUTIH", -6.18442, 106.84388, 5, 0);
		helper.insertHalte("KRAMAT SENTIONG", -6.18820, 106.84606, 5, 0);
		helper.insertHalte("SALEMBA UI", -6.19364, 106.84903, 5, 0);
		helper.insertHalte("SALEMBA CAROLUS", -6.196844, 106.851111, 5, 0);
		helper.insertHalte("MATRAMAN 1", -6.199678, 106.85415, 5, 2); // transit
		helper.insertHalte("TEGALAN", -6.20295, 106.85705, 5, 0);
		helper.insertHalte("SLAMET RIYADI", -6.20856, 106.85924, 5, 0);
		helper.insertHalte("KEBON PALA", -6.21317, 106.86131, 5, 0);
		helper.insertHalte("PASAR JATINEGARA", -6.21566, 106.86624, 0, 2); // transit
		helper.insertHalte("KAMPUNG MELAYU", -6.2248, 106.86672, 0, 2); // transit

		// koridor 6 , rute RAGUNAN - DUKUH ATAS 2
		Log.v("RouteBuswayActivity","--KORIDOR 6--");
		helper.insertHalte("RAGUNAN", -6.30596, 106.82002, 6, 1); // exit
		helper.insertHalte("DEPARTEMEN PERTAHANAN", -6.29446, 106.82217, 6, 0);
		helper.insertHalte("SMK 57", -6.29121, 106.82353, 6, 0);
		helper.insertHalte("JATI PADANG", -6.28550, 106.82622, 6, 0);
		helper.insertHalte("PEJATEN", -6.27831, 106.82979, 6, 0);
		helper.insertHalte("BUNCIT INDAH", -6.27441, 106.8303, 6, 0);
		helper.insertHalte("IMIGRASI", -6.26225, 106.82966, 6, 0);
		helper.insertHalte("WARUNG JATI", -6.25671, 106.82799, 6, 0);
		helper.insertHalte("DUREN TIGA", -6.25233, 106.82692, 6, 0);
		helper.insertHalte("MAMPANG PRAPATAN", -6.24177, 106.82572, 6, 0);
		helper.insertHalte("KUNINGAN TIMUR", -6.23588, 106.82791, 6, 2); // transit
		helper.insertHalte("PATRA KUNINGAN", -6.23351, 106.8313, 6, 0);
		helper.insertHalte("DEPKES", -6.22856, 106.83319, 6, 0);
		helper.insertHalte("GOR SUMANTRI", -6.22057, 106.83195, 6, 0);
		helper.insertHalte("KARET KUNINGAN", -6.21729, 106.8309, 6, 0);
		helper.insertHalte("KUNINGAN MADYA", -6.21235, 106.83042, 6, 0);
		helper.insertHalte("SETIABUDI UTARA", -6.20801, 106.82983, 6, 0);
		helper.insertHalte("LATUHARHARI", -6.20270, 106.82776, 6, 0); //
		
		// koridor 7, rute KAMPUNG RAMBUTAN - KAMPUNG MELAYU
		Log.v("RouteBuswayActivity","--KORIDOR 7--");
		helper.insertHalte("KAMPUNG RAMBUTAN", -6.308917, 106.881803, 7, 1); // exit
		helper.insertHalte("TANAH MERDEKA", -6.30821, 106.87424, 7, 0);
		helper.insertHalte("FLY OVER RAYA BOGOR", -6.30655, 106.86558, 7, 0);
		helper.insertHalte("RS HARAPAN BUNDA", -6.30192, 106.86809, 7, 0);
		helper.insertHalte("PASAR INDUK", -6.29432, 106.87205, 7, 0);
		helper.insertHalte("PASAR KRAMAT JATI", -6.26891, 106.86669, 0, 0); //  transit
		helper.insertHalte("PGC CILILITAN", -6.26235, 106.86652, 0, 0); //  transit
		helper.insertHalte("BKN", -6.25783, 106.86992, 0, 2); // transit);
		helper.insertHalte("CAWANG UKI", -6.25035, 106.87359, 0, 2); // transit);
		helper.insertHalte("BNN", -6.24615, 106.8721, 0, 2); // transit);
		helper.insertHalte("CAWANG OTISTA", -6.24361, 106.86876, 7, 0);
		helper.insertHalte("GELANGGANG REMAJA", -6.2355, 106.86791, 7, 0);
		helper.insertHalte("BIDARA CINA", -6.22971, 106.86716, 7, 0); //

		// koridor 8 , rute LEBAK BULUS - HARMONI
		Log.v("RouteBuswayActivity","--KORIDOR 8--");
		helper.insertHalte("LEBAK BULUS", -6.289331, 106.774242, 8, 1); // exit
		helper.insertHalte("PONDOK PINANG", -6.28218, 106.77208, 8, 0);
		helper.insertHalte("PONDOK INDAH 1", -6.28732, 106.77951, 8, 0);
		helper.insertHalte("PONDOK INDAH 2", -6.26726, 106.78360, 8, 0);
		helper.insertHalte("TANAH KUSIR KODAM", -6.256947, 106.781744, 8, 0);
		helper.insertHalte("KEBAYORAN LAMA BUNGUR", -6.2527339, 106.7816504, 8, 0);
		helper.insertHalte("PASAR KEBAYORAN LAMA", -6.23845, 106.78321, 8, 0);
		helper.insertHalte("SIMPRUG", -6.233914, 106.786683, 8, 0);
		helper.insertHalte("PERMATA HIJAU", -6.2214523, 106.7832916, 8, 0);
		helper.insertHalte("PERMATA HIJAU RS MEDIKA", -6.218510, 106.77773, 8, 0);
		helper.insertHalte("POS PENGUMBEN", -6.2129134, 106.7722694, 8, 0);
		helper.insertHalte("KELAPA DUA SASAK", -6.2055522, 106.769554, 8, 0);
		helper.insertHalte("KEBON JERUK", -6.1942839, 106.7688949, 8, 0);
		helper.insertHalte("DURI KEPA", -6.18535, 106.76845, 8, 0);
		helper.insertHalte("KEDOYA ASSIDDIQIYAH", -6.17453, 106.76589, 8, 0);
		helper.insertHalte("KEDOYA GREEN GARDEN", -6.1644374, 106.7629993, 8, 0); // 
		
		// koridor 9 , rute PLUIT - PINANG RANTI
		Log.v("RouteBuswayActivity","--KORIDOR 9--");
		helper.insertHalte("PLUIT", -6.11782, 106.79189, 0, 1); // exit
		helper.insertHalte("PENJARINGAN", -6.12626, 106.79204, 0, 2); // transit
		helper.insertHalte("JEMBATAN TIGA", -6.13333, 106.79273, 0, 2); // transit
		helper.insertHalte("JEMBATAN DUA", -6.1432875, 106.7935255, 9, 0);
		helper.insertHalte("JEMBATAN BESI", -6.15201, 106.79494, 9, 0);
		helper.insertHalte("LATUMENTEN STASIUN GROGOL", -6.16106, 106.79041, 9, 0);
		helper.insertHalte("GROGOL 2", -6.16726, 106.7878, 9, 2); // transit
		helper.insertHalte("S PARMAN CENTRAL PARK", -6.17604, 106.79307, 9, 0);
		helper.insertHalte("RS HARAPAN KITA", -6.18511, 106.79689, 9, 0);
		helper.insertHalte("SLIPI KEMANGGISAN", -6.1900928, 106.7969852, 9, 0);
		helper.insertHalte("SLIPI PETAMBURAN", -6.20198, 106.79997, 9, 0);
		helper.insertHalte("SENAYAN JCC", -6.21406, 106.80872, 9, 0);
		helper.insertHalte("SEMANGGI", -6.22057, 106.81316, 9, 2); // transit
		helper.insertHalte("GATOT SUBROTO LIPI", -6.22677, 106.8174, 9, 0);
		helper.insertHalte("GATOT SUBROTO JAMSOSTEK", -6.23274, 106.82151, 9, 0);
		helper.insertHalte("KUNINGAN BARAT", -6.23674, 106.82701, 9, 2); // transit
		helper.insertHalte("TEGAL PARANG", -6.23925, 106.83109, 9, 0);
		helper.insertHalte("PANCORAN BARAT", -6.24158, 106.83759, 9, 0);
		helper.insertHalte("PANCORAN TUGU", -6.24338, 106.844707, 9, 0);
		helper.insertHalte("TEBET BKPM", -6.24325, 106.85088, 9, 0);
		helper.insertHalte("CIKOKO", -6.24319, 106.858143, 9, 0);
		helper.insertHalte("CAWANG CILIWUNG", -6.24312, 106.86300, 9, 0);
		helper.insertHalte("GARUDA TAMAN MINI", -6.2902006, 106.8811523, 9, 0);
		helper.insertHalte("PINANG RANTI", -6.29086, 106.88655, 9, 1); // exit
		
		Log.v("RouteBuswayActivity","--KORIDOR 10--");
		// koridor 10 , rute TANJUNG PRIOK - PGC
		helper.insertHalte("TANJUNG PRIOK", -6.10973, 106.88175, 0, 1); // exit
		helper.insertHalte("ENGGANO", -6.1100935, 106.8924393, 0, 2);
		helper.insertHalte("KOJA", -6.11390, 106.89314, 0, 2);
		helper.insertHalte("WALIKOTA JAKARTA UTARA", -6.1188329, 106.893433, 0, 2);
		helper.insertHalte("PLUMPANG PERTAMINA", -6.13057, 106.89356, 0, 2);
		helper.insertHalte("SUNTER KELAPA GADING", -6.1427902, 106.8910149, 0, 2);
		helper.insertHalte("YOS SUDARSO KODAMAR", -6.1525776, 106.8872962, 10, 0);
		helper.insertHalte("CEMPAKA MAS 2", -6.16618, 106.87910, 10, 2); // transit 		-6.1620814, 106.8817701
		helper.insertHalte("CEMPAKA PUTIH", -6.17439, 106.87642, 10, 0);
		helper.insertHalte("PULOMAS SELATAN", -6.17731, 106.87596, 10, 0);
		helper.insertHalte("RAWASARI", -6.1872579, 106.8755846, 10, 0);
		helper.insertHalte("PRAMUKA BPKP 2", -6.19277, 106.87500, 10, 2); // transit
		helper.insertHalte("RAWA MANGUN", -6.1974176, 106.8741671, 10, 0);
		helper.insertHalte("BEA CUKAI", -6.20629, 106.87378, 10, 0);
		helper.insertHalte("STASIUN JATINEGARA", -6.21549, 106.87415, 10, 2); // transit
		helper.insertHalte("PEDATI", -6.2237396, 106.8748285, 10, 0);
		helper.insertHalte("KEBON NANAS CIPINANG", -6.2314283, 106.8761048, 10, 0);
		helper.insertHalte("KALIMALANG", -6.23917, 106.87807, 10, 0);
		helper.insertHalte("CAWANG SUTOYO", -6.24482, 106.87592, 10, 0);
		helper.insertHalte("PGC", -6.26311, 106.86547, 10, 1); // exit
		
		Log.v("RouteBuswayActivity","--KORIDOR 11--");
		helper.insertHalte("PULOGEBANG", -6.20790, 106.95653, 11, 1); // exit
		helper.insertHalte("WALIKOTA JAKARTA TIMUR", -6.21218, 106.94633, 11, 0);
		helper.insertHalte("PENGGILINGAN", -6.21572, 106.93936, 11, 0);
		helper.insertHalte("PERUMNAS KLENDER", -6.21646, 106.92901, 11, 0);
		helper.insertHalte("FLYOVER RADEN INTEN", -6.21608, 106.92509, 11, 0);
		helper.insertHalte("BUARAN", -6.21476, 106.91474, 11, 0);
		helper.insertHalte("KAMPUNG SUMUR", -6.21418057, 106.91004775, 11, 0);
		helper.insertHalte("FLYOVER KLENDER", -6.21359595, 106.90214798, 11, 0);
		helper.insertHalte("STASIUN KLENDER", -6.21355795, 106.89934306, 11, 0);
		helper.insertHalte("CIPINANG", -6.21451121, 106.88525878, 11, 0);
		helper.insertHalte("IMIGRASI JAKARTA TIMUR", -6.21465919, 106.88185908, 11, 0);
		helper.insertHalte("PASAR ENJO", -6.21487251, 106.87776133, 11, 0);
		helper.insertHalte("FLYOVER JATINEGARA", -6.21513449, 106.87407196, 11, 2);
		helper.insertHalte("STASIUN JATINEGARA 2", -6.21561645, 106.86782174, 11, 2);
		helper.insertHalte("JATINEGARA RS. PREMIER", -6.22135, 106.86820, 11, 0);
//		helper.insertHalte("KAMPUNG MELAYU", -6.2248, 106.86672, 11, 2); // transit
		
		Log.v("RouteBuswayActivity","--KORIDOR 12--");
//		helper.insertHalte("PLUIT", -6.11782, 106.79189, 12, 1); // exit
//		helper.insertHalte("PENJARINGAN", -6.12626, 106.79204, 12, 0);
		helper.insertHalte("BANDENGAN PEKOJAN", -6.13667, 106.79891, 12, 0);
		helper.insertHalte("KALI BESAR BARAT", -6.13506, 106.81164, 12, 0);
//		helper.insertHalte("KOTA", -6.13779, 106.81368, 9, 2); // transit
		helper.insertHalte("PANGERAN JAYAKARTA", -6.13763, 106.81751, 12, 2);
		helper.insertHalte("MANGGA DUA", -6.13618, 106.82578, 12, 0);
//		helper.insertHalte("GUNUNG SAHARI MANGGA DUA", -6.1372, 106.83247, 12, 2);	// transit
//		helper.insertHalte("JEMBATAN MERAH", -6.14878, 106.83467, 12, 2);	// transit
		helper.insertHalte("KEMAYORAN LANDAS PACU TIMUR", -6.152155, 106.85405, 12, 0);
		helper.insertHalte("DANAU SUNTER BARAT", -6.14628, 106.85805, 12, 0);
		helper.insertHalte("SUNTER SMP 140", -6.13963, 106.85966, 12, 0);
		helper.insertHalte("SUNTER KARYA", -6.13770, 106.87005, 12, 0);
		helper.insertHalte("SUNTER BOULEVARD BARAT", -6.15011, 106.88885, 12, 0);
//		helper.insertHalte("SUNTER KELAPA GADING", -6.1427902, 106.8910149, 12, 0);
//		helper.insertHalte("PLUMPANG PERTAMINA", -6.13057, 106.89356, 12, 0);
//		helper.insertHalte("WALIKOTA JAKARTA UTARA", -6.1188329, 106.893433, 12, 0);
//		helper.insertHalte("KOJA", -6.11390, 106.89314, 12, 0);
//		helper.insertHalte("ENGGANO", -6.1100935, 106.8924393, 12, 0);
//		helper.insertHalte("TANJUNG PRIOK", -6.10973, 106.88175, 12, 1); // exit
		
		//---------------------------------------------------------------------------
//		helper.insertHalte("PULOGADUNG KORIDOR 4", -6.18327, 106.90888, 4, 1); // exit
		
		Log.v("BuswayMaps","----KETERANGAN------");
		// keterangan
		Log.v("RouteBuswayActivity","BLOK M = 1"); // exit
		Log.v("RouteBuswayActivity","MASJID AGUNG = 2");
		Log.v("RouteBuswayActivity","BUNDARAN SENAYAN = 3");
		Log.v("RouteBuswayActivity","GELORA BUNG KARNO = 4");
		Log.v("RouteBuswayActivity","POLDA METRO JAYA = 5");
		Log.v("RouteBuswayActivity","BENDUNGAN HILIR = 6"); // transit
		Log.v("RouteBuswayActivity","KARET = 7");
		Log.v("RouteBuswayActivity","SETIABUDI = 8");
		Log.v("RouteBuswayActivity","DUKUH ATAS 1 = 9"); // transit
		Log.v("RouteBuswayActivity","TOSARI = 10");
		Log.v("RouteBuswayActivity","BUNDARAN HI = 11");
		Log.v("RouteBuswayActivity","SARINAH = 12");
		Log.v("RouteBuswayActivity","BANK INDONESIA = 13");
		Log.v("RouteBuswayActivity","MONAS = 14"); // transit
		Log.v("RouteBuswayActivity","HARMONI SENTRAL = 15"); // transit
		Log.v("RouteBuswayActivity","SAWAH BESAR = 16");
		Log.v("RouteBuswayActivity","MANGGA BESAR = 17");
		Log.v("RouteBuswayActivity","OLIMO = 18");
		Log.v("RouteBuswayActivity","GLODOK = 19");
		Log.v("RouteBuswayActivity","KOTA = 20"); // exit

		// koridor 2 , rute PULOGADUNG - HARMONI
		Log.v("RouteBuswayActivity","PULOGADUNG KORIDOR 2 = 21"); // exit
		Log.v("RouteBuswayActivity","BERMIS = 22");
		Log.v("RouteBuswayActivity","PULOMAS = 23");
		Log.v("RouteBuswayActivity","ASMI = 24");
		Log.v("RouteBuswayActivity","PENDONGKELAN = 25");
		Log.v("RouteBuswayActivity","CEMPAKA TIMUR = 26"); // transit
		Log.v("RouteBuswayActivity","RS ISLAM = 27");
		Log.v("RouteBuswayActivity","CEMPAKA TENGAH = 28");
		Log.v("RouteBuswayActivity","PASAR CEMPAKA PUTIH = 29");
		Log.v("RouteBuswayActivity","RAWA SELATAN = 30");
		Log.v("RouteBuswayActivity","GALUR = 31");
		Log.v("RouteBuswayActivity","SENEN = 32"); // transit
		Log.v("RouteBuswayActivity","ATRIUM SENEN = 33");
		Log.v("RouteBuswayActivity","RSPAD = 34");
		Log.v("RouteBuswayActivity","DEPLU = 35");
		Log.v("RouteBuswayActivity","GAMBIR 1 = 36");
		Log.v("RouteBuswayActivity","ISTIQLAL = 37");
		Log.v("RouteBuswayActivity","JUANDA = 38"); // transit
		Log.v("RouteBuswayActivity","PECENONGAN = 39"); // transit
		Log.v("RouteBuswayActivity","BALAIKOTA = 40");
		Log.v("RouteBuswayActivity","GAMBIR 2 = 41");
		Log.v("RouteBuswayActivity","KWITANG = 42");

		// koridor 3 , rute KALIDERES - PASAR BARU
		Log.v("RouteBuswayActivity","KALIDERES = 43"); // exit
		Log.v("RouteBuswayActivity","PESAKIH = 44");
		Log.v("RouteBuswayActivity","SUMUR BOR = 45");
		Log.v("RouteBuswayActivity","RAWA BUAYA = 46");
		Log.v("RouteBuswayActivity","JEMBATAN BARU = 47");
		Log.v("RouteBuswayActivity","DISPENDA = 48");
		Log.v("RouteBuswayActivity","JEMBATAN GANTUNG = 49");
		Log.v("RouteBuswayActivity","TAMAN KOTA = 50");
		Log.v("RouteBuswayActivity","INDOSIAR = 51"); // transit
		Log.v("RouteBuswayActivity","JELAMBAR = 52"); // transit
		Log.v("RouteBuswayActivity","GROGOL 1 = 53"); // transit
		Log.v("RouteBuswayActivity","RS SUMBER WARAS = 54"); // transit
		Log.v("RouteBuswayActivity","PASAR BARU = 55");

		// koridor 4 , rute PULogADUNG - DUKUH ATAS 2
		Log.v("RouteBuswayActivity","PASAR PULOGADUNG = 56");
		Log.v("RouteBuswayActivity","TU GAS = 57");
		Log.v("RouteBuswayActivity","LAYUR = 58");
		Log.v("RouteBuswayActivity","PEMUDA RAWAMANGUN = 59");
		Log.v("RouteBuswayActivity","VELODROME = 60");
		Log.v("RouteBuswayActivity","SUNAN GIRI = 61");
		Log.v("RouteBuswayActivity","UNJ = 62");
		Log.v("RouteBuswayActivity","PRAMUKA BPKP = 63"); // transit
		Log.v("RouteBuswayActivity","PRAMUKA LIA = 64");
		Log.v("RouteBuswayActivity","UTAN KAYU = 65");
		Log.v("RouteBuswayActivity","PASAR GENJING  = 66");
		Log.v("RouteBuswayActivity","MATRAMAN 2 = 67"); // transit
		Log.v("RouteBuswayActivity","MANGGARAI = 68");
		Log.v("RouteBuswayActivity","PASAR RUMPUT = 69");
		Log.v("RouteBuswayActivity","HALIMUN = 70"); // transit
		Log.v("RouteBuswayActivity","DUKUH ATAS 2 = 71"); // transit

		// koridor 5 , rute ANCOL - KAMPUNG MELAYU
		Log.v("RouteBuswayActivity","ANCOL = 72"); // exit
		Log.v("RouteBuswayActivity","PADEMANGAN = 73");
		Log.v("RouteBuswayActivity","GUNUNG SAHARI MANGGA DUA = 74");
		Log.v("RouteBuswayActivity","JEMBATAN MERAH = 75");
		Log.v("RouteBuswayActivity","PASAR BARU TIMUR = 76");
		Log.v("RouteBuswayActivity","BUDI UTOMO = 77");
		Log.v("RouteBuswayActivity","SENEN SENTRAL = 78"); // transit
		Log.v("RouteBuswayActivity","PAL PUTIH = 79");
		Log.v("RouteBuswayActivity","KRAMAT SENTIONG = 80");
		Log.v("RouteBuswayActivity","SALEMBA UI = 81");
		Log.v("RouteBuswayActivity","SALEMBA CAROLUS = 82");
		Log.v("RouteBuswayActivity","MATRAMAN 1 = 83"); // transit
		Log.v("RouteBuswayActivity","TEGALAN = 84");
		Log.v("RouteBuswayActivity","SLAMET RIYADI = 85");
		Log.v("RouteBuswayActivity","KEBON PALA = 86");
		Log.v("RouteBuswayActivity","PASAR JATINEGARA = 87"); // transit
		Log.v("RouteBuswayActivity","KAMPUNG MELAYU = 88"); // transit

		// koridor 6 , rute RAGUNAN - DUKUH ATAS 2
		Log.v("RouteBuswayActivity","RAGUNAN = 89"); // exit
		Log.v("RouteBuswayActivity","DEPARTEMEN PERTAHANAN = 90");
		Log.v("RouteBuswayActivity","SMK 57 = 91");
		Log.v("RouteBuswayActivity","JATI PADANG = 92");
		Log.v("RouteBuswayActivity","PEJATEN = 93");
		Log.v("RouteBuswayActivity","BUNCIT INDAH = 94");
		Log.v("RouteBuswayActivity","IMIGRASI = 95");
		Log.v("RouteBuswayActivity","WARUNG JATI = 96");
		Log.v("RouteBuswayActivity","DUREN TIGA = 97");
		Log.v("RouteBuswayActivity","MAMPANG PRAPATAN = 98");
		Log.v("RouteBuswayActivity","KUNINGAN TIMUR = 99"); // transit
		Log.v("RouteBuswayActivity","PATRA KUNINGAN = 100");
		Log.v("RouteBuswayActivity","DEPKES = 101");
		Log.v("RouteBuswayActivity","GOR SUMANTRI = 102");
		Log.v("RouteBuswayActivity","KARET KUNINGAN = 103");
		Log.v("RouteBuswayActivity","KUNINGAN MADYA = 104");
		Log.v("RouteBuswayActivity","SETIABUDI UTARA = 105");
		Log.v("RouteBuswayActivity","LATUHARHARI = 106");
		
		// koridor 7, rute KAMPUNG RAMBUTAN - KAMPUNG MELAYU
		Log.v("RouteBuswayActivity","KAMPUNG RAMBUTAN = 107"); // exit
		Log.v("RouteBuswayActivity","TANAH MERDEKA = 108");
		Log.v("RouteBuswayActivity","FLY OVER RAYA BOGOR = 109");
		Log.v("RouteBuswayActivity","RS HARAPAN BUNDA = 110");
		Log.v("RouteBuswayActivity","PASAR INDUK = 111");
		Log.v("RouteBuswayActivity","PASAR KRAMAT JATI = 112");
		Log.v("RouteBuswayActivity","PGC CILILITAN = 113");
		Log.v("RouteBuswayActivity","BKN = 114"); // transit);
		Log.v("RouteBuswayActivity","CAWANG UKI = 115"); // transit);
		Log.v("RouteBuswayActivity","BNN = 116"); // transit);
		Log.v("RouteBuswayActivity","CAWANG OTISTA = 117");
		Log.v("RouteBuswayActivity","GELANGGANG REMAJA = 118");
		Log.v("RouteBuswayActivity","BIDARA CINA = 119");

		// koridor 8 , rute LEBAK BULUS - HARMONI
		Log.v("RouteBuswayActivity","LEBAK BULUS = 120"); // exit
		Log.v("RouteBuswayActivity","PONDOK PINANG = 121");
		Log.v("RouteBuswayActivity","PONDOK INDAH 1 = 122");
		Log.v("RouteBuswayActivity","PONDOK INDAH 2 = 123");
		Log.v("RouteBuswayActivity","TANAH KUSIR KODAM = 124");
		Log.v("RouteBuswayActivity","KEBAYORAN LAMA BUNGUR = 125");
		Log.v("RouteBuswayActivity","PASAR KEBAYORAN LAMA = 126");
		Log.v("RouteBuswayActivity","SIMPRUG = 127");
		Log.v("RouteBuswayActivity","PERMATA HIJAU = 128");
		Log.v("RouteBuswayActivity","PERMATA HIJAU RS MEDIKA = 129");
		Log.v("RouteBuswayActivity","POS PENGUMBEN = 130");
		Log.v("RouteBuswayActivity","KELAPA DUA SASAK = 131");
		Log.v("RouteBuswayActivity","KEBON JERUK = 132");
		Log.v("RouteBuswayActivity","DURI KEPA = 133");
		Log.v("RouteBuswayActivity","KEDOYA ASSIDDIQIYAH = 134");
		Log.v("RouteBuswayActivity","KEDOYA GREEN GARDEN = 135");
		
		// koridor 9 , rute PLUIT - PINANG RANTI
		Log.v("RouteBuswayActivity","PLUIT = 136"); // exit
		Log.v("RouteBuswayActivity","PENJARINGAN = 137");
		Log.v("RouteBuswayActivity","JEMBATAN TIGA = 138");
		Log.v("RouteBuswayActivity","JEMBATAN DUA = 139");
		Log.v("RouteBuswayActivity","JEMBATAN BESI = 140");
		Log.v("RouteBuswayActivity","LATUMENTEN STASIUN GROGOL = 141");
		Log.v("RouteBuswayActivity","GROGOL 2 = 142"); // transit
		Log.v("RouteBuswayActivity","S PARMAN CENTRAL PARK = 143");
		Log.v("RouteBuswayActivity","RS HARAPAN KITA = 144");
		Log.v("RouteBuswayActivity","SLIPI KEMANGGISAN = 145");
		Log.v("RouteBuswayActivity","SLIPI PETAMBURAN = 146");
		Log.v("RouteBuswayActivity","SENAYAN JCC = 147");
		Log.v("RouteBuswayActivity","SEMANGGI = 148"); // transit
		Log.v("RouteBuswayActivity","GATOT SUBROTO LIPI = 149");
		Log.v("RouteBuswayActivity","GATOT SUBROTO JAMSOSTEK = 150");
		Log.v("RouteBuswayActivity","KUNINGAN BARAT = 151"); // transit
		Log.v("RouteBuswayActivity","TEGAL PARANG = 152");
		Log.v("RouteBuswayActivity","PANCORAN BARAT = 153");
		Log.v("RouteBuswayActivity","PANCORAN TUGU = 154");
		Log.v("RouteBuswayActivity","TEBET BKPM = 155");
		Log.v("RouteBuswayActivity","CIKOKO = 156");
		Log.v("RouteBuswayActivity","CAWANG CILIWUNG = 157");
		Log.v("RouteBuswayActivity","GARUDA TAMAN MINI = 158");
		Log.v("RouteBuswayActivity","PINANG RANTI = 159"); // exit	
		
		// koridor 10 , rute TANJUNG PRIOK - PGC
		Log.v("RouteBuswayActivity","TANJUNG PRIOK = 160"); // exit
		Log.v("RouteBuswayActivity","ENGGANO = 161");
		Log.v("RouteBuswayActivity","KOJA = 162");
		Log.v("RouteBuswayActivity","WALIKOTA JAKARTA UTARA = 163");
		Log.v("RouteBuswayActivity","PLUMPANG PERTAMINA = 164");
		Log.v("RouteBuswayActivity","SUNTER KELAPA GADING = 165");
		Log.v("RouteBuswayActivity","YOS SUDARSO KODAMAR = 166");
		Log.v("RouteBuswayActivity","CEMPAKA MAS 2 = 167"); // transit
		Log.v("RouteBuswayActivity","CEMPAKA PUTIH = 168");
		Log.v("RouteBuswayActivity","PULOMAS SELATAN = 169");
		Log.v("RouteBuswayActivity","RAWASARI = 170");
		Log.v("RouteBuswayActivity","PRAMUKA BPKP 2 = 171"); // transit
		Log.v("RouteBuswayActivity","RAWA MANGUN = 172");
		Log.v("RouteBuswayActivity","BEA CUKAI = 173");
		Log.v("RouteBuswayActivity","STASIUN JATINEGARA = 174"); // transit
		Log.v("RouteBuswayActivity","PEDATI = 175");
		Log.v("RouteBuswayActivity","KEBON NANAS CIPINANG = 176");
		Log.v("RouteBuswayActivity","KALIMALANG = 177");
		Log.v("RouteBuswayActivity","CAWANG SUTOYO = 178");
		Log.v("RouteBuswayActivity","PGC = 179"); // exit
		
		// koridor 11 , rute PULOGEBANG - KAMPUNG MELAYU
		Log.v("RouteBuswayActivity","PULOGEBANG = 180"); // exit
		Log.v("RouteBuswayActivity","WALIKOTA JAKARTA TIMUR = 181");
		Log.v("RouteBuswayActivity","PENGGILINGAN = 182");
		Log.v("RouteBuswayActivity","PERUMNAS KLENDER = 183");
		Log.v("RouteBuswayActivity","FLYOVER RADEN INTEN = 184");
		Log.v("RouteBuswayActivity","BUARAN = 185");
		Log.v("RouteBuswayActivity","KAMPUNG SUMUR = 186");
		Log.v("RouteBuswayActivity","FLYOVER KLENDER = 187");
		Log.v("RouteBuswayActivity","STASIUN KLENDER = 188");
		Log.v("RouteBuswayActivity","CIPINANG = 189");
		Log.v("RouteBuswayActivity","IMIGRASI JAKARTA TIMUR = 190");
		Log.v("RouteBuswayActivity","PASAR ENJO = 191");
		Log.v("RouteBuswayActivity","FLYOVER JATINEGARA = 192");
		Log.v("RouteBuswayActivity","STASIUN JATINEGARA 2 = 193");
		Log.v("RouteBuswayActivity","JATINEGARA RS. PREMIER = 194");
		
		// koridor 12 , rute PLUIT - TANJUNG PRIOK
		Log.v("RouteBuswayActivity","BANDENGAN PEKOJAN = 195"); // exit
		Log.v("RouteBuswayActivity","KALI BESAR BARAT = 196");
		Log.v("RouteBuswayActivity","PANGERAN JAYAKARTA = 197");
		Log.v("RouteBuswayActivity","MANGGA DUA = 198");
		Log.v("RouteBuswayActivity","KEMAYORAN LANDAS PACU TIMUR = 199");
		Log.v("RouteBuswayActivity","DANAU SUNTER BARAT = 200");
		Log.v("RouteBuswayActivity","SUNTER SMP 140 = 201");
		Log.v("RouteBuswayActivity","SUNTER KARYA = 202");
		Log.v("RouteBuswayActivity","SUNTER BOULEVARD BARAT = 203");
		
//		Log.v("RouteBuswayActivity","PULOGADUNG KORIDOR 4 = 180"); // exit
	}
	
	private void inputRuteKoridor1BlokMKota()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 1--");
		
		// koridor 1 , rute blok m - kota
		helper.insertHomeAndNeighbour(1, 2); // "BLOK M", "MASJID AGUNG", -6.24349, 106.80061, "1", 1); // exit
		helper.insertHomeAndNeighbour(2, 3); // "MASJID AGUNG", "BUNDARAN SENAYAN", -6.235034,	106.798418, "1", 0);
		helper.insertHomeAndNeighbour(3, 4); // "BUNDARAN SENAYAN", "GELORA BUNG KARNO", -6.227496, 106.801435, "1", 0);
		helper.insertHomeAndNeighbour(4, 5); // "GELORA BUNG KARNO", "POLDA METRO JAYA", -6.224217, 106.805785, "1", 0);
		helper.insertHomeAndNeighbour(5, 6); // "POLDA METRO JAYA", "BENDUNGAN HILIR", -6.221535,	106.809633, "1", 0);
		helper.insertHomeAndNeighbour(6, 7); // "BENDUNGAN HILIR", "KARET", -6.217058, 106.815320, "1", 2); // transit
		helper.insertHomeAndNeighbour(7, 8); // "KARET", "SETIABUDI", -6.214562, 106.818425, "1", 0);
		helper.insertHomeAndNeighbour(8, 9); // "SETIABUDI", "DUKUH ATAS 1", -6.210257, 106.821055, "1", 0);
		helper.insertHomeAndNeighbour(9, 10); // "DUKUH ATAS 1", "TOSARI", -6.20585, 106.82222, "1", 2); // transit
		helper.insertHomeAndNeighbour(10, 11); // "TOSARI", "BUNDARAN HI", -6.198650, 106.823134, "1", 0);
		helper.insertHomeAndNeighbour(11, 12); // "BUNDARAN HI", "SARINAH", -6.193468, 106.822961, "1", 0);
		helper.insertHomeAndNeighbour(12, 13); // "SARINAH", "BANK INDONESIA",-6.18797, 106.82297, "1", 0);
		helper.insertHomeAndNeighbour(13, 14); // "BANK INDONESIA", "MONAS", -6.182719, 106.822929, "1", 0);
		helper.insertHomeAndNeighbour(14, 15); // "MONAS", "HARMONI SENTRAL", -6.175542, 106.822764, "1", 2); // transit
		helper.insertHomeAndNeighbour(15, 16); // "HARMONI SENTRAL", "SAWAH BESAR", -6.16581, 106.82048, "1", 2); // transit
		helper.insertHomeAndNeighbour(16, 17); // "SAWAH BESAR", "MANGGA BESAR", -6.160279, 106.819165, "1", 0);
		helper.insertHomeAndNeighbour(17, 18); // "MANGGA BESAR", "OLIMO", -6.151947, 106.817305, "1", 0);
		helper.insertHomeAndNeighbour(18, 19); // "OLIMO", "GLODOK", -6.149136, 106.816626, "1", 0);
		helper.insertHomeAndNeighbour(19, 20); // "GLODOK", "KOTA", -6.144697, 106.815455, "1", 0);
//		helper.insertHomeAndNeighbour(20, 0); // "KOTA", "",-6.13779, 106.81368, "1", 1); // exit
		
		// koridor 1 , rute kota - blok m
		helper.insertHomeAndNeighbour(20, 19); // "KOTA", "GLODOK", -6.13779, 106.81368, "1", 1); // exit
		helper.insertHomeAndNeighbour(19, 18); // "GLODOK", "OLIMO", -6.144697, 106.815455, "1", 0);
		helper.insertHomeAndNeighbour(18, 17); // "OLIMO", "MANGGA BESAR", -6.149136, 106.816626, "1", 0);
		helper.insertHomeAndNeighbour(17, 16); // "MANGGA BESAR", "SAWAH BESAR", -6.151947, 106.817305, "1", 0);
		helper.insertHomeAndNeighbour(16, 15); // "SAWAH BESAR", "HARMONI SENTRAL", -6.160279, 106.819165, "1", 0);
		helper.insertHomeAndNeighbour(15, 14); // "HARMONI SENTRAL", "MONAS", -6.16581, 106.82048, "1", 2); // transit
		helper.insertHomeAndNeighbour(14, 13); // "MONAS", "BANK INDONESIA", -6.175542, 106.822764, "1", 2); // transit
		helper.insertHomeAndNeighbour(13, 12); // "BANK INDONESIA", "SARINAH", -6.182719, 106.822929, "1", 0);
		helper.insertHomeAndNeighbour(12, 11); // "SARINAH", "BUNDARAN HI",-6.18797, 106.82297, "1", 0);
		helper.insertHomeAndNeighbour(11, 10); // "BUNDARAN HI", "TOSARI", -6.193468, 106.822961, "1", 0);
		helper.insertHomeAndNeighbour(10, 9); // "TOSARI", "DUKUH ATAS 1", -6.198650, 106.823134, "1", 0);
		helper.insertHomeAndNeighbour(9, 8); // "DUKUH ATAS 1", "SETIABUDI", -6.20585, 106.82222, "1", 2); // transit
		helper.insertHomeAndNeighbour(8, 7); // "SETIABUDI", "KARET", -6.210257, 106.821055, "1", 0);
		helper.insertHomeAndNeighbour(7, 6); // "KARET", "BENDUNGAN HILIR", -6.214562, 106.818425, "1", 0);
		helper.insertHomeAndNeighbour(6, 5); // "BENDUNGAN HILIR", "POLDA METRO JAYA", -6.217058, 106.815320, "1", 2); // transit
		helper.insertHomeAndNeighbour(5, 4); // "POLDA METRO JAYA", "GELORA BUNG KARNO", -6.221535,	106.809633, "1", 0);
		helper.insertHomeAndNeighbour(4, 3); // "GELORA BUNG KARNO", "BUNDARAN SENAYAN", -6.224217, 106.805785, "1", 0);
		helper.insertHomeAndNeighbour(3, 2); // "BUNDARAN SENAYAN", "MASJID AGUNG", -6.227496, 106.801435, "1", 0);
		helper.insertHomeAndNeighbour(2, 1); // "MASJID AGUNG", "BLOK M", -6.235034,	106.798418, "1", 0);
		helper.insertHomeAndNeighbour(1, 0); // "BLOK M", "", -6.24349, 106.80061, "1", 1); // exit
	}

	private void inputRuteKoridor2PulogadungHarmoni()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 2--");

		// koridor 2 , rute PULOGADUNG KORIDOR 2 - HARMONI
		helper.insertHomeAndNeighbour(21, 22); // "PULOGADUNG KORIDOR 2", "BERMIS", -6.18327, 106.90888, "2", 1); // exit
		helper.insertHomeAndNeighbour(22, 23); // "BERMIS", "PULOMAS", -6.17814, 106.89851, "2", 0);
		helper.insertHomeAndNeighbour(23, 24); // "PULOMAS", "ASMI", -6.17471, 106.89273, "2", 0);
		helper.insertHomeAndNeighbour(24, 25); // "ASMI", "PENDONGKELAN", -6.17175, 106.88875, "2", 0);
		helper.insertHomeAndNeighbour(25, 26); // "PENDONGKELAN", "CEMPAKA TIMUR", -6.16800, 106.88256, "2", 0);
		helper.insertHomeAndNeighbour(26, 27); // "CEMPAKA TIMUR", "RS ISLAM", -6.16649, 106.87598, "2", 2); // transit
		helper.insertHomeAndNeighbour(27, 28); // "RS ISLAM", "CEMPAKA TENGAH", -6.1686, 106.87026, "2", 0);
		helper.insertHomeAndNeighbour(28, 29); // "CEMPAKA TENGAH", "PASAR CEMPAKA PUTIH", -6.17036, 106.86645, "2", 0);
		helper.insertHomeAndNeighbour(29, 30); // "PASAR CEMPAKA PUTIH", "RAWA SELATAN", -6.17231, 106.86226, "2", 0);
		helper.insertHomeAndNeighbour(30, 31); // "RAWA SELATAN", "GALUR", -6.17389, 106.85777, "2", 0);
		helper.insertHomeAndNeighbour(31, 32); // "GALUR", "SENEN", -6.17437, 106.85463, "2", 0);
		helper.insertHomeAndNeighbour(32, 33); // "SENEN", "ATRIUM SENEN", -6.17821, 106.84240, "2", 2); // transit
		helper.insertHomeAndNeighbour(33, 34); // "ATRIUM SENEN", "RSPAD", -6.1772, 106.84091, "2", 0);
		helper.insertHomeAndNeighbour(34, 35); // "RSPAD", "DEPLU", -6.17555, 106.83675, "2", 0);
		helper.insertHomeAndNeighbour(35, 36); // "DEPLU", "GAMBIR 1", -6.1735, 106.8347, "2", 0);
		helper.insertHomeAndNeighbour(36, 37); // "GAMBIR 1", "ISTIQLAL", -6.17426, 106.8303, "2", 0);
		helper.insertHomeAndNeighbour(37, 38); // "ISTIQLAL", "JUANDA", -6.17220, 106.83098, "2", 0);
		helper.insertHomeAndNeighbour(38, 39); // "JUANDA", "PECENONGAN", -6.16805, 106.83071, "2", 2); // transit
		helper.insertHomeAndNeighbour(39, 15); // "PECENONGAN", "HARMONI SENTRAL", -6.16768, 106.82815, "2", 2); // transit
//		helper.insertHomeAndNeighbour(15, 0); // "HARMONI SENTRAL", "", -6.16581, 106.82048, "2", 2); // transit
		
		// koridor 2 , rute HARMONI - PULOGADUNG KORIDOR 2
//		helper.insertHomeAndNeighbour(15, 40); // "HARMONI SENTRAL", "BALAIKOTA", -6.16581, 106.82048, "2", 2); // transit
		helper.insertHomeAndNeighbour(14, 40); // "MONAS", "BALAIKOTA", -6.175542, 106.822764, "2", 2); // transit
		helper.insertHomeAndNeighbour(40, 41); // "BALAIKOTA", "GAMBIR 2", -6.18036, 106.82875, "2", 0);
		helper.insertHomeAndNeighbour(41, 42); // "GAMBIR 2", "KWITANG", -6.17944, 106.83126, "2", 0);
		helper.insertHomeAndNeighbour(42, 32); // "KWITANG", "SENEN", -6.18098, 106.8385, "2", 0);
		helper.insertHomeAndNeighbour(32, 31); // "SENEN", "GALUR", -6.17821, 106.84240, "2", 2); // transit
		helper.insertHomeAndNeighbour(31, 30); // "GALUR", "RAWA SELATAN", -6.17437, 106.85463, "2", 0);
		helper.insertHomeAndNeighbour(30, 29); // "RAWA SELATAN", "PASAR CEMPAKA PUTIH", -6.17389, 106.85777, "2", 0);
		helper.insertHomeAndNeighbour(29, 28); // "PASAR CEMPAKA PUTIH", "CEMPAKA TENGAH", -6.17231, 106.86226, "2", 0);
		helper.insertHomeAndNeighbour(28, 27); // "CEMPAKA TENGAH", "RS ISLAM", -6.17036, 106.86645, "2", 0);
		helper.insertHomeAndNeighbour(27, 26); // "RS ISLAM", "CEMPAKA TIMUR", -6.1686, 106.87026, "2", 0);
		helper.insertHomeAndNeighbour(26, 25); // "CEMPAKA TIMUR", "PENDONGKELAN", -6.16649, 106.87598, "2", 2); // transit
		helper.insertHomeAndNeighbour(25, 24); // "PENDONGKELAN", "ASMI", -6.16800, 106.88256, "2", 0);
		helper.insertHomeAndNeighbour(24, 23); // "ASMI", "PULOMAS", -6.17175, 106.88875, "2", 0);
		helper.insertHomeAndNeighbour(23, 22); // "PULOMAS", "BERMIS", -6.17471, 106.89273, "2", 0);
		helper.insertHomeAndNeighbour(22, 21); // "BERMIS", "PULOGADUNG", -6.17814, 106.89851, "2", 0);
		helper.insertHomeAndNeighbour(21, 0); // "PULOGADUNG KORIDOR 2", "", -6.18327, 106.90888, "2", 1); // exit	
	}
	
	private void inputRuteKoridor3KalideresPasarbaru()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 3--");
		// koridor 3 , rute KALIDERES - PASAR BARU
		helper.insertHomeAndNeighbour(43, 44); // "KALIDERES", "PESAKIH", -6.15469, 106.70565, "3", 1); // exit
		helper.insertHomeAndNeighbour(44, 45); // "PESAKIH", "SUMUR BOR", -6.15477, 106.71525, "3", 0);
		helper.insertHomeAndNeighbour(45, 46); // "SUMUR BOR", "RAWA BUAYA", -6.15303, 106.71917, "3", 0);
		helper.insertHomeAndNeighbour(46, 47); // "RAWA BUAYA", "JEMBATAN BARU", -6.153933, 106.726333, "3", 0);
		helper.insertHomeAndNeighbour(47, 48); // "JEMBATAN BARU", "DISPENDA", -6.15485, 106.73053, "3", 0);
		helper.insertHomeAndNeighbour(48, 49); // "DISPENDA", "JEMBATAN GANTUNG", -6.15457, 106.73804, "3", 0);
		helper.insertHomeAndNeighbour(49, 50); // "JEMBATAN GANTUNG", "TAMAN KOTA", -6.15547, 106.74926, "3", 0);
		helper.insertHomeAndNeighbour(50, 51); // "TAMAN KOTA", "INDOSIAR", -6.15717, 106.75791, "3", 0);
		helper.insertHomeAndNeighbour(51, 52); // "INDOSIAR", "JELAMBAR", -6.16347, 106.77536, "3", 2); // transit
		helper.insertHomeAndNeighbour(52, 53); // "JELAMBAR", "GROGOL 1", -6.16653, 106.78645, "3", 2); // transit
		helper.insertHomeAndNeighbour(53, 54); // "GROGOL 1", "RS SUMBER WARAS", -6.16676, 106.79060, "3", 2); // transit
		helper.insertHomeAndNeighbour(54, 15); // "RS SUMBER WARAS", "HARMONI SENTRAL", -6.16625, 106.79714, "3", 2); // transit
		helper.insertHomeAndNeighbour(15, 39); // "HARMONI SENTRAL", "PECENONGAN", -6.16581, 106.82048, "3", 2); // transit
		helper.insertHomeAndNeighbour(39, 38); // "PECENONGAN", "JUANDA", -6.16768, 106.82815, "3", 2); // transit
		helper.insertHomeAndNeighbour(38, 55); // "JUANDA", "PASAR BARU", -6.16805, 106.83071, "3", 2); // transit

		// koridor 3 , rute PASAR BARU - KALIDERES
		helper.insertHomeAndNeighbour(55, 38); // "PASAR BARU", "JUANDA", -6.16596, 106.83488, "3", 0);
//		helper.insertHomeAndNeighbour(38, 39); // "JUANDA", "PECENONGAN", -6.16805, 106.83071, "3", 2); // transit
//		helper.insertHomeAndNeighbour(39, 15); // "PECENONGAN", "HARMONI SENTRAL", -6.16768, 106.82815, "3", 2); // transit
		helper.insertHomeAndNeighbour(15, 54); // "HARMONI SENTRAL", "RS SUMBER WARAS", -6.16581, 106.82048, "3", 2); // transit
		helper.insertHomeAndNeighbour(54, 53); // "RS SUMBER WARAS", "GROGOL 1", -6.16625, 106.79714, "3", 2); // transit
		helper.insertHomeAndNeighbour(53, 52); // "GROGOL 1", "JELAMBAR", -6.16676, 106.79060, "3", 2); // transit
		helper.insertHomeAndNeighbour(52, 51); // "JELAMBAR", "INDOSIAR", -6.16653, 106.78645, "3", 2); // transit
		helper.insertHomeAndNeighbour(51, 50); // "INDOSIAR", "TAMAN KOTA", -6.16347, 106.77536, "3", 2); // transit
		helper.insertHomeAndNeighbour(50, 49); // "TAMAN KOTA", "JEMBATAN GANTUNG", -6.15717, 106.75791, "3", 0);
		helper.insertHomeAndNeighbour(49, 48); // "JEMBATAN GANTUNG", "DISPENDA", -6.15547, 106.74926, "3", 0);
		helper.insertHomeAndNeighbour(48, 47); // "DISPENDA", "JEMBATAN BARU", -6.15457, 106.73804, "3", 0);
		helper.insertHomeAndNeighbour(47, 46); // "JEMBATAN BARU", "RAWA BUAYA", -6.15485, 106.73053, "3", 0);
		helper.insertHomeAndNeighbour(46, 45); // "RAWA BUAYA", "SUMUR BOR", -6.153933, 106.726333, "3", 0);
		helper.insertHomeAndNeighbour(45, 44); // "SUMUR BOR", "PESAKIH", -6.15303, 106.71917, "3", 0);
		helper.insertHomeAndNeighbour(44, 43); // "PESAKIH", "KALIDERES", -6.15477, 106.71525, "3", 0);
		helper.insertHomeAndNeighbour(43, 0); // "KALIDERES", "", -6.15469, 106.70565, "3", 1); // exit
	}
	
	private void inputRuteKoridor4PulogadungDukuhatas2()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 4--");
		// koridor 4 , rute PULOGADUNG KORIDOR 4 - DUKUH ATAS 2
		helper.insertHomeAndNeighbour(21, 56); // 21, 56); // "PULOGADUNG", "PASAR PULOGADUNG", -6.18327, 106.90888, "4", 1); // exit
//		helper.insertHomeAndNeighbour(180, 56); // 21, 56); // "PULOGADUNG KORIDOR 4", "PASAR PULOGADUNG", -6.18327, 106.90888, "4", 1); // exit
		helper.insertHomeAndNeighbour(56, 57); // "PASAR PULOGADUNG", "TU GAS", -6.18745, 106.9059, "4", 0);
		helper.insertHomeAndNeighbour(57, 58); // "TU GAS", "LAYUR", -6.1923, 106.90488, "4", 0);
		helper.insertHomeAndNeighbour(58, 59); // "LAYUR", "PEMUDA RAWAMANGUN", -6.19349, 106.89902, "4", 0);
		helper.insertHomeAndNeighbour(59, 60); // "PEMUDA RAWAMANGUN", "VELODROME", -6.19346, 106.89167, "4", 0);
		helper.insertHomeAndNeighbour(60, 61); // "VELODROME", "SUNAN GIRI", -6.19341, 106.88818, "4", 0);
		helper.insertHomeAndNeighbour(61, 62); // "SUNAN GIRI", "UNJ", -6.19323, 106.88367, "4", 0);
		helper.insertHomeAndNeighbour(62, 63); // "UNJ", "PRAMUKA BPKP", -6.19286, 106.8802, "4", 0);
		helper.insertHomeAndNeighbour(63, 64); // "PRAMUKA BPKP", "PRAMUKA LIA", -6.19202, 106.87325, "4", 2); // transit
		helper.insertHomeAndNeighbour(64, 65); // "PRAMUKA LIA", "UTAN KAYU", -6.19218, 106.86857, "4", 0);
		helper.insertHomeAndNeighbour(65, 66); // "UTAN KAYU", "PASAR GENJING", -6.19264, 106.86463, "4", 0);
		helper.insertHomeAndNeighbour(66, 67); // "PASAR GENJING", "MATRAMAN 2", -6.19432, 106.86095, "4", 0);
		helper.insertHomeAndNeighbour(67, 68); // "MATRAMAN 2", "MANGGARAI", -6.19901, 106.85431, "4", 2); // transit
		helper.insertHomeAndNeighbour(68, 69); // "MANGGARAI", "PASAR RUMPUT", -6.20893, 106.84752, "4", 0);
		helper.insertHomeAndNeighbour(69, 70); // "PASAR RUMPUT", "HALIMUN", -6.20709, 106.84104, "4", 0);
		helper.insertHomeAndNeighbour(70, 71); // "HALIMUN", "DUKUH ATAS 2", -6.20511, 106.8334, "4", 2); // transit
//		helper.insertHomeAndNeighbour(71, 0); // "DUKUH ATAS 2", "", -6.20422, 106.82316, "4", 2); // transit
		
		// koridor 4 , rute DUKUH ATAS 2 - PULOGADUNG KORIDOR 4
		helper.insertHomeAndNeighbour(71, 70); // "DUKUH ATAS 2", "HALIMUN", -6.20422, 106.82316, "4", 2); // transit
		helper.insertHomeAndNeighbour(70, 69); // "HALIMUN", "PASAR RUMPUT", -6.20511, 106.8334, "4", 2); // transit
		helper.insertHomeAndNeighbour(69, 68); // "PASAR RUMPUT", "MANGGARAI", -6.20709, 106.84104, "4", 0);
		helper.insertHomeAndNeighbour(68, 67); // "MANGGARAI", "MATRAMAN 2", -6.20893, 106.84752, "4", 0);
		helper.insertHomeAndNeighbour(67, 66); // "MATRAMAN 2", "PASAR GENJING", -6.19901, 106.85431, "4", 2); // transit
		helper.insertHomeAndNeighbour(66, 65); // "PASAR GENJING", "UTAN KAYU", -6.19432, 106.86095, "4", 0);
		helper.insertHomeAndNeighbour(65, 64); // "UTAN KAYU", "PRAMUKA LIA",-6.19264, 106.86463, "4", 0);
		helper.insertHomeAndNeighbour(64, 63); // "PRAMUKA LIA", "PRAMUKA BPKP", -6.19218, 106.86857, "4", 0);
		helper.insertHomeAndNeighbour(63, 62); // "PRAMUKA BPKP", "UNJ", -6.19202, 106.87325, "4", 2); // transit
		helper.insertHomeAndNeighbour(62, 61); // "UNJ", "SUNAN GIRI", -6.19286, 106.8802, "4", 0);
		helper.insertHomeAndNeighbour(61, 60); // "SUNAN GIRI", "VELODROME", -6.19323, 106.88367, "4", 0);
		helper.insertHomeAndNeighbour(60, 59); // "VELODROME", "PEMUDA RAWAMANGUN", -6.19341, 106.88818, "4", 0);
		helper.insertHomeAndNeighbour(59, 58); // "PEMUDA RAWAMANGUN", "LAYUR", -6.19346, 106.89167, "4", 0);
		helper.insertHomeAndNeighbour(58, 57); // "LAYUR", "TU GAS", -6.19349, 106.89902, "4", 0);
		helper.insertHomeAndNeighbour(57, 56); // "TU GAS", "PASAR PULOGADUNG", -6.1923, 106.90488, "4", 0);
		helper.insertHomeAndNeighbour(56, 21); // "PASAR PULOGADUNG", "PULOGADUNG", -6.18745, 106.9059, "4", 0);
		helper.insertHomeAndNeighbour(21, 0); // "PULOGADUNG", "", -6.18327, 106.90888, "4", 1); // exit
//		helper.insertHomeAndNeighbour(56, 180); // "PASAR PULOGADUNG", "PULOGADUNG KORIDOR 4", -6.18745, 106.9059, "4", 0);
//		helper.insertHomeAndNeighbour(180, 0); // "PULOGADUNG KORIDOR 4", "", -6.18327, 106.90888, "4", 1); // exit
	}
	
	private void inputRuteKoridor5AncolKampungmelayu()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 5--");
		// koridor 5 , rute ANCOL - KAMPUNG MELAYU
		helper.insertHomeAndNeighbour(72, 73); // "ANCOL", "PADEMANGAN", -6.12756, 106.83040, "5", 1); // exit
		helper.insertHomeAndNeighbour(73, 74); // "PADEMANGAN", "GUNUNG SAHARI MANGGA DUA", -6.13366, 106.83165, "5", 0);
		helper.insertHomeAndNeighbour(74, 75); // "GUNUNG SAHARI MANGGA DUA", "JEMBATAN MERAH", -6.1372, 106.83247, "5", 0);
		helper.insertHomeAndNeighbour(75, 76); // "JEMBATAN MERAH", "PASAR BARU TIMUR", -6.14878, 106.83429, "5", 0);
		helper.insertHomeAndNeighbour(76, 77); // "PASAR BARU TIMUR", "BUDI UTOMO", -6.16245, 106.83818, "5", 0);
		helper.insertHomeAndNeighbour(77, 78); // "BUDI UTOMO", "SENEN SENTRAL", -6.16599, 106.83903, "5", 0);
		helper.insertHomeAndNeighbour(78, 79); // "SENEN SENTRAL", "PAL PUTIH", -6.17793, 106.84209, "5", 2); // transit
		helper.insertHomeAndNeighbour(79, 80); // "PAL PUTIH", "KRAMAT SENTIONG", -6.18442, 106.84388, "5", 0);
		helper.insertHomeAndNeighbour(80, 81); // "KRAMAT SENTIONG", "SALEMBA UI", -6.18820, 106.84606, "5", 0);
		helper.insertHomeAndNeighbour(81, 82); // "SALEMBA UI", "SALEMBA CAROLUS", -6.19364, 106.84903, "5", 0);
		helper.insertHomeAndNeighbour(82, 83); // "SALEMBA CAROLUS", "MATRAMAN 1", -6.196844, 106.851111, "5", 0);
		helper.insertHomeAndNeighbour(83, 84); // "MATRAMAN 1", "TEGALAN", -6.199678, 106.85415, "5", 2); // transit
		helper.insertHomeAndNeighbour(84, 85); // "TEGALAN", "SLAMET RIYADI", -6.20295, 106.85705, "5", 0);
		helper.insertHomeAndNeighbour(85, 86); // "SLAMET RIYADI", "KEBON PALA", -6.20856, 106.85924, "5", 0);
		helper.insertHomeAndNeighbour(86, 87); // "KEBON PALA", "PASAR JATINEGARA", -6.21317, 106.86131, "5", 0);
		helper.insertHomeAndNeighbour(87, 88); // "PASAR JATINEGARA", "KAMPUNG MELAYU", -6.21566, 106.86624, "5", 2); // transit
//		helper.insertHomeAndNeighbour(88, 0); // "KAMPUNG MELAYU", "", -6.2248, 106.86672, "5", 2); // transit
		
//		 koridor 5 , rute KAMPUNG MELAYU - ANCOL
		helper.insertHomeAndNeighbour(88, 86); // "KAMPUNG MELAYU", "KEBON PALA", -6.2248, 106.86672, "5", 2); // transit
//		helper.insertHomeAndNeighbour(56, 21); // "PASAR JATINEGARA", "KEBON PALA", -6.21566, 106.86624, 5);
		helper.insertHomeAndNeighbour(86, 85); // "KEBON PALA", "SLAMET RIYADI", -6.21317, 106.86133, "5", 0);
		helper.insertHomeAndNeighbour(85, 84); // "SLAMET RIYADI", "TEGALAN", -6.20856, 106.85924, "5", 0);
		helper.insertHomeAndNeighbour(84, 83); // "TEGALAN", "MATRAMAN 1", -6.20295, 106.85705, "5", 0);
		helper.insertHomeAndNeighbour(83, 82); // "MATRAMAN 1", "SALEMBA CAROLUS", -6.199678, 106.85415, "5", 2); // transit
		helper.insertHomeAndNeighbour(82, 81); // "SALEMBA CAROLUS", "SALEMBA UI", -6.196844, 106.851111, "5", 0);
		helper.insertHomeAndNeighbour(81, 80); // "SALEMBA UI", "KRAMAT SENTIONG", -6.19364, 106.84903, "5", 0);
		helper.insertHomeAndNeighbour(80, 79); // "KRAMAT SENTIONG", "PAL PUTIH", -6.18820, 106.84606, "5", 0);
		helper.insertHomeAndNeighbour(79, 78); // "PAL PUTIH", "SENEN SENTRAL", -6.18442, 106.84388, "5", 0);
		helper.insertHomeAndNeighbour(78, 77); // "SENEN SENTRAL", "BUDI UTOMO", -6.17793, 106.84209, "5", 2); // transit
		helper.insertHomeAndNeighbour(77, 76); // "BUDI UTOMO", "PASAR BARU TIMUR", -6.16599, 106.83903, "5", 0);
		helper.insertHomeAndNeighbour(76, 75); // "PASAR BARU TIMUR", "JEMBATAN MERAH", -6.16245, 106.83818, "5", 0);
		helper.insertHomeAndNeighbour(75, 74); // "JEMBATAN MERAH", "GUNUNG SAHARI MANGGA DUA",-6.14878, 106.83429, "5", 0);
		helper.insertHomeAndNeighbour(74, 73); // "GUNUNG SAHARI MANGGA DUA", "PADEMANGAN", -6.1372, 106.83247, "5", 0);
		helper.insertHomeAndNeighbour(73, 72); // "PADEMANGAN", "ANCOL", -6.13366, 106.83165, "5", 0);
		helper.insertHomeAndNeighbour(72, 0); // "ANCOL", "", -6.12756, 106.83040, "5", 1); // exit
	}
	
	private void inputRuteKoridor6Dukuhatas2Ragunan()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 6--");
		
		// koridor 6 , rute RAGUNAN - DUKUH ATAS 2
		helper.insertHomeAndNeighbour(89, 90); // "RAGUNAN", "DEPARTEMEN PERTAHANAN", -6.30596, 106.82002, "6", 1); // exit
		helper.insertHomeAndNeighbour(90, 91); // "DEPARTEMEN PERTAHANAN", "SMK 57", -6.29446, 106.82217, "6", 0);
		helper.insertHomeAndNeighbour(91, 92); // "SMK 57", "JATI PADANG", -6.29121, 106.82353, "6", 0);
		helper.insertHomeAndNeighbour(92, 93); // "JATI PADANG", "PEJATEN", -6.28550, 106.82622, "6", 0);
		helper.insertHomeAndNeighbour(93, 94); // "PEJATEN", "BUNCIT INDAH", -6.27831, 106.82979, "6", 0);
		helper.insertHomeAndNeighbour(94, 95); // "BUNCIT INDAH", "IMIGRASI", -6.27441, 106.8303, "6", 0);
		helper.insertHomeAndNeighbour(95, 96); // "IMIGRASI", "WARUNG JATI", -6.26225, 106.82966, "6", 0);
		helper.insertHomeAndNeighbour(96, 97); // "WARUNG JATI", "DUREN TIGA", -6.25671, 106.82799, "6", 0);
		helper.insertHomeAndNeighbour(97, 98); // "DUREN TIGA", "MAMPANG PRAPATAN", -6.25233, 106.82692, "6", 0);
		helper.insertHomeAndNeighbour(98, 99); // "MAMPANG PRAPATAN", "KUNINGAN TIMUR", -6.24177, 106.82572, "6", 0);
		helper.insertHomeAndNeighbour(99, 100); // "KUNINGAN TIMUR", "PATRA KUNINGAN", -6.23588, 106.82791, "6", 2); // transit
		helper.insertHomeAndNeighbour(100, 101); // "PATRA KUNINGAN", "DEPKES", -6.23351, 106.8313, "6", 0);
		helper.insertHomeAndNeighbour(101, 102); // "DEPKES", "GOR SUMANTRI", -6.22856, 106.83319, "6", 0);
		helper.insertHomeAndNeighbour(102, 103); // "GOR SUMANTRI", "KARET KUNINGAN", -6.22057, 106.83195, "6", 0);
		helper.insertHomeAndNeighbour(103, 104); // "KARET KUNINGAN", "KUNINGAN MADYA", -6.21729, 106.8309, "6", 0);
		helper.insertHomeAndNeighbour(104, 105); // "KUNINGAN MADYA", "SETIABUDI UTARA", -6.21235, 106.83042, "6", 0);
		helper.insertHomeAndNeighbour(105, 106); // "SETIABUDI UTARA", "LATUHARHARI", -6.20801, 106.82983, "6", 0);
		helper.insertHomeAndNeighbour(106, 70); // "LATUHARHARI", "HALIMUN", -6.20270, 106.82776, "6", 0);
//		helper.insertHomeAndNeighbour(70, 71); // "HALIMUN", "DUKUH ATAS 2", -6.20511, 106.8334, "6", 2); // transit
//		helper.insertHomeAndNeighbour(71, 0); // "DUKUH ATAS 2", "", -6.20422, 106.82316, "6", 2); // transit
		
		// koridor 6 , rute DUKUH ATAS 2 - RAGUNAN
		helper.insertHomeAndNeighbour(71, 105); // "DUKUH ATAS 2", "SETIABUDI UTARA", -6.20422, 106.82316, "6", 2); // transit
		helper.insertHomeAndNeighbour(105, 104); // "SETIABUDI UTARA", "KUNINGAN MADYA", -6.20801, 106.82983, "6", 0);
		helper.insertHomeAndNeighbour(104, 103); // "KUNINGAN MADYA", "KARET KUNINGAN", -6.21235, 106.83042, "6", 0);
		helper.insertHomeAndNeighbour(103, 102); // "KARET KUNINGAN", "GOR SUMANTRI", -6.21729, 106.8309, "6", 0);
		helper.insertHomeAndNeighbour(102, 101); // "GOR SUMANTRI", "DEPKES", -6.22057, 106.83195, "6", 0);
		helper.insertHomeAndNeighbour(101, 100); // "DEPKES", "PATRA KUNINGAN", -6.22856, 106.83319, "6", 0);
		helper.insertHomeAndNeighbour(100, 99); // "PATRA KUNINGAN", "KUNINGAN TIMUR", -6.23351, 106.8313, "6", 0);
		helper.insertHomeAndNeighbour(99, 98); // "KUNINGAN TIMUR", "MAMPANG PRAPATAN", -6.23588, 106.82791, "6", 2); // transit
		helper.insertHomeAndNeighbour(98, 97); // "MAMPANG PRAPATAN", "DUREN TIGA", -6.24177, 106.82572, "6", 0);
		helper.insertHomeAndNeighbour(97, 96); // "DUREN TIGA", "WARUNG JATI", -6.25233, 106.82692, "6", 0);
		helper.insertHomeAndNeighbour(96, 95); // "WARUNG JATI", "IMIGRASI", -6.25671, 106.82799, "6", 0);
		helper.insertHomeAndNeighbour(95, 94); // "IMIGRASI", "BUNCIT INDAH", -6.26225, 106.82966, "6", 0);
		helper.insertHomeAndNeighbour(94, 93); // "BUNCIT INDAH", "PEJATEN", -6.27441, 106.8303, "6", 0);
		helper.insertHomeAndNeighbour(93, 92); // "PEJATEN", "JATI PADANG", -6.27831, 106.82979, "6", 0);
		helper.insertHomeAndNeighbour(92, 91); // "JATI PADANG", "SMK 57", -6.28550, 106.82622, "6", 0);
		helper.insertHomeAndNeighbour(91, 90); // "SMK 57", "DEPARTEMEN PERTAHANAN", -6.29121, 106.82353, "6", 0);
		helper.insertHomeAndNeighbour(90, 89); // "DEPARTEMEN PERTAHANAN", "RAGUNAN", -6.29446, 106.82217, "6", 0);
		helper.insertHomeAndNeighbour(89, 0); // "RAGUNAN", "", -6.30596, 106.82002, "6", 1); // exit
	}
	
	private void inputRuteKoridor7KampungmelayuKampungrambutan()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 7--");
		
		// koridor 7, rute KAMPUNG RAMBUTAN - KAMPUNG MELAYU
		helper.insertHomeAndNeighbour(107, 108); // "KAMPUNG RAMBUTAN", "TANAH MERDEKA", -6.308917, 106.881803, "7", 1); // exit
		helper.insertHomeAndNeighbour(108, 109); // "TANAH MERDEKA", "FLY OVER RAYA BOGOR", -6.30821, 106.87424, "7", 0);
		helper.insertHomeAndNeighbour(109, 110); // "FLY OVER RAYA BOGOR", "RS HARAPAN BUNDA", -6.30655, 106.86558, "7", 0);
		helper.insertHomeAndNeighbour(110, 111); // "RS HARAPAN BUNDA", "PASAR INDUK", -6.30192, 106.86809, "7", 0);
		helper.insertHomeAndNeighbour(111, 112); // "PASAR INDUK", "PASAR KRAMAT JATI", -6.29432, 106.87205, "7", 0);
		helper.insertHomeAndNeighbour(112, 113); // "PASAR KRAMAT JATI", "PGC CILILITAN", -6.26891, 106.86669, "7", 0);
		helper.insertHomeAndNeighbour(113, 114); // "PGC CILILITAN", "BKN", -6.26235, 106.86652, "7", 0);
		helper.insertHomeAndNeighbour(114, 115); // "BKN", "CAWANG UKI", -6.25783, 106.86992, "7", 2); // transit);
		helper.insertHomeAndNeighbour(115, 116); // "CAWANG UKI", "BNN", -6.25035, 106.87359, "7", 2); // transit);
		helper.insertHomeAndNeighbour(116, 117); // "BNN", "CAWANG OTISTA", -6.24615, 106.8721, "7", 2); // transit);
		helper.insertHomeAndNeighbour(117, 118); // "CAWANG OTISTA", "GELANGGANG REMAJA", -6.24361, 106.86876, "7", 0);
		helper.insertHomeAndNeighbour(118, 119); // "GELANGGANG REMAJA", "BIDARA CINA", -6.2355, 106.86791, "7", 0);
		helper.insertHomeAndNeighbour(119, 88); // "BIDARA CINA", "KAMPUNG MELAYU", -6.22971, 106.86716, "7", 0);
//		helper.insertHomeAndNeighbour(88, 0); // "KAMPUNG MELAYU", "", -6.2248, 106.86672, "7", 2); // transit);
		
		// koridor 7, rute KAMPUNG MELAYU - KAMPUNG RAMBUTAN
		helper.insertHomeAndNeighbour(88, 119); // "KAMPUNG MELAYU", "BIDARA CINA", -6.2248, 106.86672, "7", 2); // transit
		helper.insertHomeAndNeighbour(119, 118); // "BIDARA CINA", "GELANGGANG REMAJA", -6.22971, 106.86716, "7", 0);
		helper.insertHomeAndNeighbour(118, 117); // "GELANGGANG REMAJA", "CAWANG OTISTA", -6.2355, 106.86791, "7", 0);
		helper.insertHomeAndNeighbour(117, 116); // "CAWANG OTISTA", "BNN", -6.24361, 106.86876, "7", 0);
		helper.insertHomeAndNeighbour(116, 115); // "BNN", "CAWANG UKI", -6.24615, 106.8721, "7", 2); // transit
		helper.insertHomeAndNeighbour(115, 114); // "CAWANG UKI", "BKN", -6.25035, 106.87359, "7", 2); // transit
		helper.insertHomeAndNeighbour(114, 113); // "BKN", "PGC CILILITAN", -6.25783, 106.86992, "7", 2); // transit
		helper.insertHomeAndNeighbour(113, 112); // "PGC CILILITAN", "PASAR KRAMAT JATI", -6.26235, 106.86652, "7", 0);
		helper.insertHomeAndNeighbour(112, 111); // "PASAR KRAMAT JATI", "PASAR INDUK", -6.26891, 106.86669, "7", 0);
		helper.insertHomeAndNeighbour(111, 110); // "PASAR INDUK", "RS HARAPAN BUNDA", -6.29432, 106.87205, "7", 0);
		helper.insertHomeAndNeighbour(110, 109); // "RS HARAPAN BUNDA", "FLY OVER RAYA BOGOR", -6.30192, 106.86809, "7", 0);
		helper.insertHomeAndNeighbour(109, 107); // "FLY OVER RAYA BOGOR", "KAMPUNG RAMBUTAN", -6.30655, 106.86558, "7", 0);
		helper.insertHomeAndNeighbour(107, 0); // "KAMPUNG RAMBUTAN", "", -6.308917, 106.881803, "7", 1); // exit
	}
	
	private void inputRuteKoridor8LebakbulusHarmoni()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 8--");
		// koridor 8 , rute LEBAK BULUS - HARMONI
		helper.insertHomeAndNeighbour(120, 121); // "LEBAK BULUS", "PONDOK PINANG", -6.289331, 106.774242, "8", 1); // exit
		helper.insertHomeAndNeighbour(121, 122); // "PONDOK PINANG", "PONDOK INDAH 1", -6.28218, 106.77208, "8", 0);
		helper.insertHomeAndNeighbour(122, 123); // "PONDOK INDAH 1", "PONDOK INDAH 2", -6.28732, 106.77951, "8", 0);
		helper.insertHomeAndNeighbour(123, 124); // "PONDOK INDAH 2", "TANAH KUSIR KODAM", -6.26726, 106.78360, "8", 0);
		helper.insertHomeAndNeighbour(124, 125); // "TANAH KUSIR KODAM", "KEBAYORAN LAMA BUNGUR", -6.256947, 106.781744, "8", 0);
		helper.insertHomeAndNeighbour(125, 126); // "KEBAYORAN LAMA BUNGUR", "PASAR KEBAYORAN LAMA", -6.2527339, 106.7816504, "8", 0);
		helper.insertHomeAndNeighbour(126, 127); // "PASAR KEBAYORAN LAMA", "SIMPRUG", -6.23845, 106.78321, "8", 0);
		helper.insertHomeAndNeighbour(127, 128); // "SIMPRUG", "PERMATA HIJAU", -6.233914, 106.786683, "8", 0);
		helper.insertHomeAndNeighbour(128, 129); // "PERMATA HIJAU", "PERMATA HIJAU RS MEDIKA", -6.2214523, 106.7832916, "8", 0);
		helper.insertHomeAndNeighbour(129, 130); // "PERMATA HIJAU RS MEDIKA", "POS PENGUMBEN", -6.218510, 106.77773, "8", 0);
		helper.insertHomeAndNeighbour(130, 131); // "POS PENGUMBEN", "KELAPA DUA SASAK", -6.2129134, 106.7722694, "8", 0);
		helper.insertHomeAndNeighbour(131, 132); // "KELAPA DUA SASAK", "KEBON JERUK", -6.2055522, 106.769554, "8", 0);
		helper.insertHomeAndNeighbour(132, 133); // "KEBON JERUK", "DURI KEPA", -6.1942839, 106.7688949, "8", 0);
		helper.insertHomeAndNeighbour(133, 134); // "DURI KEPA", "KEDOYA ASSIDDIQIYAH", -6.18535, 106.76845, "8", 0);
		helper.insertHomeAndNeighbour(134, 135); // "KEDOYA ASSIDDIQIYAH", "KEDOYA GREEN GARDEN", -6.17453, 106.76589, "8", 0);
		helper.insertHomeAndNeighbour(135, 51); // "KEDOYA GREEN GARDEN", "INDOSIAR", -6.1644374, 106.7629993, "8", 0);
//		helper.insertHomeAndNeighbour(51, 52); // "INDOSIAR", "JELAMBAR", -6.16347, 106.77536, "8", 2); // transit
//		helper.insertHomeAndNeighbour(52, 53); // "JELAMBAR", "GROGOL 1", -6.16653, 106.78645, "8", 2); // transit
//		helper.insertHomeAndNeighbour(53, 54); // "GROGOL 1", "RS SUMBER WARAS", -6.16676, 106.79060, "8", 2); // transit
//		helper.insertHomeAndNeighbour(54, 15); // "RS SUMBER WARAS", "HARMONI SENTRAL", -6.16625, 106.79714, "8", 2); // transit
//		helper.insertHomeAndNeighbour(15, 54); // "HARMONI SENTRAL", "RS SUMBER WARAS", -6.16581, 106.82048, "8", 2); // transit

		// koridor 8 , rute HARMONI - LEBAK BULUS
//	helper.insertHomeAndNeighbour(15, 54); // "HARMONI SENTRAL", "RS SUMBER WARAS", -6.16581, 106.82048, "8", 2); // transit
//		helper.insertHomeAndNeighbour(54, 53); // "RS SUMBER WARAS", "GROGOL 1", -6.16625, 106.79714, "8", 2); // transit
//		helper.insertHomeAndNeighbour(53, 52); // "GROGOL 1", "JELAMBAR", -6.16676, 106.79060, "8", 2); // transit
//		helper.insertHomeAndNeighbour(52, 51); // "JELAMBAR", "INDOSIAR", -6.16653, 106.78645, "8", 2); // transit
		helper.insertHomeAndNeighbour(51, 135); // "INDOSIAR", "KEDOYA GREEN GARDEN", -6.16347, 106.77536, "8", 2); // transit
		helper.insertHomeAndNeighbour(135, 134); // "KEDOYA GREEN GARDEN", "KEDOYA ASSIDDIQIYAH", -6.1644374, 106.7629993, "8", 0);
		helper.insertHomeAndNeighbour(134, 133); // "KEDOYA ASSIDDIQIYAH", "DURI KEPA", -6.17453, 106.76589, "8", 0);
		helper.insertHomeAndNeighbour(133, 132); // "DURI KEPA", "KEBON JERUK", -6.18535, 106.76845, "8", 0);
		helper.insertHomeAndNeighbour(132, 131); // "KEBON JERUK", "KELAPA DUA SASAK", -6.1942839, 106.7688949, "8", 0);
		helper.insertHomeAndNeighbour(131, 130); // "KELAPA DUA SASAK", "POS PENGUMBEN", -6.2055522, 106.769554, "8", 0);
		helper.insertHomeAndNeighbour(130, 129); // "POS PENGUMBEN", "PERMATA HIJAU RS MEDIKA", -6.2129134, 106.7722694, "8", 0);
		helper.insertHomeAndNeighbour(129, 128); // "PERMATA HIJAU RS MEDIKA", "PERMATA HIJAU", -6.218510, 106.77773, "8", 0);
		helper.insertHomeAndNeighbour(128, 127); // "PERMATA HIJAU", "SIMPRUG", -6.2214523, 106.7832916, "8", 0);
		helper.insertHomeAndNeighbour(127, 126); // "SIMPRUG", "PASAR KEBAYORAN LAMA", -6.233914, 106.786683, "8", 0);
		helper.insertHomeAndNeighbour(126, 125); // "PASAR KEBAYORAN LAMA", "KEBAYORAN LAMA BUNGUR", -6.23845, 106.78321, "8", 0);
		helper.insertHomeAndNeighbour(125, 124); // "KEBAYORAN LAMA BUNGUR", "TANAH KUSIR KODAM", -6.2527339, 106.7816504, "8", 0);
		helper.insertHomeAndNeighbour(124, 123); // "TANAH KUSIR KODAM", "PONDOK INDAH 2", -6.256947, 106.781744, "8", 0);
		helper.insertHomeAndNeighbour(123, 122); // "PONDOK INDAH 2", "PONDOK INDAH 1", -6.26726, 106.78360, "8", 0);
		helper.insertHomeAndNeighbour(122, 120); // "PONDOK INDAH 1", "LEBAK BULUS", -6.28732, 106.77951, "8", 0);
		helper.insertHomeAndNeighbour(120, 0); // "LEBAK BULUS", "",-6.289331, 106.774242, "8", 1); // exit
	}
	
	private void inputRuteKoridor9PluitPinangranti()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 9--");
		
		// koridor 9 , rute PLUIT - PINANG RANTI
		helper.insertHomeAndNeighbour(136, 137); // "PLUIT", "PENJARINGAN", -6.11782, 106.79189, "9", 1); // exit
		helper.insertHomeAndNeighbour(137, 138); // "PENJARINGAN", "JEMBATAN TIGA", -6.12626, 106.79204, "9", 0);
		helper.insertHomeAndNeighbour(138, 139); // "JEMBATAN TIGA", "JEMBATAN DUA", -6.13333, 106.79273, "9", 0);
		helper.insertHomeAndNeighbour(139, 140); // "JEMBATAN DUA", "JEMBATAN BESI", -6.1432875, 106.7935255, "9", 0);
		helper.insertHomeAndNeighbour(140, 141); // "JEMBATAN BESI", "LATUMENTEN STASIUN GROGOL", -6.15201, 106.79494, "9", 0);
		helper.insertHomeAndNeighbour(141, 142); // "LATUMENTEN STASIUN GROGOL", "GROGOL 2", -6.16106, 106.79041, "9", 0);
		helper.insertHomeAndNeighbour(142, 143); // "GROGOL 2", "S PARMAN CENTRAL PARK", -6.16726, 106.7878, "9", 2); // transit
		helper.insertHomeAndNeighbour(143, 144); // "S PARMAN CENTRAL PARK", "RS HARAPAN KITA", -6.17604, 106.79307, "9", 0);
		helper.insertHomeAndNeighbour(144, 145); // "RS HARAPAN KITA", "SLIPI KEMANGGISAN", -6.18511, 106.79689, "9", 0);
		helper.insertHomeAndNeighbour(145, 146); // "SLIPI KEMANGGISAN", "SLIPI PETAMBURAN", -6.1900928, 106.7969852, "9", 0);
		helper.insertHomeAndNeighbour(146, 147); // "SLIPI PETAMBURAN", "SENAYAN JCC", -6.20198, 106.79997, "9", 0);
		helper.insertHomeAndNeighbour(147, 148); // "SENAYAN JCC", "SEMANGGI", -6.21406, 106.80872, "9", 0);
		helper.insertHomeAndNeighbour(148, 149); // "SEMANGGI", "GATOT SUBROTO LIPI", -6.22057, 106.81316, "9", 2); // transit
		helper.insertHomeAndNeighbour(149, 150); // "GATOT SUBROTO LIPI", "GATOT SUBROTO JAMSOSTEK", -6.22677, 106.8174, "9", 0);
		helper.insertHomeAndNeighbour(150, 151); // "GATOT SUBROTO JAMSOSTEK", "KUNINGAN BARAT", -6.23274, 106.82151, "9", 0);
		helper.insertHomeAndNeighbour(151, 152); // "KUNINGAN BARAT", "TEGAL PARANG", -6.23674, 106.82701, "9", 2); // transit
		helper.insertHomeAndNeighbour(152, 153); // "TEGAL PARANG", "PANCORAN BARAT", -6.23925, 106.83109, "9", 0);
		helper.insertHomeAndNeighbour(153, 154); // "PANCORAN BARAT", "PANCORAN TUGU", -6.24158, 106.83759, "9", 0);
		helper.insertHomeAndNeighbour(154, 155); // "PANCORAN TUGU", "TEBET BKPM", -6.24338, 106.844707, "9", 0);
		helper.insertHomeAndNeighbour(155, 156); // "TEBET BKPM", "CIKOKO", -6.24325, 106.85088, "9", 0);
		helper.insertHomeAndNeighbour(156, 157); // "CIKOKO", "CAWANG CILIWUNG", -6.24319, 106.858143, "9", 0);
		helper.insertHomeAndNeighbour(157, 116); // "CAWANG CILIWUNG", "BNN", -6.24312, 106.86300, "9", 0);
		
//		helper.insertHomeAndNeighbour(116, 115); // "BNN", "CAWANG UKI", -6.24615, 106.8721, "9", 2); // transit
//		helper.insertHomeAndNeighbour(115, 114); // "CAWANG UKI", "BKN", -6.25035, 106.87359, "9", 2); // transit
//		helper.insertHomeAndNeighbour(114, 113); // "BKN", "PGC CILILITAN", -6.25783, 106.86992, "9", 2); // transit
//		helper.insertHomeAndNeighbour(113, 112); // "PGC CILILITAN", "PASAR KRAMAT JATI", -6.26235, 106.86652, "9", 0);
		
		helper.insertHomeAndNeighbour(112, 158); // "PASAR KRAMAT JATI", "GARUDA TAMAN MINI", -6.26891, 106.86669, "9", 0);
		helper.insertHomeAndNeighbour(158, 159); // "GARUDA TAMAN MINI", "PINANG RANTI", -6.2902006, 106.8811523, "9", 0);
		helper.insertHomeAndNeighbour(159, 0); // "PINANG RANTI", "", -6.29086, 106.88655, "9", 1); // exit
		
		
		// koridor 9 , rute PINANG RANTI - PLUIT
		helper.insertHomeAndNeighbour(159, 158); // "PINANG RANTI", "GARUDA TAMAN MINI", -6.29086, 106.88655, "9", 1); // exit
		helper.insertHomeAndNeighbour(158, 112); // "GARUDA TAMAN MINI", "PASAR KRAMAT JATI", -6.2902006, 106.8811523, "9", 0);
		
//		helper.insertHomeAndNeighbour(112, 113); // "PASAR KRAMAT JATI", "PGC CILILITAN", -6.26891, 106.86669, "9", 0);
//		helper.insertHomeAndNeighbour(113, 114); // "PGC CILILITAN", "BKN", -6.26235, 106.86652, "9", 0);
//		helper.insertHomeAndNeighbour(114, 115); // "BKN", "CAWANG UKI", -6.25783, 106.86992, "9", 2); // transit);
//		helper.insertHomeAndNeighbour(115, 116); // "CAWANG UKI", "BNN", -6.25035, 106.87359, "9", 2); // transit);
//		helper.insertHomeAndNeighbour(116, 117); // "BNN", "CAWANG CILIWUNG", -6.24615, 106.8721, "9", 2); // transit);
		
		helper.insertHomeAndNeighbour(157, 156); // "CAWANG CILIWUNG", "CIKOKO", -6.24312, 106.86300, "9", 0);
		helper.insertHomeAndNeighbour(156, 155); // "CIKOKO", "TEBET BKPM", -6.24319, 106.858143, "9", 0);
		helper.insertHomeAndNeighbour(155, 154); // "TEBET BKPM", "PANCORAN TUGU", -6.24325, 106.85088, "9", 0);
		helper.insertHomeAndNeighbour(154, 153); // "PANCORAN TUGU", "PANCORAN BARAT", -6.24338, 106.844707, "9", 0);
		helper.insertHomeAndNeighbour(153, 152); // "PANCORAN BARAT", "TEGAL PARANG", -6.24158, 106.83759, "9", 0);
		helper.insertHomeAndNeighbour(152, 151); // "TEGAL PARANG", "KUNINGAN BARAT", -6.23925, 106.83109, "9", 0);
		helper.insertHomeAndNeighbour(151, 150); // "KUNINGAN BARAT", "GATOT SUBROTO JAMSOSTEK", -6.23674, 106.82701, "9", 2); // transit
		helper.insertHomeAndNeighbour(150, 149); // "GATOT SUBROTO JAMSOSTEK", "GATOT SUBROTO LIPI", -6.23274, 106.82151, "9", 0);
		helper.insertHomeAndNeighbour(149, 148); // "GATOT SUBROTO LIPI", "SEMANGGI", -6.22677, 106.8174, "9", 0);
		helper.insertHomeAndNeighbour(148, 147); // "SEMANGGI", "SENAYAN JCC", -6.22057, 106.81316, "9", 2); // transit
		helper.insertHomeAndNeighbour(147, 146); // "SENAYAN JCC", "SLIPI PETAMBURAN", -6.21406, 106.80872, "9", 0);
		helper.insertHomeAndNeighbour(146, 145); // "SLIPI PETAMBURAN", "SLIPI KEMANGGISAN", -6.20198, 106.79997, "9", 0);
		helper.insertHomeAndNeighbour(145, 144); // "SLIPI KEMANGGISAN", "RS HARAPAN KITA", -6.1900928, 106.7969852, "9", 0);
		helper.insertHomeAndNeighbour(144, 143); // "RS HARAPAN KITA", "S PARMAN CENTRAL PARK", -6.18511, 106.79689, "9", 0);
		helper.insertHomeAndNeighbour(143, 142); // "S PARMAN CENTRAL PARK", "GROGOL 2", -6.17604, 106.79307, "9", 0);
		helper.insertHomeAndNeighbour(142, 141); // "GROGOL 2", "LATUMENTEN STASIUN GROGOL", -6.16726, 106.7878, "9", 2); // transit
		helper.insertHomeAndNeighbour(141, 140); // "LATUMENTEN STASIUN GROGOL", "JEMBATAN BESI", -6.16106, 106.79041, "9", 0);
		helper.insertHomeAndNeighbour(140, 139); // "JEMBATAN BESI", "JEMBATAN DUA", -6.15201, 106.79494, "9", 0);
		helper.insertHomeAndNeighbour(139, 138); // "JEMBATAN DUA", "JEMBATAN TIGA", -6.1432875, 106.7935255, "9", 0);
		helper.insertHomeAndNeighbour(138, 137); // "JEMBATAN TIGA", "PENJARINGAN", -6.13333, 106.79273, "9", 0);
		helper.insertHomeAndNeighbour(137, 136); // "PENJARINGAN", "PLUIT", -6.12626, 106.79204, "9", 0);
		helper.insertHomeAndNeighbour(136, 0); // "PLUIT", "", -6.11782, 106.79189, "9", 1); // exit			
	}
	
	private void inputRuteKoridor10TanjungpriokPGC()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 10--");
		// koridor 10 , rute TANJUNG PRIOK - PGC
		helper.insertHomeAndNeighbour(160, 161); // "TANJUNG PRIOK", "ENGGANO", -6.10973, 106.88175, "10", 1); // exit
		helper.insertHomeAndNeighbour(161, 162); // "ENGGANO", "KOJA", -6.1100935, 106.8924393, "10", 0);
		helper.insertHomeAndNeighbour(162, 163); // "KOJA", "WALIKOTA JAKARTA UTARA", -6.11390, 106.89314, "10", 0);
		helper.insertHomeAndNeighbour(163, 164); // "WALIKOTA JAKARTA UTARA", "PLUMPANG PERTAMINA", -6.1188329, 106.893433, "10", 0);
		helper.insertHomeAndNeighbour(164, 165); // "PLUMPANG PERTAMINA", "SUNTER KELAPA GADING", -6.13057, 106.89356, "10", 0);
		helper.insertHomeAndNeighbour(165, 166); // "SUNTER KELAPA GADING", "YOS SUDARSO KODAMAR", -6.1427902, 106.8910149, "10", 0);
		helper.insertHomeAndNeighbour(166, 167); // "YOS SUDARSO KODAMAR", "CEMPAKA MAS 2", -6.1525776, 106.8872962, "10", 0);
		helper.insertHomeAndNeighbour(167, 168); // "CEMPAKA MAS 2", "CEMPAKA PUTIH", -6.16618, 106.87910, "10", 2); // transit 		-6.1620814, 106.8817701
		helper.insertHomeAndNeighbour(168, 169); // "CEMPAKA PUTIH", "PULOMAS SELATAN", -6.17439, 106.87642, "10", 0);
		helper.insertHomeAndNeighbour(169, 170); // "PULOMAS SELATAN", "RAWASARI", -6.17731, 106.87596, "10", 0);
		helper.insertHomeAndNeighbour(170, 171); // "RAWASARI", "PRAMUKA BPKP 2", -6.1872579, 106.8755846, "10", 0);
		helper.insertHomeAndNeighbour(171, 172); // "PRAMUKA BPKP 2", "RAWA MANGUN", -6.19277, 106.87500, "10", 2); // transit
		helper.insertHomeAndNeighbour(172, 173); // "RAWA MANGUN", "BEA CUKAI", -6.1974176, 106.8741671, "10", 0);
		helper.insertHomeAndNeighbour(173, 174); // "BEA CUKAI", "STASIUN JATINEGARA", -6.20629, 106.87378, "10", 0);
		helper.insertHomeAndNeighbour(174, 175); // "STASIUN JATINEGARA", "PEDATI", -6.21549, 106.87415, "10", 2); // transit
		helper.insertHomeAndNeighbour(175, 176); // "PEDATI", "KEBON NANAS CIPINANG", -6.2237396, 106.8748285, "10", 0);
		helper.insertHomeAndNeighbour(176, 177); // "KEBON NANAS CIPINANG", "KALIMALANG", -6.2314283, 106.8761048, "10", 0);
		helper.insertHomeAndNeighbour(177, 178); // "KALIMALANG", "CAWANG SUTOYO", -6.23917, 106.87807, "10", 0);
		helper.insertHomeAndNeighbour(178, 115); // "CAWANG SUTOYO", "CAWANG UKI", -6.24482, 106.87592, "10", 0);
		
//	helper.insertHomeAndNeighbour(115, 114); // "CAWANG UKI", "BKN", -6.25035, 106.87359, "10", 2); // transit
//	helper.insertHomeAndNeighbour(114, 113); // "BKN", "PGC CILILITAN", -6.25783, 106.86992, "10", 2); // transit
		
		helper.insertHomeAndNeighbour(113, 179); // "PGC CILILITAN", "PGC", -6.26235, 106.86652, "10", 0);
		helper.insertHomeAndNeighbour(179, 0); // "PGC", "", -6.26311, 106.86547, "10", 1); // exit
		
		// koridor 10 , rute PGC - TANJUNG PRIOK
		helper.insertHomeAndNeighbour(179, 113); // "PGC", "PGC CILILITAN", -6.26311, 106.86547, "10", 1); // exit
		
//	helper.insertHomeAndNeighbour(113, 114); // "PGC CILILITAN", "BKN", -6.26235, 106.86652, "10", 0);
//	helper.insertHomeAndNeighbour(114, 115); // "BKN", "CAWANG UKI", -6.25783, 106.86992, "10", 2); // transit);
		
		helper.insertHomeAndNeighbour(115, 178); // "CAWANG UKI", "CAWANG SUTOYO", -6.25035, 106.87359, "10", 2); // transit);
		helper.insertHomeAndNeighbour(178, 177); // "CAWANG SUTOYO", "KALIMALANG", -6.24482, 106.87592, "10", 0);
		helper.insertHomeAndNeighbour(177, 176); // "KALIMALANG", "KEBON NANAS CIPINANG", -6.23917, 106.87807, "10", 0);
		helper.insertHomeAndNeighbour(176, 175); // "KEBON NANAS CIPINANG", "PEDATI", -6.2314283, 106.8761048, "10", 0);
		helper.insertHomeAndNeighbour(175, 174); // "PEDATI", "STASIUN JATINEGARA", -6.2237396, 106.8748285, "10", 0);
		helper.insertHomeAndNeighbour(174, 173); // "STASIUN JATINEGARA", "BEA CUKAI", -6.21549, 106.87415, "10", 2); // transit
		helper.insertHomeAndNeighbour(173, 172); // "BEA CUKAI", "RAWA MANGUN", -6.20629, 106.87378, "10", 0);
		helper.insertHomeAndNeighbour(172, 171); // "RAWA MANGUN", "PRAMUKA BPKP 2", -6.1974176, 106.8741671, "10", 0);
		helper.insertHomeAndNeighbour(171, 170); // "PRAMUKA BPKP 2", "RAWASARI", -6.19277, 106.87500, "10", 2); // transit
		helper.insertHomeAndNeighbour(170, 169); // "RAWASARI", "PULOMAS SELATAN", -6.1872579, 106.8755846, "10", 0);
		helper.insertHomeAndNeighbour(169, 168); // "PULOMAS SELATAN", "CEMPAKA PUTIH", -6.17731, 106.87596, "10", 0);
		helper.insertHomeAndNeighbour(168, 167); // "CEMPAKA PUTIH", "CEMPAKA MAS 2", -6.17439, 106.87642, "10", 0);
		helper.insertHomeAndNeighbour(167, 166); // "CEMPAKA MAS 2", "YOS SUDARSO KODAMAR", -6.16618, 106.87910, "10", 2); // transit
		helper.insertHomeAndNeighbour(166, 165); // "YOS SUDARSO KODAMAR", "SUNTER KELAPA GADING", -6.1525776, 106.8872962, "10", 0);
		helper.insertHomeAndNeighbour(165, 164); // "SUNTER KELAPA GADING", "PLUMPANG PERTAMINA", -6.1427902, 106.8910149, "10", 0);
		helper.insertHomeAndNeighbour(164, 163); // "PLUMPANG PERTAMINA", "WALIKOTA JAKARTA UTARA", -6.13057, 106.89356, "10", 0);
		helper.insertHomeAndNeighbour(163, 162); // "WALIKOTA JAKARTA UTARA", "KOJA", -6.1188329, 106.893433, "10", 0);
		helper.insertHomeAndNeighbour(162, 161); // "KOJA", "ENGGANO", -6.11390, 106.89314, "10", 0);
		helper.insertHomeAndNeighbour(161, 160); // "ENGGANO", "TANJUNG PRIOK", -6.1100935, 106.8924393, "10", 0);
		helper.insertHomeAndNeighbour(160, 0); // "TANJUNG PRIOK", "", -6.10973, 106.88175, "10", 1); // exit
	}
	
	private void inputRuteKoridor11WalikotajaktimKampungmelayu()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 11--");
		
		// koridor 11 , rute walikota jaktim - kampung melayu
		helper.insertHomeAndNeighbour(180, 181); // "PULOGEBANG", "WALIKOTA JAKARTA TIMUR", -6.20790, 106.95653, 11, 1); // exit
		helper.insertHomeAndNeighbour(181, 182); // "WALIKOTA JAKARTA TIMUR", "PENGGILINGAN", -6.21218, 106.94633, 11, 0);
		helper.insertHomeAndNeighbour(182, 183); // "PENGGILINGAN", "PERUMNAS KLENDER", -6.21572, 106.93936, 11, 0);
		helper.insertHomeAndNeighbour(183, 184); // "PERUMNAS KLENDER", "FLYOVER RADEN INTEN", -6.21646, 106.92901, 11, 0);
		helper.insertHomeAndNeighbour(184, 185); // "FLYOVER RADEN INTEN", "BUARAN", -6.21608, 106.92509, 11, 0);
		helper.insertHomeAndNeighbour(185, 186); // "BUARAN", "KAMPUNG SUMUR", -6.21476, 106.91474, 11, 0);
		helper.insertHomeAndNeighbour(186, 187); // "KAMPUNG SUMUR", "FLYOVER KLENDER", -6.21418057, 106.91004775, 11, 0);
		helper.insertHomeAndNeighbour(187, 188); // "FLYOVER KLENDER", "STASIUN KLENDER", -6.21359595, 106.90214798, 11, 0);
		helper.insertHomeAndNeighbour(188, 189); // "STASIUN KLENDER", "CIPINANG", -6.21355795, 106.89934306, 11, 0);
		helper.insertHomeAndNeighbour(189, 190); // "CIPINANG", "IMIGRASI JAKARTA TIMUR", -6.21451121, 106.88525878, 11, 0);
		helper.insertHomeAndNeighbour(190, 191); // "IMIGRASI JAKARTA TIMUR", "PASAR ENJO", -6.21465919, 106.88185908, 11, 0);
		helper.insertHomeAndNeighbour(191, 192); // "PASAR ENJO", "FLYOVER JATINEGARA", -6.21487251, 106.87776133, 11, 0);
		helper.insertHomeAndNeighbour(192, 193); // "FLYOVER JATINEGARA", "STASIUN JATINEGARA 2", -6.21513449, 106.87407196, 11, 0);
		helper.insertHomeAndNeighbour(193, 194); // "STASIUN JATINEGARA 2", "JATINEGARA RS. PREMIER", -6.21561645, 106.86782174, 11, 0);
		helper.insertHomeAndNeighbour(194, 88); // "JATINEGARA RS. PREMIER", "KAMPUNG MELAYU", -6.22135, 106.86820, 11, 0);
//		helper.insertHomeAndNeighbour(88, 0); // "KAMPUNG MELAYU", -6.2248, 106.86672, 0, 2); // transit
		
		helper.insertHomeAndNeighbour(88, 194); // "KAMPUNG MELAYU", "JATINEGARA RS. PREMIER", -6.2248, 106.86672, 0, 2); // transit
		helper.insertHomeAndNeighbour(194, 193); // "JATINEGARA RS. PREMIER", "STASIUN JATINEGARA 2", -6.22135, 106.86820, 11, 0);
		helper.insertHomeAndNeighbour(193, 192); // "STASIUN JATINEGARA 2", "FLYOVER JATINEGARA", -6.21561645, 106.86782174, 11, 0);
		helper.insertHomeAndNeighbour(192, 191); // "FLYOVER JATINEGARA", "PASAR ENJO", -6.21513449, 106.87407196, 11, 0);
		helper.insertHomeAndNeighbour(191, 190); // "PASAR ENJO", "IMIGRASI JAKARTA TIMUR", -6.21487251, 106.87776133, 11, 0);
		helper.insertHomeAndNeighbour(190, 189); // "IMIGRASI JAKARTA TIMUR", "CIPINANG", -6.21465919, 106.88185908, 11, 0);
		helper.insertHomeAndNeighbour(189, 188); // "CIPINANG", "IMIGRASI JAKARTA TIMUR", -6.21451121, 106.88525878, 11, 0);
		helper.insertHomeAndNeighbour(188, 187); // "STASIUN KLENDER", "CIPINANG", -6.21355795, 106.89934306, 11, 0);
		helper.insertHomeAndNeighbour(187, 186); // "FLYOVER KLENDER", "STASIUN KLENDER", -6.21359595, 106.90214798, 11, 0);
		helper.insertHomeAndNeighbour(186, 185); // "KAMPUNG SUMUR", "FLYOVER KLENDER", -6.21418057, 106.91004775, 11, 0);
		helper.insertHomeAndNeighbour(185, 184); // "BUARAN", "KAMPUNG SUMUR", -6.21476, 106.91474, 11, 0);
		helper.insertHomeAndNeighbour(184, 183); // "FLYOVER RADEN INTEN", "BUARAN", -6.21608, 106.92509, 11, 0);
		helper.insertHomeAndNeighbour(183, 182); // "PERUMNAS KLENDER", "FLYOVER RADEN INTEN", -6.21646, 106.92901, 11, 0);
		helper.insertHomeAndNeighbour(182, 181); // "PENGGILINGAN", "PERUMNAS KLENDER", -6.21572, 106.93936, 11, 0);
		helper.insertHomeAndNeighbour(181, 180); // "WALIKOTA JAKARTA TIMUR", "PENGGILINGAN", -6.21218, 106.94633, 11, 0);
		helper.insertHomeAndNeighbour(180, 0); // "PULOGEBANG", "WALIKOTA JAKARTA TIMUR", -6.20790, 106.95653, 11, 1); // exit
	}
	
	private void inputRuteKoridor12PluitTanjungPriok()
	{
		Log.v("RouteBuswayActivity","--RUTE KORIDOR 12--");
		
//		helper.insertHomeAndNeighbour(136, 137); // "PLUIT", "PENJARINGAN", -6.11782, 106.79189, 12, 1); // exit
		helper.insertHomeAndNeighbour(137, 195); // "PENJARINGAN", "BANDENGAN PEKOJAN", -6.12626, 106.79204, 12, 0);
		helper.insertHomeAndNeighbour(195, 196); // "BANDENGAN PEKOJAN", "KALI BESAR BARAT", -6.13667, 106.79891, 12, 0);
		helper.insertHomeAndNeighbour(196, 20); // "KALI BESAR BARAT", "KOTA", -6.13506, 106.81164, 12, 0);
		helper.insertHomeAndNeighbour(20, 197); // "KOTA", "PANGERAN JAYAKARTA", -6.13779, 106.81368, 9, 2); // transit
		helper.insertHomeAndNeighbour(197, 198); // "PANGERAN JAYAKARTA", "MANGGA DUA", -6.13763, 106.81751, 12, 0);
		helper.insertHomeAndNeighbour(198, 74); // "MANGGA DUA", "GUNUNG SAHARI MANGGA DUA", -6.13618, 106.82578, 12, 0);
//		helper.insertHomeAndNeighbour(74, 75); // "GUNUNG SAHARI MANGGA DUA", "JEMBATAN MERAH", -6.1372, 106.83247, 12, 2);	// transit
		helper.insertHomeAndNeighbour(75, 199); // "JEMBATAN MERAH", "KEMAYORAN LANDAS PACU TIMUR", -6.14878, 106.83467, 12, 2);	// transit
		helper.insertHomeAndNeighbour(199, 200); // "KEMAYORAN LANDAS PACU TIMUR", "DANAU SUNTER BARAT", -6.152155, 106.85405, 12, 0);
		helper.insertHomeAndNeighbour(200, 201); // "DANAU SUNTER BARAT", "SUNTER SMP 140", -6.14628, 106.85805, 12, 0);
		helper.insertHomeAndNeighbour(201, 202); // "SUNTER SMP 140", "SUNTER KARYA", -6.13963, 106.85966, 12, 0);
		helper.insertHomeAndNeighbour(202, 203); // "SUNTER KARYA", "SUNTER BOULEVARD BARAT", -6.13770, 106.87005, 12, 0);
		helper.insertHomeAndNeighbour(203, 165); // "SUNTER BOULEVARD BARAT", "SUNTER KELAPA GADING", -6.15011, 106.88885, 12, 0);
//		helper.insertHomeAndNeighbour(165, 164); // "SUNTER KELAPA GADING", "PLUMPANG PERTAMINA", -6.1427902, 106.8910149, 12, 0);
//		helper.insertHomeAndNeighbour(164, 163); // "PLUMPANG PERTAMINA", "WALIKOTA JAKARTA UTARA", -6.13057, 106.89356, 12, 0);
//		helper.insertHomeAndNeighbour(163, 162); // "WALIKOTA JAKARTA UTARA", "KOJA", -6.1188329, 106.893433, 12, 0);
//		helper.insertHomeAndNeighbour(162, 161); // "KOJA", "ENGGANO", -6.11390, 106.89314, 12, 0);
//		helper.insertHomeAndNeighbour(161, 160); // "ENGGANO", "TANJUNG PRIOK", -6.1100935, 106.8924393, 12, 0);
//		helper.insertHomeAndNeighbour(160, 0); // "TANJUNG PRIOK", -6.10973, 106.88175, 12, 1); // exit
		
		
//		helper.insertHomeAndNeighbour(160, 161); // "TANJUNG PRIOK", "ENGGANO", -6.10973, 106.88175, 12, 1); // exit
//		helper.insertHomeAndNeighbour(161, 162); // "ENGGANO", "KOJA", -6.1100935, 106.8924393, 12, 0);
//		helper.insertHomeAndNeighbour(162, 163); // "KOJA", "WALIKOTA JAKARTA UTARA", -6.11390, 106.89314, 12, 0);
//		helper.insertHomeAndNeighbour(163, 164); // "WALIKOTA JAKARTA UTARA", "PLUMPANG PERTAMINA", -6.1188329, 106.893433, 12, 0);
//		helper.insertHomeAndNeighbour(164, 165); // "PLUMPANG PERTAMINA", "SUNTER KELAPA GADING", -6.13057, 106.89356, 12, 0);
		helper.insertHomeAndNeighbour(165, 203); // "SUNTER KELAPA GADING", SUNTER BOULEVARD BARAT", -6.1427902, 106.8910149, 12, 0);
		helper.insertHomeAndNeighbour(203, 202); // "SUNTER BOULEVARD BARAT", "SUNTER KARYA", -6.15011, 106.88885, 12, 0);
		helper.insertHomeAndNeighbour(202, 201); // "SUNTER KARYA", "SUNTER SMP 140", -6.13770, 106.87005, 12, 0);
		helper.insertHomeAndNeighbour(201, 200); // "SUNTER SMP 140", "DANAU SUNTER BARAT", -6.13963, 106.85966, 12, 0);
		helper.insertHomeAndNeighbour(200, 199); // "DANAU SUNTER BARAT", "KEMAYORAN LANDAS PACU TIMUR", -6.14628, 106.85805, 12, 0);
		helper.insertHomeAndNeighbour(199, 75); // "KEMAYORAN LANDAS PACU TIMUR", "JEMBATAN MERAH", -6.152155, 106.85405, 12, 0);
//		helper.insertHomeAndNeighbour(75, 74); // "JEMBATAN MERAH", "GUNUNG SAHARI MANGGA DUA", -6.14878, 106.83467, 12, 2);	// transit
		helper.insertHomeAndNeighbour(74, 198); // "GUNUNG SAHARI MANGGA DUA", "MANGGA DUA", -6.1372, 106.83247, 12, 2);	// transit
		helper.insertHomeAndNeighbour(198, 197); // "MANGGA DUA", "PANGERAN JAYAKARTA", -6.13618, 106.82578, 12, 0);
		helper.insertHomeAndNeighbour(197, 20); // "PANGERAN JAYAKARTA", "KOTA", -6.13763, 106.81751, 12, 0);
		helper.insertHomeAndNeighbour(20, 196); // "KOTA", "KALI BESAR BARAT", -6.13779, 106.81368, 9, 2); // transit
		helper.insertHomeAndNeighbour(196, 195); // "KALI BESAR BARAT", "BANDENGAN PEKOJAN", -6.13506, 106.81164, 12, 0);
		helper.insertHomeAndNeighbour(195, 137); // "BANDENGAN PEKOJAN", "PENJARINGAN", -6.13667, 106.79891, 12, 0);
//		helper.insertHomeAndNeighbour(137, 136); // "PENJARINGAN", "PLUIT", -6.12626, 106.79204, 12, 0);
//		helper.insertHomeAndNeighbour(136, 0); // "PLUIT", 0, -6.11782, 106.79189, 12, 1); // exit
	}
	
	private void inputRuteTransit()
	{
		Log.v("RouteBuswayActivity","--RUTE TRANSIT--");
		// KORIDOR 1
		helper.insertHomeAndNeighbour(9, 71); // "DUKUH ATAS 1", "DUKUH ATAS 2", -6.20585, 106.82222, "1", 2); // transit);
		helper.insertHomeAndNeighbour(6, 148); // "BENDUNGAN HILIR", "SEMANGGI", -6.217058, 106.815320, "1", 2); // transit);

		// KORIDOR 2
		helper.insertHomeAndNeighbour(26, 167); // "CEMPAKA TIMUR", "CEMPAKA MAS 2", -6.16649, 106.87598, "2", 2); // transit);
		helper.insertHomeAndNeighbour(32, 78); // "SENEN", "SENEN SENTRAL", -6.17821, 106.84240, "2", 2); // transit);
		
		// KORIDOR 3 & KORIDOR 8
		helper.insertHomeAndNeighbour(53, 142); // "GROGOL 1", "GROGOL 2", -6.16676, 106.79060, "3", 2); // transit);
		
		// KORIDOR 4		
		helper.insertHomeAndNeighbour(63, 171); // "PRAMUKA BPKP", "PRAMUKA BPKP 2", -6.19202, 106.87325, "4", 2); // transit);
		helper.insertHomeAndNeighbour(71, 9); // "DUKUH ATAS 2", "DUKUH ATAS 1", -6.20422, 106.82316, "4", 2); // transit);
		helper.insertHomeAndNeighbour(67, 83); // "MATRAMAN 2", "MATRAMAN 1", -6.19901, 106.85431, "4", 2); // transit);

		// KORIDOR 5
		helper.insertHomeAndNeighbour(78, 32); // "SENEN SENTRAL", "SENEN", -6.17793, 106.84209, "5", 2); // transit);
		helper.insertHomeAndNeighbour(83, 67); // "MATRAMAN 1", "MATRAMAN 2", -6.199678, 106.85415, "5", 2); // transit);		
		helper.insertHomeAndNeighbour(87, 193); // "PASAR JATINEGARA", "STASIUN JATINEGARA 2", -6.21566, 106.86624, "5", 2); // transit);
		
		// KORIDOR 6
		helper.insertHomeAndNeighbour(99, 151); // "KUNINGAN TIMUR", "KUNINGAN BARAT", -6.23588, 106.82791, "6", 2); // transit);
		
		// KORIDOR 7

		// KORIDOR 9
		helper.insertHomeAndNeighbour(142, 53); // "GROGOL 2", "GROGOL 1", -6.16726, 106.7878, "9", 2); // transit);
		helper.insertHomeAndNeighbour(148, 6); // "SEMANGGI", "BENDUNGAN HILIR", -6.22057, 106.81316, "9", 2); // transit);
		helper.insertHomeAndNeighbour(151, 99); //"KUNINGAN BARAT", "KUNINGAN TIMUR", -6.23674, 106.82701, "9", 2); // transit);
		
		// KORIDOR 10
		helper.insertHomeAndNeighbour(167, 26); // "CEMPAKA MAS 2", "CEMPAKA TIMUR", -6.1620814, 106.8817701, "10", 2); // transit);
		helper.insertHomeAndNeighbour(171, 63); // "PRAMUKA BPKP 2", "PRAMUKA BPKP", -6.19277, 106.87482, "10", 2); // transit);
		helper.insertHomeAndNeighbour(174, 192); // "STASIUN JATINEGARA", "FLYOVER JATINEGARA", -6.21549, 106.87415, 10); // transit);
		
		// KORIDOR 11
		helper.insertHomeAndNeighbour(192, 174); // "FLYOVER JATINEGARA", "STASIUN JATINEGARA", -6.21566, 106.86624, 5); // transit);
		helper.insertHomeAndNeighbour(193, 87); // "STASIUN JATINEGARA 2", "PASAR JATINEGARA", -6.21566, 106.86624, 5); // transit);
	}
	
	@Override
  public void onPause()
  {
  	if (helper != null)
  		helper.close();
  	
  	super.onPause();
  }
  
  @Override
  protected void onDestroy() 
  {
    super.onDestroy();
    
    if (helper != null)
    	helper.close();
  }
} 