package urlshortener.gr4.geoIpLocation;

import java.sql.Timestamp;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import urlshortener.common.domain.Location;
import urlshortener.common.repository.LocationRepository;

public class LocationIp {
	
	/**
	 * This function is the construct of the object associated with the geoLocation functionality.
	 */
	public LocationIp() {}
	
	/**
	 * This function connect with the location api, process the api's response and save
	 * this info in the data base.
	 */
	public void saveLocation(String ip, String hash, LocationRepository locationRepository) {
		JSONObject locationIp = getLocationByIP(ip);
		createAndSaveLocation(hash, locationIp, locationRepository);
	}
	
	/**
	 * This fucntion get an ArrayList of all Locations associated to <pattern> 
	 * between <dateInit> and <dateEnd>
	 */
	public List<Location> getLocationsByHash(String pattern, Long dateInit, Long dateEnd, 
			LocationRepository locationRepository) {
		
		//Convert <dateInit> and <dateEnd> to Timestamps
		Timestamp initDate = new Timestamp(dateInit);
		Timestamp endDate = new Timestamp(dateEnd);
		
		return locationRepository.listByPattern(pattern, initDate, endDate);
	}
	
	/**
	 * This function get a location associated to <ip> and return
	 * a JSONObject with the ip's location.
	 */
	public JSONObject getLocationByIP(String ip) {
		JSONObject obj = null;
		try {
			//Get LAT, LNG and other info associated with this ip.
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = 
				restTemplate.getForEntity("http://localhost:9000/" + ip, String.class);
			
			//Save the result in JSON Object
			obj = new JSONObject(response.getBody());
			obj.put("ip", ip);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	/**
	 * This function creates a new object location and save one in the spring's data base.
	 */
	public void createAndSaveLocation(String hash, JSONObject locationIp, LocationRepository locationRepository) {
		try {
			Location location = null;
			
			/*
			 * Distinct to IP in public range or IP in private range. The response of the Location API doesn't
			 * equals in public and private ranges.
			 */
			if (locationIp.getString("status").compareTo("success") == 0) {
				//Public range
				location = new Location(hash, locationIp.getString("city"), locationIp.getString("country"),
					locationIp.getString("lat"), locationIp.getString("lon"), locationIp.getString("ip"),
					locationIp.getString("regionName"), locationIp.getString("org"), null, 
					new Timestamp(System.currentTimeMillis()));
				
			} else {
				//Private range
				location = new Location(hash, null, "private range",
					null, null, locationIp.getString("ip"),
					null, null, null, new Timestamp(System.currentTimeMillis()));
			}
			
			//Save the location in the data base.
			location = locationRepository.save(location);
			
			System.out.println(location!=null?"["+hash+"] saved with id [" + location.getId() 
				+ " in Location]" : "[" + hash + "] was not saved");
			System.out.println(location.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
