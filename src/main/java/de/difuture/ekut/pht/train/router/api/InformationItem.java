package de.difuture.ekut.pht.train.router.api;

import java.util.Set;

public interface InformationItem {

    /**
     * Determines whether the set of specified information items
     * requires the stationOffice service
     *
     * @param items The set of Information items to be considered
     *
     * @return Whether the specified set of InformationItems
     * requires to contact the Station Office.
     */
    // TODO Refactor once we have model representation of the PHT involved services
    static boolean requiresStationOffice(final Set<InformationItem> items) {

        return items.contains(NodeInformationItem.STATION_NAME);
    }
}
