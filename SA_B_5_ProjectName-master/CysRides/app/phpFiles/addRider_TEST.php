<?php


$rider = $_POST["rider"];
$rider_num = $_POST["rider_num"];
$id = $_POST["id"];


$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';
$sel_column = 'RIDER_' . $rider_num;


$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);


$sql = "SELECT * FROM GROUP_TABLE WHERE ID = " .$id. ";";

if($result = mysqli_query($con,$sql)) {
  $row = mysqli_fetch_row($result);
  for($i = 1; $i < count($row); $i++){

    if($row[$i] == $rider){
      echo "You are already in this group";
      return;
    }
  }
} else {
    echo "Error while insertion... ".$sql." ".mysqli_error($con);
}

$sql =  "UPDATE GROUP_TABLE SET " .$sel_column. " = '" .$rider. "' WHERE ID = " .$id. ";";

if(mysqli_query($con,$sql)) {
    echo "Data insertion success...";
} else {
    echo "Error while insertion... ".$sql." ".mysqli_error($con);
}

?>
