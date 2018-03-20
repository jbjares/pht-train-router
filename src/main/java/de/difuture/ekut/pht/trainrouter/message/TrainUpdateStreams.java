package de.difuture.ekut.pht.trainrouter.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface TrainUpdateStreams {

	// TODO Move to centralized configuration
	String TRAIN_AVAILABLE = "trainavailable";
	String TRACK = "track";

	@Input(TRAIN_AVAILABLE)
	SubscribableChannel outboundTrainAvailable();

	@Output(TRACK)
	MessageChannel outboundTrack();
}
