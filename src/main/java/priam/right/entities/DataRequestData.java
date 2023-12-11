package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(DataRequestDataKey.class)
@Table(name="data_request_data")
public class DataRequestData {
    @Id
    private int dataRequestId;
    @Id
    private int dataId;
    private boolean answer = false;
}
