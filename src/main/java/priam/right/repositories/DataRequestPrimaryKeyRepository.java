package priam.right.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import priam.right.entities.DataRequestPrimaryKey;
import priam.right.entities.DataRequestPrimaryKeyKey;

//@Repository
public interface DataRequestPrimaryKeyRepository extends JpaRepository<DataRequestPrimaryKey, DataRequestPrimaryKeyKey> {
}
