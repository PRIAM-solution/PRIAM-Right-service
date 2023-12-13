package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

//@lombok.Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name="data_request_data")
//public class DataRequestData {
//    @EmbeddedId
//    private DataRequestDataKey id;
//
//    @ManyToOne
//    @MapsId("data_request_id")
//    @JoinColumn(name="id")
//    private DataRequest dataRequest;
//
//    @ManyToOne
//    @MapsId("data_id")
//    @JoinColumn(name="id")
//    private Data data;
//
//    private boolean answer = false;
//}
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