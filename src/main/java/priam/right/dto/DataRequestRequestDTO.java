package priam.right.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestRequestDTO {
    private int id;
    private String claim;
    //private Date claimDate;
    private String newValue;
    private boolean isIsolated;
    private String type = "Rectification";
    private int dataId;
    private int dataSubjectId;
    private boolean response;

    private String attribute;
    private String idRef;

    private String primaryKeyValue;


}
