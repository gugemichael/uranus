<?php

/***************************************************************************
 * 
 * 	Description 	: 	[request.php] presents a request network
 *		Author 			: 	Michael LiuXin
 *		Date 			:	01/07/2013 
 *
 **************************************************************************/

 
require_once dirname ( __FILE__ ) . '/mercury_mvc.php';

class Request {

	var $inputs = array ();
	var $headers = array ();
	var $http_method = 'GET';
	var $method = '';
	var $userip = '127.0.0.1';
	var $url = '';
	var $uri = '';
	var $serverEnvs = array ();
	var $of = "json";
	var $is_https = false;
	var $action = '';
	var $acl = '';
	private $app = null;
	
	function __construct(MercuryMVC $app) {
		$this->app = $app;
		$this->now = time ();
	}
	
	// raw request data , no dealed
	function getraw($key, $default = null) {
		if (isset ( $_GET [$key] )) {
			return $_GET [$key];
		}
		if (isset ($_POST [$key])) {
			return $_POST [$key];
		}
		return $default;
	}
	
	function get($key, $default = null) {
		if (is_string ( $key )) {
			if (isset ( $this->inputs [$key] )) {
				return $this->inputs [$key];
			}
			return $default;
		} else {
			$ret = array ();
			foreach ( $key as $k ) {
				if (isset ( $this->inputs [$k] )) {
					$ret [] = $this->inputs [$k];
				} else {
					$ret [] = $default;
				}
			}
			return $ret;
		}
	}

	function getHeader($key, $default = null) {
		$name = 'HTTP_' . strtoupper ( $key );
		if (isset ( $this->serverEnvs [$name] )) {
			return $this->serverEnvs [$name];
		}
		return $default;
	}

}

/* vim: set ts=4 sw=4 sts=4 tw=100 noet: */
?>
