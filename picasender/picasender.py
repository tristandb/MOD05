import time
import binascii
import picamera
import RPi.GPIO as GPIO
import sys
from math import *

# Define ports and clockspeed
# TODO: Set correct pins
CLOCK_INPUT = 1
CLOCK_OUTPUT = 2
NEW_IMAGE_INPUT = 3
CLOCK_SPEED = 50.0 * 1000.0 * 1000.0
GPIO_FIRST_OUTPUT = 4

# Set Input/Output ports
GPIO.setmode(GPIO.BCM)
GPIO.setup(CLOCK_INPUT, GPIO.IN)
GPIO.setup(CLOCK_OUTPUT, GPIO.OUT)
GPIO.setup(NEW_IMAGE_INPUT, GPIO.IN)
for z in range (0, 8):
	GPIO.setup(z, GPIO.OUT)

class CameraWriter(object):
	# Color to search for
	color = 0
	# Convert a character to binary
	def text_to_bits(text, encoding='utf-8', errors='surrogatepass'):
   	 	bits = bin(int(binascii.hexlify(text.encode(encoding, errors)), 16))[2:]
    		return bits.zfill(8 * ((len(bits) + 7) // 8))
	# Initialize class with color
	def __init__(self, color):
		self.color = color
	# Write to GPIO pins
	def write(self, string):

		currentState = 0
		# Loop string with increments of three
		for x in range (color, len(string), 3):
			# while True, used for polling the pins
			while True:
				# Check if edge has changed
				if currentstate != GPIO.input(CLOCK_INPUT):
					# Set current state to new state
					currentState = (currentState + 1) % 2
					toPin = string[x]
					# Set output pins
					toPinBinary = text_to_bits(toPin)
					for y in range (0, 8):
						GPIO.output(y + GPIO_FIRST_OUTPUT, toPinBinary[y])
					# Let the FPGA know values have changed
					GPIO.output(CLOCK_OUTPUT, currentState) 
					# Break while True
					break
				# Sleep for 1/4th of the clock cycle of the FPGA
				time.sleep(1.0/(float(CLOCK_SPEED)/2.0))

# Start camera
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
	newImage = 0
	# While loop, keep checking for new image changes
	while True:
		# If the FPGA demands a new picture, take a new picture.
		if newImage != GPIO.input(NEW_IMAGE_INPUT):	
			newImage = (newImage + 1) % 2
			camera.capture(cw, 'rgb')
		time.sleep(1.0/(float(CLOCK_SPEED)/2))


