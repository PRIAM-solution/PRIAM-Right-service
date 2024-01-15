package priam.right.services;


import priam.right.dto.DataRequestRequestDTO;
import priam.right.dto.DataRequestResponseDTO;
import priam.right.dto.RequestDetailDTO;
import priam.right.dto.RequestListDTO;
import priam.right.entities.RequestAnswer;
import priam.right.enums.DataRequestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DataRequestService {

    DataRequestResponseDTO getDataRequest(int id);

    List<DataRequestResponseDTO> getListDataRequestByDataSubjectId(int id);

    DataRequestResponseDTO saveDataRequest(DataRequestRequestDTO dataRequestRequestDTO, DataRequestType dataRequestType);

    DataRequestResponseDTO RectificationAnswer(int idRequest, boolean answer, String claim);

    public DataRequestResponseDTO ErasureAnswer(int idDataRequest, boolean answer, String claimAnswer);
    List<DataRequestResponseDTO> getListRectificationRequests();

    List<DataRequestResponseDTO> getListErasureRequests();
    List<Map<String, String>> DataAccess(int idDS, String dataTypeName, List<String> attributes);

    void saveAccessRequest(String idRef, String claim, ArrayList<Integer> listOfSelectedDataId);

    boolean isAccepted(int dataSubjectId, int dataId);

    List<RequestListDTO> getDataRequestByFilters(List<String> listOfSelectedTypeDataRequests, List<String> listOfSelectedStatus, List<String> listOfSelectedDataSubjectCategories);

    RequestDetailDTO getRequestDataDetail(int requestId);
    RequestAnswer getRequestAnswerByDataRequestId(int requestId);
}
