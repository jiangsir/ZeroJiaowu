import os
import fnmatch
import sys
import time

#checkout HEAD 1

path = os.path.split(os.path.realpath(__file__))[0]

os.system('apt-get install sudo')
os.system('apt-get install ntpdate')
os.system('apt-get install dos2unix')
os.system('apt-get install rsync')

for file in os.listdir('/etc/init.d/'):
    if fnmatch.fnmatch(file, 'tomcat*'):
        tomcatN = file

os.system('cp ' + path + '/WEB-INF/lib/mysql-connector-java-5.1.5-bin.jar /usr/share/' + tomcatN + '/lib')
os.system('java -Xmx1024m -Dfile.encoding=utf-8 -classpath /usr/share/' + tomcatN + '/lib/servlet-api.jar:' + path + '/WEB-INF/lib/jericho-html-3.2.jar:' + path + '/WEB-INF/lib/jackson-all-1.8.3.jar:' + path + '/WEB-INF/lib/jdom.jar:' + path + '/WEB-INF/lib/mysql-connector-java-5.1.5-bin.jar:' + path + '/WEB-INF/classes Setup ' + path)

os.system('sudo /etc/init.d/' + tomcatN + ' stop')
os.system('sudo /etc/init.d/mysql restart')
os.system('sudo /etc/init.d/' + tomcatN + ' start')

# rm $path/Setup.sh
