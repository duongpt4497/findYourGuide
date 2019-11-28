package services.guider;

import entities.Guider;
import entities.Contract;

import java.util.List;

public interface GuiderService {
    Guider findGuiderWithID(long id);

    Guider findGuiderWithPostId(long id);

    Contract findGuiderContract(long id);

    long createGuider(Guider newGuider);

    long createGuiderContract(Contract newGuiderContract);

    long updateGuiderWithId(Guider guiderNeedUpdate);

    long activateGuider(long id);

    long deactivateGuider(long id);

    List<Guider> searchGuider(String key);

    List<Guider> getTopGuiderByRate();

    List<Guider> getTopGuiderByContribute();

    void linkGuiderWithContract(long guider_id, long contract_id);
}
