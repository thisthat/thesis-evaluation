@echo off

rd /s /q out
mkdir out
cd out
mkdir forecast_1
mkdir forecast_2
mkdir forecast_3
mkdir forecast_4
mkdir forecast_5
cd .. 



set SW_LIST=(00:00:00:00:00:00:03:01 00:00:00:00:00:00:03:02 00:00:00:00:00:00:03:03 00:00:00:00:00:00:03:04 00:00:00:00:00:00:03:05 00:00:00:00:00:00:03:06 00:00:00:00:00:00:03:07 00:00:00:00:00:00:03:08 00:00:00:00:00:00:03:09 00:00:00:00:00:00:03:0a 00:00:00:00:00:00:03:0b 00:00:00:00:00:00:03:0c 00:00:00:00:00:00:03:0d 00:00:00:00:00:00:03:0e 00:00:00:00:00:00:03:0f)
set FORECAST=(1,2,3,4,5)


start cmd /k mongod --dbpath=C:\Users\this\Documents\db

for %%i in %SW_LIST% do (
	for %%j in %FORECAST% do (
echo %%i -- %%j

python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_1 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_2 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_3 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_4 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_5 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_6 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:12 -day:p2p5_7 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_8 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_9 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_10 -switch:%%i -forecast:%%j -derivate -win_size:8

python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_11 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_12 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_13 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_14 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_15 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_16 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_17 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_18 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_19 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_20 -switch:%%i -forecast:%%j -derivate -win_size:8

python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_21 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_22 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_23 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p5_24 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_25 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_26 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_27 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_28 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_29 -switch:%%i -forecast:%%j -derivate -win_size:8
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p5_30 -switch:%%i -forecast:%%j -derivate -win_size:8

php -f merge.php forecast_%%j/%%i
	)
)
pause


