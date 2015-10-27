import time
import binascii
import picamera
import RPi.GPIO as GPIO
import sys
from math import *

#NOT: 11, 12, 29, and 30
# Define ports and clockspeed
# TODO: Set correct pins
CLOCK_INPUT = 36
CLOCK_OUTPUT = 35
NEW_IMAGE_INPUT = 33
CLOCK_SPEED = 50.0 * 1000.0
# First output = GPIO_PIN_ARRAY[0]
GPIO_FIRST_OUTPUT = 4
# 8 pins to send the data over
GPIO_PIN_ARRAY = [13, 15, 16, 18, 22, 31, 32, 38]
GPIO_PIN_READ = 37

def text_to_bits(text, encoding='utf-8', errors='surrogatepass'):
	result = bin(ord(text))
	print result
        return result[2:]

# Cleanup GPIO
GPIO.cleanup()
# Set Input/Output ports
GPIO.setmode(GPIO.BOARD)
GPIO.setup(CLOCK_INPUT, GPIO.IN)
GPIO.setup(CLOCK_OUTPUT, GPIO.OUT)
GPIO.setup(NEW_IMAGE_INPUT, GPIO.IN)
GPIO.setup(GPIO_PIN_READ, GPIO.IN)
for z in GPIO_PIN_ARRAY:
	GPIO.setup(z, GPIO.OUT)
class CameraWriter:
	# Color to search for
	color = 0
	# Initialize class with color
	def __init__(self, color):
		self.color = color
	# Write to GPIO pins
	def write(self, string):
		print "Called write object"
		currentState = 0
		# Loop string with increments of three
		print "color: " + str(color) + " stringlen:" + str(len(string))
		for x in range (color, len(string), 3):
			print "Queuing data number: " + str(x)
			print "Read GPIO_PIN_READ: " + str(GPIO.input(GPIO_PIN_READ))
			# while True, used for polling the pins
			while True:
				# Check if edge has changed
				if currentState != GPIO.input(CLOCK_INPUT):
					print "Sending data number: " + str(x)
					# Set current state to new state
					currentState = (currentState + 1) % 2
					toPin = string[x]
					# Set output pins
					toPinBinary = text_to_bits(toPin)
					count = 0
					for y in GPIO_PIN_ARRAY:
						print "Output of pin: " + str(y) +  "is set to: " + str(toPinBinary[count])
						GPIO.output(y, int(toPinBinary[count]))
						count += 1
					# Let the FPGA know values have changed
					GPIO.output(CLOCK_OUTPUT, (currentState+1)%2) 
					# Break while True
					break
				# Sleep for 1/4th of the clock cycle of the FPGA
			
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
			print "Calling writer"
			newImage = (newImage + 1) % 2
			camera.capture(cw, 'rgb')
