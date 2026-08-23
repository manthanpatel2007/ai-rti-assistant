package com.hacthon.ai_rti_assistant.config;

import com.hacthon.ai_rti_assistant.entity.*;
import com.hacthon.ai_rti_assistant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void run(String... args) {



        District ahmedabad = districtRepository
                .findAll()
                .stream()
                .filter(d -> d.getName().equals("Ahmedabad"))
                .findFirst()
                .orElseGet(() ->
                        districtRepository.save(
                                District.builder()
                                        .name("Ahmedabad")
                                        .state("Gujarat")
                                        .build()
                        )
                );



        SubDistrict sanand = subDistrictRepository
                .findAll()
                .stream()
                .filter(sd ->
                        sd.getName().equals("Sanand")
                                && sd.getDistrict().getId()
                                .equals(ahmedabad.getId())
                )
                .findFirst()
                .orElseGet(() ->
                        subDistrictRepository.save(
                                SubDistrict.builder()
                                        .name("Sanand")
                                        .district(ahmedabad)
                                        .build()
                        )
                );

        SubDistrict daskroi = subDistrictRepository
                .findAll()
                .stream()
                .filter(sd ->
                        sd.getName().equals("Daskroi")
                                && sd.getDistrict().getId()
                                .equals(ahmedabad.getId())
                )
                .findFirst()
                .orElseGet(() ->
                        subDistrictRepository.save(
                                SubDistrict.builder()
                                        .name("Daskroi")
                                        .district(ahmedabad)
                                        .build()
                        )
                );

        SubDistrict dholka = subDistrictRepository
                .findAll()
                .stream()
                .filter(sd ->
                        sd.getName().equals("Dholka")
                                && sd.getDistrict().getId()
                                .equals(ahmedabad.getId())
                )
                .findFirst()
                .orElseGet(() ->
                        subDistrictRepository.save(
                                SubDistrict.builder()
                                        .name("Dholka")
                                        .district(ahmedabad)
                                        .build()
                        )
                );



        Department roads = departmentRepository
                .findAll()
                .stream()
                .filter(d ->
                        d.getName().equals("Roads")
                                && d.getSubDistrict().getId()
                                .equals(sanand.getId())
                )
                .findFirst()
                .orElseGet(() ->
                        departmentRepository.save(
                                Department.builder()
                                        .name("Roads")
                                        .subDistrict(sanand)
                                        .build()
                        )
                );

        Department water = departmentRepository
                .findAll()
                .stream()
                .filter(d ->
                        d.getName().equals("Water Supply")
                                && d.getSubDistrict().getId()
                                .equals(sanand.getId())
                )
                .findFirst()
                .orElseGet(() ->
                        departmentRepository.save(
                                Department.builder()
                                        .name("Water Supply")
                                        .subDistrict(sanand)
                                        .build()
                        )
                );

        Department waste = departmentRepository
                .findAll()
                .stream()
                .filter(d ->
                        d.getName().equals("Waste Management")
                                && d.getSubDistrict().getId()
                                .equals(sanand.getId())
                )
                .findFirst()
                .orElseGet(() ->
                        departmentRepository.save(
                                Department.builder()
                                        .name("Waste Management")
                                        .subDistrict(sanand)
                                        .build()
                        )
                );


        if (problemCategoryRepository
                .findByDepartmentId(roads.getId())
                .isEmpty()) {

            problemCategoryRepository.save(
                    ProblemCategory.builder()
                            .name("Road Repair")
                            .keywords(
                                    "road,pothole,road damage,road repair"
                            )
                            .department(roads)
                            .build()
            );
        }

        if (problemCategoryRepository
                .findByDepartmentId(water.getId())
                .isEmpty()) {

            problemCategoryRepository.save(
                    ProblemCategory.builder()
                            .name("Water Supply Issue")
                            .keywords(
                                    "water,no water,pipeline,water supply"
                            )
                            .department(water)
                            .build()
            );
        }

        if (problemCategoryRepository
                .findByDepartmentId(waste.getId())
                .isEmpty()) {

            problemCategoryRepository.save(
                    ProblemCategory.builder()
                            .name("Garbage Collection")
                            .keywords(
                                    "garbage,waste,dustbin,trash"
                            )
                            .department(waste)
                            .build()
            );
        }



        if (pioRepository
                .findByDepartmentId(roads.getId())
                .isEmpty()) {

            pioRepository.save(
                    PIO.builder()
                            .name(
                                    "Chief Officer, Sanand Nagarpalika"
                            )
                            .designation(
                                    "Public Information Officer (Chief Officer)"
                            )
                            .email(
                                    "np_sanand@yahoo.co.in"
                            )
                            .phone(
                                    "02717222112"
                            )
                            .officeAddress(
                                    "Sanand Nagarpalika Office, " +
                                            "Taluka - Sanand, " +
                                            "Ahmedabad - 382110"
                            )
                            .sourceUrl(
                                    "https://ahmedabad.nic.in/public-utility/sanad-nagarpalika/"
                            )
                            .verifiedAt(
                                    LocalDateTime.of(
                                            2026,
                                            8,
                                            14,
                                            0,
                                            0
                                    )
                            )
                            .department(roads)
                            .build()
            );
        }



        if (pioRepository
                .findByDepartmentId(water.getId())
                .isEmpty()) {

            pioRepository.save(
                    PIO.builder()
                            .name(
                                    "Chief Officer, Sanand Nagarpalika"
                            )
                            .designation(
                                    "Public Information Officer (Chief Officer)"
                            )
                            .email(
                                    "np_sanand@yahoo.co.in"
                            )
                            .phone(
                                    "02717222112"
                            )
                            .officeAddress(
                                    "Sanand Nagarpalika Office, " +
                                            "Taluka - Sanand, " +
                                            "Ahmedabad - 382110"
                            )
                            .sourceUrl(
                                    "https://ahmedabad.nic.in/public-utility/sanad-nagarpalika/"
                            )
                            .verifiedAt(
                                    LocalDateTime.of(
                                            2026,
                                            8,
                                            14,
                                            0,
                                            0
                                    )
                            )
                            .department(water)
                            .build()
            );
        }



        if (pioRepository
                .findByDepartmentId(waste.getId())
                .isEmpty()) {

            pioRepository.save(
                    PIO.builder()
                            .name(
                                    "Chief Officer, Sanand Nagarpalika"
                            )
                            .designation(
                                    "Public Information Officer (Chief Officer)"
                            )
                            .email(
                                    "np_sanand@yahoo.co.in"
                            )
                            .phone(
                                    "02717222112"
                            )
                            .officeAddress(
                                    "Sanand Nagarpalika Office, " +
                                            "Taluka - Sanand, " +
                                            "Ahmedabad - 382110"
                            )
                            .sourceUrl(
                                    "https://ahmedabad.nic.in/public-utility/sanad-nagarpalika/"
                            )
                            .verifiedAt(
                                    LocalDateTime.of(
                                            2026,
                                            8,
                                            14,
                                            0,
                                            0
                                    )
                            )
                            .department(waste)
                            .build()
            );
        }


    }
}