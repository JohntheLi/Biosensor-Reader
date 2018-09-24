package com.example.john.biosensorreader;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    TextView mDataText;

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                tvAppend(mDataText, data);
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
                }
                data = data.concat("\n");
                tvAppend(mDataText, data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataText = (TextView) findViewById(R.id.dataText);

        //UsbDevice device = ((Global) this.getApplication()).getDevice();
        //UsbDeviceConnection connection = ((Global) this.getApplication()).getConnection();


        final UsbSerialDevice serialPort = ((Global) this.getApplication()).getSerialDevice();
        if (serialPort != null) {
            if (serialPort.open())//Set Serial Connection Parameter
            {
                serialPort.setBaudRate(9600);
                serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);

                serialPort.write("5".getBytes());
                serialPort.read(mCallback);
                tvAppend(mDataText, "Serial Connection Opened!\n");
            } else {
                Log.d("SERIAL", "PORT NOT OPEN");
            }
        } else {
            Log.d("SERIAL", "PORT IS NULL");
        }

        /*
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
        */


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

}
