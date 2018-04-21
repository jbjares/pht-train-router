package de.difuture.ekut.pht.train.router.client;

import de.difuture.ekut.pht.lib.core.model.Station;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("station-office")
public interface StationClient {

    @RequestMapping(method = RequestMethod.GET, value = "/station")
    List<Station> getStations();
}
