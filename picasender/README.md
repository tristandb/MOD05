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
7 	 | 07	    | 
6 	 | 11	    |	
5 	 | 12	    |	
4 	 | 13       |
3 	 | 15	    |
2 	 | 16	    |		
1 	 | 18	    |
0 	 | 22	    |	

Other pins          | Pin | Altera pin
--------------------|-----|------------
Request a new image | 29  |
Request next data   | 31  |
Output new data	    | 32  |

