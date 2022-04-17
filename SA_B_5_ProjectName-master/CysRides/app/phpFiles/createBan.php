<?php

    $email = $_POST["email"];
    $reason = $_POST["reason"];

    $host="mysql.cs.iastate.edu";
    $port=3306;
    $socket="";
    $username = 'dbu309sab5';
    $password = 'VRCc@3V2';
    $dbname = 'db309sab5';

    $conn = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

    $sql = "INSERT INTO BANNED_USERS_TABLE (EMAIL, REASON) VALUES ('".$email."','".$reason."');";

    if(mysqli_query($conn,$sql)) {
        echo "Data insertion success...";
    } else {
        echo "Error while insertion... ".$sql." ".mysqli_error($conn);
    }

    mysqli_close($conn);

?>