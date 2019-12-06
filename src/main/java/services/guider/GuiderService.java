package services.guider;

import entities.Guider;
import entities.Contract;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    void rejectContract(long contract_id) throws Exception;

    String uploadContractDocument(MultipartFile file) throws Exception;
}
