<?php


$pathSearch = $argv[1];
$pathCopy = $argv[2];

$exclude = array("..", "." );
$exts = array("arff");

if ($handle = opendir($pathSearch)) {
   	while (false !== ($file = readdir($handle))) { 
   		$forecast = $file;
       	//Forecast
   		$current = $pathSearch . "/" . $file;
   		if(!in_array($file, $exclude)){
   			//Search for Switch
   			$workingPath = $pathSearch . "/" . $file;
   			$handleForecast = opendir($workingPath);
   			while (false !== ($swName = readdir($handleForecast))) { 
		       	//Switch
		       	$switch = $swName;
				if(!in_array($swName, $exclude)){
		   			//Search for Switch
		   			$arffDir = $workingPath . "/" . $swName;
		   			$handleArff = opendir($arffDir);
		   			while (false !== ($arff = readdir($handleArff))) { 
		   				$fileOnlyName = name(basename($arff));
		   				if(in_array(ext($arff), $exts) && $fileOnlyName == "merge"){
		   					$from = $pathSearch . "/$forecast/$switch/$arff";
		   					$to = $pathCopy . "/$forecast/$switch/$arff";
		   					if(!copy($from, $to)){
		   						echo "Error in file: " . $from . "->" . $to . " \r\n";
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

function name($file){
	$exp = explode(".", $file);
	return $exp[0];
}