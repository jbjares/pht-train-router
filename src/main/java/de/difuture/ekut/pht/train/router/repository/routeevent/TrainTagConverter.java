package de.difuture.ekut.pht.train.router.repository.routeevent;

import de.difuture.ekut.pht.lib.core.traintag.TrainTag;

import javax.persistence.AttributeConverter;

public class TrainTagConverter implements AttributeConverter<TrainTag, String> {

    @Override
    public String convertToDatabaseColumn(final TrainTag trainTag) {

        return trainTag.getStringRepresentation();
    }

    @Override
    public TrainTag convertToEntityAttribute(final String tag) {

        return TrainTag.of(tag);
    }
}
