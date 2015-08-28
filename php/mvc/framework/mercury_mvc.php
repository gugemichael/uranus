<?php

/***************************************************************************
 * 
 * 	Description 	: 	[MercuryMVC.php] Php Mercury MVC framework main file
 *		Author 			: 	Michael LiuXin
 *		Date 			:	01/07/2013 
 *
 **************************************************************************/

require_once 'logger.class.php';
 
class MercuryMVC {

	public $debug = false;
	public $encoding = 'UTF-8';
	public $endStatus = 'error';

	public $request = null;
	public $response = null;
	
	function __construct() {
		$this->internalInit();
	}
	
	function run() {
		$this->init();
		$this->process();
		$this->endStatus = 'ok';
	}

	function init() {
		$this->initConf();
		$this->initHttpCore();
		$this->initEnv();
		$this->initLogger();
	}
	
    function process() {

		$input_filter = new InputFilter();
		$input_filter->process($this);

		$dispatch = new Dispatch($this);
		$dispatch->dispatch_url($this->request->url);
		$this->feedback();
	}
	
	private function internalInit() {
		$path = get_include_path ();
		$path = LIB_ROOT . PATH_SEPARATOR . $path;
		set_include_path ($path);
	}
	
	private function initHttpCore() {
		require_once FRAMEWORK_ROOT . 'request.php';
		require_once FRAMEWORK_ROOT . 'response.php';
		require_once FRAMEWORK_ROOT . 'dispatch.php';
		require_once FRAMEWORK_ROOT . 'controller.php';
		require_once FRAMEWORK_ROOT . 'inputfilter.php';
		$this->request = new Request($this);
		$this->response = new Response($this);
	}
	
	private function initConf() {
		require_once CONF_ROOT. 'conf.class.php';
		$this->debug = MercuryConf::$debug;
		$this->encoding = MercuryConf::$encoding;

		// timezone
		date_default_timezone_set("Asia/Shanghai");
	}
	
	private function initEnv() {
		mb_internal_encoding($this->encoding);
		iconv_set_encoding("internal_encoding", $this->encoding);
		// error level to be reported
		error_reporting(E_ALL|E_STRICT);
		// throwing exception & error handler
		//set_error_handler(array($this,'errorHandler'));
		set_exception_handler(array($this,'exceptionHandler'));
		//register_shutdown_function(array($this,'terminalHandler'));
	}
	
    private function initLogger() {
		MercuryLogger::init(MercuryConf::$logName,MercuryConf::$logLevel);
	}

	function errorHandler()	{
		$error = func_get_args();

		if (true === $this->debug) {
			unset($error[4]);
			echo "<pre>\n";
			print_r($error);
			echo "\n</pre>";
		}
		$this->response->setHeader("HTTP/1.1 500 internal server error");
		$this->response->set("error_code", MercuryConf::$arr_error_info[500]['error_code']);
		$this->response->set("error_msg", MercuryConf::$arr_error_info[500]['error_msg']);
		$this->response->setError($error);
		$this->response->send();
		exit;
	}

    private function _exceptionHandler($ex)	{
		restore_exception_handler();
		$errcode = $ex->getMessage();
    	if (0 < ($pos = strpos($errcode,' '))) {
			$errcode = substr($errcode, 0, $pos); 
		}
		$error_code = 500;
		if("mercury.not_found" == $errcode) {
			$this->response->setHeader("HTTP/1.1 404 method not allowed");
			$error_code = 404;
		}else if("mercury.param" == $errcode) {
			$this->response->setHeader("HTTP/1.1 400 bad request");
			$error_code = 400;
		}else if("mercury.reject" == $errcode) {
			$this->response->setHeader("HTTP/1.1 402 Server Reject");
			$error_code = 402;
		}else if("mercury.null" == $errcode) {
			$this->response->setHeader("HTTP/1.1 404 not found");
			$error_code = 404;
		}else if("mercury.auth" == $errcode) {
			$this->response->setHeader("HTTP/1.1 401 Authentication failed");
			$error_code = 401;
		}else {
			$this->response->setHeader("HTTP/1.1 500 internal server error");
		}
		$this->response->set("error_code", MercuryConf::$arr_error_info[$error_code]['error_code']);
		$this->response->set("error_msg", MercuryConf::$arr_error_info[$error_code]['error_msg']);
		$errmsg = sprintf('Caught Exception, errcode:%s', $errcode);
		MercuryLogger::warnning($errmsg);
		$this->endStatus = $errcode;
	}

	function exceptionHandler($ex) {
		$this->_exceptionHandler($ex);
		$this->response->setException($ex);
		$this->response->send();
		exit;
	}
	
	function feedback()
	{
		echo $this->response->send();
	}
}

/* vim: set ts=4 sw=4 sts=4 tw=100 noet: */
?>
