<?php

$groupString = $_GET["groupString"];

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$groupArr = explode(" ", $groupString);
$tripArr = array();

for($i=0; $i < count($groupArr); $i++){
  $sql = "SELECT OFFER_ID, REQUEST_ID FROM GROUP_TABLE WHERE ID = ".$groupArr[$i].";";
  $result = $con->query($sql)->fetch_assoc();

  if($result['OFFER_ID'] !== null){
      //Group is for offer
      $sql = "SELECT * FROM OFFER_TABLE WHERE GROUP_ID = ".$groupArr[$i].";";
      array_push($tripArr, $con->query($sql)->fetch_assoc());
  }else{
      //Group is for request
      $sql = "SELECT * FROM REQUEST_TABLE WHERE GROUP_ID = ".$groupArr[$i].";";
      array_push($tripArr, $con->query($sql)->fetch_assoc());
  }
}

echo json_encode($tripArr);

 ?>
