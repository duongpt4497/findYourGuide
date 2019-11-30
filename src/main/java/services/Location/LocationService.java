package services.Location;

import entities.Location;

import java.util.List;

public interface LocationService {

    List<Location> showAllLocation();

    void createLocation(String country, String city, String place);
}
