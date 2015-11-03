import spidev

# Configure SpiDev
spi = spidev.SpiDev()
spi.open(0,1)
spi.max_speed_hz = 5000000
while True:
	list = [104]	
	result = spi.xfer(list)
	if result != [0]:
		print result

