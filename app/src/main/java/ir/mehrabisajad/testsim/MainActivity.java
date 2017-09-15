package ir.mehrabisajad.testsim;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.kirianov.multisim.MultiSimTelephonyManager;

public class MainActivity extends AppCompatActivity {
    private final static int READ_PHONE_STATE_REQUEST_CODE = 12;

    private MultiSimTelephonyManager multiSimTelephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_STATE_REQUEST_CODE);
            return;
        }

        multiSimTelephonyManager = new MultiSimTelephonyManager(this, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateInfo();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    multiSimTelephonyManager = new MultiSimTelephonyManager(this, new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            updateInfo();
                        }
                    });
                } else {
                    Toast.makeText(this, "سطح دسترسی ها لغو شد", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void updateInfo() {
        // for update UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                multiSimTelephonyManager.update();
                useInfo();
            }
        });
        // for update background information
        multiSimTelephonyManager.update();
        useInfo();
    }

    public void useInfo() {

        // get number of slots:
        if (multiSimTelephonyManager != null) {
            multiSimTelephonyManager.sizeSlots();
        }
        String str = "";
        // get info from each slot:
        if (multiSimTelephonyManager != null) {
            for (int i = 0; i < multiSimTelephonyManager.sizeSlots(); i++) {
                str += "\r\nImei = " + multiSimTelephonyManager.getSlot(i).getImei();
                str += "\r\nImsi = " + multiSimTelephonyManager.getSlot(i).getImsi();
                str += "\r\nSimSerialNumber = " + multiSimTelephonyManager.getSlot(i).getSimSerialNumber();
                str += "\r\nSimState = " + multiSimTelephonyManager.getSlot(i).getSimState();
                str += "\r\nSimOperator = " + multiSimTelephonyManager.getSlot(i).getSimOperator();
                str += "\r\nSimOperatorName = " + multiSimTelephonyManager.getSlot(i).getSimOperatorName();
                str += "\r\nSimCountryIso = " + multiSimTelephonyManager.getSlot(i).getSimCountryIso();
                str += "\r\nNetworkOperator = " + multiSimTelephonyManager.getSlot(i).getNetworkOperator();
                str += "\r\nNetworkOperatorName = " + multiSimTelephonyManager.getSlot(i).getNetworkOperatorName();
                str += "\r\nNetworkCountryIso = " + multiSimTelephonyManager.getSlot(i).getNetworkCountryIso();
                str += "\r\nNetworkType = " + multiSimTelephonyManager.getSlot(i).getNetworkType();
                str += "\r\nNetworkRoaming = " + multiSimTelephonyManager.getSlot(i).isNetworkRoaming();
                str += "\r\n--------------";
            }
        }

        ((TextView) findViewById(R.id.txt)).setText(str);
    }

}
