package priam.datarequest.mappers;

import org.mapstruct.Mapper;
import priam.datarequest.dto.DataRequestRequestDTO;
import priam.datarequest.dto.DataRequestResponseDTO;
import priam.datarequest.entities.DataRequest;

@Mapper(componentModel = "spring")
public interface DataRequestMapper {
    DataRequest fromDataRequestRequestDTO(DataRequestRequestDTO dataRequestRequestDTO);

    DataRequestResponseDTO fromDataRequest(DataRequest dataRequest);
}
