<?

require_once "logger.class.php";

class MercuryCurl
{
	private $curl = null;
	private $url = "";

	public function __construct($url="")
	{
		if (empty($url)) {
			$this->curl = curl_init();
			MERCURY_LOG_TRACE("[warn=param] curl_init without url");
		} else {
			$this->curl = curl_init($url);
			MERCURY_LOG_DEBUG("[ok=param] curl_init with url[{$url}]");
		}

	}

	public function setOption($op,$v)
	{
		curl_setopt($this->curl,$op,$v);
		MERCURY_LOG_DEBUG("[ok=param] set option[" . $op . "]");
	}

	public function getInfo()
	{
		return curl_getinfo($this->curl);
	}

	public function fetch()
	{
		if (!isset($this->curl)) {
			MERCURY_LOG_FATAL("[err=curl] curl handle is null ");
			return false;
		} else {
			MERCURY_LOG_DEBUG("[ok=curl] fetching data ...");
			$r = curl_exec($this->curl);
			if ($r === false) {
				$info = curl_getinfo($this->curl);
				MERCURY_LOG_WARNING("[err=curl] url[{$info->url}] http_code[{$info['http_code']}]");
				return false;
			} else
				return $r;
		}
	}

	public function close()
	{
		curl_close($this->curl);
	}
}

?>

