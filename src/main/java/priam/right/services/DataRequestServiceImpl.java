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

    public List<Map<String, String>> DataAccess(int idDS, String dataTypeName, List<String> attributes){
        System.out.println(providerRestClient.getPersonalDataValues(idDS, dataTypeName, attributes));
        return providerRestClient.getPersonalDataValues(idDS, dataTypeName, attributes);
    }
    @Override
    public DataRequestResponseDTO saveDataRequest(DataRequestRequestDTO dataRequestDTO, DataRequestType dataRequestType) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setDataSubjectId(dataRequestDTO.getDataSubjectId());
        dataRequest.setNewValue(dataRequestDTO.getNewValue());
        dataRequest.setClaim(dataRequestDTO.getClaim());

        dataRequest.setRequestType(dataRequestType);
        dataRequest.setIssuedAt(new Date());
        dataRequest.setResponse(false);
        dataRequest.setIsolated(true);

        HashMap<Integer, String> primaryKeys = dataRequestDTO.getPrimaryKeys();

        Data data = dataRestClient.getData(dataRequestDTO.getDataId());

        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequestDTO.getDataSubjectId());
        dataRequest.setDataSubject(dataSubject);

        DataRequest result = dataRequestRepository.save(dataRequest);

        // DataRequestData
        DataRequestData drd = new DataRequestData(result.getRequestId(), data.getDataId(), false);
        dataRequestDataRepository.save(drd);

        ArrayList<Data> datas = new ArrayList<>();
        datas.add(data);

        // PrimaryKeys
        primaryKeys.forEach((id, value)-> {
            DataRequestPrimaryKey dataRequestPrimaryKey = new DataRequestPrimaryKey(result.getRequestId(), id, value);
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
        dataRequest.setIssuedAt(new Date());
        dataRequest.setClaim(accessRequestRequestDTO.getDataRequestClaim());
        dataRequest.setRequestType(DataRequestType.Access);

        dataRequest.setResponse(false);
        dataRequest.setIsolated(true);
        dataRequest.setNewValue(null);

        DataRequest result = dataRequestRepository.save(dataRequest);

        ArrayList<Data> datas = new ArrayList<>();


        // Save all DataRequestData
        accessRequestRequestDTO.getData().forEach(dataId -> {
            DataRequestData drd = new DataRequestData(result.getRequestId(), dataId.getDataId(), false);
            dataRequestDataRepository.save(drd);
            Data data = dataRestClient.getData(dataId.getDataId());
            datas.add(data);
        });

        // Response DTO
        DataRequestResponseDTO response = new DataRequestResponseDTO(result, datas, null);
        return response;
    }

    @Override
    public DataRequestResponseDTO getDataRequest(int id) {
        DataRequest dataRequest = dataRequestRepository.getById(id);
        ArrayList<Data> datas = new ArrayList<>();

        List<Integer> dataIds = new ArrayList<>();
        dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(id);
        dataIds.forEach(dataId -> {
            Data data = dataRestClient.getData(dataId);
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
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getRequestId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
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
        List<DataRequest> dataRequestList = dataRequestRepository.findByRequestType(DataRequestType.Rectification);

        for (DataRequest dataRequest : dataRequestList)
        {

            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getRequestId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
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
        List<DataRequest> dataRequestList = dataRequestRepository.findByRequestType(DataRequestType.Erasure);

        for (DataRequest dataRequest : dataRequestList)
        {

            // Get all Data object
            ArrayList<Data> datas = new ArrayList<>();
            List<Integer> dataIds = new ArrayList<>();
            dataIds = dataRequestDataRepository.findDataIdsByDataRequestId(dataRequest.getRequestId());
            dataIds.forEach(dataId -> {
                Data data = dataRestClient.getData(dataId);
                datas.add(data);
            });

            DataSubject dataSubject1 = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject1);
            response.add(new DataRequestResponseDTO(dataRequest, datas, null));

        }
        return response;
    }

    @Override
    public RequestAnswer getRequestAnswer(long requestId) {
        Optional<RequestAnswer> res = requestAnswerRepository.findRequestAnswerByDataRequestId(requestId);
        return res.orElse(null);
    }
    @Override
    public RequestAnswer saveRequestAnswer(RequestAnswerRequestDTO requestAnswerRequestDTO) {
        DataRequest dataRequest = dataRequestRepository.getById(requestAnswerRequestDTO.getRequestId());

        RequestAnswer requestAnswer = new RequestAnswer();
        requestAnswer.setDataRequestId((long) requestAnswerRequestDTO.getRequestId());
        requestAnswer.setAnswerClaim(requestAnswerRequestDTO.getProviderClaim());
        requestAnswer.setClaimDate(new Date());

        ArrayList<DataRequestData> drdList = new ArrayList<>(dataRequestDataRepository.findDataRequestDataByDataRequestId(requestAnswerRequestDTO.getRequestId()));

        // Set answer boolean for each data request data
        // For rectification and erasure, only one data is involved
        if(dataRequest.getRequestType().equals(DataRequestType.Rectification) || dataRequest.getRequestType().equals(DataRequestType.Erasure)) {
            DataRequestData drd = drdList.get(0);
            drd.setAnswer(requestAnswerRequestDTO.isAnswer());
            dataRequestDataRepository.save(drd);

            requestAnswer.setAnswerType(requestAnswerRequestDTO.isAnswer() ? AnswerType.FULL : AnswerType.REFUSED);

            // Apply the rectification or the erasure in the provider application
            Data data = dataRestClient.getData(drd.getDataId());

            String dataTypeName= data.getData_type_name();
            String attribute = data.getAttributeName();
            String newValue= dataRequest.getNewValue();
            DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());

            String dsId = dataSubject.getReferenceId();

            List<Map<String, String>> parameters = new ArrayList<>();

            Map<String, String> parameter = new HashMap<>();
            parameter.put("attribute", attribute);
            parameter.put("dsId", dsId);
            parameter.put("dataTypeName", dataTypeName);
            if(dataRequest.getRequestType().equals(DataRequestType.Rectification))
                parameter.put("newValue", newValue);

            parameters.add(parameter);
            if(!requestAnswer.getAnswerType().equals(AnswerType.REFUSED)) {
                if(dataRequest.getRequestType().equals(DataRequestType.Rectification))
                    providerRestClient.rectification(parameters);
                else if(dataRequest.getRequestType().equals(DataRequestType.Erasure))
                    providerRestClient.forgotten(parameters);
            }

        }
        else if (dataRequest.getRequestType().equals(DataRequestType.Access)) {
            List<Integer> dataIdList = requestAnswerRequestDTO.getData().stream().map(d -> d.getDataId()).toList();
            // We look if the dataRequestData is present in the datas send in the request
            drdList.forEach(drd -> {
                if(dataIdList.contains(drd.getDataId())) {
                    drd.setAnswer(true);
                }
                else {
                    drd.setAnswer(false);
                }
            });
            if(drdList.size() == requestAnswerRequestDTO.getData().size()) {
                requestAnswer.setAnswerType(AnswerType.FULL);
            }
            else if(requestAnswerRequestDTO.getData().size() > 0) {
                requestAnswer.setAnswerType(AnswerType.PARTIAL);
            }
            else {
                requestAnswer.setAnswerType(AnswerType.REFUSED);
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
                Optional<RequestAnswer> answer = requestAnswerRepository.findRequestAnswerByDataRequestId((long) dataRequest.getRequestId());
                // First case : If looking for validated or refused requests
                if(answer.isPresent()) {
                    AnswerType answerType = answer.get().getAnswerType();
                    if(listOfSelectedStatus.contains(answerType.toString())) {
                        filteredStatusList.add(dataRequest);
                    } // If we just want completed answer (so no difference between full or partial answer)
                    else if(listOfSelectedStatus.contains(StatusDataRequestType.Completed.toString())) {
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
            DSCategoryResponseDTO category = actorRestClient.getDSCategoryById(dataRequest.getDataSubjectId());
            RequestListDTO r = new RequestListDTO();
            r.setRequestId(dataRequest.getRequestId());
            r.setResponse(dataRequest.isResponse());
            r.setRequestType(dataRequest.getRequestType());
            r.setIssuedAt(dataRequest.getIssuedAt());
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
    public RequestDetailDTO getRequestDataDetail(int requestId) {
        RequestDetailDTO response = new RequestDetailDTO();

        // DataRequest information
        DataRequest dataRequest = dataRequestRepository.getById(requestId);
        response.setRequestId(dataRequest.getRequestId());
        response.setClaim(dataRequest.getClaim());
        response.setResponse(dataRequest.isResponse());
        response.setIsolated(dataRequest.isIsolated());
        response.setIssuedAt(dataRequest.getIssuedAt());
        response.setNewValue(dataRequest.getNewValue());
        response.setTypeRequest(dataRequest.getRequestType());

        // DataSubject information
        DataSubject dataSubject = actorRestClient.getDataSubject(dataRequest.getDataSubjectId());
        DSCategoryResponseDTO category = actorRestClient.getDSCategoryById(dataRequest.getDataSubjectId());
        response.setDataSubject(dataSubject.getDataSubjectId(), dataSubject.getReferenceId(), category.getDataSubjectCategoryName());

        // Data information
        List<DataRequestData> dataRequestDatas = dataRequestDataRepository.findDataRequestDataByDataRequestId(requestId);
        dataRequestDatas.forEach(drd -> {
            Data data = dataRestClient.getData(drd.getDataId());
            String dataTypeName = dataRestClient.getDataTypeNameByDataTypeId(data.getData_type_id());
            // Primary keys
            Map<String, String> primaryKeys = new HashMap<>();
            if(dataRequest.getRequestType() == DataRequestType.Rectification || dataRequest.getRequestType() == DataRequestType.Erasure) {
                ArrayList<DataRequestPrimaryKey> list = new ArrayList<>(this.dataRequestPrimaryKeyRepository.findByDataRequestId(requestId));
                list.forEach(primaryKey -> {
                    Data d = dataRestClient.getData(primaryKey.getPrimaryKeyId());
                    primaryKeys.put(d.getAttributeName(), primaryKey.getPrimaryKeyValue());
                });
            }

            response.addData(dataTypeName, data.getDataId(), data.getAttributeName(), drd.isAnswer(), primaryKeys);
        });

        return response;
    }

    @Override
    public RequestAnswer getRequestAnswerByDataRequestId(int requestId) {
        Optional<RequestAnswer> res = requestAnswerRepository.findRequestAnswerByDataRequestId((long) requestId);
        if(res.isPresent())
            return res.get();
        else
            return null;
    }
}

