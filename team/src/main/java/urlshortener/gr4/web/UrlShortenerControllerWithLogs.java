package urlshortener.gr4.web;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import urlshortener.common.domain.Location;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.domain.UserInfo;
import urlshortener.common.repository.LocationRepository;
import urlshortener.common.web.UrlShortenerController;
import urlshortener.gr4.googlesafebrowsing.SafeBrowsing;
import urlshortener.gr4.browseros.BrowserOs;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);
	
	@Autowired
	protected LocationRepository locationRepository;
	
	private BrowserOs browserOs = new BrowserOs();

	@Override
	@RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection with hash " + id);
		ResponseEntity<?> shortURL =  super.redirectTo(id, request);
		
		String ip = request.getRemoteAddr();
		logger.info("IP := " + ip);
		
		//IP PUBLICA DE PRUEBA = 155.210.211.33
		JSONObject locationIp = getLocationByIP(request.getRemoteAddr());
		//JSONObject locationIp = getLocationByIP(request.getRemoteAddr());
		
		createAndSaveLocation(id, locationIp);
		return shortURL;
	}

	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		
		logger.info("Requested new short for uri " + url);
		
		SafeBrowsing sb = new SafeBrowsing();
        try {
            boolean isSafe = sb.isSafe(url);
            logger.info("The uri " + url + "is safe? " + isSafe);
        } 
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		return super.shortener(url, sponsor, request);
	}
	
	public JSONObject getLocationByIP(String ip) {
		JSONObject obj = null;
		try {
			//Get LAT, LNG and other info associated with this ip.
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.getForEntity(
			        "http://ip-api.com/json/" + ip, String.class);
			
			//Save the result in JSON Object
			obj = new JSONObject(response.getBody());
			obj.put("ip", ip);
			logger.info(obj.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj;
		
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
	
	private void createAndSaveLocation(String hash, JSONObject locationIp) {
		try {
			Location location = null;
			if (locationIp.getString("status").compareTo("success") == 0) {
				location = new Location(hash, locationIp.getString("city"), locationIp.getString("country"),
						locationIp.getString("lat"), locationIp.getString("lon"), locationIp.getString("ip"),
						locationIp.getString("regionName"), locationIp.getString("org"), null);
				
				logger.info("INFO IP := " + location.toString());
			} else {
				location = new Location(hash, null, "private range",
						null, null, locationIp.getString("ip"),
						null, null, null);
			}
			location=locationRepository.save(location);
			logger.info(location!=null?"["+hash+"] saved with id ["+location.getId()+" in Location]":"["+hash+"] was not saved");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
