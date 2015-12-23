<?php

/***************************************************************************
 * 
 * 	Description 	: 	[inputfilter.php] Configure File
 *		Author 			: 	Michael LiuXin
 *		Date 			:	01/05/2013 
 *
 **************************************************************************/

class ActionController extends Controller {

	public function default_func($args) {
		MercuryLogger::warnning('[warn=param] Action[' . $args['action'] . '] or Method[' . $args['do'] . '] Invailed');
		throw new Exception ('mercury.not_found not vailed');
    }
	
}

?>
