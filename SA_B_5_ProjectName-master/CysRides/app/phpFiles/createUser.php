<html>
    <head>
      <title>Create User</title>
    </head>
    <body>
        <?php

        $netID = $_POST["netID"];
        $userPassword = $_POST["userPassword"];
        $confirmationCode = $_POST["confirmationCode"];
        $firstName = $_POST["firstName"];
        $lastName = $_POST["lastName"];
        $venmo = $_POST["venmo"];
        $profileDescription = $_POST["profileDescription"];
        $userType = $_POST["userType"];
        $userRating = $_POST["userRating"];
        $dateJoined = $_POST["dateJoined"];

        $host="mysql.cs.iastate.edu";
        $port=3306;
        $socket="";
        $username = 'dbu309sab5';
        $password = 'VRCc@3V2';
        $dbname = 'db309sab5';

        $conn = new mysqli($host, $username, $password, $dbname, $port, $socket) or die('Could not connect to database server'.mysqli_connect_error);

        //inserts user fields into the USER_TABLE
        $sql = "INSERT INTO USER_TABLE (NETID, PASSWORD, CONFIRMATION_CODE, FIRST_NAME, LAST_NAME, VENMO, PROFILE_DESCRIPTION, USER_TYPE, USER_RATING, DATE_JOINED) VALUES ('".$netID."','".$userPassword."','".$confirmationCode."','".$firstName."','".$lastName."','".$venmo."','".$profileDescription."','".$userType."',".$userRating.", '".$dateJoined."');";

        $sql1 = "INSERT INTO USER_RATINGS_TABLE (RATINGS_NETID) VALUES ('".$netID."');";

        if(mysqli_query($conn,$sql)) {
            echo "Data insertion success for user...";
        } else {
            echo "Error while insertion for user..." . $sql;
        }

        if(mysqli_query($conn,$sql1)) {
            echo "Data insertion success for rating...";
        } else {
            echo "Error while insertion for rating..." . $sql1;
        }

        mysqli_close($conn);

        ?>
    </body>
</html>