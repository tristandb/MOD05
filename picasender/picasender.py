import time
import picamera
import RPi.GPIO as GPIO
import sys

with picamera.PiCamera() as camera:
        # Set color
        if len(sys.argv) > 1:
            if sys.argv[1] == "g":
                camera = 1
            elif sys.argv[1] == "b":
                camera = 2
            else:
                camera = 0
        else
            camera = 0

        # Set camera color
        camera.resolution(1280, 720)
        setCameraPreferences()

        camera.capture(variable, 'rgb')

        print "%s" % variable
        

def setCameraPreferences:
    camera.sharpness = 0
    camera.contrast = 0
    camera.brightness = 50
    camera.saturation = 0
    camera.ISO = 0
    camera.video_stabilization = False
    camera.exposure_compensation = 0
    camera.exposure_mode = 'auto'
    camera.meter_mode = 'average'
    camera.awb_mode = 'auto'
    camera.image_effect = 'none'
    camera.color_effects = None
    camera.rotation = 0
    camera.hflip = False
    camera.vflip = False
    camera.crop = (0.0, 0.0, 1.0, 1.0)
