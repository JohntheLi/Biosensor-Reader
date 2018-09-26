# Biosensor-Reader
Android Application for serial communication with an electrochemical biosensor for detecting mutated BRCA1 genes.

Uses the UsbSerial library to establish UART communication with a biosensor for detecting BRCA1 mutations, which are associated with breast cancer.
The biosensor was created using nanoscale interdigitated electrode arrays (IDAs) found on Surface Acoustic Wave (SAW) filters.
The sensing mechanism was taken from a capacitance sensor that was part of a proximity sensor, and is accurate up to 5 femtofarads.
The sensor readings were collected with a MSP430 microcontroller, which then sent data to the Android application.
