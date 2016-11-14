package urlshortener.common.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import urlshortener.common.domain.Location;


@Repository
public class LocationRepositoryImpl implements LocationRepository {

	private static final Logger log = LoggerFactory
			.getLogger(LocationRepositoryImpl.class);

	private static final RowMapper<Location> rowMapper = new RowMapper<Location>() {
		@Override
		public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Location(rs.getString("shortURL"), rs.getString("city"), rs.getString("country"),
					rs.getString("lat"), rs.getString("lon"), rs.getString("ip"),
					rs.getString("regionName"), rs.getString("org"), rs.getLong("id"));
		}
	};

	@Autowired
	protected JdbcTemplate jdbc;

	public LocationRepositoryImpl() {
	}

	public LocationRepositoryImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public List<Location> findByHash(String hash) {
		try {
			return jdbc.query("SELECT * FROM location WHERE hash=?",
					new Object[] { hash }, rowMapper);
		} catch (Exception e) {
			log.debug("When select for hash " + hash, e);
			return null;
		}
	}

	@Override
	public Location save(final Location loc) {
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			jdbc.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn
							.prepareStatement(
									"INSERT INTO LOCATION VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
									Statement.RETURN_GENERATED_KEYS);
					ps.setNull(1, Types.BIGINT);
					ps.setString(2, loc.getShortURL());
					ps.setString(3, loc.getCity());
					ps.setString(4, loc.getCountry());
					ps.setString(5, loc.getIp());
					ps.setString(6, loc.getLatitude());
					ps.setString(7, loc.getLongitude());
					ps.setString(8, loc.getRegionName());
					ps.setString(9, loc.getOrganization());
					return ps;
				}
			}, holder);
			new DirectFieldAccessor(loc).setPropertyValue("id", holder.getKey()
					.longValue());
		} catch (DuplicateKeyException e) {
			log.debug("When insert for location with id " + loc.getShortURL(), e);
			return loc;
		} catch (Exception e) {
			log.debug("When insert a location", e);
			return null;
		}
		return loc;
	}

	@Override
	public void update(Location location) {
		log.info(location.toString());
		try {
			jdbc.update(
					"update location set hash=?, city=?, country=?, ip=?, lat=?, lng=?, region=?, organization=? where id=?",
					location.getShortURL(), location.getCity(), location.getCountry(),
					location.getIp(), location.getLatitude(), location.getLongitude(),
					location.getRegionName(), location.getOrganization(), location.getId());
			
		} catch (Exception e) {
			log.info("When update for id " + location.getId(), e);
		}
	}

	@Override
	public void delete(Long id) {
		try {
			jdbc.update("delete from location where id=?", id);
		} catch (Exception e) {
			log.debug("When delete for id " + id, e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			jdbc.update("delete from location");
		} catch (Exception e) {
			log.debug("When delete all", e);
		}
	}

	@Override
	public Long count() {
		try {
			return jdbc
					.queryForObject("select count(*) from location", Long.class);
		} catch (Exception e) {
			log.debug("When counting", e);
		}
		return -1L;
	}

	@Override
	public List<Location> list(Long limit, Long offset) {
		try {
			return jdbc.query("SELECT * FROM location LIMIT ? OFFSET ?",
					new Object[] { limit, offset }, rowMapper);
		} catch (Exception e) {
			log.debug("When select for limit " + limit + " and offset "
					+ offset, e);
			return null;
		}
	}

}
