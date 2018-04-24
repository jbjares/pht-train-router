package de.difuture.ekut.pht.train.router.api;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class NodeInfo {

    private Long nodeID;
    private String stationName;
}
