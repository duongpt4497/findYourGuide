package services.guider;

import entities.Guider;
import entities.Guider_Contract;

public interface GuiderService {
	public Guider findGuiderWithID(long id);
	
	public Guider findGuiderWithPostId(long id);

	public Guider_Contract findGuiderContract(long id);

	public long createGuider(Guider newGuider) throws Exception;

	public void createGuiderContract(long guider_id, Guider_Contract newGuiderContract);

	public long updateGuiderWithId(Guider guiderNeedUpdate) throws Exception;

	public long activateGuider(long id) throws Exception;

	public long deactivateGuider(long id) throws Exception;
}
