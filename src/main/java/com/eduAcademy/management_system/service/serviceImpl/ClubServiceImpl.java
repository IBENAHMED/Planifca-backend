package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubMapper clubMapper;
    private final ClubRepository clubRepository;

    private static final String DEFAULT_LOGO = "/uploads/PlanifcaLogo.png";

    @Override
    public ClubResponseDto createClub(ClubRequestDto requestDto) throws IOException {

        if (clubRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new ConflictException("The email <'"+requestDto.getEmail()+"'> is already in use");
        }

        Club club= clubMapper.toClub(requestDto);

        club.setName(club.getName());
        club.setReference(generateUniqueReference(club.getName()));
        club.setEmail(club.getEmail());
        club.setLogo(DEFAULT_LOGO);
        club.setFrontPath(requestDto.getFrontPath());
        club.setActive(true);

        clubRepository.save(club);

        return clubMapper.toClubResponseDto(club);
    }

    @Override
    public String generateUniqueReference(String name) {
        String cleanedName = name.replaceAll("[^a-zA-Z\\s]", "").toUpperCase();

        StringBuilder initials = new StringBuilder();
        String[] words = cleanedName.split("\\s+");

        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
            if (initials.length() >= 4) break;
        }

        String reference = "PL" + initials.toString();
        int suffix = 1;
        while (clubRepository.existsByReference(reference)) {
            reference = "PL" + initials.toString() + suffix;
            suffix++;
        }

        return reference;
    }

    @Override
    public ClubResponseDto getClubByFrontPath(String frontPath) {
        Club club = clubRepository.findByFrontPath(frontPath)
                .orElseThrow(() -> new NotFoundException("club not found with path <'" + frontPath+"'>"));

        return clubMapper.toClubResponseDto(club);
    }


    @Override
    public void updateClub(ClubRequestDto requestDto) {

    }

    @Override
    public void deleteClub(ClubRequestDto requestDto) {

    }

    @Override
    public Page<ClubResponseDto> getClubs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Club> clubs = clubRepository.findAll(pageable);

        if (clubs.isEmpty()) {
            throw new NotFoundException("No club found.");
        }

        return clubs.map(clubMapper::toClubResponseDto);
    }
}
