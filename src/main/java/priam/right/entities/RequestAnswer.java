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
public class RequestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerId;
    private AnswerType answerType;
    private String answerClaim;
    private Date claimDate;
    private Long dataRequestId;
    @OneToOne
    private DataRequest dataRequest;
}
