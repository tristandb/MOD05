#!/usr/bin/env bash
#Script needs to be executed with root privileges. Check if user has sudo rights.
ROOT_UID=0
if [ "$UID" != "$ROOT_UID" ]; then
	echo "You need sudo rights to run this script."
	exit 1
fi
#HOME_DIR represents the user's home directory.
HOME_DIR="$(eval echo ~"$SUDO_USER")"



echo "Setting GPU memory to 512MB."
cp /boot/config.txt /boot/config.txt.bak
grep -q "^gpu_mem" /boot/config.txt && sed -i "s/gpu_mem=(\d)+/gpu_mem=512/" /boot/config.txt || echo "gpu_mem=512" >> /boot/config.txt



echo "Setting up X configuration in $HOME_DIR."
if [ -e "$HOME_DIR/.xsession" ]; then
	mv $HOME_DIR/.xsession ~/.xsession.bak
fi
echo "STARTUP=" > $HOME_DIR/.xsession
if [ -e "$HOME_DIR/.xsessionrc" ]; then
	mv $HOME_DIR/.xsessionrc ~/.xsessionrc.bak
fi
echo "xrandr --output HDMI1 --mode 1000x600 & x-window-manager & java -jar Breakout.jar" > $HOME_DIR/.xsessionrc



echo "Make sure /tmp is mounted as tmpfs"
cp /etc/fstab /etc/fstab.bak
printf "tmpfs /tmp tmpfs rw,nodev,nosuid,size=50M 0 0" >> /etc/fstab



read -p "System needs to be rebooted for changes to take effect. Reboot now (y/n)? " choice
case "$choice" in 
  y|Y ) reboot;;
  * ) echo "Not rebooting.";;
esac