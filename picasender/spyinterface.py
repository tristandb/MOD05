import io
import spidev
import time
import picamera
import cv2
import numpy as np
import os, tempfile
from PIL import Image


tosend = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 137, 137, 176, 0, 0, 0, 0, 0, 0, 0, 137, 176]
tosend = [176, 176, 176, 176, 176]
# Setup pipes
tmpdir = tempfile.mkdtemp()	
locationpipe = os.path.join(tmpdir, 'breakoutlocationpipe')
imagepipe = os.path.join(tmpdir, 'breakoutimagepipe')
os.mkfifo(locationpipe)
os.mkfifo(imagepipe)
print "Namedpipe created at: " + imagepipe
print "Namedpipe create at: " + locationpipe


def sendPipe(location, filename):
    fifo = open(filename, 'w+')
    print >> fifo, location
    fifo.close()

# Setup PIL
tumbsize = 200, 150
stream = io.BytesIO()
boundaries = [
    ([2, 2, 160], [90, 90, 255])
]

# Setup SPI
spi = spidev.SpiDev()
spi.open(0, 0)
spi.max_speed_hz = 50000000

with picamera.PiCamera() as camera:
    # Camera resolution
    camera.resolution = (600, 300)
    
    # Make pictures until interrupted
    while True:
 	time.sleep(1)
        print "Next picture"
	# Reset stream
        stream = io.BytesIO()
	
	# Capture image
        camera.start_preview()
        camera.capture(stream, format='jpeg')
	
	# Get data from stream
        data = np.fromstring(stream.getvalue(), dtype=np.uint8)
        image = cv2.imdecode(data, 1)
        for (lower, upper) in boundaries:
            # Create NumPy arrays
            lower = np.array(lower, dtype="uint8")
            upper = np.array(upper, dtype="uint8")

            # Find colors and apply mask
            mask = cv2.inRange(image, lower, upper)
            output = cv2.bitwise_and(image, image, mask=mask)
            sendlist = output[:, :, 2].tolist()
	    sendravel = output[:,:, 2].ravel().tolist()
	    sendravel = tosend
		#sendlist = tosend
            # Save images (can be commented out for more speed)
            img = Image.fromarray(output[:, :, ::-1])
            img.save('/var/www/imgr.jpeg')
            imgor = Image.fromarray(image[:, :, ::-1])
            imgor.save('/var/www/imgor.jpeg')
            imgtumb = Image.fromarray(image[:, :, ::-1])
            imgtumb.thumbnail(tumbsize)
            #imgtumb.save('/var/www/imgtumb.jpeg')
	 	
	    # Add data to pipe
            sendPipe(imgtumb, imagepipe)
            # Make calculations
            countx = 0
            county = 0
            matches = 0
	    for z in range(0, len(sendravel), 1):
	    	result = spi.xfer([sendravel[z]])
	    	print result
	
            for x in range(0, len(sendlist), 1):
                	# Calculate the location on the RPi
		#result = spi.xfer(sendlist[x])
		
                for y in range(0, len(sendlist[x]),1):
                       	if sendlist[x][y] > 0:
                               	countx = countx + y
                               	county = county + x
                               	matches = matches + 1
			#if result[y] != 0:
			#	print result[y]

            # Send location trough pipe
	    if matches > 0:
            	sendPipe(str(countx/matches), locationpipe)
	    	print "location: (" + str((countx/matches)) + "," +str(county/matches) + ")"
	    	print "matches: " + str(matches)
	    else:
		print "No location found"
