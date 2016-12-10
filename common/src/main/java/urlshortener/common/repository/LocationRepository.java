package urlshortener.common.repository;

import java.util.List;
import java.sql.Timestamp;

import urlshortener.common.domain.Location;

public interface LocationRepository {

	List<Location> findByHash(String hash);

	Location save(Location loc);

	int update(Location loc);

	void delete(Long id);

	void deleteAll();

	Long count();

	List<Location> list(Long limit, Long offset);
	
	List<Location> listByPattern(String pattern, Timestamp dateInit, Timestamp dateEnd);

	List<Location> listByRange(String hash, Timestamp dateInit, Timestamp dateEnd);
}
