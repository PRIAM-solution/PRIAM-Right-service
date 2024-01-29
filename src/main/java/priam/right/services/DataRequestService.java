package priam.right.services;


import priam.right.dto.*;
import priam.right.entities.RequestAnswer;
import priam.right.enums.DataRequestType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataRequestService {

    DataRequestResponseDTO getDataRequest(int id);

    List<DataRequestResponseDTO> getListDataRequestByDataSubjectId(int id);

    DataRequestResponseDTO saveDataRequest(DataRequestRequestDTO dataRequestRequestDTO, DataRequestType dataRequestType);
    DataRequestResponseDTO saveAccessRequest(AccessRequestRequestDTO accessRequestRequestDTO);

    RequestAnswer getRequestAnswer(long requestId);
    RequestAnswer saveRequestAnswer(RequestAnswerRequestDTO requestAnswerRequestDTO);

    List<DataRequestResponseDTO> getListRectificationRequests();

    List<DataRequestResponseDTO> getListErasureRequests();
    List<Map<String, String>> DataAccess(int idDS, String dataTypeName, List<String> attributes);


    boolean isAccepted(int dataSubjectId, int dataId);

    List<RequestListDTO> getDataRequestByFilters(List<String> listOfSelectedTypeDataRequests, List<String> listOfSelectedStatus, List<String> listOfSelectedDataSubjectCategories);

    RequestDetailDTO getRequestDataDetail(int requestId);
    RequestAnswer getRequestAnswerByDataRequestId(int requestId);
}
