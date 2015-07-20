#MongoDB Driver include
import pymongo
import time
import urllib.request
import json
import math
import sys
import os
from pymongo import MongoClient
import pprint
pp = pprint.PrettyPrinter(indent=4)

#Params
ip_vm = "192.168.56.1"
port_vm = "8080"
size_class = 200
max_bandwidthClass = 10000


#Vars
switch = "00:00:00:00:00:00:04:01"
timing = 60 # How many seconds for each bandwidth class in the TrafficTester
skip = 1 # How many measurement to skip
skipend = 1
win_size = 6
derivate = False
day = "day_4"
filename = "dataset_"
forecast = 1
howManyMeasuremtsPerHours = 5

for i in sys.argv:
	if (i.startswith('-switch:')) :
		switch = i[8:]
	if (i.startswith('-timing:')) :
		timing = i[8:]
	if (i.startswith('-skip:')) :
		skip = int(i[6:])
	if (i.startswith('-skip_end:')) :
		skipend = int(i[10:])
	if (i.startswith('-win_size:')) :
		win_size = int(i[10:])
	if (i.startswith('-file:')) :
		filename = i[6:]
	if (i.startswith('-class_size:')) :
		size_class = int(i[12:])
	if (i.startswith('-derivate')) :
		derivate = True
	if (i.startswith('-forecast:')) :
		forecast = int(i[10:])
	if (i.startswith('-day:')) :
		day = i[5:]
		filename = "{0}{1}" . format(filename, day)

if derivate:
	filename = "{0}_der" . format(filename)

#Connection
client = MongoClient('mongodb://localhost:27017/')
db = client.FloodLight
print("Connection to DB Enstablished")


def classification(bandwidth):
	n = 1
	while (n*size_class < bandwidth) :
		n += 1
	ret = n*size_class
	if ret > max_bandwidthClass :
		ret = max_bandwidthClass
	return ret

# this is usefull in real envirorment
def timeClass(seconds,start):
	#print("{0} - {1} = {2}" . format(seconds,start, seconds - start))
	#start += 1
	return math.floor((seconds-start) / timing)


countTime = 0
countIstance = 0

prevTime = 0
prevByte = 0

bandwidth = []
tmp = []
#collect data from DB
for post in db.DataTime.find({ "test" : day },{'_id':0}).sort("_time"):
	time = post['_time']
	byte = 0;

	obj = {"DPID" : switch, "_time" : time, "test" : day}

	for data in db.SwitchFlowData.find( obj , {'_id' : 0}):
		b = int(data["byteCount"])
		byte = byte + b

	#print("Bandwidth: {0}" . format(byte) )
	currentTime = (time-prevTime)
	currentByte = (byte-prevByte) / currentTime
	if (byte-prevByte) < 0:
		currentByte = byte / currentTime
	tmp.append({"bandwidth": currentByte, "sec": time})

	prevByte = byte
	prevTime = time
	countTime = countTime + 1
	# print("Instance Getted")

for i in range(skip, len(tmp) - skipend):
	bandwidth.append(tmp[i])

#The data is collected, now we have to print in a corect manner

#
#  OUTPUT Format
#
#  time_class, bandwidth_time, _ , bandwidth_{time+win_size-1}, prediction_class
#	

print("Data collected! Creating the file...")

switch_path = switch.replace(":", "-")
directory = "out/forecast_{0}/{1}" . format(forecast, switch_path)
if not os.path.exists(directory):
    os.makedirs(directory)

f = open("out/forecast_{1}/{2}/{0}.arff" . format(filename, forecast, switch_path), 'w')

# Header definition
out = "@relation {0}\n" . format(filename)
f.write(out)

out = "@attribute time_class {"
for i in range(24):
	if i == 23:
		out += "'time_{0}'" . format(i)
	else :
		out += "'time_{0}'," . format(i)
out += "}\n"
f.write(out)

for i in range(win_size-1):
	out = "@attribute bandwidth_{0} numeric\n" . format(i)
	f.write(out)
	if(derivate and i < win_size-2):
		out = "@attribute d_bandwidth_{0} numeric\n" . format(i)
		f.write(out)

out = "@attribute prediction_class {"
last = math.ceil(max_bandwidthClass / size_class) + 1
for i in range(1,last):
	if i == last - 1:
		out += "'byte_{0}'" . format(i*size_class)
	else :
		out += "'byte_{0}'," . format(i*size_class)
out += "}\n"
f.write(out)

# Data creation
f.write("@data\n")
#pp.pprint(tmp)
start = tmp[skip]['sec']
old_value = 0
der = 0
for i in range(win_size-1,len(bandwidth) + win_size - 1):
	out = "'time_{0}'" . format( timeClass(bandwidth[i-win_size+1]['sec'], start) )
	for j in range(win_size):
		index = (i - win_size + j)  % len(bandwidth)
		value = 0
		if j == win_size-1:
			index = ( index + (forecast - 1)  * howManyMeasuremtsPerHours) % len(bandwidth)
			value = "'byte_{0}'" . format(classification(bandwidth[index]['bandwidth']))
		else:
			value = round(bandwidth[index]['bandwidth'])
			if(derivate and j > 0):
				der = value - old_value
				old_value = value
				out += ",{0}" . format(der)
		out += ",{0}" . format(value)
	f.write(out)
	f.write("\n")




print("Found {0} elements" . format(countTime))
print("Create {0} istances" . format(len(bandwidth)))