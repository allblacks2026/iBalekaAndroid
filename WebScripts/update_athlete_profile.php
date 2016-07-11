<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$currentEmailAddress = $_POST['CurrentEmailAddress'];
$newEmailAddress = $_POST['EmailAddress'];
$password = $_POST['Password'];
$weight = $_POST['Weight'];
$height = $_POST['Height'];
$licenseNo = $_POST['LicenseNo'];
$gender = $_POST['Gender'];

if (isset($currentEmailAddress, $newEmailAddress, $password)) {

    $checkNewEmail = $connectionObject->prepare("CALL CheckAthlete(?)");
    $checkNewEmail->bindParam(1, $newEmailAddress, PDO::PARAM_STR);
    
} else {
    echo "Error200";
}