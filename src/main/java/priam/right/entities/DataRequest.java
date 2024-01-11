package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.right.enums.TypeDataRequest;

import javax.persistence.*;
import java.util.Date;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="data_request")
public class DataRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@NotNull
    //@JsonProperty(value = "claim")
    private String claim;
    private Date claimDate;
    //@NotNull
    //@JsonProperty(value = "newValue")
    private String newValue;

    private boolean isIsolated;

    @Enumerated(EnumType.STRING)
    private TypeDataRequest type;
    private int dataSubjectId;
    @Transient
    private DataSubject dataSubject;

    private String primaryKeyValue;

    private boolean response;
}
