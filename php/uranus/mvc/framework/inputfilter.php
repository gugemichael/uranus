<?php

/***************************************************************************
 * 
 * 	Description 	: 	[inputfilter.php] input dispatcher
 *		Author 			: 	Michael LiuXin
 *		Date 			:	01/07/2013 
 *
 **************************************************************************/

class InputFilter {

	private function genAccessCode($app) {
		if (!isset($app->request->inputs['token'])) {
			MercuryLogger::warnning('request is without auth token');
			throw new Exception('mercury.auth Without Auth Token');
		}
		// params need sort
		ksort($app->request->inputs);
		$basic_string = '';
		foreach ($app->request->inputs as $key => $value) {
			if ($key != 'vcode')
				$basic_string .= $key . "=" . $value . "&";
		}
		$basic_string .= 'vcode=' . MercuryConf::$vcode;
		
		// final encode sequence
		$app->request->acl = md5($basic_string);
		
		if ($app->request->acl != $app->request->inputs['token']) {
			MercuryLogger::warnning('request auth token[' . $app->request->inputs['token'] . '] is not same as [ ' . $app->request->acl . ']');
			throw new Exception('mercury.auth Auth Token Not Vailed');
		}
	}

	private function traitsParams($app) {
		// GET or POST
		$app->request->http_method = $_SERVER ['REQUEST_METHOD'];
		// merge get & post params
		$arr = array_merge ( $_GET, $_POST );
		if (!isset($arr[MercuryConf::$dispatchActionWord]) || !isset($arr[MercuryConf::$dispatchMethodWord])) {
			$app->request->action = MercuryConf::$defaultAction;
			$app->request->method = MercuryConf::$defaultMethod;
		} else {
			$app->request->action = $arr[MercuryConf::$dispatchActionWord];
			$app->request->method = $arr[MercuryConf::$dispatchMethodWord];
		}
		$app->request->inputs = $arr;
		// get client ip
		if (! empty ( $_SERVER ['HTTP_CLIENTIP'] )) 
			$app->request->userip = $_SERVER ['HTTP_CLIENTIP'];
		elseif (! empty ( $_SERVER ['REMOTE_ADDR'] )) 
			$app->request->userip = $_SERVER ['REMOTE_ADDR'];
		// whole url
		$app->request->url = $_SERVER['REQUEST_URI'];
		if (0 == strcasecmp($app->request->http_method,'GET')) {
			$pos = strpos($app->request->url, "?");
			// we just get the stirng before "?"
			if(false !== $pos) 
				$app->request->url = substr($app->request->url, 0, $pos);
		}
		$app->request->url = strtolower($app->request->url);
		
		$query = http_build_query ( $_GET );
		// uri == url + params
		$app->request->uri = $app->request->url . ($query ? "?$query" : "");

		$app->request->serverEnvs = $_SERVER;
	}
	
	private function isLocalIp($ip) {
		if (0 == strncmp ( $ip, '10.', 3 ) 
			|| 0 == strncmp ( $ip, '172.', 4 ) 
			|| 0 == strncmp ( $ip, '127.', 4 ) 
			|| 0 == strncmp ( $ip, '192.', 4 )) {
			return true;
		}
		return false;
	}

	function process(MercuryMVC $app) {
		$this->traitsParams ( $app );
		$this->genAccessCode ($app);
		$ip = $app->request->userip;
		if (MercuryConf::$localIPOnly && !($this->isLocalIp($ip))) {
			throw new Exception("ip [$ip] not permit to login ");
		}
	}
}

?>
