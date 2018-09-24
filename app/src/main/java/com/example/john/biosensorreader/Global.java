package com.example.john.biosensorreader;

import android.app.Application;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.felhr.usbserial.UsbSerialDevice;


public class Global extends Application {

    private UsbDevice device;
    private UsbDeviceConnection connection;
    private UsbSerialDevice serialDevice;

    public UsbDevice getDevice() {
        return device;
    }
    public UsbDeviceConnection getConnection() {
        return connection;
    }
    public UsbSerialDevice getSerialDevice() {
        return serialDevice;
    }

    public void setSomeVariable(UsbDevice device) {
        this.device = device;
    }
    public void setSerialDeviceDevice(UsbSerialDevice serialDevice) {
        this.serialDevice = serialDevice;
    }

    public void setConnection(UsbDeviceConnection connection) {
        this.connection = connection;
    }


}
