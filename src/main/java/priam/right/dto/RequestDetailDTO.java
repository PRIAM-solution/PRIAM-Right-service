package priam.right.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priam.right.enums.DataRequestType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class DataListItem {
        private int dataId;
        private String attributeName;
        private boolean answerByData;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class DataTypeListItem {
        private String dataTypeName;
        private List<DataListItem> data = new ArrayList<>();
        private void addData(int dataId, String attributeName, boolean answerByData) {
            this.data.add(new DataListItem(dataId, attributeName, answerByData));
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataSubject {
        private int id;
        private String idRef;
        private String dataSubjectCategoryName;
    }

    private int requestId;
    private DataRequestType typeRequest;
    private String newValue;
    private Date issuedAt;
    private boolean response;
    private boolean isIsolated;
    private DataSubject dataSubject;
    private List<DataTypeListItem> dataTypeList = new ArrayList<>();

    public void setDataSubject(int id, String idRef, String dataSubjectCategoryName) {
        this.dataSubject = new DataSubject(id, idRef, dataSubjectCategoryName);
    }
    public void addData(String dataTypeName, int dataId, String attributeName, boolean answerByData) {
        Optional<DataTypeListItem> dataType = this.dataTypeList.stream().filter(dataTypeListItem -> dataTypeName.equals(dataTypeName)).findFirst();
        if(dataType.isPresent()) {
            dataType.get().addData(dataId, attributeName, answerByData);
        }
        else {
            DataTypeListItem dt = new DataTypeListItem();
            dt.setDataTypeName(dataTypeName);
            dt.addData(dataId, attributeName, answerByData);
            this.dataTypeList.add(dt);
        }
    }
}
