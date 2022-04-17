<?php

    $to = $_POST['to'];
    $from = $_POST['from'];
    $subject = $_POST['subject'];
    $message .= $_POST['message'];
    $headers = "From:" . $from;

    mail($to,$subject,$message,$headers);

?>