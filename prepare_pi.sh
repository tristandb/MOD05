#!/usr/bin/env bash
#Script needs to be executed with root privileges. Check if user has sudo rights.
ROOT_UID=0
if [ "$UID" != "$ROOT_UID" ]; then
	echo "You need sudo rights to run.sh this script."
	exit 1
fi
#HOME_DIR represents the user's home directory.
HOME_DIR="$(eval echo ~"$SUDO_USER")"



echo "Setting GPU memory to 512MB."
cp /boot/config.txt /boot/config.txt.bak
grep -q "^gpu_mem" /boot/config.txt && sed -i "s/^gpu_mem=(\d)+/gpu_mem=512/" /boot/config.txt || echo "gpu_mem=512" >> /boot/config.txt



echo "Setting up X configuration in $HOME_DIR."
if [ -e "$HOME_DIR/.xsession" ]; then
	mv "$HOME_DIR/.xsession" ~/.xsession.bak
fi
echo "STARTUP=" > "$HOME_DIR/.xsession"
chown "$SUDO_USER:$SUDO_USER" "$HOME_DIR/.xsession"
if [ -e "$HOME_DIR/.xsessionrc" ]; then
	mv "$HOME_DIR/.xsessionrc" ~/.xsessionrc.bak
fi
echo "x-window-manager & java -jar ./Breakout.jar" > "$HOME_DIR/.xsessionrc"
chown "$SUDO_USER:$SUDO_USER" "$HOME_DIR/.xsessionrc"



echo "Make sure /tmp is mounted as tmpfs"
cp /etc/fstab /etc/fstab.bak
grep -q "\/tmp" /etc/fstab && sed -i '/ \/tmp /s/^/#/' /etc/fstab && printf "tmpfs /tmp tmpfs rw,nodev,nosuid,size=50M 0 0\n" >> /etc/fstab

echo "Installing necessary packages"
#install necessary packages
apt-get update
apt-get install -y python-dev python-numpy python-opencv python-imaging
if [ `python -c 'import pkgutil; print(1 if pkgutil.find_loader("spidev") else 0)'` -eq "0" ]; then
	dir="$(mktemp -d)"
	cd "$dir"
	wget https://github.com/doceme/py-spidev/archive/master.zip
	unzip master.zip
	cd ./py-spidev-master
	python ./setup.py install
	cd "$HOME_DIR"
fi


read -p "System needs to be rebooted for changes to take effect. Reboot now (y/n)? " choice
case "$choice" in 
  y|Y ) reboot;;
  * ) echo "Not rebooting.";;
esac