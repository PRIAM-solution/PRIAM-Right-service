package priam.right.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.right.entities.Data;
import priam.right.entities.DataRequest;
import priam.right.entities.DataSubject;
import priam.right.enums.TypeDataRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private List<Data> datas = new ArrayList<>();
    private DataSubject dataSubject;

    private String primaryKeyValue;

    private boolean response;

    public DataRequestResponseDTO(DataRequest dataRequest, List<Data> datas) {
        this.id = dataRequest.getId();
        this.claim = dataRequest.getClaim();
        this.claimDate = dataRequest.getClaimDate();
        this.newValue = dataRequest.getNewValue();
        this.isIsolated = dataRequest.isIsolated();
        this.type = dataRequest.getType();
        this.dataSubject = dataRequest.getDataSubject();
        this.primaryKeyValue = dataRequest.getPrimaryKeyValue();
        this.response = dataRequest.isResponse();

        this.datas = datas;
    }
}
