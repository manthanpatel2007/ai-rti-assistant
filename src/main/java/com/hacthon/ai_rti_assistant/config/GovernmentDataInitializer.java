package com.hacthon.ai_rti_assistant.config;

import com.hacthon.ai_rti_assistant.entity.*;
import com.hacthon.ai_rti_assistant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GovernmentDataInitializer implements CommandLineRunner {

    private final DistrictRepository districtRepository;
    private final SubDistrictRepository subDistrictRepository;
    private final DepartmentRepository departmentRepository;
    private final ProblemCategoryRepository problemCategoryRepository;
    private final PIORepository pioRepository;

    @Override
    public void run(String... args) {

        if (districtRepository.count() > 0) {
            return;
        }

        District ahmedabad = districtRepository.save(
                District.builder()
                        .name("Ahmedabad")
                        .state("Gujarat")
                        .build()
        );


        SubDistrict sanand = subDistrictRepository.save(
                SubDistrict.builder()
                        .name("Sanand")
                        .district(ahmedabad)
                        .build()
        );

        SubDistrict daskroi = subDistrictRepository.save(
                SubDistrict.builder()
                        .name("Daskroi")
                        .district(ahmedabad)
                        .build()
        );

        SubDistrict dholka = subDistrictRepository.save(
                SubDistrict.builder()
                        .name("Dholka")
                        .district(ahmedabad)
                        .build()
        );


        Department roads = departmentRepository.save(
                Department.builder()
                        .name("Roads")
                        .subDistrict(sanand)
                        .build()
        );

        Department water = departmentRepository.save(
                Department.builder()
                        .name("Water Supply")
                        .subDistrict(sanand)
                        .build()
        );

        Department waste = departmentRepository.save(
                Department.builder()
                        .name("Waste Management")
                        .subDistrict(sanand)
                        .build()
        );


        problemCategoryRepository.save(
                ProblemCategory.builder()
                        .name("Road Repair")
                        .keywords("road,pothole,road damage,road repair")
                        .department(roads)
                        .build()
        );

        problemCategoryRepository.save(
                ProblemCategory.builder()
                        .name("Water Supply Issue")
                        .keywords("water,no water,pipeline,water supply")
                        .department(water)
                        .build()
        );

        problemCategoryRepository.save(
                ProblemCategory.builder()
                        .name("Garbage Collection")
                        .keywords("garbage,waste,dustbin,trash")
                        .department(waste)
                        .build()
        );

        // NOTE: Roads, Water Supply, and Waste Management in Sanand taluka are all
        // administered by Sanand Nagarpalika (the municipal body). The Chief Officer
        // acts as PIO for RTI requests to this office. The specific officeholder's
        // name and direct phone number are NOT published online as of verification
        // date below — confirm these by phone/visit before using in production.

        pioRepository.save(
                PIO.builder()
                        .name("Chief Officer, Sanand Nagarpalika")
                        .designation("Public Information Officer (Chief Officer)")
                        .email("np_sanand@yahoo.co.in")
                        .phone("02717222112")
                        .officeAddress("Sanand Nagarpalika Office, Taluka - Sanand, Ahmedabad - 382110")
                        .sourceUrl("https://ahmedabad.nic.in/public-utility/sanad-nagarpalika/")
                        .verifiedAt(LocalDateTime.of(2026, 8, 14, 0, 0))
                        .department(roads)
                        .build()
        );

        pioRepository.save(
                PIO.builder()
                        .name("Chief Officer, Sanand Nagarpalika")
                        .designation("Public Information Officer (Chief Officer)")
                        .email("np_sanand@yahoo.co.in")
                        .phone("02717222112")
                        .officeAddress("Sanand Nagarpalika Office, Taluka - Sanand, Ahmedabad - 382110")
                        .sourceUrl("https://ahmedabad.nic.in/public-utility/sanad-nagarpalika/")
                        .verifiedAt(LocalDateTime.of(2026, 8, 14, 0, 0))
                        .department(water)
                        .build()
        );


        pioRepository.save(
                PIO.builder()
                        .name("Chief Officer, Sanand Nagarpalika")
                        .designation("Public Information Officer (Chief Officer)")
                        .email("np_sanand@yahoo.co.in")
                        .phone("02717222112")
                        .officeAddress("Sanand Nagarpalika Office, Taluka - Sanand, Ahmedabad - 382110")
                        .sourceUrl("https://ahmedabad.nic.in/public-utility/sanad-nagarpalika/")
                        .verifiedAt(LocalDateTime.of(2026, 8, 14, 0, 0))
                        .department(waste)
                        .build()
        );
    }
}