package urlshortener.common.repository.fixture;

import java.sql.Date;

import urlshortener.common.domain.Location;
import urlshortener.common.domain.ShortURL;

public class LocationFixture {

	public static Location locationSuccess(ShortURL su) {
		return new Location(su.getHash(), "Zaragoza", "España", "49,4568", "-0,8563", "255.255.255.255",
				"Aragón", "Universidad de Zaragoza", null, new Date(System.currentTimeMillis()));
	}
	
	public static Location locationFailed(ShortURL su) {
		return new Location(su.getHash(), null, "private range", null, null, "255.255.255.255",
				null, null, null, new Date(System.currentTimeMillis()));
	}
}
