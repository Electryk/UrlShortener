package urlshortener.gr4.web;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import urlshortener.common.domain.Click;
import urlshortener.common.domain.Location;
import urlshortener.common.domain.ShortURL;

import org.json.JSONException;
import org.json.JSONObject;

import urlshortener.common.domain.Location;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.domain.UserInfo;
import urlshortener.common.repository.ClickRepository;
import urlshortener.common.repository.LocationRepository;
import urlshortener.common.web.UrlShortenerController;
import urlshortener.gr4.geoIpLocation.LocationIp;
import urlshortener.gr4.googlesafebrowsing.SafeBrowsing;
import urlshortener.gr4.browseros.BrowserOs;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);
	private LocationIp locationIp = new LocationIp();
	private BrowserOs browserOs = new BrowserOs();
	
	@Autowired
	protected LocationRepository locationRepository;
	
	@Autowired
	protected ClickRepository clickRepository;
	


	@Override
	@RequestMapping(value = "/{id:(?!link|index|locations).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection with hash " + id);

		String hash = "";

		if (id.contains("+")) {
			hash = id.split("\\+")[0];
		} else {
			hash = id;
		}
		
		String ip = request.getRemoteAddr();
		locationIp.saveLocation(ip, hash, locationRepository);
		String browser = browserOs.getBrowser(request);
		String version = browserOs.getVersion(request);
		String platform = browserOs.getPlatform(request);
		createAndSaveClick(id, ip, browser, version, platform);

		//Get the associated location to ip and save in the data base.
		locationIp.saveLocation(ip, hash, locationRepository);
				
		return super.redirectTo(id, request);
	}
	
	private void createAndSaveClick(String hash, String ip, String browser, String version, String platform) {
		  Click cl = new Click(null, hash, new Date(System.currentTimeMillis()),
		      null, browser + " " + version, platform, ip, null);
		  cl=clickRepository.save(cl);
		  logger.info(cl!=null?"["+hash+"] saved with id ["+cl.getId()+"]":"["+hash+"] was not saved");
		  logger.info(cl!=null?"Click: " + hash + " | " + browser + " | " + version + " | " + platform + " | " + ip:"["+hash+"] was not saved");

		}

	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request,
											  boolean isSafe) {
		
		logger.info("Requested new short for uri " + url);
		try {
			isSafe = SafeBrowsing.CheckIsSafe(url);
		} catch (JsonProcessingException e) {
			logger.info("Error in the SafeBrowsing request");
			e.printStackTrace();
		}

		return super.shortener(url, sponsor, request, isSafe);
	}

	@RequestMapping(value = "/locations", method = RequestMethod.GET)
	public ResponseEntity<List<Location>> getLocations(@RequestParam("pattern") String pattern, 
													   @RequestParam("dateInit") Long dateInit,
													   @RequestParam("dateEnd") Long dateEnd,
													   @RequestParam("center") String center,
													   @RequestParam("limitPoint") String limitPoint) {
		if (pattern.compareTo("") != 0) {
			//Get an ArrayList of Locations
			List<Location> list = locationIp.getLocationsByHash(pattern, dateInit, dateEnd, locationRepository);

			if (center.compareTo("") == 0 && limitPoint.compareTo("") == 0) {
				return new ResponseEntity<List<Location>>(list, HttpStatus.OK);
			} else {
				list = locationIp.getCorrectListByArea(list, center, limitPoint);
				return new ResponseEntity<List<Location>>(list, HttpStatus.OK);
			}		
		} else {
			return new ResponseEntity<List<Location>>(new ArrayList<Location>(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/userinfo", method = RequestMethod.GET)
	public ResponseEntity<List<Click>> getUserinfo(@RequestParam("pattern") String pattern, 
													   @RequestParam("dateInit") Long dateInit,
													   @RequestParam("dateEnd") Long dateEnd) {

		//Get an ArrayList of Locations
		List<Click> list = browserOs.getClicksByHash(pattern, dateInit, dateEnd, clickRepository);
		System.out.println("LISTA: ");
			System.out.println("Elemento: ");
			for (Click c : list) {
				System.out.println("HORA: " + c.getCreated() + " | HASH: " + c.getHash() + " | BROWSER: " + c.getBrowser() + 
						" | PLATFORM: " + c.getPlatform() + " | IP: " + c.getIp());

			}
			
		return new ResponseEntity<List<Click>>(list, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/getUniqueUsers", method = RequestMethod.GET)
	public Integer getUserinfo() {
		return browserOs.getUniqueUsers(clickRepository);

	}
	
	@RequestMapping(value = "/{id:(?!link).*}+.json", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody UserInfo getBrowserOsInfoJson(@PathVariable String id,
			HttpServletRequest request) {
		return browserOs.getUserInfo(request);
	
	}
	
	@RequestMapping(value = "/{id:(?!link).*}+.html", method = RequestMethod.GET, produces = "text/html")
	public @ResponseBody String getInfoPage(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("redireccion");
		return browserOs.getUserInfo(request).toHtml();
	}
}
