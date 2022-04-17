<?php

$numBags = $_POST["numBags"];
$email = $_POST["email"];
$destination = $_POST["destination"];
$start = $_POST["start"];
$description = $_POST["description"];
$datetime = $_POST["datetime"];

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "INSERT INTO REQUEST_TABLE (NUM_BAGS, REQUEST_EMAIL, REQUEST_DESTINATION, REQUEST_START, REQUEST_DESCRIPTION, REQUEST_DATETIME) VALUES ('".$numBags."','".$email."','".$destination."','".$start."','".$description."',".$datetime.");";

if(mysqli_query($con,$sql)) {
  $sql = "SELECT ID from REQUEST_TABLE ORDER BY ID DESC LIMIT 1;";

  $result = mysqli_query($con,$sql);

  $row = mysqli_fetch_row($result);
  echo $row[0];

  } else {
    echo "Error while insertion... ".$sql." ".mysqli_error($con);
  }

?>
