#Augmented Breakout

##Installation
Before running Augmented Breakout, the Raspberry Pi has to be set up to get the ultimate playing 
experience. To set up the Pi, use the `prepare_pi.sh` script. This will set the GPU memory to 
512MB, set up a desktop environment-less X11 configuration and makes sure /tmp is mounted as 
tmpfs.

##Java programme command line arguments
* `debug=[true]`: if set to true, debug output will be printed.
* `input=[camera|cheat]`: 'camera' for Camera input, 'cheat' for Cheat input, otherwise Mouse is 
used.
* `pipename=[%name%]`: The pipe to use when using camera input.
* `width=[%width%]`: Gamefield with, default 800
* `height=[%height%]`: Gamefield height, default 600
* `fullscreen=[true]`: if set to true, the game will run fullscreen.
* `name=[%name%]`: The name to use for the highscores.
* `cameraview=[%location%]`: The location of the camera view, when not present or location does 
not exist, it wont draw any image.
 