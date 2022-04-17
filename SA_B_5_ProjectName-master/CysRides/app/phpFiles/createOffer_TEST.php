<?php

$cost = $_POST["cost"];
$email = $_POST["email"];
$destination = $_POST["destination"];
$description = $_POST["description"];
$date =$_POST["date"];

$host="mysql.cs.iastate.edu";
$port=3306;
$socket="";
$username = 'dbu309sab5';
$password = 'VRCc@3V2';
$dbname = 'db309sab5';

$con = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

$sql = "insert into OFFER_TABLE (COST, OFFER_EMAIL, OFFER_DESTINATION, OFFER_DESCRIPTION, OFFER_DATE) values('".$cost."','".$email."','".$destination."','".$description."',".$date.");";

if(mysqli_query($con,$sql)) {
    $sql = "SELECT ID from OFFER_TABLE ORDER BY ID DESC LIMIT 1;";

    $result = mysqli_query($con,$sql);

    $row = mysqli_fetch_row($result);
    echo $row[0];

} else {
    echo "Error while insertion... ".$sql." ".mysqli_error($con);
}

?>
