package priam.right.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import priam.right.entities.RequestAnswer;

public interface RequestAnswerRepository extends JpaRepository<RequestAnswer, Integer> {

    @Query(value= "SELECT id_answer FROM request_answer ORDER BY claim_date DESC LIMIT 1", nativeQuery = true)
    String selectLastId();
}
