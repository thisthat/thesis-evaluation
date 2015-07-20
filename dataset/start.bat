@echo off
set SW_LIST=(00:00:00:00:00:00:04:01 00:00:00:00:00:00:04:02 00:00:00:00:00:00:03:01 00:00:00:00:00:00:03:02 00:00:00:00:00:00:03:03 00:00:00:00:00:00:03:04 00:00:00:00:00:00:03:05 00:00:00:00:00:00:03:06 00:00:00:00:00:00:03:07 00:00:00:00:00:00:03:08 00:00:00:00:00:00:03:09 00:00:00:00:00:00:03:0a 00:00:00:00:00:00:03:0b 00:00:00:00:00:00:03:0c 00:00:00:00:00:00:03:0d)
set FORECAST=(1,2,3,4,5)

start cmd /k mongod --dbpath=C:\Users\this\Documents\db

for %%i in %SW_LIST% do (
	echo %%i
	for %%j in %FORECAST% do (
echo %%i -- %%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_1 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_2 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_3 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:3 -day:day_4 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:day_5 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_6 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_7 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:3 -day:day_8 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_9 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:6 -day:day_10 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_11 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_12 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_13 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_14 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:day_15 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_16 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_17 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_18 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_19 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_20 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_21 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_22 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_23 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_24 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_25 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_26 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:2 -day:day_27 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_28 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_29 -switch:%%i -forecast:%%j
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:day_30 -switch:%%i  -forecast:%%j

	)
)
pause


