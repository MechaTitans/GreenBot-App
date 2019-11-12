package com.machatitans.greenbot;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;



import java.util.Set;

public class deviceList extends ListActivity {

    private BluetoothAdapter BTH2 = null;
    static String MAC_ADDRESS = null;
    static String MacAddress = "98:D3:61:FD:4E:AA";
    boolean macTrue = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        BTH2 = BluetoothAdapter.getDefaultAdapter();


        Set<BluetoothDevice> PairedDevices = BTH2.getBondedDevices();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BTH2.startDiscovery();
        registerReceiver(mReceiver, filter);
        BTH2.startDiscovery();
        if(PairedDevices.size()> 0) {
            for(BluetoothDevice Device : PairedDevices){
                String Btname = Device.getName();
                String BtMAC = Device.getAddress();
                ArrayBluetooth.add(Btname + "\n" + BtMAC);
            }
        }

        setListAdapter(ArrayBluetooth);

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String s = device.getAddress();
                System.out.println(device.getAddress());
                if(device.getName().equals("HC-06")){
                    macTrue = true;
                    Intent returnMAC = new Intent();
                    returnMAC.putExtra(MAC_ADDRESS,MacAddress);
                    deviceList.this.setResult(RESULT_OK, returnMAC);
                    Intent myIntent= new Intent(context, PaintActivity.class);
                    startActivity(myIntent);
                    finish();
                    unregisterReceiver(mReceiver);
                    System.out.println("HC-06hhfgff");
                }
            }
        }
    };
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        String generalInformation = ((TextView) v).getText().toString();
        String MACaddress = generalInformation.substring(generalInformation.length() - 17);
        if(macTrue){
            Toast.makeText(getApplicationContext(), "Info: " + MACaddress, Toast.LENGTH_LONG).show();
            Intent returnMAC = new Intent();
            returnMAC.putExtra(MAC_ADDRESS,MACaddress);
            setResult(RESULT_OK, returnMAC);
            Intent myIntent= new Intent(v.getContext(), PaintActivity.class);

            startActivity(myIntent);
            finish();
        }

    }



}
