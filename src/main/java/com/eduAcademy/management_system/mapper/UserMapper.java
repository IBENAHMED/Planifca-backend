package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.UserDto;
import com.eduAcademy.management_system.dto.UserResponse;
import com.eduAcademy.management_system.entity.Roles;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.repository.RolesRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Autowired
    protected RolesRepository roleRepository;  // Injection du repository

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRolesToStrings")
    public abstract UserDto userToUserDto(User user);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRolesToStrings")
    public abstract UserResponse userToUserResponse(User user);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapStringsToRoles")
    public abstract User userDtoToUser(UserDto userDto);

    @Named("mapRolesToStrings")
    protected static Set<String> mapRolesToStrings(Set<Roles> roles) {
        return roles.stream()
                .map(Roles::getName)
                .collect(Collectors.toSet());
    }

    @Named("mapStringsToRoles")
    protected Set<Roles> mapStringsToRoles(Set<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName) // On récupère l'objet depuis la base
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
    }
}
