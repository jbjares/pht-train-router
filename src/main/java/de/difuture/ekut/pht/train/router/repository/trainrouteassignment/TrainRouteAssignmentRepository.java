package de.difuture.ekut.pht.train.router.repository.trainrouteassignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrainRouteAssignmentRepository extends JpaRepository<TrainRouteAssignment, UUID> {}
