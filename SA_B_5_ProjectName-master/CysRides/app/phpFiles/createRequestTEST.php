<?php

$numBags = $_POST["numBags"];
$email = $_POST["email"];
$destination = $_POST["destination"];
$description = $_POST["description"];
$date =$_POST["date"]

// $user = "dbu309sab5";
// $password = "VRCc@3V2";
// $host = "mysql.cs.iastate.edu";
// $db_name = "db309sab5";
//
// $con = myspli_connect($host,$user,$password$db_name);

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "INSERT INTO REQUEST_TABLE (NUM_BAGS, EMAIL, DESTINATION, DESCRIPTION, DATE) VALUES ('".$numBags."','".$email."','".$destination."','".$description."','".$date."'";

if(mysqli_query($con,$sql)) {
    echo "Data insertion success...";
} else {
    echo "Error while insertion...";
}

?>
