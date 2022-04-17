<?php

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "SELECT gt.*,
        ot.OFFER_EMAIL, ot.OFFER_DESTINATION, ot.OFFER_START, ot.OFFER_DATETIME,
        rt.REQUEST_EMAIL, rt.REQUEST_DESTINATION, rt.REQUEST_START, rt.REQUEST_DATETIME
        FROM GROUP_TABLE gt
        LEFT JOIN OFFER_TABLE ot
          ON ot.OFFER_GROUP_ID = gt.ID
        LEFT JOIN REQUEST_TABLE rt
          ON rt.REQUEST_GROUP_ID = gt.ID;";


// $sql = "SELECT GROUP_TABLE.*,
//         OFFER_TABLE.OFFER_EMAIL, OFFER_TABLE.DESTINATION, OFFER_TABLE.START, OFFER_TABLE.DATETIME,
//         REQUEST_TABLE.REQUEST_EMAIL, REQUEST_TABLE.DESTINATION, REQUEST_TABLE.START, REQUEST_TABLE.DATETIME
//         FROM GROUP_TABLE
//         LEFT JOIN OFFER_TABLE
//           ON OFFER_TABLE.GROUP_ID = GROUP_TABLE.ID
//         LEFT JOIN REQUEST_TABLE
//           ON REQUEST_TABLE.GROUP_ID = GROUP_TABLE.ID;";

$arr = array();
$result = $con->query($sql);

if($result->num_rows > 0){
  while($row = $result->fetch_assoc()){
      array_push($arr, $row);
  }
}else{
  echo "0 results...".$sql." ".mysqli_error($con);
  return;
}

// if($result = mysqli_query($con, $sql)){
//   echo json_encode($result);
// }else{
//   echo "still broke";
// }
echo json_encode($arr);


 ?>
