package de.difuture.ekut.pht.train.router.client;


import de.difuture.ekut.pht.lib.core.model.Train;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FeignClient("train-office")
public interface TrainOfficeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/train/{trainID}")
    Optional<Train> getTrain(@PathVariable UUID trainID);

    @RequestMapping(method = RequestMethod.GET, value = "/train")
    List<Train> getTrains();
}
