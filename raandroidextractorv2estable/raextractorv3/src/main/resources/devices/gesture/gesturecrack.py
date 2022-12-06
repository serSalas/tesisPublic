#Quick and Easy Android Pattern Lock Cracker - Kieron Craggs 2014
#This quick script will use a gesture.key file or raw text hash and compare it against a rainbow table to find the pattern used to lock the device
#Uses GestureKey Rainbow Table

#****MODULES*************#
import argparse
import sqlite3 as lite
import gzip
import os
#************************#

parser = argparse.ArgumentParser(description = "Easy Android Pattern Lock Cracker")
group = parser.add_mutually_exclusive_group(required=True)
group.add_argument("-f", "--file", help="Path to a gesture.key file", required=False)
group.add_argument("-r", "--rawhash", help="Raw text input of SHA1 hash", required=False)
args = parser.parse_args()

#os.chdir("..")
#os.chdir("..")
f = open("src/main/resources/output/data.txt", "w")

if args.file:
    gesturekey = open (args.file,'rb').read()
    inkey = str(gesturekey).encode('hex-codec')
elif args.rawhash:
    inkey = args.rawhash
else:
    print "Supply a valid gesture key file or raw text hash"


try:
    dbin = gzip.GzipFile('src/main/resources/devices/gesture/rainbow.db.gz','rb')
except IOError:
    print "El archivo de Base de Datos no existe, coloque el archvivo rainbow.db.gz en directorio correcto"
    exit()
else:
    dbbuf = dbin.read()
    dbin.close()

    dbout = file('src/main/resources/devices/gesture/rainbow.db','wb')
    dbout.write(dbbuf)
    dbout.close()    

try:
    db = lite.connect('src/main/resources/devices/gesture/rainbow.db') 
except NameError:
    print "Nothing was given to the database to check. Check gesture input file."
except IOError:
    print "No se puede encontrar el archivo de BD."
else:    
    with db:
        cur = db.cursor()
        cur.execute('SELECT pattern FROM RainbowTable WHERE hash = ?',(inkey,))
        rows = cur.fetchone()

        if rows:
            for row in rows:
                result = row
                print """   
        The Lock Pattern code is %s
		      """ % (row)
		#Escritura de archivo:

		f.write( str(row) + "\n")
		f.write("\n")
        else:
            print "No match, check input."                    

if os.path.exists("src/main/resources/devices/gesture/rainbow.db"):       
        os.remove("src/main/resources/devices/gesture/rainbow.db")
        exit()    