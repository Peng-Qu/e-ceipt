<?php 

//Handles connection to database

$db_name = "panzooka_prj566";		//default = panzooka_prj566
$mysql_username = "panzooka_prj566";	//default = panzooka_prj566
$mysql_password = "";			//Please see Discord
$server_name = "localhost";

$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);

/*
if($conn){
	echo "connection success";
}else{
	echo "connection failed";
}
*/

?>