<?php

$expDir = "onlyModel";
if(file_exists ( $expDir )){
	deleteDir($expDir);
}
mkdir($expDir);


$path = $argv[1];
$exclude = array("..", "." );
$exts = array("model");

if ($handle = opendir($path)) {
   	while (false !== ($file = readdir($handle))) { 
       	//Forecast
   		$current = $expDir . "/" . $file;
   		if(!in_array($file, $exclude)){
   			//Create the dir
   			mkdir($current);
   			//Search for Switch
   			$workingPath = $path . "/" . $file;
   			//echo $current . "<-\r\n";
   			$handleForecast = opendir($workingPath);
   			while (false !== ($swName = readdir($handleForecast))) { 
		       	//Switch
				if(!in_array($swName, $exclude)){
					//Create the dir for the switch
					$currentSwPath = $expDir . "/" . $file . "/" . $swName;
		   			mkdir($currentSwPath);
		   			//Search for Switch
		   			$modelDir = $workingPath . "/" . $swName;
		   			$handleModel = opendir($modelDir);
		   			while (false !== ($mod = readdir($handleModel))) { 
		   				if(in_array(ext($mod), $exts)){
		   					$pathMod = $modelDir . "/" . $mod;
		   					$savePath = $currentSwPath . "/" . $mod;
		   					if(!copy($pathMod, $savePath)){
		   						echo "Error in file: " . $pathMod . "->" . $savePath . " \r\n";
		   					}
		   				}
		   			}
				}
			}
   		}
   	}
   	closedir($handle); 
}
echo "Bye :)\r\n";

function deleteDir($path){
	if (PHP_OS === 'Windows')
	{
	    exec("rd /s /q {$path}");
	}
	else
	{
	    exec("rm -rf {$path}");
	}
}

function ext($file){
	$exp = explode(".", $file);
	return $exp[count($exp) - 1];
}