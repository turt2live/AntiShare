<html>
<div style='
	width: 275px;
	margin-left: 5px;
'>
<?php
$type = $_GET['type'];
if(empty($type)){
	$type = "UNKNOWN";
}
echo file_get_contents("$type.html");
if($type!="HOME"){
	echo "<br><hr><br><b>Actual line in the log:</b><br>-ORIG-";
}
?>
<br><br>
<hr>
<br>
<b>Need to yell at me?</b><br>
Email: <i>turt2live@turt2live.com</i><br>
<span>
Copyright 2012 Travis Ralston
</span>
</div>
</html>