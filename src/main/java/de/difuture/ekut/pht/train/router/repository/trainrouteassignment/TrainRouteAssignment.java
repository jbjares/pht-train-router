package de.difuture.ekut.pht.train.router.repository.trainrouteassignment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public final class TrainRouteAssignment {

    @Id
    private UUID trainID;

    @ElementCollection(targetClass = Long.class, fetch = FetchType.EAGER)
    private Set<Long> routes;
}
