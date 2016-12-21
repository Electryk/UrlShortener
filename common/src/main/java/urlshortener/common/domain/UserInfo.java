package urlshortener.common.domain;

public class UserInfo {

	private String browserName;
	private String browserVersion;
	private String os;


	public UserInfo(String browserName, String browserVersion, String os) {
		this.browserName = browserName;
		this.browserVersion = browserVersion;
		this.os = os;
	}

	public String getBrowserName() {
		return browserName;
	}

	public String getbrowserVersion() {
		return browserVersion;
	}

	public String getOs() {
		return os;
	}
	
	public String toHtml() {
		return "<html><body>" + getBrowserName() + 
				" | " + getbrowserVersion() + 
				" | " + getOs() + "</body></html>";
	}
}
