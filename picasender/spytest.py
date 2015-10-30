import spidev

# Configure SpiDev
spi = spidev.SpiDev()
spi.open(0,0)
spi.max_speed_hz = 5000000
list =[]
for a in range(0, 256):
    list.append(a)
print spi.xfer(list)

print spi.xfer([166])