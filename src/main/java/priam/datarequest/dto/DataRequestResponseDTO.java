package priam.datarequest.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.datarequest.entities.Data;
import priam.datarequest.entities.DataSubject;
import priam.datarequest.enums.TypeDataRequest;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestResponseDTO {
    private int id;
    private String claim;
    private Date claimDate;
    private String newValue;
    private boolean isIsolated;
    private TypeDataRequest type;

    private Data data;
    private DataSubject dataSubject;

    private String primaryKeyValue;

    private boolean response;
}
