<?php


$driver = $_POST["driver"];
$id = $_POST["id"];

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "SELECT * FROM GROUP_TABLE WHERE ID = " .$id. ";";
if($result = mysqli_query($con,$sql)) {
  $row = mysqli_fetch_row($result);
  for($i = 1; $i < count($row); $i++){
    if($row[$i] == $driver){
      echo "You are already in this group";
      return;
    }
  }
} else {
    echo "Error while insertion... ".$sql." ".mysqli_error($con);
}

$sql = "SELECT DRIVER FROM GROUP_TABLE WHERE ID = " .$id. ";";

if($result = mysqli_query($con, $sql)){
  $row = mysqli_fetch_row($result);
  if(!$row[0] == ""){
    echo "Group already has a driver";
    return;
  }
}else{
  echo "Something went wrong... ".$sql." ".mysqli_error($con);
}

$sql = "UPDATE GROUP_TABLE SET DRIVER = '" .$driver. "' WHERE ID = " .$id. ";";

if(mysqli_query($con,$sql)) {
    echo "Data insertion success...";
} else {
    echo "Error while insertion... ".$sql." ".mysqli_error($con);
}

?>
