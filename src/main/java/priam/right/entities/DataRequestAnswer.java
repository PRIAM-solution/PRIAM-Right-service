package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.right.enums.AnswerType;

import javax.persistence.*;
import java.util.Date;
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DataRequestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dataRequestAnswerId;
    private AnswerType answer;
    private String dataRequestClaim;
//    private Date claimDate; //TODO: remove
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "data_request_id")
    private DataRequest dataRequest;
}
