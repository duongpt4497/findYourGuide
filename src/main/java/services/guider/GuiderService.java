package services.guider;

import entities.Guider;
import entities.Guider_Contract;

import java.util.List;

public interface GuiderService {
    public Guider findGuiderWithID(long id);

    public Guider findGuiderWithPostId(long id);

    public Guider_Contract findGuiderContract(long id);

    public long createGuider(Guider newGuider);

    public void createGuiderContract(long guider_id, Guider_Contract newGuiderContract);

    public long updateGuiderWithId(Guider guiderNeedUpdate);

    public long activateGuider(long id);

    public long deactivateGuider(long id);

    public List<Guider> searchGuider(String key);

    public List<Guider> getTopGuiderByRate();

    public List<Guider> getTopGuiderByContribute();
}
