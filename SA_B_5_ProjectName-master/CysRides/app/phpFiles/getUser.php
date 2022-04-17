<?php

        $host="mysql.cs.iastate.edu";
        $port=3306;
        $socket="";
        $username = 'dbu309sab5';
        $password = 'VRCc@3V2';
        $dbname = 'db309sab5';

        $conn = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

        $post_netid = mysqli_real_escape_string($conn, $_POST['netID']);

        $sql = "SELECT * FROM USER_TABLE WHERE NETID='".$post_netid."'";

        $sql = //'SELECT * FROM USER_TABLE';
        "SELECT ut.*, but.REASON
          FROM USER_TABLE ut
          LEFT JOIN BANNED_USERS_TABLE but
            ON but.EMAIL = ut.NETID";

        $result = $conn->query($sql);

//        if ($result->num_rows > 0) {
//            $row = $result->fetch_assoc();
//            echo $json = json_encode($row);
//        } else {
//            echo "0 results";
//        }
//
//        $conn->close();

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
        $conn->close();
?>
