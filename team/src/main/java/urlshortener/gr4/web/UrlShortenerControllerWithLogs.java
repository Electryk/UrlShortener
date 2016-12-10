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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;


import javax.servlet.http.HttpServletRequest;

import urlshortener.common.domain.Location;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.repository.LocationRepository;
import urlshortener.common.web.UrlShortenerController;
import urlshortener.gr4.geoIpLocation.LocationIp;
import urlshortener.gr4.googlesafebrowsing.SafeBrowsing;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);
	private LocationIp locationIp = new LocationIp();
	
	@Autowired
	protected LocationRepository locationRepository;

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

		//Get the associated location to ip and save in the data base.
		String ip = request.getRemoteAddr();
		locationIp.saveLocation(ip, hash, locationRepository);
				
		return super.redirectTo(id, request);
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

	@RequestMapping(value = "/locations", method = RequestMethod.GET)
	public ResponseEntity<List<Location>> getLocations(@RequestParam("pattern") String pattern, 
													   @RequestParam("dateInit") Long dateInit,
													   @RequestParam("dateEnd") Long dateEnd) {

		//Get an ArrayList of Locations
		List<Location> list = locationIp.getLocationsByHash(pattern, dateInit, dateEnd, locationRepository);

		return new ResponseEntity<List<Location>>(list, HttpStatus.OK);		
	}
}
