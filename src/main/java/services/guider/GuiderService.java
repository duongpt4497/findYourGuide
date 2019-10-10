package services.guider;

import entities.Guider;

public interface GuiderService {
	public Guider findGuiderWithID(long id);

	public long updateGuiderWithId(Guider guiderNeedUpdate);
}
