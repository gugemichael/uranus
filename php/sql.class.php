<?php

require_once ("utils.php");

/**
 * template configure , No Useful ! 
 */
$__templateConf =  array(
	'ip' => '10.232.31.176',
	'port' => 3506,
	'dbname' => "jushita_mbp",
	'user'=>'jushita_mbp',
	'pass' => 'jushita_mbp123'
);

class MysqlRunner {

	/**
	 * mysql connection resource
	 */
	private $svrConn = false;

	private function __sql_execute($sql) {

		$result = mysql_query(($sql), $this->svrConn);

		if ($result === false)
			logger_error("sql : $sql, msyql-thread : " . mysql_thread_id($this->svrConn) . ", execute failed < " . mysql_error($this->svrConn) . " >");

		return $result;
	}

	protected function __rows() {
		return mysql_affected_rows($this->svrConn);
	}

	public function __construct() {
	}

	public function execute($sql) {
		return $this->__sql_execute($sql);
	}


	public function query($sql) {
		$result = $this->__sql_execute($sql);
		if ($result === false)
			return false;

		$sum = mysql_num_rows($result);
		$ret = array();
		while ($record = mysql_fetch_assoc($result)) {
			array_push($ret, $record);
		}
		logger("sql : $sql, query success , count($sum)");
		return $ret;
	}

	public function update($sql) {
		$result = $this->__sql_execute($sql);
		if ($result === false)
			return false;

		$affect = $this->__rows();
		logger("sql : $sql, update success , effect($affect)");
		return $affect;
	}

	public function insert($sql) {
		$result = $this->__sql_execute($sql);
		if ($result === false)
			return false;
		$affect = $this->__rows();
		logger("sql : $sql, insert success , effect($affect)");
		return $affect;
	}

	public function del($sql) {
		$result = $this->__sql_execute($sql);
		if ($result === false)
			return false;
		$affect = $this->__rows();
		logger("sql : $sql, delete success , remove($affect)");
		return $affect;
	}

	public function lastid() {
		return mysql_insert_id($this->svrConn);
	}

	public function connect($mysqlConf) 
	{
		global $__templateConf;

		$hiddenPassConf = array_merge(array(),$mysqlConf);
		$hiddenPassConf['pass'] = str_repeat("*",strlen($hiddenPassConf['pass']));

		logger("Mysql Url : " . json_encode($hiddenPassConf));

		if (count(array_diff_key($__templateConf,$mysqlConf))) {
			logger_error("Invalid Database Conf !");
			return false;
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
					logger_error("Connect DB Use Up RetryTimes !");
				}
				logger_error("Connect DB , $ipAddress:$port Failed , retry {$j}");
				continue;
			}

			logger("Connect DB Success");
			$dbName = $mysqlConf['dbname'];
			if (!mysql_select_db($dbName, $svrConn)) {	
				logger_error("Select DB : ".$dbName." Failed");
				mysql_close($svrConn);
				break;
			}

			logger("Select DB : ".$dbName." OK");
			$this->svrConn = $svrConn;
			return $svrConn;
		}

		// no connection avaliable !
		return false;
	}


	public function close() {
		mysql_close($this->svrConn);
	}
}

?>
