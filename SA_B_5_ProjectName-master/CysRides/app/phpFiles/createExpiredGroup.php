<?php

    $host="mysql.cs.iastate.edu";
    $port=3306;
    $socket="";
    $username = 'dbu309sab5';
    $password = 'VRCc@3V2';
    $dbname = 'db309sab5';

    $conn = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

    $driver = $_POST["driver"];
    $rider1 = $_POST["riderOne"];
    $rider2 = $_POST["riderTwo"];
    $rider3 = $_POST["riderThree"];
    $rider4 = $_POST["riderFour"];
    $rider5 = $_POST["riderFive"];
    $rider6 = $_POST["riderSix"];
    $rider7 = $_POST["riderSeven"];

    $sql = "INSERT INTO EXPIRED_GROUP_TABLE (EXPIRED_DRIVER, EXPIRED_RIDER_1, EXPIRED_RIDER_2, EXPIRED_RIDER_3, EXPIRED_RIDER_4, EXPIRED_RIDER_5, EXPIRED_RIDER_6, EXPIRED_RIDER_7) VALUES ('".$driver."','".$rider1."','".$rider2."','".$rider3."','".$rider4."','".$rider5."','".$rider6."','".$rider7."');";

    if(mysqli_query($conn,$sql)) {
        echo "Data insertion success...";
    } else {
        echo "Error while insertion... ".$sql." ".mysqli_error($conn);
    }

    mysqli_close($conn);
?>

