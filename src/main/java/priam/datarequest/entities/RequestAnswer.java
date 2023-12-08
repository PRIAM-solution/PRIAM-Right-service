package priam.datarequest.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RequestAnswer {
    @Id
    private int idAnswer;
    private boolean answer;
    private String claimAnswer;
    private Date claimDate;
    @OneToOne
    private DataRequest dataRequest;
}
