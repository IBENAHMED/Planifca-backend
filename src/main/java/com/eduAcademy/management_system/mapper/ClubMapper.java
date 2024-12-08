package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClubMapper {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToStrings")
    ClubRequestDto ClubToClubDTO(Club club);
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapStringsToRoles")
    Club ClubDTOToClub(ClubRequestDto clubRequestDto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToStrings")
    ClubResponseDto ClubToClubDtoResponse(Club club);


    @Named("mapRolesToStrings")
    default List<String> mapRolesToStrings(Set<Role> roles) {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(Role::getName) // On mappe chaque rôle à son nom
                .collect(Collectors.toList());
    }

    // Méthode pour convertir List<String> en Set<Role>
    @Named("mapStringsToRoles")
    default Set<Role> mapStringsToRoles(List<String> roleNames) {
        if (roleNames == null) {
            return Set.of();
        }
        return roleNames.stream()
                .map(name -> Role.builder().name(name).build()) // Création d'objets Role à partir des noms
                .collect(Collectors.toSet());
    }


}
