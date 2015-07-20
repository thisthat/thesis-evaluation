@echo off

set SW_LIST=(00:00:00:00:00:00:04:01 00:00:00:00:00:00:04:02 00:00:00:00:00:00:03:01 00:00:00:00:00:00:03:02 00:00:00:00:00:00:03:03 00:00:00:00:00:00:03:04 00:00:00:00:00:00:03:05 00:00:00:00:00:00:03:06 00:00:00:00:00:00:03:07 00:00:00:00:00:00:03:08 00:00:00:00:00:00:03:09 00:00:00:00:00:00:03:0a 00:00:00:00:00:00:03:0b 00:00:00:00:00:00:03:0c 00:00:00:00:00:00:03:0d)
set FORECAST=(1,2,3,4,5)


for %%i in %SW_LIST% do (
	for %%j in %FORECAST% do (
		php -f merge.php forecast_%%j/%%i
	)
)
pause