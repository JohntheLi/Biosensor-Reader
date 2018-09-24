package com.example.john.biosensorreader;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<Integer> reading1 = new ArrayList<Integer>();

    TextView mFortuneText;
    Button mStartButton;
    Button mStopButton;
    Button mGraphButton;
    Boolean stopFlag = false;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {});
    int counter = 2;

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                if (!data.contains("s") & !data.equals(null)) {
                    List<String> dataList = Arrays.asList(data.split(","));
                    System.out.println("List: " + dataList);
                    String[] convertList = new String[4];
                    String digits = "0123456789ABCDEF";
                    int offset = 0;

                    for (int j = 0; j < 4; j++) {
                        String temp = dataList.get(j).toUpperCase();
                        temp = temp.trim();
                        int decVal = 0;
                        for (int i = 0; i < temp.length(); i++) {
                            char c = temp.charAt(i);
                            int d = digits.indexOf(c);
                            decVal = 16 * decVal + d;
                        }
                        System.out.println("decVal: " + decVal);
                        BigDecimal b1,b2,b3;
                        b1 = new BigDecimal(decVal);
                        b2 = new BigDecimal("524288");

                        b3 = b1.divide(b2, 4, RoundingMode.CEILING);
                        convertList[j] = (String.valueOf(b3));
                    }
                    data = convertList[0] + "," + convertList[1] + "," + convertList[2] + "," + convertList[3];
                    //reading1.add(Integer.parseInt(convertList[0]));
                    //tvAppend(mFortuneText, convertList[0]);
                    graphAppend(series, counter, Double.parseDouble(convertList[0]));
                    counter++;
                }

                data = data.concat("\n");
                tvAppend(mFortuneText, data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //GUI
        mFortuneText = (TextView) findViewById(R.id.fortuneText);
        mStartButton = (Button) findViewById(R.id.startButton);
        mGraphButton = (Button) findViewById(R.id.graphButton);
        mStopButton = (Button) findViewById(R.id.stopButton);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(1.7);
        graph.getViewport().setMaxY(2.1);

        Intent intent = getIntent();
        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        //((Global) MainActivity.this.getApplication()).setSomeVariable(device);
        Toast toast = Toast.makeText(getApplicationContext(), device.getDeviceName(), Toast.LENGTH_SHORT);
        toast.show();

        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbInterface intf = device.getInterface(0);
        UsbDeviceConnection connection = mUsbManager.openDevice(device);
        //((Global) MainActivity.this.getApplication()).setConnection(connection);
        connection.claimInterface(intf, true);

        //begin serial
        final UsbSerialDevice serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
        //((Global) MainActivity.this.getApplication()).setSerialDeviceDevice(serialPort);
        if (serialPort != null) {
            if (serialPort.open())//Set Serial Connection Parameter
            {
                serialPort.setBaudRate(9600);
                serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
            } else {
                Log.d("SERIAL", "PORT NOT OPEN");
            }
        } else {
            Log.d("SERIAL", "PORT IS NULL");
        }
        //On button click:
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serialPort != null) {
                    serialPort.write("5".getBytes());
                    if (!stopFlag)
                        serialPort.read(mCallback);
                    tvAppend(mFortuneText, "Serial Connection Opened!\n");
                }
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serialPort != null) {
                    serialPort.write("6".getBytes());
                    stopFlag = true;
                }
            }
        });
        mGraphButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(serialPort != null)
                {
                    serialPort.write("6".getBytes());
                    stopFlag = true;
                    //serialPort.close();
                }
                /*Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
                MainActivity.this.startActivity(myIntent);
                finish();*/
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void tvAppend(TextView tv, CharSequence text)
    {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                ftv.append(ftext);
            }
        });
    }
    private void graphAppend(LineGraphSeries s, int x, double y)
    {
        final LineGraphSeries fs = s;
        final double yval = y;
        final int xval = x;
        runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                fs.appendData(new DataPoint(xval,yval),true,40);
            }
        });
    }
}
