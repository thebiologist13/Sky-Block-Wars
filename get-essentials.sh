#### Get-Essentials.sh
# A script to download Essentials and install it into Maven.
# Date: 12 July 2013
################

# Clean up any earlier run
rm -f Essentials.zip
rm -f Essentials.jar
# Download known version from BukkitDev
wget -O Essentials.zip http://dev.bukkit.org/media/files/711/777/Essentials.zip
# Only unzip one file
unzip Essentials.zip Essentials.jar
# Install to the local repo
# When the download URL is changed, change the version on the end of this
mvn install:install-file -Dfile=Essentials.jar -DgroupId=com.earth2me -DartifactId=essentials -Dversion=2.11.1 -Dpackaging=jar
# Clean up
rm -f Essentials.zip
rm -f Essentials.jar
