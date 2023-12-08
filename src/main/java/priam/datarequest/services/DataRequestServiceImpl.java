package priam.datarequest.services;

import org.springframework.stereotype.Service;
import priam.datarequest.dto.DataRequestRequestDTO;
import priam.datarequest.dto.DataRequestResponseDTO;
import priam.datarequest.entities.*;
import priam.datarequest.enums.TypeDataRequest;
import priam.datarequest.mappers.DataRequestMapper;
import priam.datarequest.openfeign.DataRestClient;
import priam.datarequest.openfeign.DataSubjectRestClient;

import priam.datarequest.openfeign.ProviderViewRestClient;
import priam.datarequest.repositories.DataRequestRepository;
import priam.datarequest.repositories.RequestAnswerRepository;

import javax.annotation.Generated;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-05-23T23:03:41+0530"
)

@Service
@Transactional
public class DataRequestServiceImpl implements DataRequestService {
    private DataRequestRepository dataRequestRepository;
    private DataRequestMapper dataRequestMapper;
    private DataRestClient dataRestClient;
    private DataSubjectRestClient dataSubjectRestClient;
    private ProviderViewRestClient providerViewRestClient;
    private RequestAnswerRepository requestAnswerRepository;
    public DataRequestServiceImpl(DataRequestRepository dataRequestRepository, DataRequestMapper dataRequestMapper,
                                  ProviderViewRestClient providerViewRestClient, DataRestClient dataRestClient, DataSubjectRestClient dataSubjectRestClient, RequestAnswerRepository requestAnswerRepository) {
        this.dataRequestRepository = dataRequestRepository;
        this.dataRequestMapper = dataRequestMapper;
        this.dataRestClient = dataRestClient;
        this.dataSubjectRestClient = dataSubjectRestClient;
        this.providerViewRestClient = providerViewRestClient;
        this.requestAnswerRepository = requestAnswerRepository;
    }

    public List<Map<String, String>> DataAccess(int idDS, String dataTypeName, List<String> attributes){
        System.out.println(providerViewRestClient.getPersonalDataValues(idDS, dataTypeName, attributes));
        return providerViewRestClient.getPersonalDataValues(idDS, dataTypeName, attributes);
    }
    @Override
    public DataRequestResponseDTO saveRectificationRequest(String idRef,String attribute, String newValue,  String claim, String primaryKeyaValue) {

        DataRequest dataRequest = new DataRequest();
        if ((selectLastIdRequest() == null)) { dataRequest.setId(1); }
        else { dataRequest.setId(Integer.parseInt(selectLastIdRequest())+1); }

        dataRequest.setNewValue(newValue);

        int idd = dataRestClient.getIdByName(attribute);
        Data data = dataRestClient.getData(idd);

        dataRequest.setData(data);

        DataSubject dataSubject2 = dataSubjectRestClient.getDataSubjectByRef(idRef);
        dataRequest.setDataSubject(dataSubject2);

        dataRequest.setClaimDate(new Date());
        dataRequest.setClaim(claim);
        dataRequest.setType(TypeDataRequest.Rectification);

        dataRequest.setResponse(false);

        dataRequest.setIsolated(true);
        dataRequest.setDataId(data.getId());
        dataRequest.setDataSubjectId(dataSubject2.getId());

        dataRequest.setPrimaryKeyValue(primaryKeyaValue);

        dataRequestRepository.save(dataRequest);
        return dataRequestMapper.fromDataRequest(dataRequest);
    }

    @Override
    public DataRequestResponseDTO saveErasureRequest(String idRef,String attribute, String claim, String primaryKeyaValue) {

        DataRequest dataRequest = new DataRequest();
        if ((selectLastIdRequest() == null)) { dataRequest.setId(1); }
        else { dataRequest.setId(Integer.parseInt(selectLastIdRequest())+1); }

        int idd = dataRestClient.getIdByName(attribute);
        Data data = dataRestClient.getData(idd);

        dataRequest.setData(data);

        DataSubject dataSubject2 = dataSubjectRestClient.getDataSubjectByRef(idRef);
        dataRequest.setDataSubject(dataSubject2);

        dataRequest.setClaimDate(new Date());
        dataRequest.setClaim(claim);
        dataRequest.setType(TypeDataRequest.Forgotten);

        dataRequest.setResponse(false);

        dataRequest.setIsolated(true);
        dataRequest.setDataId(data.getId());
        dataRequest.setDataSubjectId(dataSubject2.getId());

        dataRequest.setPrimaryKeyValue(primaryKeyaValue);

        dataRequestRepository.save(dataRequest);
        return dataRequestMapper.fromDataRequest(dataRequest);
    }

    @Override
    public String selectLastIdRequest() {
        return dataRequestRepository.selectLastIdRequest();
    }

    @Override
    public String selectLastIdAnswer() {
        return requestAnswerRepository.selectLastId();
    }

    @Override
    public DataRequestResponseDTO getDataRequest(int id) {
        DataRequest dataRequest = dataRequestRepository.getById(id);
        Data data = dataRestClient.getData(dataRequest.getDataId());
        DataSubject dataSubject = dataSubjectRestClient.getDataSubject(dataRequest.getDataSubjectId());
        dataRequest.setData(data);
        dataRequest.setDataSubject(dataSubject);
        return dataRequestMapper.fromDataRequest(dataRequest);
    }

    @Override
    public List<DataRequestResponseDTO> getListDataRequest(int dataSubjectId) {

        List<DataRequest> dataRequestList = dataRequestRepository.findByDataSubjectId(dataSubjectId);
            for (DataRequest dataRequest : dataRequestList)
            {
                Data data1 = dataRestClient.getData(dataRequest.getDataId());
                dataRequest.setData(data1);

                DataSubject dataSubject1 = dataSubjectRestClient.getDataSubject(dataRequest.getDataSubjectId());
                dataRequest.setDataSubject(dataSubject1);

            }

        return dataRequestList
                .stream().map(dataRequest -> dataRequestMapper.fromDataRequest(dataRequest)).
                collect(Collectors.toList());
    }

    @Override
    public List<DataRequestResponseDTO> getListRectificationRequests() {
        List<DataRequest> dataRequestList = dataRequestRepository.findByType(TypeDataRequest.Rectification);

        for (DataRequest dataRequest : dataRequestList
        ) {
            Data data = dataRestClient.getData(dataRequest.getDataId());
            dataRequest.setData(data);

            DataSubject dataSubject = dataSubjectRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject);

        }
        return dataRequestList
                .stream().map(dataRequest -> dataRequestMapper.fromDataRequest(dataRequest)).
                collect(Collectors.toList());
    }

    @Override
    public List<DataRequestResponseDTO> getListErasureRequests() {
        List<DataRequest> dataRequestList = dataRequestRepository.findByType(TypeDataRequest.Forgotten);

        for (DataRequest dataRequest : dataRequestList
        ) {
            Data data = dataRestClient.getData(dataRequest.getDataId());
            dataRequest.setData(data);

            DataSubject dataSubject = dataSubjectRestClient.getDataSubject(dataRequest.getDataSubjectId());
            dataRequest.setDataSubject(dataSubject);

        }
        return dataRequestList
                .stream().map(dataRequest -> dataRequestMapper.fromDataRequest(dataRequest)).
                collect(Collectors.toList());
    }

    @Override
    public DataRequestResponseDTO RectificationAnswer(int idDataRequest, boolean answer, String claimAnswer){
        //Code redondant, de la fonction getDataRequest (pas même type de retour
        DataRequest dataRequest = dataRequestRepository.getById(idDataRequest);
        Data data = dataRestClient.getData(dataRequest.getDataId());
        DataSubject dataSubject = dataSubjectRestClient.getDataSubject(dataRequest.getDataSubjectId());
        dataRequest.setData(data);
        dataRequest.setDataSubject(dataSubject);

        dataRequest.setResponse(true);

        RequestAnswer rectificationAnswer = new RequestAnswer();
        if(answer == true){

            if ((selectLastIdAnswer() == null)) { rectificationAnswer.setIdAnswer(1); }
            else { rectificationAnswer.setIdAnswer(Integer.parseInt(selectLastIdAnswer())+1); }
            rectificationAnswer.setAnswer(true);
            rectificationAnswer.setClaimAnswer(claimAnswer);
            rectificationAnswer.setDataRequest(dataRequest);
            rectificationAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(rectificationAnswer);

            String dataTypeName= data.getData_type_name();
            String primaryKeyName= data.getPrimary_key_name();
            String primaryKeyValue= dataRequest.getPrimaryKeyValue();
            String attribute = data.getAttribute();
            String newValue= dataRequest.getNewValue();

            int dsId = Integer.parseInt(dataRequest.getDataSubject().getIdRef());

            List<Map<String, String>> parameters = new ArrayList<>();

            Map<String, String> parameter = new HashMap<>();
            parameter.put("attribute", attribute);
            parameter.put("newValue", newValue );
            parameter.put("dsId", String.valueOf(dsId));
            parameter.put("dataTypeName", dataTypeName);
            parameter.put("primaryKeyName", primaryKeyName);
            parameter.put("primaryKeyValue", primaryKeyValue);

            parameters.add(parameter);

            providerViewRestClient.rectification(parameters);

        }else{
            if ((selectLastIdAnswer() == null)) { rectificationAnswer.setIdAnswer(1); }
            else { rectificationAnswer.setIdAnswer(Integer.parseInt(selectLastIdAnswer())+1); }
            rectificationAnswer.setAnswer(false);
            rectificationAnswer.setClaimAnswer(claimAnswer);
            rectificationAnswer.setDataRequest(dataRequest);
            rectificationAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(rectificationAnswer);
        }
        dataRequest.setIsolated(false);
        return dataRequestMapper.fromDataRequest(dataRequest);
    }

    @Override
    public DataRequestResponseDTO ErasureAnswer(int idDataRequest, boolean answer, String claimAnswer){
        DataRequest dataRequest = dataRequestRepository.getById(idDataRequest);
        Data data = dataRestClient.getData(dataRequest.getDataId());
        DataSubject dataSubject = dataSubjectRestClient.getDataSubject(dataRequest.getDataSubjectId());
        dataRequest.setData(data);
        dataRequest.setDataSubject(dataSubject);

        dataRequest.setResponse(true);

        RequestAnswer erasureAnswer = new RequestAnswer();
        if(answer == true){

            if ((selectLastIdAnswer() == null)) { erasureAnswer.setIdAnswer(1); }
            else { erasureAnswer.setIdAnswer(Integer.parseInt(selectLastIdAnswer())+1); }
            erasureAnswer.setAnswer(true);
            erasureAnswer.setClaimAnswer(claimAnswer);
            erasureAnswer.setDataRequest(dataRequest);
            erasureAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(erasureAnswer);

            String dataTypeName= data.getData_type_name();
            String primaryKeyName= data.getPrimary_key_name();
            String primaryKeyValue= dataRequest.getPrimaryKeyValue();
            String attribute = data.getAttribute();

            int dsId = Integer.parseInt(dataRequest.getDataSubject().getIdRef())/*getId()*/;

            List<Map<String, String>> parameters = new ArrayList<>();

            Map<String, String> parameter = new HashMap<>();
            parameter.put("attribute", attribute);
            parameter.put("dsId", String.valueOf(dsId));
            parameter.put("dataTypeName", dataTypeName);
            parameter.put("primaryKeyName", primaryKeyName);
            parameter.put("primaryKeyValue", primaryKeyValue);

            parameters.add(parameter);
            System.out.println(attribute+ "       "+ dsId+ "       "+ dataTypeName+"  "+"  "+primaryKeyName);
            providerViewRestClient.forgotten(parameters);

        }else{
            if ((selectLastIdAnswer() == null)) { erasureAnswer.setIdAnswer(1); }
            else { erasureAnswer.setIdAnswer(Integer.parseInt(selectLastIdAnswer())+1); }
            erasureAnswer.setAnswer(false);
            erasureAnswer.setClaimAnswer(claimAnswer);
            erasureAnswer.setDataRequest(dataRequest);
            erasureAnswer.setClaimDate(new Date());
            requestAnswerRepository.save(erasureAnswer);
        }
        dataRequest.setIsolated(false);
        return dataRequestMapper.fromDataRequest(dataRequest);
    }


}

