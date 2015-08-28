<?php

/***************************************************************************
 * 
 * 	Description 	: 	[response.php] presents a response network
 *		Author 			: 	Michael LiuXin
 *		Date 			:	01/07/2013 
 *
 **************************************************************************/

require_once dirname ( __FILE__ ) . '/mercury_mvc.php';

class Response {
	private $app = null;
	
	var $template = null;
	var $exception = null;
	var $error = null;
	
	var $headers = array ();
	var $outputs = array ();
	
	function __construct(MercuryMVC $app) {
		$this->app = $app;
	}
	
	function setHeader($header) {
		$this->headers [] = $header;
	}
	
	function set($key, $value = null) {
		$this->outputs [$key] = $value;
	}
	
	function setException($ex) {
		$this->exception = $ex;
	}
	
	function setError($err) {
		$this->error = $err;
	}
	
	function redirect($url, $status = 'ok') {
		$this->setHeader ( 'Location: ' . $url );
		$this->sendHeaders ();
		$this->app->endStatus = $status;
		exit ();
	}
	
	function end() {
		$this->app->filterExecutor->executeFilter ( 'output' );
		$this->app->endStatus = 'ok';
		exit ();
	}
	
	private function _buildContentType($of) {
		switch ($of) {
			case 'json' :
				$this->headers = array_merge ( array ('Content-Type: application/json; charset=' . MercuryConf::$encoding ), $this->headers );
				break;
			case 'html' :
				$this->headers = array_merge ( array ('Content-Type: text/html; charset=' .	MercuryConf::$encoding ), $this->headers );
				break;
			default :
				$this->headers = array_merge ( array ('Content-Type: text/plain; charset=' . MercuryConf::$encoding ), $this->headers );
		}
	}
	
	private function _getResult() {
		if ($this->exception || $this->error) {
			$result = $this->outputs;
		} else {
			if(!empty($this->outputs)) {
			    $result = array(
			                       "response_params" => $this->outputs
					            );
			}else {
				$result = array();
			}
		}
		return $result;
	}
	
	private function _formatResponse() {
		$result = $this->_getResult ();
		$of = $this->app->request->of;
		$this->_buildContentType ( $of );
		
		if ($of == 'json') {
			return json_encode ( $result );
		} else {
			return print_r ( $result, true );
		}		
	}
	
	function sendHeaders() {
		$headers = $this->headers;
		if ($headers) {
			//echo header
			foreach ( $headers as $header ) {
				header ( $header );
			}
		}
	}
	
	function send() {
		$data = $this->_formatResponse ();
		$this->sendHeaders ();

		if ($data) {
			// send to client really
			echo $data;
		}
	}

}

/* vim: set ts=4 sw=4 sts=4 tw=100 noet: */
?>
