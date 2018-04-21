package de.difuture.ekut.pht.train.router.repository.station;

import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {

    Optional<Station> findStationByUri(final URI uri);
    List<Station> findAllByEnabledIsTrue();
}
