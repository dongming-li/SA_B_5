<?php



  $group_id = $_POST["group_id"];


  $host="mysql.cs.iastate.edu";
  $port=3306;
  $socket="";
  $username = 'dbu309sab5';
  $password = 'VRCc@3V2';
  $dbname = 'db309sab5';

  $con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

  $sql = "SELECT * FROM GROUP_TABLE WHERE ID = ".$group_id.";";


  $result = $con->query($sql);
  $jsonArr = array();

  $row = $result->fetch_assoc();
  array_push($jsonArr, $row);

  echo json_encode($jsonArr);

  // if($result = mysqli_query($con, $sql)){
  //    $row = mysqli_fetch_row($result);
  //
  //    echo json_encode($row);
  // }else {
  //   echo "Error while insertion... ".$sql." ".mysqli_error($con);
  // }
 ?>
