#!/bin/bash 

phpmd ./sql.php text cleancode,codesize,controversial,design,naming,unusedcode
((score=score+$?))
phpmd ./mercury.php text cleancode,codesize,controversial,design,naming,unusedcode
((score=score+$?))

if [ $score -eq 0 ] 
then
	echo "Perfect , code passed !";
else
	echo "Bad code !";
fi

