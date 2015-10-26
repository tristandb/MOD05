# Picasender
This application sends one byte of an RGB-set over GPIO pins to an FPGA.
## Installing
First, Python must be installed on your Raspberry Pi. I assume you know how to do this.
When Python is installed, you can install picamera. For instructions please read their website: https://picamera.readthedocs.org/en/release-1.10/install2.html.
You'll also need RPi.GPIO. For further instructions, please read https://pypi.python.org/pypi/RPi.GPIO.

# Running

# Ports
Data bit | Data pin
---------|---------
7 	 | 29
6 	 | 31
5 	 | 32
4 	 | 33
3 	 | 35
2 	 | 36
1 	 | 37	
0 	 | 38

Other pins          | Pin
--------------------|-----
Request a new image | 18
Request next data   | 40
Output new data	    | 22

