import sys
import io
import spidev
import time
import picamera
import cv2
import numpy as np
#import os, tempfile
from PIL import Image

if len(sys.argv) != 3:
	print "Not enough arguments given."
	print "Usage: %s <locationpipe> <videopipe>"
	sys.exit(1)

locationpipe = sys.argv[1]

if sys.argv[2] == "--enablespi":
	enablespi = True
else:
	enablespi = False

print "Using pipe: " + locationpipe


def sendPipe(location, filename):
	fifo = open(filename, 'wb+')
	fifo.write(location)
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
		# Reset stream
		stream = io.BytesIO()

		# Capture image
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
			#sendlist = tosend
			# Save images (can be commented out for more speed)
			# img = Image.fromarray(output[:, :, ::-1])
			# img.save('/var/www/imgr.jpeg')
			# imgor = Image.fromarray(image[:, :, ::-1])
			# imgor.save('/var/www/imgor.jpeg')
			#imgtumb = Image.fromarray(image[:, :, ::-1])
			#imgtumb.thumbnail(tumbsize)
			#imgtumb.save('/var/www/imgtumb.jpeg')

			# Add data to pipe
			#sendPipe(imgtumb, imagepipe)
			# Make calculations
			countx = 0
			county = 0
			matches = 0

			for x in range(0, len(sendlist), 1):
				# Calculate the location on the RPi
				#result = spi.xfer(sendlist[x])

				for y in range(0, len(sendlist[x]),1):
					if sendlist[x][y] > 0:
						countx += y
						county += x
						matches += 1


			# Send location trough pipe
			if matches > 0:
				pos = int(countx/matches)
				if enablespi:
					spi.xfer(pos)
				print "SEND: %d, %d  - Total: %d\n" % (((pos >> 8) & 0xFF), (pos & 0xFF), pos)
				sendPipe(bytearray([((pos >> 8) & 0xFF), (pos & 0xFF)]), locationpipe)
			else:
				if enablespi:
					spi.xfer([0])
