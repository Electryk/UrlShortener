package urlshortener.common.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import urlshortener.common.domain.Location;
import urlshortener.common.repository.fixture.LocationFixture;
import urlshortener.common.repository.fixture.ShortURLFixture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class LocationRepositoryTests {

	private EmbeddedDatabase db;
	private LocationRepository repository;
	private JdbcTemplate jdbc;

	@Before
	public void setup() {
		db = new EmbeddedDatabaseBuilder().setType(HSQL)
				.addScript("schema-hsqldb.sql").build();
		jdbc = new JdbcTemplate(db);
		ShortURLRepository shortUrlRepository = new ShortURLRepositoryImpl(jdbc);
		shortUrlRepository.save(ShortURLFixture.url1());
		shortUrlRepository.save(ShortURLFixture.url2());
		repository = new LocationRepositoryImpl(jdbc);
	}

	@Test
	public void thatSavePersistsLocation() {
		Location location = repository.save(LocationFixture.locationSuccess(ShortURLFixture.url1()));
		assertEquals(repository.count().intValue(), 1);
		assertNotNull(location);
		assertNotNull(location.getId());
	}

	@Test
	public void thatErrorsInSaveReturnsNull() {
		assertNull(repository.save(LocationFixture.locationSuccess(ShortURLFixture.badUrl())));
		assertEquals(repository.count().intValue(), 0);
	}

	@Test
	public void thatFindByKeyReturnsAURL() {
		repository.save(LocationFixture.locationSuccess(ShortURLFixture.url1()));
		repository.save(LocationFixture.locationSuccess(ShortURLFixture.url2()));
		repository.save(LocationFixture.locationFailed(ShortURLFixture.url1()));
		repository.save(LocationFixture.locationSuccess(ShortURLFixture.url2()));
		repository.save(LocationFixture.locationFailed(ShortURLFixture.url1()));
		assertEquals(repository.findByHash(ShortURLFixture.url1().getHash()).size(), 3);
		assertEquals(repository.findByHash(ShortURLFixture.url2().getHash()).size(), 2);
	}

	@Test
	public void thatFindByKeyReturnsEmpty() {
		repository.save(LocationFixture.locationSuccess(ShortURLFixture.url1()));
		repository.save(LocationFixture.locationSuccess(ShortURLFixture.url2()));
		repository.save(LocationFixture.locationFailed(ShortURLFixture.url1()));
		repository.save(LocationFixture.locationSuccess(ShortURLFixture.url2()));
		repository.save(LocationFixture.locationFailed(ShortURLFixture.url1()));
		assertEquals(repository.findByHash(ShortURLFixture.badUrl().getHash()).size(), 0);
	}

	@Test
	public void thatDeleteDelete() {
		Long id1 = repository.save(LocationFixture.locationSuccess(ShortURLFixture.url2())).getId();
		Long id2 = repository.save(LocationFixture.locationFailed(ShortURLFixture.url1())).getId();
		repository.delete(id1);
		assertEquals(repository.count().intValue(), 1);
		repository.delete(id2);
		assertEquals(repository.count().intValue(), 0);
	}
	
	@Test
	public void thatDeleteAll() {
		repository.save(LocationFixture.locationSuccess(ShortURLFixture.url2()));
		repository.save(LocationFixture.locationFailed(ShortURLFixture.url1()));
		repository.deleteAll();
		assertEquals(repository.count().intValue(), 0);
	}
	
	@Test
	public void thatUpdate() {
		Long id = repository.save(LocationFixture.locationSuccess(ShortURLFixture.url1())).getId();
		Location loc = LocationFixture.locationFailed(ShortURLFixture.url1());
		loc.setId(id);
		int numUpdatedRows = repository.update(loc);
		assertEquals(numUpdatedRows, 1);
	}

	@After
	public void shutdown() {
		db.shutdown();
	}

}
