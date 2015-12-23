<?php

define('NORMAL_FILE','normal');
define('ERR_FILE','err');

/* Logger Level */
define('LOGGER_DEBUG',1);
define('LOGGER_TRACE',2);
define('LOGGER_WARNNING',4);
define('LOGGER_FATAL',8);
	
class UranusLog
{
	/* normal & wf log file */
	public static $log_file = array();

	public static $log_level = LOGGER_DEBUG;

	protected static function __log_write($file,$basic_line,$str)
	{
		$date = date("[Y-m-d][H:i:s] - ");
		fwrite($file,$basic_line.$date.$str."\n");
	}

	public static function destroy($flush=true)
	{

		if ($flush) {
			if (isset(self::$log_file[NORMAL_FILE]) && self::$log_file[NORMAL_FILE])
				fflush(self::$log_file[NORMAL_FILE]);
			if (isset(self::$log_file[ERR_FILE]) && self::$log_file[ERR_FILE])
				fflush(self::$log_file[ERR_FILE]);
		}
		if (isset(self::$log_file[NORMAL_FILE]) && self::$log_file[NORMAL_FILE])
			fclose(self::$log_file[NORMAL_FILE]);
		if (isset(self::$log_file[ERR_FILE]) && self::$log_file[ERR_FILE])
			fclose(self::$log_file[ERR_FILE]);
	}

	public static function debug($str)
	{
		if (LOGGER_DEBUG < self::$log_level)
			return;

		if (!isset(self::$log_file[NORMAL_FILE]) || !self::$log_file[NORMAL_FILE])
			return false;

		// get backtrace for __FUNCTION_NAME__ __LINE__
		$bt = debug_backtrace (); 
		if (isset ( $bt [1] ) && isset ( $bt [1] ['file'] )) {
			$c = $bt [1];
		} else if (isset ( $bt [2] ) && isset ( $bt [2] ['file'] )) { 
			$c = $bt [2];
		} else if (isset ( $bt [0] ) && isset ( $bt [0] ['file'] )) {
			$c = $bt [0];
		} else {
			$c = array ('file' => 'faint', 'line' => 'faint' );
		}   

		$line_no = '[' . basename($c ['file']) . ':' . $c ['line'] . ']';

		self::__log_write(self::$log_file[NORMAL_FILE],"[DEBUG]".$line_no,$str);
	}

	public static function trace($str)
	{
		if (LOGGER_TRACE < self::$log_level)
			return;

		if (!isset(self::$log_file[NORMAL_FILE]) || !self::$log_file[NORMAL_FILE])
			return false;

		$bt = debug_backtrace (); 
		if (isset ( $bt [1] ) && isset ( $bt [1] ['file'] )) {
			$c = $bt [1];
		} else if (isset ( $bt [2] ) && isset ( $bt [2] ['file'] )) { 
			$c = $bt [2];
		} else if (isset ( $bt [0] ) && isset ( $bt [0] ['file'] )) {
			$c = $bt [0];
		} else {
			$c = array ('file' => 'faint', 'line' => 'faint' );
		}   

		$line_no = '[' . basename($c ['file']) . ':' . $c ['line'] . ']';

		self::__log_write(self::$log_file[NORMAL_FILE],"[DEBUG]".$line_no,$str);
	}

	public static function warnning($str)
	{
		if (LOGGER_WARNNING < self::$log_level)
			return;

		if (!isset(self::$log_file[ERR_FILE]) || !self::$log_file[ERR_FILE])
			return false;

		$bt = debug_backtrace (); 
		if (isset ( $bt [1] ) && isset ( $bt [1] ['file'] )) {
			$c = $bt [1];
		} else if (isset ( $bt [2] ) && isset ( $bt [2] ['file'] )) { 
			$c = $bt [2];
		} else if (isset ( $bt [0] ) && isset ( $bt [0] ['file'] )) {
			$c = $bt [0];
		} else {
			$c = array ('file' => 'faint', 'line' => 'faint' );
		}   

		$line_no = '[' . basename($c ['file']) . ':' . $c ['line'] . ']';

		self::__log_write(self::$log_file[ERR_FILE],"[WARNING]".$line_no,$str);
	}

	public static function fatal($str)
	{
		if (LOGGER_FATAL < self::$log_level)
			return;

		if (!isset(self::$log_file[ERR_FILE]) || !self::$log_file[ERR_FILE])
			return false;

		$bt = debug_backtrace (); 
		if (isset ( $bt [1] ) && isset ( $bt [1] ['file'] )) {
			$c = $bt [1];
		} else if (isset ( $bt [2] ) && isset ( $bt [2] ['file'] )) { 
			$c = $bt [2];
		} else if (isset ( $bt [0] ) && isset ( $bt [0] ['file'] )) {
			$c = $bt [0];
		} else {
			$c = array ('file' => 'faint', 'line' => 'faint' );
		}   

		$line_no = '[' . basename($c ['file']) . ':' . $c ['line'] . ']';

		self::__log_write(self::$log_file[ERR_FILE],$line_no,$str);
	}

	public static function init($name,$level = LOGGER_DEBUG)
	{
		$is_ok = is_dir("./logs") ? true : mkdir("./logs", 0775);
		if (!$is_ok) {
			echo "[err=mkdir]\n";
			return false;
		}

		if ($name == '')
			$name = 'mercury';
		
		self::$log_level = $level;

		// open file faild will return false
		self::$log_file[NORMAL_FILE] = fopen("./logs/".$name.".log","a+");
		if (self::$log_file[NORMAL_FILE]===false) {
			echo "[err=open] normal file\n";
			return false;
		}
		self::$log_file[ERR_FILE] = fopen("./logs/".$name.".log.wf","a+");
		if (self::$log_file[ERR_FILE]===false) {
			echo "[err=open] err file\n";
			return false;
		}

		return true;
	}
}

?>
