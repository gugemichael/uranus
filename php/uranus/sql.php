<?php

ini_set('date.timezone','Asia/Shanghai');

/**
 * mysql connection resource
 */
$svrConn = false;


$log = false;

function log_on() {
	global $log;
	$log = true;
}

function logger($msg) {
	global $log;
	if ($log) {
		$time = date("Y-m-d H:i:s");
		echo "[ $time ] ". " => " . $msg . "\n"; 
	}
}

function __sql_execute($sql) {

	global $svrConn;

	$result = mysql_query(($sql), $svrConn);

	if ($result === false)
		logger("[ sql : $sql ] [ thread-id : " . mysql_thread_id($svrConn) . " ] execute failed < " . mysql_error($svrConn) . " >");

	return $result;
}

function __rows() {
	global $svrConn;
	return mysql_affected_rows($svrConn);
}

function sql_query($sql) {
	$result = __sql_execute($sql);
	if ($result === false)
		return false;

	$sum = mysql_num_rows($result);
	$ret = array();
	while ($record = mysql_fetch_assoc($result)) {
		array_push($ret, $record);
	}
	logger("[ sql : $sql ] query success , count [ $sum ]");
	return $ret;
}

function sql_update($sql) {
	$result = __sql_execute($sql);
	if ($result === false)
		return false;

	$affect = __rows();
	logger("[ sql : $sql ] update success , effect [ $affect ]");
	return $affect;
}

function sql_insert($sql) {
	$result = __sql_execute($sql);
	if ($result === false)
		return false;
	$affect = __rows();
	logger("[ sql : $sql ] insert success , incr [ $affect ]");
	return $affect;
}

function sql_delete($sql) {
	$result = __sql_execute($sql);
	if ($result === false)
		return false;
	$affect = __rows();
	logger("[ sql : $sql ] delete success , del [ $affect ]");
	return $affect;
}

function sql_last_id() {
	global $svrConn;
	return mysql_insert_id($svrConn);
}

function sql_connection($mysqlConf) 
{
	global $svrConn;
	global $templateConf;

	$hiddenPassConf = array_merge(array(),$mysqlConf);
	$hiddenPassConf['pass'] = str_repeat("*",strlen($hiddenPassConf['pass']));

	logger("Mysql Url : " . json_encode($hiddenPassConf));

	if (count(array_diff_key($templateConf,$mysqlConf))) {
		logger("[ Invalid Database Conf ! ]");
		return;
	}

	// try all the db isntance
	for ($j=0;$j!=3;$j++) {
		$ipAddress = $mysqlConf['ip'];
		$port = $mysqlConf['port'];
		$user = $mysqlConf['user'];
		$pass = $mysqlConf['pass'];
		$svrConn = mysql_connect("$ipAddress:$port", $user,$pass,	1);

		if (!$svrConn) {	
			if ($j==count($mysqlConf)-1) {
				logger("[ Connect DB Use Up RetryTimes ]");
			}
			logger("[ Connect DB , $ipAddress:$port ] Failed , retry {$j}");
			continue;
		}

		logger("[ Connect DB Success ]");
		$dbName = $mysqlConf['dbname'];
		if (!mysql_select_db($dbName, $svrConn)) {	
			logger("[ Select DB : ".$dbName." ] Failed");
			mysql_close($svrConn);
			continue;
		}

		logger("[ Select DB : ".$dbName." ] OK");
		return $svrConn;
	}

	// no connection avaliable !
	return false;
}


function sql_close() {
	global $svrConn;
	mysql_close($svrConn);
}

?>
