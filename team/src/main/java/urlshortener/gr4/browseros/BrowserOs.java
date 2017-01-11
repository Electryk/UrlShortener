package urlshortener.gr4.browseros;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import urlshortener.common.domain.Click;
import urlshortener.common.domain.UserInfo;
import urlshortener.common.repository.ClickRepository;

public class BrowserOs {
	
	/**
	 * This fucntion get an ArrayList of all Locations associated to <pattern> 
	 * between <dateInit> and <dateEnd>
	 */
	public List<Click> getClicksByHash(String pattern, Long dateInit, Long dateEnd, 
			ClickRepository clickRepository) {
		
		//Convert <dateInit> and <dateEnd> to Timestamps
		Timestamp initDate = new Timestamp(dateInit);
		Timestamp endDate = new Timestamp(dateEnd);
		
		return clickRepository.listByPattern(pattern, initDate, endDate);
	}
	
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
	
	public String getBrowser(HttpServletRequest request) {
	    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
	    Browser browser = userAgent.getBrowser();
	    String browserName = browser.getName();
	    return browserName;
		
	}
	
	public String getVersion(HttpServletRequest request) {
	    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
	    Version browserVersion = userAgent.getBrowserVersion();
	    String version = browserVersion.toString();
	    return version;
		
	}
	
	public String getPlatform(HttpServletRequest request) {
	    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
	    String os = userAgent.getOperatingSystem().getName();
	    return os;
		
	}

}
