package de.difuture.ekut.pht.train.router.repository.trainroutes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrainRoutesRepository extends JpaRepository<TrainRoutes, UUID> {}
