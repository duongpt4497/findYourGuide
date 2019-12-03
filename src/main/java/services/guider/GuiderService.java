package services.guider;

import entities.Guider;
import entities.Contract;

import java.util.List;

public interface GuiderService {
    Guider findGuiderWithID(long id) throws Exception;

    Guider findGuiderWithPostId(long id) throws Exception;

    Contract findGuiderContract(long id) throws Exception;

    long createGuider(Guider newGuider) throws Exception;

    long createGuiderContract(Contract newGuiderContract) throws Exception;

    long updateGuiderWithId(Guider guiderNeedUpdate) throws Exception;

    long activateGuider(long id) throws Exception;

    long deactivateGuider(long id) throws Exception;

    List<Guider> searchGuiderByName(String key) throws Exception;

    List<Guider> getTopGuiderByRate() throws Exception;

    List<Guider> getTopGuiderByContribute() throws Exception;

    void linkGuiderWithContract(long guider_id, long contract_id) throws Exception;

    List<Contract> getAllContract() throws Exception;
}
