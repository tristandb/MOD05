# Picasender
This application sends one byte of an RGB-set over GPIO pins to an FPGA.
## Installing
First, Python must be installed on your Raspberry Pi. I assume you know how to do this.
When Python is installed, you can install picamera. For instructions please read their website: https://picamera.readthedocs.org/en/release-1.10/install2.html.
You'll also need RPi.GPIO. For further instructions, please read https://pypi.python.org/pypi/RPi.GPIO.

# Running

# Ports
Data bit | Data pin | Altera pin
---------|----------|-----------
7 	 | 13	    | color
6 	 | 15	    |	color
5 	 | 16	    |	color
4 	 | 18     | color
3 	 | 22	    | color
2 	 | 31	    |	color
1 	 | 32	    | color
0 	 | 38	    |	color

Other pins          | Pin | Altera pin
--------------------|-----|------------
Request a new image | 33  | frame
Request next data   | 35  | received
Output new data	    | 36  | receive
Serial location     | 37  | location

