<html>
<head>
<title>View your chart</title></head>
<body>
<h1>Display an influencers chart</h1>
<?php 
include_once 'config.inc.php';
// get influencer chart json file


//print_r($dir);
$files = scandir($dir);
//print_r($files);
?>
<form name="module" action="visualize_results.php" method="get">
	<h3>Choose a file</h3>
	<select name="file">
<?php foreach ($files as $filename){ 	
	$path_parts = pathinfo($filename);
	if ($path_parts['extension']=="json") {
	?>

	<option value="<?php print $filename ?>"><?php print $filename ?></option>
<?php  } 
	}
	?>
	</select>
	<br />
	<h3>How many influencers to display?</h3>
	<br />
	<input type="text" name="max_influencers" value="100" />
	<br />
	
	<br />
	<input type="submit" value="Visualize results!"/>
	</form>
	</body>
	</html>