package com.machatitans.greenbot;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static android.view.View.VISIBLE;

public class PaintActivity extends FragmentActivity implements OnMapReadyCallback {
    Context context;
    Button setUp, submit, locate, next, settings, apply, cancel, apples, peaches, oranges ,poire , recentLocation;
    ImageView setPanel , setaLad;

    GoogleMap map;
    String fruitType;
    LinearLayout linearLayout;
    TextView textWidth, textHeight, textFruit;
    EditText editLenght, editWidth;
    MainActivity activity;
    HorizontalScrollView scroll;
    boolean isLocating = false;
    static public EditText widthText;
    static TextView text;
    static PaintView paintView;
    static public int width = 3;
    ConnectivityManager cnxManager;
    LocationManager locManager;
    ImageView mapImage;
    SupportMapFragment mapFragment;
    FileOutputStream outputStream;
    final int MY_PERMISSIONS_REQUEST_LOCATION = 25;
    LatLng center = new LatLng(0, 0);
    double[] gps = new double[2];
    String language = activity.getLanguage();
    int widthVal;
    int lenghtVal;
    File dirc = new File(Environment.getExternalStorageDirectory() , "GreenBot");
    String path = Environment.getExternalStorageDirectory().toString();
    OutputStream fOut = null;
    File file = new File(dirc.getPath(), "Recent.jpg");

//    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  //  ConstraintLayout ll = (ConstraintLayout) inflater.inflate(R.layout.paint_layout, null);




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_layout);
        System.out.println(language);
        final ImageView setaLad1;
        dirc.mkdirs();
        System.out.println(file.getPath());
        linearLayout = (LinearLayout) findViewById(R.id.lLayout);
        //linearLayout2 = (LinearLayout)((LinearLayout) scroll.getChildAt(0)).getChildAt(3);
        editLenght = (EditText) findViewById(R.id.editLenght);
        editWidth = (EditText) findViewById(R.id.editWidth);
        scroll = (HorizontalScrollView) findViewById(R.id.scroll);
        setPanel = (ImageView) findViewById(R.id.setPanel);
        setaLad = (ImageView) findViewById(R.id.setLand);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.GONE);
        setUp = (Button) findViewById(R.id.SetUp);
        settings = (Button) findViewById(R.id.settings);
        submit = (Button) findViewById(R.id.Submit);
        next = (Button) findViewById(R.id.next);
        recentLocation = (Button) findViewById(R.id.rcLocation);
        apply = (Button) findViewById(R.id.apply);
        locate = (Button) findViewById(R.id.Locate);
        cancel = (Button) findViewById(R.id.cancel);
        apples = (Button) ((LinearLayout) scroll.getChildAt(0)).getChildAt(0);
        oranges = (Button) ((LinearLayout) scroll.getChildAt(0)).getChildAt(1);
        peaches = (Button) ((LinearLayout) scroll.getChildAt(0)).getChildAt(2);
        poire = (Button) ((LinearLayout) scroll.getChildAt(0)).getChildAt(3);
        textFruit = findViewById(R.id.textFruit);
        textWidth = findViewById(R.id.textWidth);
        textHeight = findViewById(R.id.textHeight);
        cnxManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapImage = (ImageView) findViewById(R.id.mapImage);
        next.setVisibility(View.GONE);
        recentLocation.setVisibility(View.GONE);
        locate.setVisibility(View.GONE);
        setPanel.setVisibility(View.GONE);
        setaLad.setVisibility(View.GONE);
        apply.setVisibility(View.GONE);
        textWidth.setVisibility(View.GONE);
        textHeight.setVisibility(View.GONE);
        textFruit.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        editLenght.setVisibility(View.GONE);
        editWidth.setVisibility(View.GONE);
        scroll.setVisibility(View.GONE);
        widthVal = 100;
        lenghtVal = 100;
        editWidth.setText("100");
        editLenght.setText("100");

      //  textFruit.setTypeface(typeface);
        //textWidth.setTypeface(typeface);
       // textHeight.setTypeface(typeface);

        if (language.equals("English")) {
            textHeight.setText("Lenght");
            textWidth.setText("Width");
            textFruit.setText("Fruit type");
            Drawable applyen = getResources().getDrawable(R.drawable.applybtn);
            apply.setBackground(applyen);
            Drawable map = getResources().getDrawable(R.drawable.mapselected);
            setUp.setBackground(map);
            Drawable submitto = getResources().getDrawable(R.drawable.send);
            submit.setBackground(submitto);
            Drawable setting = getResources().getDrawable(R.drawable.settings);
            settings.setBackground(setting);
            Drawable location = getResources().getDrawable(R.drawable.locate);
            locate.setBackground(location);
            Drawable nextbtn = getResources().getDrawable(R.drawable.select);
            next.setBackground(nextbtn);
            Drawable peach = getResources().getDrawable(R.drawable.peach);
            peaches.setBackground(peach);
            Drawable orange = getResources().getDrawable(R.drawable.oranges);
            oranges.setBackground(orange);
            Drawable apple = getResources().getDrawable(R.drawable.apple);
            apples.setBackground(apple);
            Drawable poires = getResources().getDrawable(R.drawable.poire);
            poire.setBackground(poires);
            Drawable RcLocation = getResources().getDrawable(R.drawable.load);
            recentLocation.setBackground(RcLocation);



        } else if (language.equals("français")) {
            textWidth.setText("Largeur");
            textHeight.setText("Longueur");
            textFruit.setText("Type de fruit");
            Drawable applyfr = getResources().getDrawable(R.drawable.applybtnfr);
            apply.setBackground(applyfr);
            Drawable map = getResources().getDrawable(R.drawable.mapselectedfr);
            setUp.setBackground(map);
            Drawable submitto = getResources().getDrawable(R.drawable.sendfr);
            submit.setBackground(submitto);
            Drawable setting = getResources().getDrawable(R.drawable.settingsfr);
            settings.setBackground(setting);
            Drawable location = getResources().getDrawable(R.drawable.locatefr);
            locate.setBackground(location);
            Drawable nextbtn = getResources().getDrawable(R.drawable.selectfr);
            next.setBackground(nextbtn);
            Drawable peach = getResources().getDrawable(R.drawable.peachfr);
            peaches.setBackground(peach);
            Drawable orange = getResources().getDrawable(R.drawable.orangesfr);
            oranges.setBackground(orange);
            Drawable apple = getResources().getDrawable(R.drawable.applefr);
            apples.setBackground(apple);
            Drawable poires = getResources().getDrawable(R.drawable.poirefr);
            poire.setBackground(poires);
            Drawable RcLocation = getResources().getDrawable(R.drawable.loadfr);
            recentLocation.setBackground(RcLocation);



        } else if (language.equals("العربية")) {
            textWidth.setText("العرض");
            textHeight.setText("الطول");
            textFruit.setText("نوع الغلال");
            Drawable applyar = getResources().getDrawable(R.drawable.applybtnar);
            apply.setBackground(applyar);
            Drawable map = getResources().getDrawable(R.drawable.mapselectedar);
            setUp.setBackground(map);
            Drawable submitto = getResources().getDrawable(R.drawable.sendar);
            submit.setBackground(submitto);
            Drawable setting = getResources().getDrawable(R.drawable.settingsar);
            settings.setBackground(setting);
            Drawable location = getResources().getDrawable(R.drawable.locatear);
            locate.setBackground(location);
            Drawable nextbtn = getResources().getDrawable(R.drawable.selectar);
            next.setBackground(nextbtn);
            Drawable peach = getResources().getDrawable(R.drawable.peachar);
            peaches.setBackground(peach);
            Drawable orange = getResources().getDrawable(R.drawable.orangesar);
            oranges.setBackground(orange);
            Drawable apple = getResources().getDrawable(R.drawable.applear);
            apples.setBackground(apple);
            Drawable poires = getResources().getDrawable(R.drawable.poirear);
            poire.setBackground(poires);
            Drawable RcLocation = getResources().getDrawable(R.drawable.loadar);
            recentLocation.setBackground(RcLocation);

        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanel.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);
                textWidth.setVisibility(View.GONE);
                textHeight.setVisibility(View.GONE);
                textFruit.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                editLenght.setVisibility(View.GONE);
                editWidth.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanel.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);
                textWidth.setVisibility(View.GONE);
                textHeight.setVisibility(View.GONE);
                textFruit.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                editLenght.setVisibility(View.GONE);
                editWidth.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                if(editWidth.getText().equals("") && editLenght.getText().equals("")) {
                    widthVal = 30;
                    lenghtVal = 30;
                }
                else{
                    widthVal = Integer.valueOf((editWidth.getText().toString()));
                    if(widthVal >= 500) {
                        widthVal = (Integer.valueOf((editWidth.getText().toString()))/2);
                        lenghtVal = (Integer.valueOf((editLenght.getText().toString()))/2);
                    }
                    lenghtVal = Integer.valueOf((editLenght.getText().toString()));
                    if(lenghtVal >=900) {
                        widthVal = (Integer.valueOf((editWidth.getText().toString()))/2);
                        lenghtVal = (Integer.valueOf((editLenght.getText().toString()))/2);
                    }
                    if(language == "français"){
                        Toast.makeText(getApplicationContext(), "Veuiller remplir les champs", Toast.LENGTH_LONG).show();
                    }
                    else if (language == "english"){
                        Toast.makeText(getApplicationContext(), "Please fill in the blanks ", Toast.LENGTH_LONG).show();
                    }
                    else if (language == "العربية"){
                    Toast.makeText(getApplicationContext(), " الرجاء تعمير الفراغات", Toast.LENGTH_LONG).show();
                }
                }
                if(setaLad.getVisibility() == VISIBLE){
                    setaLad.getLayoutParams().height = lenghtVal;
                    setaLad.getLayoutParams().width = widthVal;
                }

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPanel.setVisibility(VISIBLE);
                apply.setVisibility(VISIBLE);
                textWidth.setVisibility(VISIBLE);
                textHeight.setVisibility(VISIBLE);
                textFruit.setVisibility(VISIBLE);
                cancel.setVisibility(VISIBLE);
                editLenght.setVisibility(VISIBLE);
                editWidth.setVisibility(VISIBLE);
                scroll.setVisibility(VISIBLE);

            }
        });
        apples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fruitType = "apples";
                System.out.println(fruitType);
                if (language.equals("english")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applesel);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peach);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.oranges);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poire);
                    poire.setBackground(poires);

                } else if (language.equals("français")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applefrsel);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachfr);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesfr);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirefr);
                    poire.setBackground(poires);

                } else if (language.equals("العربية")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applearsel);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachar);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesar);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirear);
                    poire.setBackground(poires);

                }
            }

        });
        peaches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fruitType = "peaches";
                System.out.println(fruitType);
                if (language.equals("english")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.apple);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachsel);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.oranges);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poire);
                    poire.setBackground(poires);

                } else if (language.equals("français")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applefr);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachfrsel);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesfr);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirefr);
                    poire.setBackground(poires);

                } else if (language.equals("العربية")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applear);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peacharsel);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesar);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirear);
                    poire.setBackground(poires);

                }
            }

        });
        oranges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fruitType = "oranges";
                System.out.println(fruitType);
                if (language.equals("english")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.apple);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peach);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangessel);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poire);
                    poire.setBackground(poires);

                } else if (language.equals("français")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applefr);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachfr);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesfrsel);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirefr);
                    poire.setBackground(poires);

                } else if (language.equals("العربية")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applear);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachar);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesarsel);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirear);
                    poire.setBackground(poires);

                }
            }

        });

        poire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fruitType = "pear";
                System.out.println(fruitType);
                if (language.equals("english")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.apple);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peach);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.oranges);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poiresel);
                    poire.setBackground(poires);

                } else if (language.equals("français")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applefr);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachfr);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesfr);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirefrsel);
                    poire.setBackground(poires);

                } else if (language.equals("العربية")) {
                    Drawable applessel = getResources().getDrawable(R.drawable.applear);
                    apples.setBackground(applessel);
                    Drawable peach = getResources().getDrawable(R.drawable.peachar);
                    peaches.setBackground(peach);
                    Drawable orange = getResources().getDrawable(R.drawable.orangesar);
                    oranges.setBackground(orange);
                    Drawable poires = getResources().getDrawable(R.drawable.poirearsel);
                    poire.setBackground(poires);

                }
            }

        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //paintView.ClearPath();

                if (isLocating) {
                    CaptureMapScreen();
                    mapFragment.getView().setVisibility(View.GONE);
                    locate.setVisibility(View.GONE);
                    setUp.setVisibility(VISIBLE);
                    settings.setVisibility(VISIBLE);
                    submit.setVisibility(VISIBLE);
                    if(language.equals("english")){
                    setUp.setBackgroundResource(R.drawable.mapselected);
                    }
                    else if(language.equals("français")){
                        setUp.setBackgroundResource(R.drawable.mapselectedfr);
                    }
                    else if(language.equals("العربية")){
                        setUp.setBackgroundResource(R.drawable.mapselectedar);
                    }
                    setUp.setVisibility(VISIBLE);
                    next.setVisibility(View.GONE);
                    recentLocation.setVisibility(View.GONE);
                    isLocating = false;
                    setPanel.setVisibility(View.GONE);
                    apply.setVisibility(View.GONE);
                    textWidth.setVisibility(View.GONE);
                    textHeight.setVisibility(View.GONE);
                    textFruit.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                } else {
                    //next button
                    System.out.println("yoo");
                    setPanel.setVisibility(View.GONE);
                    apply.setVisibility(View.GONE);
                    textWidth.setVisibility(View.GONE);
                    textHeight.setVisibility(View.GONE);
                    textFruit.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                }
            }
        });
        recentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap Recentimage = null;
                Recentimage = BitmapFactory.decodeFile(file.getPath());
                mapImage.setImageBitmap(Recentimage);
                mapFragment.getView().setVisibility(View.GONE);
                locate.setVisibility(View.GONE);
                setUp.setVisibility(VISIBLE);
                settings.setVisibility(VISIBLE);
                submit.setVisibility(VISIBLE);
                setUp.setBackgroundResource(R.drawable.mapselected);
                setUp.setVisibility(VISIBLE);
                next.setVisibility(View.GONE);
                recentLocation.setVisibility(View.GONE);
                isLocating = false;
                setPanel.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);
                textWidth.setVisibility(View.GONE);
                textHeight.setVisibility(View.GONE);
                textFruit.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
        });

        setUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (IsConnected()) {
                    map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLng(center));
                    mapFragment.getView().setVisibility(VISIBLE);
                    setPanel.setVisibility(View.GONE);
                    apply.setVisibility(View.GONE);
                    textWidth.setVisibility(View.GONE);
                    textHeight.setVisibility(View.GONE);
                    textFruit.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    locate.setVisibility(VISIBLE);
                    submit.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                    setUp.setVisibility(View.GONE);
                    next.setVisibility(VISIBLE);
                    recentLocation.setVisibility(VISIBLE);
                    locate.setVisibility(VISIBLE);
                    editLenght.setVisibility(View.GONE);
                    editWidth.setVisibility(View.GONE);
                    scroll.setVisibility(View.GONE);
                    isLocating = true;
                    if (language.equals("français")) {
                        Toast.makeText(getApplicationContext(), "Vous Êtes Connectés à L'internet", Toast.LENGTH_LONG).show();
                    } else if (language.equals("العربية")) {
                        Toast.makeText(getApplicationContext(), "أنت متصل بالإنترنت", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You Are Connected To The Internet", Toast.LENGTH_LONG).show();
                    }


                } else {
                    if (language.equals("français")) {
                        Toast.makeText(getApplicationContext(), "Vous N'êtes Pas Connectés à L'internet", Toast.LENGTH_LONG).show();
                    } else if (language.equals("العربية")) {
                        Toast.makeText(getApplicationContext(), "أنت لست متصل بالإنترنت", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You Are Not Connected To The Internet", Toast.LENGTH_LONG).show();
                    }

                    System.out.println(widthVal);

                    setaLad.getLayoutParams().height = lenghtVal;
                    setaLad.getLayoutParams().width = widthVal;
                    mapFragment.getView().setVisibility(View.GONE);
                    setaLad.setVisibility(VISIBLE);
                }
            }
        });
        locate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ContextCompat.checkSelfPermission(PaintActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PaintActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    } else {
                        double[] loc = getLocation();
                        LatLng myLocation = new LatLng(loc[0], loc[1]);
                        map.clear();
                        if (language.equals("français")) {
                            map.addMarker(new MarkerOptions().position(myLocation).title("Votre Position"));
                        }
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(myLocation)
                                .zoom(18)
                                .bearing(0)
                                .build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                } else {
                    buildAlertMessageNoGps();
                }
            }
        });
    }

    public boolean IsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    static int Getwidth() {
        width = Integer.parseInt(widthText.getText().toString());
        return width;
    }

    static void showText() {
        text.setText(paintView.txt);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        double[] loc = getLocation();
        LatLng Hammmamet = new LatLng(gps[0], gps[1]);
        map.addMarker(new MarkerOptions().position(Hammmamet).title("Hammamet"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(Hammmamet));
        //map.setMinZoomPreference(1);
        map.setMapType(map.MAP_TYPE_SATELLITE);
        try {
            map.setMyLocationEnabled(true);
        }catch(SecurityException sec){}
    }

    private double[] getLocation() {
        List<String> providers = locManager.getProviders(true);
        Location l = null;
        for (int i = providers.size() - 1; i >= 0; i--) {
            try {
                l = locManager.getLastKnownLocation(providers.get(i));
                if (l != null) break;
            } catch (SecurityException e) {
            }
        }
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

    public void CaptureMapScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                mapImage.setImageBitmap(bitmap);
                try {
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                }catch(FileNotFoundException error404){}
                catch(IOException eio){}
            }
        };
        map.snapshot(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                return;
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (language.equals("français")) {
            builder.setMessage("Votre GPS semble être désactivé, voulez-vous l'activer?")
                    .setTitle("Services De Localisation")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
        } else if (language.equals("العربية")) {
            builder.setMessage("يبدو أنّ نظام تحديد المواقع غير فعّال، هل تريد تشغيله؟")
                    .setTitle("خدمات تحديد المواقع")
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int id) {
                    dialog.cancel();
                }
            });
        } else {
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setTitle("Location Services")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
        }
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

