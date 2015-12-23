<?php

/***************************************************************************
 * 
 * 	Description 	: 	[inputfilter.php] Configure File
 *		Author 			: 	Michael LiuXin
 *		Date 			:	01/07/2013 
 *
 **************************************************************************/
 
class MercuryConf {

	public static $debug = false;
	public static $encoding = "UTF-8";
	
	public static $vcode = "sports";
	public static $localIPOnly = false;
	
	public static $defaultAction = 'default';
	public static $defaultMethod = 'default_func';

	public static $dispatchActionWord = 'action';
	public static $dispatchMethodWord = 'do';
	
	/**
	  *  1  =>  LOG_DEBUG
	  *  2  =>  LOG_TRACE
	  *  4  =>  LOG_WARNNING
	  *  8  =>  LOG_FATAL
	  */
	public static $logName = 'mercury';
	public static $logLevel = 1;
	
	public static $arr_error_info = array(
										400 => array('error_code' => 10001, 'error_msg' => 'Request params not valid'),
										401 => array('error_code' => 10002, 'error_msg' => 'Authentication failed'),
										402 => array('error_code' => 10003, 'error_msg' => 'Server Reject'),
										404 => array('error_code' => 10004, 'error_msg' => 'Resource not found'),
										500 => array('error_code' => 10005, 'error_msg' => 'Server backend error'),
									  );
}

?>
