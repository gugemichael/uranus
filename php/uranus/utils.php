<?php

/**
 * console echo logger
 *
 */ 

$__logger = false;

function open_uranus_logger() {
	global $__logger;
	$__logger = true;
}

function logger($msg) {
	global $__logger;
	if ($__logger)
		echo date("Y-m-d H:i:s") . " - TRACE - [ " . $msg . " ] \n"; 
}

function logger_error($msg) {
	global $__logger;
	if ($__logger)
		echo date("Y-m-d H:i:s") . " - ERROR - [ " . $msg . " ] \n"; 
}

/**
 * variables replacer
 *
 */
class Replacer {

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
