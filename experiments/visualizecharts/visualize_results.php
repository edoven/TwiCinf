<html class="no-js" lang="en-US"> <!--<![endif]-->
<head>

<link rel="stylesheet" type="text/css" media="all" href="style.css" />

</head>

<body>
<?php
//print_r($myrows[0]->influencers);
include_once 'config.inc.php';
$file = $_GET['file'];
$max_influencers_to_visualize = 10;
if (isset($_GET['max_influencers']) && is_null($_GET['max_influencers'])==false){
	$max_influencers_to_visualize = $_GET['max_influencers'];
}

//print $file;

// print $dir."/".$file;
$influencers_json  = file_get_contents($dir."/".$file);


$influencers = json_decode($influencers_json);

$position=0;
?>

<div><h1>Chart: <?php echo $file ?></h1> <div>
<table>
<thead>
</thead>
	<tr>
		<th class="position">Position</th>
		<th class="field">Avatar</th>
		<th class="field">Name</th>
		<th class="description">Description</th>
		<th class="position">Score</th>
		<th class="field">Followers</th>
		<th class="field"># Tweet</th>
		<th class="field"># Topic Tweet</th>
		<th class="field">#Mean retweet</th>
		
	</tr>
</thead>
<tbody>
<?php 





$top_rank= $influencers[0]->ranked_user->rank;



//echo $top_rank;
//echo log10($influencers[0]->ranked_user->rank);
/* echo "<br />";
echo ($influencers[0]->ranked_user->rank);
*/
 $rounded_top_rank = round(log10($influencers[0]->ranked_user->rank),2,PHP_ROUND_HALF_UP);



while($position<$max_influencers_to_visualize) {

$influencer = $influencers[$position];



$rank_100 = round(log10($influencer->ranked_user->rank)*100/log10($top_rank),0);

$position_to_print = $position+1;

$message = "https://twitter.com/share?text=&#64".$influencer->twitter_user->screen_name." is in position ".$position_to_print." of the ".urlencode("#FashionRipple chart about #Fashion #influencers")."&amp;url=http://www.findtheripple.com/fashion/";

?>
	<tr>
		<td class="position"><?php print($position_to_print); ?></td>

		<td class="field"><a href="http://www.twitter.com/<?php print($influencer->twitter_user->screen_name); ?>">
<img src="<?php print($influencer->twitter_user->profile_image_url); ?>"></a></td>
		<td class="field"><a href="http://www.twitter.com/<?php print($influencer->twitter_user->screen_name); ?>"><?php print($influencer->twitter_user->name); ?></a></td>
		<td class="description">
<?php print($influencer->twitter_user->description."  ".$influencer->twitter_user->location)." <br /><a href=\"".$influencer->twitter_user->url."\">".$influencer->twitter_user->url."</a>" ?>
                </td>
		<td class="position"><?php print($rank_100); ?></td>
		<td class="field"><?php print($influencer->ranked_user->followers_count) ?></td>
		<td class="field"><?php print_r($influencer->ranked_user->original_tweets) ?></td>
		<td class="field"><?php print_r($influencer->ranked_user->topic_tweets_count) ?></td>
		<td class="field"><?php print_r($influencer->ranked_user->mean_retweets_count) ?></td>
		
	</tr>
<?php 

$position++;
} ?>

</table>
</body>
</html>