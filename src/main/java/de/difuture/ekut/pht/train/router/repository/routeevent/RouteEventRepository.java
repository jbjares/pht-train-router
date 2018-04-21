package de.difuture.ekut.pht.train.router.repository.routeevent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteEventRepository extends JpaRepository<RouteEvent, Long> {

    Optional<RouteEvent> getFirstByProcessedIsFalse();
}
