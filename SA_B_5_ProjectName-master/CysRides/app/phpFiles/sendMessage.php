<?php

$GROUP_ID = $_POST["GROUP_ID"];
$SENDER = $_POST["SENDER"];
$MESSAGE = $_POST["MESSAGE"];


$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "INSERT INTO MESSAGE_TABLE (GROUP_ID, SENDER, MESSAGE) VALUES (".$GROUP_ID.",'".$SENDER."','".$MESSAGE."');";

if(mysqli_query($con,$sql)){
  echo "Data insertion success....";
}else{
  echo "Error while insertion... ".$sql." ".mysqli_error($con);
}
?>
