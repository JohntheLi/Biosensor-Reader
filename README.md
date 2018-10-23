# Biosensor Reader
Android Application for serial communication with an electrochemical biosensor for detecting mutated BRCA1 genes.
This readme is an overview of the goals, components, and results of my project.
Abstract: https://aaas.confex.com/aaas/2016/webprogram/Paper18283.html

## Summary
Uses the UsbSerial library to establish UART communication with a biosensor for detecting BRCA1 mutations, which are associated with breast cancer.
The biosensor was created using nanoscale interdigitated electrode arrays (IDAs) found on Surface Acoustic Wave (SAW) filters.
The sensing mechanism was taken from a capacitance sensor that was part of a proximity sensor, and is accurate up to 5 femtofarads.
The sensor readings were collected with a MSP430 microcontroller, which then sent data to the Android application. The holograph library is then used to display the readings on a real-time graph for interpretation by the user.

## Engineering Goals
The major goal of this project is to develop a *portable* and *inexpensive* biosensor with the capability of detecting mutated BRCA1 SNP strands and to enhance its sensitivity using the application of the following technologies:
- Nanoscale Interdigitated Electrode Arrays (IDA) - *picks up capacitance changes when SNP is attached*
- Microfluidic Apparatus - *provides constant flow of sample solution on the IDA surface*
- Android Application - *provides GUI, readability, and accessibility for biosensor readings*

## Overview of Components
![alt text](https://imgur.com/bibz7Bw.jpg)

## Android Application
![alt text](https://imgur.com/GSO6ZD2.png)

By using the microcontroller and an Android application for a UI, the accessibility of my biosensor is greatly improved, as both MCUs and Android devices are relatively cheap and readily available compared to laboratory testing equipment.

## Result
The biosensor created fulfilled the initial goal.
- Capable of detecting low concentrations of mutated BRCA1 genes linked to breast and ovarian cancer.
- Reliable data with little interference from noise, high range of detection and high sensitivity

It can be concluded that this project significantly improves the commercial viability of electrochemical biosensing, and allows for more accessible prognosis of breast cancer.

