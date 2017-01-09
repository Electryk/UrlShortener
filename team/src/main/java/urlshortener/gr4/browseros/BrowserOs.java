package urlshortener.gr4.browseros;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import urlshortener.common.domain.UserInfo;

public class BrowserOs {
	public UserInfo getUserInfo(HttpServletRequest request) {
	    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
	    Browser browser = userAgent.getBrowser();
	    String browserName = browser.getName();
	    Version browserVersion = userAgent.getBrowserVersion();
	    String version = browserVersion.toString();
	    String os = userAgent.getOperatingSystem().getName();
	    //Spring will handle the JSON conversion automatically.
	    
	    UserInfo info = new UserInfo(browserName, version, os);
	    return info;
		
	}

}
