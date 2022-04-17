<?php

$netID = $_POST["netID"];

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "SELECT ID FROM GROUP_TABLE WHERE DRIVER= '".$netID."' OR RIDER_1= '".$netID."' OR RIDER_2= '".$netID."' OR RIDER_3= '".$netID."' OR RIDER_4= '".$netID."' OR RIDER_5= '".$netID."' OR RIDER_6= '".$netID."' OR RIDER_7= '".$netID."';";

$result = $con->query($sql);
$output = "";
if($result->num_rows > 0){
  while($row = $result->fetch_assoc()){
    $output = $output . strval($row["ID"]) . " ";
  }
}else{
  echo "0 results";
  return;
}
$output = substr($output, 0, -1);

echo $output;


 ?>
