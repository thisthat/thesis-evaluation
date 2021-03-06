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

python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p_rnd20_1 -switch:%%i -forecast:%%j -derivate 
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p_rnd20_2 -switch:%%i -forecast:%%j -derivate 
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p_rnd20_3 -switch:%%i -forecast:%%j -derivate 
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p_rnd20_4 -switch:%%i -forecast:%%j -derivate 
python createDataset.py -class_size:500 -skip:2 -skip_end:1 -day:p2p_rnd20_5 -switch:%%i -forecast:%%j -derivate
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p_rnd20_6 -switch:%%i -forecast:%%j -derivate 
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p_rnd20_7 -switch:%%i -forecast:%%j -derivate
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p_rnd20_8 -switch:%%i -forecast:%%j -derivate 
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p_rnd20_9 -switch:%%i -forecast:%%j -derivate 
python createDataset.py -class_size:500 -skip:3 -skip_end:1 -day:p2p_rnd20_10 -switch:%%i -forecast:%%j -derivate 


php -f merge.php forecast_%%j/%%i
	)
)
pause


