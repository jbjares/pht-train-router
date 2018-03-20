package de.difuture.ekut.pht.trainrouter;

import de.difuture.ekut.pht.trainrouter.message.TrainUpdateStreams;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;


@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(TrainUpdateStreams.class)
public class TrainrouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainrouterApplication.class, args);
	}
}
