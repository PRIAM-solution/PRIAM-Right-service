package priam.right.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import priam.right.dto.*;
import priam.right.entities.*;
import priam.right.enums.AnswerType;
import priam.right.enums.StatusDataRequestType;
import priam.right.enums.DataRequestType;
import priam.right.mappers.DataRequestMapper;
import priam.right.openfeign.DataRestClient;
import priam.right.openfeign.ActorRestClient;

import priam.right.openfeign.ProviderRestClient;
import priam.right.repositories.DataRequestDataRepository;
import priam.right.repositories.DataRequestPrimaryKeyRepository;
import priam.right.repositories.DataRequestRepository;
import priam.right.repositories.RequestAnswerRepository;

import javax.annotation.Generated;
import javax.transaction.Transactional;
import java.util.*;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-05-23T23:03:41+0530"
)

@Service
@Transactional
@AllArgsConstructor
public class DataRequestServiceImpl implements DataRequestService {
    private DataRequestRepository dataRequestRepository;
    private DataRequestMapper dataRequestMapper;
    private DataRestClient dataRestClient;
    private ActorRestClient actorRestClient;
    private ProviderRestClient providerRestClient;
    private RequestAnswerRepository requestAnswerRepository;
    private DataRequestDataRepository dataRequestDataRepository;
    private DataRequestPrimaryKeyRepository dataRequestPrimaryKeyRepository;

    public List<Map<String, String>> DataAccess(int dataSubjectId, String dataTypeName, List<String> attributes){
        System.out.println(providerRestClient.getPersonalDataValues(dataSubjectId, dataTypeName, attributes));
        return providerRestClient.getPersonalDataValues(dataSubjectId, dataTypeName, attributes);
    }

    @Override
    public DataRequestResponseDTO saveDataRequest(DataRequestRequestDTO dataRequestDTO, DataRequestType dataRequestType) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setDataSubjectId(dataRequestDTO.getDataSubjectId());
        dataRequest.setNewValue(dataRequestDTO.getNewValue());
        dataRequest.setDataRequestClaim(dataRequestDTO.getClaim());

        dataRequest.setDataRequestType(dataRequestType);
        dataRequest.setDataRequestIssuedAt(new Date());
        dataRequest.setResponse(false);
        dataRequest.setIsolated(true);

        HashMap<Integer, String> primaryKeys = dataRequestDTO.getPrimaryKeys();

        Data data = dataRestClient.getDataById(dataRequestDTO.getDataId());

        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequestDTO.getDataSubjectId());
        dataRequest.setDataSubject(dataSubject);

        DataRequest result = dataRequestRepository.save(dataRequest);

        // DataRequestData
        DataRequestData drd = new DataRequestData(result.getDataRequestId(), data.getDataId(), false);
        dataRequestDataRepository.save(drd);

        ArrayList<Data> datas = new ArrayList<>();
        datas.add(data);

        // PrimaryKeys
        primaryKeys.forEach((id, value)-> {
            DataRequestPrimaryKey dataRequestPrimaryKey = new DataRequestPrimaryKey(result.getDataRequestId(), id, value);
            dataRequestPrimaryKeyRepository.save(dataRequestPrimaryKey);
        });

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(result, datas, primaryKeys);
        return response;
    }

    @Override
    public DataRequestResponseDTO saveAccessRequest(AccessRequestRequestDTO accessRequestRequestDTO) {
        DataRequest dataRequest = new DataRequest();
        DataSubject dataSubject = actorRestClient.getDataSubject(accessRequestRequestDTO.getDataSubjectId());

        dataRequest.setDataSubject(dataSubject);
        dataRequest.setDataRequestIssuedAt(new Date());
        dataRequest.setDataRequestClaim(accessRequestRequestDTO.getDataRequestClaim());
        dataRequest.setDataRequestType(DataRequestType.ACCESS);

        dataRequest.setResponse(false);
        dataRequest.setIsolated(true);
        dataRequest.setNewValue(null);

        DataRequest result = dataRequestRepository.save(dataRequest);

        ArrayList<Data> datas = new ArrayList<>();


        // Save all DataRequestData
        accessRequestRequestDTO.getData().forEach(dataId -> {
            DataRequestData drd = new DataRequestData(result.getDataRequestId(), dataId.getDataId(), false);
            dataRequestDataRepository.save(drd);
            Data data = dataRestClient.getDataById(dataId.getDataId());
            datas.add(data);
        });

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(result, datas, null);
        return response;
    }

    @Override
    public DataRequestResponseDTO getDataRequest(int dataRequestId) {
        DataRequest dataRequest = dataRequestRepository.getById(dataRequestId);
        ArrayList<Data> datas = new ArrayList<>();

        List<Integer> dataIds = new ArrayList<>();
        dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequestId);
        dataIds.forEach(dataId -> {
            Data data = dataRestClient.getDataById(dataId);
            datas.add(data);
        });

        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
        dataRequest.setDataSubject(dataSubject);

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(dataRequest, datas, null);
        return response;
    }

    @Override
    public List<DataRequestResponseDTO> getListDataRequestByDataSubjectId(int dataSubjectId) {
        List<DataRequestResponseDTO> response = new ArrayList<>();
        List<DataRequest> dataRequestList = dataRequestRepository.findByDataSubjectId(dataSubjectId);

        for (DataRequest dataRequest : dataRequestList)
        {
            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getDataRequestId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getDataById(dataId);
                datas.add(data);
            });

            DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject);
            response.add(new DataRequestResponseDTO(dataRequest, datas, null));
        }

        return response;
    }

    @Override
    public List<DataRequestResponseDTO> getListRectificationRequests() {
        List<DataRequestResponseDTO> response = new ArrayList<>();
        List<DataRequest> dataRequestList = dataRequestRepository.findByDataRequestType(DataRequestType.RECTIFICATION);

        for (DataRequest dataRequest : dataRequestList)
        {

            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getDataRequestId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getDataById(dataId);
                datas.add(data);
            });

            DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject);
            response.add(new DataRequestResponseDTO(dataRequest, datas, null));

        }
        return response;
    }

    @Override
    public List<DataRequestResponseDTO> getListErasureRequests() {
        List<DataRequestResponseDTO> response = new ArrayList<>();
        List<DataRequest> dataRequestList = dataRequestRepository.findByDataRequestType(DataRequestType.ERASURE);

        for (DataRequest dataRequest : dataRequestList)
        {

            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getDataRequestId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getDataById(dataId);
                datas.add(data);
            });

            DataSubject dataSubject1 = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject1);
            response.add(new DataRequestResponseDTO(dataRequest, datas, null));

        }
        return response;
    }

    @Override
    public DataRequestAnswer saveRequestAnswer(RequestAnswerRequestDTO requestAnswerRequestDTO) {
        DataRequest dataRequest = dataRequestRepository.getById(requestAnswerRequestDTO.getDataRequestId());

        DataRequestAnswer requestAnswer = new DataRequestAnswer();
        requestAnswer.setDataRequestAnswerId(requestAnswerRequestDTO.getDataRequestId());
        requestAnswer.setDataRequestClaim(requestAnswerRequestDTO.getProviderClaim());

        ArrayList<DataRequestData> drdList = new ArrayList<>(dataRequestDataRepository.findDataRequestDataByDataRequestId(requestAnswerRequestDTO.getDataRequestId()));

        // Set answer boolean for each data request data
        // For rectification and erasure, only one data is involved
        if(dataRequest.getDataRequestType().equals(DataRequestType.RECTIFICATION) || dataRequest.getDataRequestType().equals(DataRequestType.ERASURE)) {
            DataRequestData drd = drdList.get(0);
            drd.setAnswerByData(requestAnswerRequestDTO.isAnswer());
            dataRequestDataRepository.save(drd);

            requestAnswer.setAnswer(requestAnswerRequestDTO.isAnswer() ? AnswerType.FULL : AnswerType.REFUSED);

            // Apply the rectification or the erasure in the provider application
            Data data = dataRestClient.getDataById(drd.getDataId());
            String dataTypeName= data.getDataType().getDataTypeName();
            String dataName = data.getDataName();
            String newValue= dataRequest.getNewValue();
            DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());

            String dsId = dataSubject.getIdRef();

            List<Map<String, String>> parameters = new ArrayList<>();

            Map<String, String> parameter = new HashMap<>();
            parameter.put("attribute", dataName);
            parameter.put("dsId", dsId);
            parameter.put("dataTypeName", dataTypeName);
            if(dataRequest.getDataRequestType().equals(DataRequestType.RECTIFICATION))
                parameter.put("newValue", newValue);

            parameters.add(parameter);
            if(!requestAnswer.getAnswer().equals(AnswerType.REFUSED)) {
                if(dataRequest.getDataRequestType().equals(DataRequestType.RECTIFICATION))
                    providerRestClient.rectification(parameters);
                else if(dataRequest.getDataRequestType().equals(DataRequestType.ERASURE))
                    providerRestClient.forgotten(parameters);
            }
        }
        else if (dataRequest.getDataRequestType().equals(DataRequestType.ACCESS)) {
            List<Integer> dataIdList = requestAnswerRequestDTO.getData().stream().map(d -> d.getDataId()).toList();
            // We look if the dataRequestData is present in the datas send in the request
            drdList.forEach(drd -> {
                if(dataIdList.contains(drd.getDataId())) {
                    drd.setAnswerByData(true);
                }
                else {
                    drd.setAnswerByData(false);
                }
            });
            if(drdList.size() == requestAnswerRequestDTO.getData().size()) {
                requestAnswer.setAnswer(AnswerType.FULL);
            }
            else if(requestAnswerRequestDTO.getData().size() > 0) {
                requestAnswer.setAnswer(AnswerType.PARTIAL);
            }
            else {
                requestAnswer.setAnswer(AnswerType.REFUSED);
            }
        }
        // Save request answer
        return requestAnswerRepository.save(requestAnswer);
    }

    @Override
    public boolean isAccepted(int dataSubjectId, int dataId) {
        System.out.println(dataId);
        Optional<Boolean> isAccepted = dataRequestDataRepository.isDataAcceptedByDataSubjectIdAndDataId(dataSubjectId, dataId);
        return isAccepted.orElse(false);
    }

    @Override
    public List<RequestListDTO> getDataRequestByFilters(List<String> listOfSelectedTypeDataRequests, List<String> listOfSelectedStatus, List<String> listOfSelectedDataSubjectCategories) {
        List<RequestListDTO> response = new ArrayList<>();
        // Filter with Type of DataRequest
        List<DataRequest> filteredTypeList;
        if(listOfSelectedTypeDataRequests.isEmpty()) {
            filteredTypeList = dataRequestRepository.findAll();
        }
        else {
            filteredTypeList = new ArrayList<>();
            filteredTypeList = dataRequestRepository.findByTypes(listOfSelectedTypeDataRequests);
        }

        // Filter with status of DataRequest
        List<DataRequest> filteredStatusList;
        if(listOfSelectedStatus.isEmpty()) {
            filteredStatusList = filteredTypeList;
        }
        else {
            filteredStatusList = new ArrayList<>();
            filteredTypeList.forEach(dataRequest -> {
                Optional<DataRequestAnswer> answer = requestAnswerRepository.findDataRequestAnswerByDataRequestAnswerId(dataRequest.getDataRequestId());
                // First case : If looking for validated or refused requests
                if(answer.isPresent()) {
                    AnswerType answerType = answer.get().getAnswer();
                    if(listOfSelectedStatus.contains(answerType.toString())) {
                        filteredStatusList.add(dataRequest);
                    } // If we just want completed answer (so no difference between full or partial answer)
                    else if(listOfSelectedStatus.contains(StatusDataRequestType.COMPLETED.toString())) {
                        if(answerType.equals(AnswerType.FULL) || answerType.equals(AnswerType.PARTIAL)) {
                            filteredStatusList.add(dataRequest);
                        }
                    }
                }
                // Second case : looking for in progess requests (so no answer yet)
                else if(listOfSelectedStatus.contains("In Progress")) {
                    filteredStatusList.add(dataRequest);
                }
            });
        }

        // Filter with DataSubjectCategory
        filteredStatusList.forEach(dataRequest -> {
            DataSubjectCategoryResponseDTO category = actorRestClient.getDataSubjectCategoryById(dataRequest.getDataSubjectId());
            RequestListDTO r = new RequestListDTO();
            r.setDataRequestId(dataRequest.getDataRequestId());
            r.setResponse(dataRequest.isResponse());
            r.setDataRequestType(dataRequest.getDataRequestType());
            r.setDataRequestIssuedAt(dataRequest.getDataRequestIssuedAt());
            r.setDataSubjectCategory(category);
            
            if(listOfSelectedDataSubjectCategories.isEmpty()) {
                response.add(r);
            }
            else if(listOfSelectedDataSubjectCategories.contains(category.getDataSubjectCategoryName())) {
                response.add(r);
            }
        });

        return response;
    }

    @Override
    public RequestDetailDTO getRequestDataDetail(int dataRequestId) {
        RequestDetailDTO response = new RequestDetailDTO();

        // DataRequest information
        DataRequest dataRequest = dataRequestRepository.getById(dataRequestId);
        response.setDataRequestId(dataRequest.getDataRequestId());
        response.setDataRequestClaim(dataRequest.getDataRequestClaim());
        response.setResponse(dataRequest.isResponse());
        response.setIsolated(dataRequest.isIsolated());
        response.setDataRequestIssuedAt(dataRequest.getDataRequestIssuedAt());
        response.setNewValue(dataRequest.getNewValue());
        response.setDataRequestType(dataRequest.getDataRequestType());

        // DataSubject information
        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
        DataSubjectCategoryResponseDTO category = actorRestClient.getDataSubjectCategoryById(dataRequest.getDataSubjectId());
        response.setDataSubject(dataSubject.getDataSubjectId(), dataSubject.getIdRef(), category.getDataSubjectCategoryName());

        // Data information
        List<DataRequestData> dataRequestDatas = dataRequestDataRepository.findDataRequestDataByDataRequestId(dataRequestId);
        dataRequestDatas.forEach(drd -> {
            Data data = dataRestClient.getDataById(drd.getDataId());
            String dataTypeName = dataRestClient.getDataTypeNameByDataTypeId(data.getDataType().getDataTypeId());
            // Primary keys
            Map<String, String> primaryKeys = new HashMap<>();
            if(dataRequest.getDataRequestType() == DataRequestType.RECTIFICATION || dataRequest.getDataRequestType() == DataRequestType.ERASURE) {
                ArrayList<DataRequestPrimaryKey> list = new ArrayList<>(this.dataRequestPrimaryKeyRepository.findByDataRequestId(dataRequestId));
                list.forEach(primaryKey -> {
                    Data d = dataRestClient.getDataById(primaryKey.getPrimaryKeyId());
                    primaryKeys.put(d.getDataName(), primaryKey.getPrimaryKeyValue());
                });
            }

            response.addData(dataTypeName, data.getDataId(), data.getDataName(), drd.isAnswerByData(), primaryKeys);
        });

        return response;
    }

    @Override
    public DataRequestAnswer getRequestAnswerByDataRequestId(int requestId) {
        Optional<DataRequestAnswer> res = requestAnswerRepository.findDataRequestAnswerByDataRequestAnswerId(requestId);
        return res.orElse(null);
    }
}

