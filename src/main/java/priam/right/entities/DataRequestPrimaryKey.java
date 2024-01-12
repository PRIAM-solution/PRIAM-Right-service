package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(DataRequestPrimaryKeyKey.class)
public class DataRequestPrimaryKey {
    @Id
    private int dataRequestId;
    @Id
    private int primaryKeyId;
    private String primaryKeyValue;

}
