package priam.right.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.right.enums.TypeDataRequest;
import java.util.Date;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestListDTO {
    private int requestId;
    private TypeDataRequest typeRequest;
    private Date issuedAt;
    private boolean response;
    private String dataSubjectCategory;
}
