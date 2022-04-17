<?php

  $group_id = $_POST["group_id"];
  $request_id = $_POST["request_id"];

  $host="mysql.cs.iastate.edu";
  $port=3306;
  $socket="";
  $username = 'dbu309sab5';
  $password = 'VRCc@3V2';
  $dbname = 'db309sab5';

  $con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

  $sql = "UPDATE REQUEST_TABLE SET REQUEST_GROUP_ID = " .$group_id. " WHERE ID = ".$request_id.";";

  if(mysqli_query($con,$sql)) {
      echo "Data insertion success...";
  } else {
      echo "Error while insertion... ".$sql." ".mysqli_error($con);
  }

 ?>
