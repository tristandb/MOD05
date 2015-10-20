import time
import picamera
import RPi.GPIO as GPIO
import sys

class CameraWriter(object):
	# Initialize class
	color = 0
	def __init__(self, color):
		self.color = color
	def write(self, string):
		# TODO: Split up strings
		var = 0

with picamera.PiCamera() as camera:
        # Set color
        if len(sys.argv) > 1:
            if sys.argv[1] == "g":
                color = 1
            elif sys.argv[1] == "b":
                color = 2
            else:
                color = 0
        else:
            color = 0

        # Set camera color
	
	cw = CameraWriter(color)
	 
        camera.capture(cw, 'rgb')


