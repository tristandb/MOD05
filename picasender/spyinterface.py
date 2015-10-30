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
spi.open(0, 0)
spi.max_speed_hz = 50000000
tmpdir = tempfile.mkdtemp()
filename = os.path.join(tmpdir, 'breakoutpipe')
os.mkfifo(filename)
print "Namedpipe created at: " + filename


def sendPipe(location):
    fifo = open(filename, 'w')
    print >> fifo, location
    fifo.close()


with picamera.PiCamera() as camera:
    while True:
        print "NR"
        with picamera.array.PiRGBArray(camera) as output:
            camera.resolution = (256, 144)
            camera.capture(output, 'rgb')
            list = output.array.ravel().tolist()
            sendlist = []
            print "Sending color: " + str(list[len(list) - 5]) + str(list[len(list) - 4]) + str(
                list[len(list) - 3]) + str(list[len(list) - 2]) + str(list[len(list) - 1])
            for x in range(0, len(list), 1):
                sendlist.append(list[x])
                #print "Send " + str(list[x])
                if len(sendlist) == 4096 or x == (len(list) - 1):
                    result = spi.xfer2(sendlist)
                    sendlist = []
                    for y in range(0, len(result)):
                        if result[y] != 255 and result[y] != 0:
                            print "Received: " + str(result[y])
        #time.sleep(10)
    os.remove(filename)
    os.rmdir(tmpdir)

