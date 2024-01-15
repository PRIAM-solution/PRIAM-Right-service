package priam.right.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import priam.right.entities.Data;
import priam.right.entities.DataRequest;
import priam.right.entities.DataSubject;
import priam.right.enums.DataRequestType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestResponseDTO {
    private int requestId;
    private String claim;
    private Date issuedAt;
    private String newValue;
    private boolean isIsolated;
    private DataRequestType requestType;

    private List<Data> datas = new ArrayList<>();
    private DataSubject dataSubject;

    private HashMap<Integer, String> primaryKeys;

    private boolean response;

    public DataRequestResponseDTO(DataRequest dataRequest, List<Data> datas, HashMap<Integer, String> primaryKeys) {
        this.requestId = dataRequest.getRequestId();
        this.claim = dataRequest.getClaim();
        this.issuedAt = dataRequest.getIssuedAt();
        this.newValue = dataRequest.getNewValue();
        this.isIsolated = dataRequest.isIsolated();
        this.requestType = dataRequest.getRequestType();
        this.dataSubject = dataRequest.getDataSubject();
        this.response = dataRequest.isResponse();

        this.datas = datas;
        this.primaryKeys = primaryKeys;
    }
}
