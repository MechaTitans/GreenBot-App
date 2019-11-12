package com.machatitans.greenbot;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button connect ;
    boolean connected = false;
    String line;
    static String language= "English";
    FileOutputStream outputStream;

    private static final int ACTIVATION_REQUEST = 1;
    private static final int CONNECTION_REQUEST = 2;

    BluetoothAdapter BTAdapter = null;
    BluetoothDevice BTDevice = null;
    BluetoothSocket BTSocket = null;
    LocationManager locManager;

    private static String NAME = null;
    private static String MAC = null;

    static UUID MyUuid = null;
    TextView welcome;
    TextView des;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            FileInputStream in = openFileInput("data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                if ((line = bufferedReader.readLine())!= null){
                    language = line;
                }

            }catch (Exception e){
                language = "English";
            }
        }catch (FileNotFoundException e){

        }
        super.onCreate(savedInstanceState);
        //System.out.println(language);
        setContentView(R.layout.activity_main);
        welcome = findViewById(R.id.welcome);
        des = findViewById(R.id.des);
        //BTAdapter = BluetoothAdapter.getDefaultAdapter();

        connect = (Button)findViewById(R.id.connect);
        //Drawable d = getResources().getDrawable(R.drawable.applybtn);
        //connect.setBackground(d);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(language.equals("français")) {
            Drawable connectfr = getResources().getDrawable(R.drawable.connectfr);
            connect.setBackground(connectfr);
            welcome.setText("Bienvenue à GreenBot");
            des.setText("On présente notre nouvelle solution pour l'agriculture");
        }else if (language.equals("العربية")){
            Drawable connectar = getResources().getDrawable(R.drawable.connectar);
            connect.setBackground(connectar);
            welcome.setText("مرحبا في GreenBot");
            des.setText("نقدم لكم حلولا جديدة لدعم الفلاحة");
        }else if(language.equals("English")){
            Drawable connecten = getResources().getDrawable(R.drawable.connect);
            connect.setBackground(connecten);
            welcome.setText("Welcome To GreenBot");
            des.setText("Introducing our new solution for agriculture");
        }

        if (BTAdapter == null) {
            if(language.equals("français")) {
                Toast.makeText(getApplicationContext(), "Cet Appareil N'a Pas BlueTooth", Toast.LENGTH_LONG).show();
            }else if (language.equals("العربية")){
                Toast.makeText(getApplicationContext(), "لا يحتوي هذا الجهاز على خدمة BlueTooth", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "This Device Doesn't Have BlueTooth", Toast.LENGTH_LONG).show();
            }
        }else {
            if (!BTAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ACTIVATION_REQUEST);
            }
        }
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }
        connect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               /*if (connected){
                    try{
                        BTSocket.close();
                        connected = false;
                        if(language.equals("français")) {
                            Toast.makeText(getApplicationContext(), "Déconnecté", Toast.LENGTH_LONG).show();
                        }else if (language.equals("العربية")){
                            Toast.makeText(getApplicationContext(), "قطع الإتصال", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
                        }
                    }catch (IOException error){
                        Toast.makeText(getApplicationContext(), "ERROR" + error, Toast.LENGTH_LONG).show();
                    }
                }else{


*/
                Intent myIntent= new Intent(v.getContext(), PaintActivity.class);
                startActivity(myIntent);

            }
    });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode , Intent data){

        switch(requestCode){
            case ACTIVATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    if(language.equals("français")) {
                        Toast.makeText(getApplicationContext(), "BlueTooth A Été Activé", Toast.LENGTH_LONG).show();
                    }else if (language.equals("العربية")){
                        Toast.makeText(getApplicationContext(), "تم تفعيل خدمة BlueTooth", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "BlueTooth Has Been Activated", Toast.LENGTH_LONG).show();
                    }
                }else if(resultCode == Activity.RESULT_CANCELED) {
                    if(language.equals("français")) {
                        Toast.makeText(getApplicationContext(), "BlueTooth N'a Pas Été Activé", Toast.LENGTH_LONG).show();
                    }else if (language.equals("العربية")){
                        Toast.makeText(getApplicationContext(), "لم يتم تفعيل خدمة BlueTooth", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "BlueTooth Hasn't Been Activated", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case CONNECTION_REQUEST:
                if(resultCode == Activity.RESULT_OK){
                    MAC =deviceList.MacAddress;
                    BTDevice = BTAdapter.getRemoteDevice(MAC);
                    NAME = BTDevice.getName();
                    System.out.println("Name " + NAME);
                    MyUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    try{
                        BTSocket = BTDevice.createRfcommSocketToServiceRecord(MyUuid);
                    } catch (Exception e1) {
                        Toast.makeText(getApplicationContext(), "Failed To Create Socket", Toast.LENGTH_LONG).show();
                    }
                    try {
                        BTSocket.connect();

                        connect.setText("Disconnect From GreenBot");
                        if(language.equals("français")) {
                            Toast.makeText(getApplicationContext(), "Connecté à : " + NAME, Toast.LENGTH_LONG).show();
                        }else if (language.equals("العربية")){
                            Toast.makeText(getApplicationContext(), "تم الإتصال بـ : " + NAME, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();
                        }
                    } catch(IOException e2) {
                        try {
                            Toast.makeText(getApplicationContext(), "Trying Fallback...", Toast.LENGTH_LONG).show();
                            BTSocket = (BluetoothSocket) BTDevice.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class}).invoke(BTDevice, MyUuid);
                            connected = true;
                            BTSocket.connect();
                            connect.setText("Disconnect From GreenBot");
                            if(language.equals("français")) {
                                Toast.makeText(getApplicationContext(), "Connecté à :" + NAME, Toast.LENGTH_LONG).show();
                            }else if (language.equals("العربية")){
                                Toast.makeText(getApplicationContext(), "تم الإتصال بـ :" + NAME, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();
                            }
                        } catch(Exception e3) {
                            MyUuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
                            try {
                                BTSocket = BTDevice.createRfcommSocketToServiceRecord(MyUuid);
                            } catch (Exception e4) {
                                Toast.makeText(getApplicationContext(), "Failed To Create Socket", Toast.LENGTH_LONG).show();
                            }
                            try {
                                BTSocket.connect();
                                connected = true;
                                connect.setText("Disconnect From GreenBot");
                                if(language.equals("français")) {
                                    Toast.makeText(getApplicationContext(), "Connecté à : " + NAME, Toast.LENGTH_LONG).show();
                                }else if (language.equals("العربية")){
                                    Toast.makeText(getApplicationContext(), "تم الإتصال بـ : " + NAME, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e5) {
                                Toast.makeText(getApplicationContext(), "Trying Fallback...", Toast.LENGTH_LONG).show();
                                try {
                                    BTSocket = (BluetoothSocket) BTDevice.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class}).invoke(BTDevice, MyUuid);
                                    connected = true;
                                    BTSocket.connect();
                                    connect.setText("Disconnect From GreenBot");
                                    if(language.equals("français")) {
                                        Toast.makeText(getApplicationContext(), "Connecté à : " + NAME, Toast.LENGTH_LONG).show();
                                    }else if (language.equals("العربية")){
                                        Toast.makeText(getApplicationContext(), "تم الإتصال بـ : " + NAME, Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e6) {
                                    try {
                                        BTSocket = (BluetoothSocket) BTDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(BTDevice, 1);
                                        connected = true;
                                        BTSocket.connect();
                                        connect.setText("Disconnect From GreenBot");
                                        if(language.equals("français")) {
                                            Toast.makeText(getApplicationContext(), "Connecté à : " + NAME, Toast.LENGTH_LONG).show();
                                        }else if (language.equals("العربية")){
                                            Toast.makeText(getApplicationContext(), "تم الإتصال بـ : " + NAME, Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();
                                        }
                                    }catch (Exception e7) {
                                        if(language.equals("français")) {
                                            Toast.makeText(getApplicationContext(), "Échec De Connection à : " + NAME, Toast.LENGTH_LONG).show();
                                        }else if (language.equals("العربية")){
                                            Toast.makeText(getApplicationContext(), "فشل الإتصال بـ : " + NAME, Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Failed to Connect To : " + NAME, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /*}else{
                        text2.setText("Not Compatible");
                    }*/
                }else{
                    if(language.equals("français")) {
                        Toast.makeText(getApplicationContext(), "Échec D'obtenir L'adresse MAC", Toast.LENGTH_LONG).show();
                    }else if (language.equals("العربية")){
                        Toast.makeText(getApplicationContext(), "فشل في الحصول على عنوان MAC", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Failed To Get The MAC Address", Toast.LENGTH_LONG).show();
                    }
                }

        }
        if(connected){
            Intent myIntent = new Intent(this, PaintActivity.class);
            startActivity(myIntent);
        }

    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(language.equals("français")) {
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
        }else if (language.equals("العربية")){
            builder.setMessage("يبدو أنّ نظام تحديد المواقع غير فعّال، هل تريد تشغيله؟")
                    .setTitle("خدمات تحديد المواقع")
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
        }else{
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

    public void changeFr (View view){
        language = "français";
        try {
            outputStream = openFileOutput("data.txt", Context.MODE_PRIVATE);
            outputStream.write(language.getBytes());
            outputStream.close();
        }
        catch (FileNotFoundException E){
        } catch (IOException e) {

        }

        recreate();
    }
    public void changeAr (View view){
        language = "العربية";
        try {
            outputStream = openFileOutput("data.txt", Context.MODE_PRIVATE);
            outputStream.write(language.getBytes());
            outputStream.close();
        }
        catch (FileNotFoundException E){
        } catch (IOException e) {

        }
        recreate();

    }
    public void changeEn (View view) {
        language = "english";
        try {
            outputStream = openFileOutput("data.txt", Context.MODE_PRIVATE);
            outputStream.write(language.getBytes());
            outputStream.close();
        } catch (FileNotFoundException E) {
        } catch (IOException e) {

        }
        recreate();
    }
    static String getLanguage(){
        return language;
    }
}