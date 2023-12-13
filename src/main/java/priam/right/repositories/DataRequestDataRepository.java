package priam.right.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import priam.right.entities.DataRequestData;

import java.util.List;

//@Repository
public interface DataRequestDataRepository extends JpaRepository<DataRequestData, Integer> {

    @Query(value = "SELECT data_id FROM Data_Request_Data WHERE data_request_id = :dataRequestId",
            nativeQuery = true)
    List<Integer> findDataIdsByDataRequestId(int dataRequestId);


    @Query(value= "SELECT answer FROM Data_Request_Data WHERE data_id =:dataId AND data_request_id IN " +
            "(SELECT id FROM data_request WHERE data_subject_id = :dataSubjectId AND claim_date BETWEEN" +
            "        DATE_SUB(DATE(NOW()), INTERVAL 2 DAY) AND DATE(NOW()) )",
            nativeQuery = true)
    boolean isDataAcceptedByDataSubjectIdAndDataId(int dataSubjectId, int dataId);
}
