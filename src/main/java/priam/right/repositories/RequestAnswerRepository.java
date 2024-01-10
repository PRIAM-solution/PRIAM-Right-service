package priam.right.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import priam.right.entities.RequestAnswer;

import java.util.Optional;

public interface RequestAnswerRepository extends JpaRepository<RequestAnswer, Integer> {

    Optional<RequestAnswer> findRequestAnswerByRequestId(Long requestId);
}
