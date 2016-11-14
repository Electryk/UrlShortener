package urlshortener.common.repository;

import java.util.List;

import urlshortener.common.domain.Location;

public interface LocationRepository {

	List<Location> findByHash(String hash);

	Location save(Location loc);

	int update(Location loc);

	void delete(Long id);

	void deleteAll();

	Long count();

	List<Location> list(Long limit, Long offset);
}
