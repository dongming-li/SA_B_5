<?php

    $host="mysql.cs.iastate.edu";
    $port=3306;
    $socket="";
    $username = 'dbu309sab5';
    $password = 'VRCc@3V2';
    $dbname = 'db309sab5';

    $conn = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

    $post_id = mysqli_real_escape_string($conn, $_POST['id']);

    $sql = "DELETE FROM OFFER_TABLE WHERE ID='".$post_id."';";    

    if(mysqli_query($conn,$sql)) {
        echo "Data deletion success...";
    } else {
        echo "Error while deletion... ".$sql." ".mysqli_error($conn);
    }

    mysqli_close($conn);
?>

