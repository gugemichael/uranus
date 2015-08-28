<?php

/***************************************************************************
 * 
 * 	Description 	: 	[inputfilter.php] Configure File
 *		Author 			: 	Michael LiuXin
 *		Date 			:	01/07/2013 
 *
 **************************************************************************/
 
class Dispatch {
	
	private $app = null;
	
	function __construct(MercuryMVC $app) {
		$this->app = $app;
	}
	
	function dispatch_url($url) {

		$action = $this->app->request->action;
		$method = $this->app->request->method;
		
		$args =  $this->app->request->inputs;

		// if we have the action php file
		if(is_readable ( CONTROLLER_ROOT . 'mercury.' . $action . '.php' )) {
			$path = CONTROLLER_ROOT . 'mercury.' . $action . '.php';
			require_once $path;
			MercuryLogger::trace('File [' . 'mercury.' . $action . '.php' . '] loaded');
		} else {
			// no action file here
			throw new Exception ( 'mercury.not_found ' . 'mercury.' . $action . '.php');
		}
		
		MercuryLogger::trace("Hit ActionController $path : $method");
		
		if (class_exists('ActionController')) {
			$controller = new ActionController();
			$controller->request = $this->app->request;
			$controller->response = $this->app->response;
			$controller->encoding = $this->app->encoding;
			$controller->debug = $this->app->debug;
			if (!$this->_callmethod ($controller, $method, $args)) {
				throw new Exception ('mercury.not_found ' . $path . " : " . $method);
			}
		} else {
			throw new Exception ('mercury.not_found No Class Loaded');
		}
	}
	
	private function _callmethod($controller, $method, $args = array()) {
		if (is_callable ( array ($controller, $method ) )) {
			$reflection = new ReflectionMethod ( $controller, $method );
			MercuryLogger::trace('Reflect Func[' . $method . ']');
			$reflection->invokeArgs ( $controller, array($args));
			return true;
		}
		MercuryLogger::fatal('Reflect Func[' . $method . '] Failed');
		return false;
	}

}

/* vim: set ts=4 sw=4 sts=4 tw=100 noet: */
?>
