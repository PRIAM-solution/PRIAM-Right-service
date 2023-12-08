package priam.datarequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import priam.datarequest.entities.DataRequest;

import javax.persistence.OneToOne;
import java.util.Date;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAnswerRequestDTO {
    private int idAnswer;
    @Getter
    private boolean answer;
    private String claimAnswer;
    private Date claimDate;

    private int idDataRequest;
}
