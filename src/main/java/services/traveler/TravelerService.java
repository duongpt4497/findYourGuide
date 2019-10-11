package services.traveler;

import entities.Traveler;

public interface TravelerService {
    public long createTraveler(Traveler newTraveler);

    public Traveler findTravelerWithId(long id);

    public void updateTraveler(Traveler travelerNeedUpdate);
}
