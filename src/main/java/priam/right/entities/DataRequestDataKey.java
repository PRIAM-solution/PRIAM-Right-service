package priam.right.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

//@Embeddable
//public class DataRequestDataKey implements Serializable {
//    @Column(name="data_request_id")
//    private int dataRequestId;
//    @Column(name="data_id")
//    private int dataId;
//}
public class DataRequestDataKey implements Serializable {
    private int dataRequestId;
    private int dataId;
}