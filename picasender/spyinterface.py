import time
import picamera
import picamera.array
import spidev
import sys
import numpy as np
import os
import tempfile
from math import ceil

# Configure SpiDev
spi = spidev.SpiDev()
spi.open(0,0)
spi.max_speed_hz = 50000000
tmpdir = tempfile.mkdtemp()
filename = os.path.join(tmpdir, 'breakoutpipe')
os.mkfifo(filename)
print "Namedpipe created at: " + filename

def sendPipe(location):
	fifo = open(filename, 'w')
	print >> fifo, location
	fifo.close()
sendPipe(400)
with picamera.PiCamera() as camera:
    	while True:
		with picamera.array.PiRGBArray(camera) as output:
    		    	camera.resolution = (256,144)
       			camera.capture(output, 'rgb')
			list = output.array.ravel()
			chunks = ceil((len(list)/4096))		
			sendlist = np.array(np.split(list, chunks)).tolist() 
			#print sendlist
			for x in range(0, len(sendlist)):
				result = spi.xfer(sendlist[x])
				for y in range(0, len(result)):
					if result[y] != 0:
						sendPipe(result[y])
						print(result[y])
	os.remove(filename)
	os.rmdir(tmpdir)
