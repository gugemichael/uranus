<?php
/***************************************************************************
 * 
 * Copyright (c) 2011 Baidu.com, Inc. All Rights Reserved
 * 
 **************************************************************************/

/**
 * @file dispatch.php
 * @date 2011/08/31 10:50:11
 * @version $Revision: 791 $ 
 * @brief 
 *  
 **/

class Controller {
	var $request = null;
	var $response = null;
	var $debug = null;
	var $encoding = null;
	var $pages = array ();
	
	function gets() {
		$keys = func_get_args ();
		$ret = array ();
		foreach ( $keys as $key ) {
			$ret [$key] = $this->get ( $key );
		}
		return $ret;
	}
	
	function set($key, $value) {
		return $this->response->set ( $key, $value );
	}
	
}
?>
