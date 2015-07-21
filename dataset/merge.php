<?php


$dir_n = "./learn_500d/" . str_replace(":", "-", $argv[1]) . "/";
$exts = ['arff'];


function ext($file){
    $e = explode('.',$file);
    return $e[count($e) - 1];
}

if(file_exists ( $dir_n .'merge.arff' )){
    unlink($dir_n . 'merge.arff' );
}
$dir = opendir($dir_n);
$first = true;
$out = "";
while (false !== ($file = readdir($dir))) {
    if(!in_array(ext($file), $exts)){
        continue;
    }
    $h = fopen($dir_n . $file,'r');
    $cnt = fread($h, filesize($dir_n .$file));
    if($first){
        $first = false;
        $out = $cnt;
    } else {
        $exp = explode("@data", $cnt);
        $out .= $exp[1];
    }
    fclose($h);
}

$write = fopen($dir_n . "merge.arff", 'w+');
fwrite($write, $out);
fclose($write);
echo "Merge done!\r\n";

?>