<html>
    <head>
    <title>get Offers</title>
    </head>
    <body>
        <?php

        $host="mysql.cs.iastate.edu";
        $port=3306;
        $socket="";
        $username = 'dbu309sab5';
        $password = 'VRCc@3V2';
        $dbname = 'db309sab5';

        $conn = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

        $sql = 'SELECT * FROM OFFER_TABLE;';
        $query = mysqli_fetch_array(mysqli_query($conn, $sql));
        echo $query;
        $result = array();

        array_push($result,array(
			"cost"=>$query['COST'],
			"email"=>$query['EMAIL'],
            "destination"=>$query['DESTINATION'],
            "description"=>$query['DESCRIPTION'],
			"date"=>$query['DATE']
        ));
        
        echo json_encode(array("result"=>$result));
//        if ($result->num_rows > 0) {
//        // output data of each row
//        while($row = $result->fetch_assoc()) {
//            echo "cost: " . $row["COST"]. " - Email: " . $row["EMAIL"]. "<br>";
//        }
//      } else {
//          echo "0 results";
//      }

        mysqli_close($conn)
        ?>
    </body>
</html>
