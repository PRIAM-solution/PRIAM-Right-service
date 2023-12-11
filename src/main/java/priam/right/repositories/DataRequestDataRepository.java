package priam.right.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import priam.right.entities.DataRequestData;

import java.util.List;

//@Repository
public interface DataRequestDataRepository extends JpaRepository<DataRequestData, Integer> {

    @Modifying
    @Query(value = "SELECT data_id FROM Data_Request_Data WHERE data_request_id = :dataRequestId",
            nativeQuery = true)
    List<Integer> findDataIdsByDataRequestId(int dataRequestId);
}
