package com.example.nazanin.finalproject.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nazanin.finalproject.AppDatabase;
import com.example.nazanin.finalproject.R;
import com.example.nazanin.finalproject.model.DTO.GSM;
import com.example.nazanin.finalproject.model.DTO.Lte;
import com.example.nazanin.finalproject.model.DTO.UMTS;
import com.example.nazanin.finalproject.model.DTO.cellIdentity;
import com.example.nazanin.finalproject.optimization.genetic_algorithm.Chromosome;
import com.example.nazanin.finalproject.optimization.genetic_algorithm.GeneticAlgorithm;
import com.example.nazanin.finalproject.optimization.genetic_algorithm.GeneticParams;
import com.example.nazanin.finalproject.optimization.genetic_algorithm.Population;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private Button startBtn;
    private LinearLayout infolayout,cellInfolayout;
    private TextView anch1Txt,anch2Txt,anch3Txt,anch4Txt,anch5Txt,processTxt,refPowerTxt,PLETxt,xTargetTxt,yTargetTxt,runningTxt;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);

    //DTO obj
    GSM gsm = new GSM();
    Lte lte = new Lte();
    UMTS umts = new UMTS();
    cellIdentity cellidentity = new cellIdentity();

    public static int PERMISSION_READ_STATE = 1;
    private static int PERMISSION_COURSE_STATE = 2;
    private static int PERMISSION_FINE_STATE = 3;
    private TelephonyManager tm;
    public static final int GROUP_PERMISSION = 1;
    private ArrayList<TextView> anchTxts = new ArrayList<>();
    private ArrayList<String> permissionsNeeded = new ArrayList<>();
    private ArrayList<String> permissionsAvailable = new ArrayList<>();
    public Map<Point,Double> anchorInfo = new HashMap<>();
    private AppDatabase myDatabase;

    int cellId;
    String gen;
    double power;
    boolean free = true;
    String PLMN;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        myDatabase = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.NAME).fallbackToDestructiveMigration().build();
        setContentView(R.layout.activity_main);
        infolayout = findViewById(R.id.infolayer);
        cellInfolayout = findViewById(R.id.celllayer);
        startBtn = findViewById(R.id.navBtn);
        anch1Txt = findViewById(R.id.anchor1);
        anch2Txt = findViewById(R.id.anchor2);
        anch3Txt = findViewById(R.id.anchor3);
        anch4Txt = findViewById(R.id.anchor4);
        anch5Txt = findViewById(R.id.anchor5);
        anchTxts.add(anch1Txt);anchTxts.add(anch2Txt);anchTxts.add(anch3Txt);
        anchTxts.add(anch4Txt);anchTxts.add(anch5Txt);
        processTxt = findViewById(R.id.process);
        runningTxt = findViewById(R.id.running);
        refPowerTxt = findViewById(R.id.refp);
        PLETxt = findViewById(R.id.beta);
        xTargetTxt = findViewById(R.id.xt);
        yTargetTxt = findViewById(R.id.yt);
        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        permissionsAvailable.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsAvailable.add(Manifest.permission.READ_PHONE_STATE);
        permissionsAvailable.add(Manifest.permission.ACCESS_FINE_LOCATION);
        for (String permission : permissionsAvailable){
            if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                permissionsNeeded.add(permission);
            }
        }
        //permissions
        if(permissionsNeeded.size()>0){
            RequestPermission(permissionsNeeded);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_COURSE_STATE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_STATE);
            }

            else {

            }
        }

    }

    private void RequestPermission(ArrayList<String> permissions){
        String[] permissionList = new String[permissions.size()];
        permissions.toArray(permissionList);
        ActivityCompat.requestPermissions(this,permissionList,GROUP_PERMISSION);
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.DARK,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {
                        enableLocationComponent(style);

                    }
                });

    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initializeLocationEngine();
        } else {

        }
    }


    @SuppressLint("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startScanning(View view) {

        infolayout.setVisibility(View.GONE);
        startBtn.setVisibility(View.GONE);
        cellInfolayout.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.VISIBLE);
        mapView.getMapAsync(this);

        startScan();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void localization(Map<Point, Double> anchorInfo){
        double x=0,y=0;
        for (Map.Entry<Point, Double> entry : anchorInfo.entrySet()) {
            x += entry.getKey().latitude();
            y += entry.getKey().longitude();

        }
        x = (x/5);
        y = (y/5);
        runningTxt.setText("....running optimization algorithm....");
        GeneticAlgorithm ga = new GeneticAlgorithm(anchorInfo,x,y);
        Population population = new Population(GeneticParams.POPULATION_SIZE,
                anchorInfo,x,y).sortByFitness();
        int generation = 0;
        while (generation<1000) {
            ++generation;
            population = ga.evolve(population).sortByFitness();
        }
        printParameters(population.getFittest());



    }
    private void printParameters(List<Double> genes){
        refPowerTxt.setText("reference power: "+genes.get(0));
        PLETxt.setText("path loss exponent: "+genes.get(1));
        xTargetTxt.setText("target node x: "+genes.get(2));
        yTargetTxt.setText("target node y: "+genes.get(3));

    }
    private class MainActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivity> activityWeakReference;


        MainActivityLocationCallback(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivity activity = activityWeakReference.get();
            ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(CONNECTIVITY_SERVICE);
            NetworkCapabilities nc = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());


//                Toast.makeText(activity,"time:"+time+"     mobile downSpeed :" + (((double)downSpeed/8)/1000)/8 + " Mb/s  and upSpeed : " + ((((double)upSpeed/8)/1000)/8) +" Mb/s",
//                        Toast.LENGTH_SHORT).show();
            }


            if (activity != null) {
                final Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }
                if(free) {
                    anchorInfo.put(Point.fromLngLat(location.getLongitude(),
                            location.getLatitude()), power);
                }

                if (anchorInfo.size()<5){
                    processTxt.setText("..... collecting anchor node's information .....");
                }
                else {
                    free = false;
                    int i=0;
                    for (Map.Entry<Point, Double> entry : anchorInfo.entrySet()) {
                        anchTxts.get(i).setText("anchor"+i+" x: "+entry.getKey().latitude()+"" +
                                "  y: "+entry.getKey().longitude()+"  P: "+entry.getValue()+" dbm");
                        i++;
                    }
                    localization(anchorInfo);
                    anchorInfo.clear();
                }

                if(cellidentity.getGeneration()!= null){
                    switch(cellidentity.getGeneration()){
                        case "gsm": gsm.setLat(location.getLatitude());gsm.setLon(location.getLongitude());
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Data
                                    myDatabase.gsmDao().insert(gsm);
                                    //get data
                                    // List<GSM> mylist = myDatabase.gsmDao().getAll();
                                }
                            });
                            break;
                        case "umts": umts.setLat(location.getLatitude());umts.setLon(location.getLongitude());
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Data
                                    myDatabase.umtsDao().insert(umts);
                                    //get data
                                    // List<UMTS> mylist = myDatabase.umtsDao().getAll();
                                }
                            });
                            break;
                        case "lte": lte.setLat(location.getLatitude());lte.setLon(location.getLongitude());
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Data
                                    myDatabase.lteDao().insert(lte);
                                    //get data
                                    //  List<Lte> mylist = myDatabase.lteDao().getAll();

                                }
                            });
                            break;
                        default:
                    }
                }
                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {


                    }
                });

                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }




        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startScan() {

        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        //get data every 5 seconds
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                @SuppressLint("MissingPermission") List<CellInfo> cellInfoList = tm.getAllCellInfo();
                for (CellInfo info : cellInfoList) {
                    if (info instanceof CellInfoGsm) {
                        gen = "2G";
                        if (info.isRegistered()) {
                            final CellIdentityGsm identityGsm = ((CellInfoGsm) info).getCellIdentity();
                            cellId = identityGsm.getCid();
                            int LAC = identityGsm.getLac();
                            PLMN = String.valueOf(identityGsm.getMcc()) + String.valueOf(identityGsm.getMnc()); //plmn = mcc+mnc
                            cellidentity.setGeneration("gsm");
                            cellidentity.setPlmn(PLMN);
                            cellidentity.setCell_id(cellId);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Data
                                    myDatabase.cellIdentityDao().insert(cellidentity);
                                }
                            });
                            gsm.setLac(LAC);
                        }
                        giveGSMPowerParameters(info);
                    }

                    // get lte data
                    else if (info instanceof CellInfoLte) {

                        gen = "4G";
                        //serving cell information
                        if (info.isRegistered()) {
                            final CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();
                            cellId = identityLte.getCi(); //cell identity
                            int TAC = identityLte.getTac(); //tracking area code
                            PLMN = String.valueOf(identityLte.getMcc()) + String.valueOf(identityLte.getMnc()); //plmn = mcc+mnc
//                    Toast.makeText(context, TAC+" tac"+PLMN+"  plmn",
//                            Toast.LENGTH_SHORT).show();
                            cellidentity.setGeneration("lte");
                            cellidentity.setPlmn(PLMN);
                            cellidentity.setCell_id(cellId);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Data
                                    myDatabase.cellIdentityDao().insert(cellidentity);
                                }
                            });
                            lte.setTac(TAC);
                        }

                        giveLtePowerParameters(info);


                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                            && info instanceof CellInfoWcdma) {
                        gen = "3G";
                        if (info.isRegistered()) {
                            final CellIdentityWcdma identityWcdma = ((CellInfoWcdma) info).getCellIdentity();
                            cellId = identityWcdma.getCid();
                            int LAC = identityWcdma.getLac();
                            PLMN = String.valueOf(identityWcdma.getMcc()) + String.valueOf(identityWcdma.getMnc());
                            cellidentity.setGeneration("umts");
                            cellidentity.setPlmn(PLMN);
                            cellidentity.setCell_id(cellId);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Data
                                    myDatabase.cellIdentityDao().insert(cellidentity);
                                }
                            });
                            umts.setLac(LAC);
                        }
                        giveUMTSPowerParameters(info);

                    } else {


                    }
                }

//                visibleTxt.setText("Visible Cells: "+cellInfoList.size());
//                powerTxt.setText(power+"dBm  "+gen );

//                }
//                else {
//                  //  ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_PHONE_STATE}, MainActivity.PERMISSION_READ_STATE);
//                }

                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(runnable, 5000);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void giveLtePowerParameters(final CellInfo info){

        CellSignalStrengthLte lteInfo = ((CellInfoLte) info).getCellSignalStrength();
        lte.setPower(lteInfo.getRsrp());  //RSRP

//        if (Build.VERSION.SDK_INT >= 29) {
//            lte.setRssi(lteInfo.getRssi()); //RSSI
//        }

        power = lte.getPower();
        //  MainActivity.coverage = coverage;


//        Toast.makeText(this, "rsrp"+lte.getRsrp()+"  "+lte.getRsrq()+"  "+lte.getSinr()
//                        +"ave  "+coverage,
//                Toast.LENGTH_SHORT).show();

    }

    private void giveGSMPowerParameters(final CellInfo info){
        CellSignalStrengthGsm gsmInfo = ((CellInfoGsm) info).getCellSignalStrength();
        gsm.setPower(gsmInfo.getDbm()); // rxlev

        power = gsm.getPower();
//        Toast.makeText(this, "rsrp"+gsmInfo.getDbm()+"  "+gsmInfo.getLevel()+"  "+
//                        "   "+gsmInfo.getAsuLevel(),
//                Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void giveUMTSPowerParameters(final CellInfo info){
        CellSignalStrengthWcdma umtsInfo = ((CellInfoWcdma) info).getCellSignalStrength();
        umts.setPower(umtsInfo.getDbm()); //RSCP
        power = umts.getPower();
//        if (Build.VERSION.SDK_INT >= 30) {
//            umtsInfo.getEcNo(); //EC/N0
//        }

    }

    // we override life cycles here because map and android both have their life cycles
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    // we should end location updates and mapview to avoid memory leak
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

