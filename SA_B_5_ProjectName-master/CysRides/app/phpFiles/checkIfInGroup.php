<?php

$netID = $_POST["netID"];
$id = $_POST["id"];

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "SELECT * FROM GROUP_TABLE WHERE ID = " .$id.";";

if($result = mysqli_query($con,$sql)) {
  $row = mysqli_fetch_row($result);
  for($i = 1; $i < count($row); $i++){
    if($row[i] == $netID){
      echo "T";
      return;
    }
  }
  echo "F";
} else {
    echo "Error while insertion... ".$sql." ".mysqli_error($con);
}

?>
