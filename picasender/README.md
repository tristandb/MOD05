# Picasender
This application sends one byte of an RGB-set over GPIO pins to an FPGA.
## Installing
First, Python must be installed on your Raspberry Pi. I assume you know how to do this.
When Python is installed, you can install picamera. For instructions please read their website: https://picamera.readthedocs.org/en/release-1.10/install2.html.
You'll also need RPi.GPIO. For further instructions, please read https://pypi.python.org/pypi/RPi.GPIO.

# Running

# Ports
Data bit | Data pin | Altera pin | GPIO PIN
---------|----------|------------|---------- 
7 	 | 13	    | color | 27
6 	 | 15	    |	color | 22
5 	 | 16	    |	color | 23
4 	 | 18     | color | 24
3 	 | 22	    | color | 25
2 	 | 31	    |	color | 06
1 	 | 32	    | color | 12
0 	 | 38	    |	color | 20

Other pins          | Pin | Altera pin | GPIO Pin
--------------------|-----|------------|----------
Request a new image | 33  | frame | 13
Request next data   | 35  | received | 19 
Output new data	    | 36  | receive | 16
Serial location     | 37  | location | 26

