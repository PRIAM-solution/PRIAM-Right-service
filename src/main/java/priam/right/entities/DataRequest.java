package priam.right.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.right.enums.DataRequestType;

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
    private int requestId;
    private String claim;
    private Date issuedAt;
    private String newValue;

    private boolean isIsolated;

    @Enumerated(EnumType.STRING)
    private DataRequestType requestType;
    private int dataSubjectId;
    @Transient
    private DataSubject dataSubject;

    private boolean response;

    @OneToOne
    RequestAnswer requestAnswer;
}
