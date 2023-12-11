package priam.right.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import priam.right.entities.DataRequest;
import priam.right.enums.TypeDataRequest;

import java.util.List;
//@Repository
public interface DataRequestRepository extends JpaRepository<DataRequest, Integer> {

    List<DataRequest> findByDataId(int DataId);

    List<DataRequest> findByDataSubjectId(int DataSubjectId);

    List<DataRequest> findByType(TypeDataRequest type);

    //List<Data> findAllDataByDataSubjectCategory(DSCategory dSCategory);
}
