package services.traveler;

import entities.Traveler;

public interface TravelerService {
    boolean createTraveler(Traveler newTraveler);

    Traveler findTravelerWithId(long id);

    void updateTraveler(Traveler travelerNeedUpdate);

    void favoritePost(int traveler_id, int post_id);
}
