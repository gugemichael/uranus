<?php
/***************************************************************************
 * 
 * Copyright Libmercury , Michael LiuXin <gugemichael@gmail.com>
 *
 * Php : 5.3.6
 * Version : 1.1
 * 
 **************************************************************************/

define ('ROOT', dirname(__FILE__) . "/");
define ('FRAMEWORK_ROOT', ROOT . 'framework/' );
define ('CONTROLLER_ROOT', ROOT . 'controller/' );
define ('LOG_ROOT', ROOT . 'log/' );
define ('CONF_ROOT', ROOT . 'conf/' );
define ('LIB_ROOT', ROOT . 'lib/' );

require_once FRAMEWORK_ROOT . 'mercury_mvc.php';

$service = new MercuryMVC();
$service->run();

/* vim: set ts=4 sw=4 sts=4 tw=100 noet: */
?>
