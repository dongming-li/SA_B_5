<?php

$GROUP_ID = $_GET["GROUP_ID"];

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "SELECT * FROM MESSAGE_TABLE WHERE GROUP_ID =  ".$GROUP_ID.";";
$result = $con->query($sql);
$jsonArr = array();

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        array_push($jsonArr, $row);
    }
} else {
    echo "0 results";
}

echo $json = json_encode($jsonArr);
$con->close();

?>
