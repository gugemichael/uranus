<?php

$__mercury_logger = false;

function mercury_log_on() {
	global $__mercury_logger;
	$__mercury_logger = true;
}

function logger($msg) {
	global $__mercury_logger;
	if ($__mercury_logger)
		echo date("Y-m-d H:i:s") . " - TRACE - [ " . $msg . " ] \n"; 
}

function logger_error($msg) {
	global $__mercury_logger;
	if ($__mercury_logger)
		echo date("Y-m-d H:i:s") . " - ERROR - [ " . $msg . " ] \n"; 
}

/**
function logger_error_stderr($msg) {
	global $__mercury_logger;
	if ($__mercury_logger) 
		fwrite($stderr, (date("Y-m-d H:i:s") . " - ERROR - [ " . $msg . " ] \n"));
}
*/

/**
 *
 * ================================= Utils ===========================================
 * 
 */
class Injector{

	public function __construct($str,$symbol) {
		$this->str = $str;
		$this->symbol = $symbol;
	}

	public function vars($pattern,$repl) {
		$this->str = str_replace($this->symbol . $pattern, $repl, $this->str);
		return $this;
	}

	public function make() {
		return $this->str;
	}

}

/**
 *
 * ================================= Logger ===========================================
 * 
 */
interface Dumper
{
	public static function dump($msg);
}

/*
 * console printer
 */
class ConsoleDumper implements Dumper
{
	public function __construct() {
	}

	/**
	 * @OVERRIDE 
	 *
	 * Dump string to console , stdout !
	 *
	 */
	public static function dump($msg) {
		echo $msg . "\n";
	}	
}

/**
 * stdout logger
 */
class Logger extends ConsoleDumper
{
	private static $isDebug = false;

	public function openDebug() {
		Logger::$isDebug = true;
	}

	/**
	 * ------------------------------------------------------
	 * generate the log msg comman part , include "- [time] ==> "
	 *
	 * ------------------------------------------------------
	 *
	 */
	private static function suffix() {
		return " - " . "[" . date("Y-m-d H:i:s") . "] ==> ";
	}

	public static function debug($msg) {
		if (Logger::$isDebug)
			parent::dump("[" . strtoupper(__FUNCTION__)  . "]" . Logger::suffix() . $msg);
	}

	public static function trace($msg) {
		parent::dump("[" . strtoupper(__FUNCTION__) . "]" . Logger::suffix() . $msg);
	}

	public static function error($msg) {
		parent::dump("[" . strtoupper(__FUNCTION__) . "]" . Logger::suffix() . $msg);
	}

	public static function newline() {
		parent::dump("\n");
	}

}

