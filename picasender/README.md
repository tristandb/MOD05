# Picasender
This application sends one byte of an RGB-set over SPI to the FPGA.
## Installing
First, Python must be installed on your Raspberry Pi. I assume you know how to do this.
When Python is installed, you can install [picamera](https://picamera.readthedocs.org/en/release-1.10/install2.html). You'll also need [py-spidev](https://github.com/doceme/py-spidev), which is included in this folder. Finally, you might need to install [NumPy](http://docs.scipy.org/doc/numpy/user/install.html). Most Linux distributions will provide packages for NumPy. [CV2](http://opencv-python-tutroals.readthedocs.org/) is also needed.
The camera on your Raspberry Pi might not be enabled by default. You can enable it by typing `sudo raspi-config` and enabling *picamera*.

## Connecting Raspberry Pi to the FPGA
//TODO

## Running
Running the application is very easy. Use `sudo python spyinterface.py`. This will run the camera and the GUI of Augmented Breakout. Locations aquired by the FPGA will be passed trough a named pipe to the GUI.

## Other remarks


