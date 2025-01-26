package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.StaffRequestDto;
import com.eduAcademy.management_system.dto.StaffResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.enums.Role;
import com.eduAcademy.management_system.entity.Staff;
import com.eduAcademy.management_system.mapper.StaffMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.StaffRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffServiceInterface{

    private final StaffRepository staffRepository;
    private final GenerateActivationToken activationToken;
    private final StaffMapper staffMapper;
    private final EmailService emailService;
    private static final String PREFIX = "staf";
    private static final String CONTEXT_ID = "1";
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private final ClubRepository clubRepository;


    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void addStaff(StaffRequestDto staffRequestDto, String clubRef) throws MessagingException {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new IllegalArgumentException("clubRef not found"));

        if (staffRepository.findByEmailAndClub(staffRequestDto.getEmail(),club).isPresent()) {
            throw new IllegalArgumentException(staffRequestDto.getEmail()+" email already exist");
        }
        Staff staff=staffMapper.StaffDtoToStaff(staffRequestDto);

        List<String> roles = staffRequestDto.getRoles().stream()
                .map(roleName -> {
                    try {
                        Role roleEnum = Role.valueOf(roleName);
                        return roleEnum.name();
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid role: " + roleName);
                    }
                })
                .collect(Collectors.toList());


        staff.setFirstName(staffRequestDto.getFirstName());
        staff.setLastName(staffRequestDto.getLastName());
        staff.setEmail(staffRequestDto.getEmail());
        staff.setPhone(staffRequestDto.getPhone());
        staff.setObjectId(generateUniqueObjectId());
        staff.setRoles(roles);
        staff.setClub(club);

        staffRepository.save(staff);

        String token=activationToken.generateActivationToken(staff.getEmail(),staff.getId());
        String link="http://localhost:4200/activate-account/staff?token="+token;
        emailService.sendEmail(staffRequestDto.getEmail(),link);
    }

    @Override
    public StaffResponseDto updateStaff(StaffRequestDto staffRequestDto,String email,String clubRef) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new IllegalArgumentException("clubRef not found"));

        Staff staff = staffRepository.findByEmailAndClub(email,club)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        staff.setFirstName(staffRequestDto.getFirstName());
        staff.setLastName(staffRequestDto.getLastName());
        staff.setEmail(staffRequestDto.getEmail());
        staff.setPhone(staffRequestDto.getPhone());
        staff.setRoles(staffRequestDto.getRoles());
        staff.setClub(club);
        staffRepository.save(staff);

        StaffResponseDto responseDto = new StaffResponseDto();
        responseDto.setEmail(staff.getEmail());
        responseDto.setFirstName(staff.getFirstName());
        responseDto.setLastName(staff.getLastName());
        responseDto.setPhone(staff.getPhone());
        responseDto.setRoles(staff.getRoles());
        responseDto.setObjectId(staff.getObjectId());
        responseDto.setClubRef(staff.getClub().getReference());

        return responseDto;
    }

    @Override
    @Transactional
    public void deleteStaff(String objectId) {
        Staff staff = staffRepository.findByObjectId(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        if (staff.getClub() != null) {
            staff.setClub(null);
        }
        staffRepository.deleteByObjectId(objectId);
    }
   public String generateUniqueObjectId() {
       String objectId;
       do {
           objectId = generateObjectId();
           System.out.println("#ATYQ objectId"+objectId);
       } while (staffRepository.existsByObjectId(objectId));
       return objectId;
   }

    private String generateObjectId(){
        int increment = COUNTER.incrementAndGet();
        return String.format("%s-%s-%d", PREFIX, CONTEXT_ID, increment);
    }

    public StaffResponseDto getStaffByEmail(String email,String clubRef) {

        Club club=clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new IllegalArgumentException("Club with reference" + clubRef +" not found"));

        Optional<Staff> staff = staffRepository.findByEmailAndClub(email,club);

        if (staff.isPresent()) {
            StaffResponseDto responseDto=new StaffResponseDto();
            responseDto.setObjectId(staff.get().getObjectId());
            responseDto.setClubRef(club.getReference());
            responseDto.setEmail(staff.get().getEmail());
            responseDto.setFirstName(staff.get().getFirstName());
            responseDto.setLastName(staff.get().getLastName());
            responseDto.setRoles(staff.get().getRoles());
            responseDto.setPhone(staff.get().getPhone());
            return responseDto;
        } else {
            throw new IllegalArgumentException("Staff not found with email: " + email);
        }
    }
}
