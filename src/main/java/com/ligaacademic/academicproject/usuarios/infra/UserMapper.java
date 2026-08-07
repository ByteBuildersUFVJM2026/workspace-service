package com.ligaacademic.academicproject.usuarios.infra;

import com.ligaacademic.academicproject.usuarios.api.UserProfileResponseDTO;
import com.ligaacademic.academicproject.usuarios.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfileResponseDTO paraResponseDTO(User user);
}
