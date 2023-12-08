package priam.datarequest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import priam.datarequest.entities.DataRequest;
import priam.datarequest.enums.TypeDataRequest;

import java.util.List;
//@Repository
public interface DataRequestRepository extends JpaRepository<DataRequest, Integer> {

    List<DataRequest> findByDataId(int DataId);

    List<DataRequest> findByDataSubjectId(int DataSubjectId);

    List<DataRequest> findByType(TypeDataRequest type);
    @Query(value= "SELECT id FROM data_request ORDER BY claim_date DESC LIMIT 1", nativeQuery = true)
    String selectLastIdRequest();















    //List<Data> findAllDataByDataSubjectCategory(DSCategory dSCategory);
}
