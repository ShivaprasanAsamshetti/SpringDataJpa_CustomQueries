package com.example.practicecrud.SBpractice.service;

import com.example.practicecrud.SBpractice.dto.DoctorDto;
import com.example.practicecrud.SBpractice.entity.Doctor;
import com.example.practicecrud.SBpractice.repo.DoctorRepo;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;


@Service
public class DoctorService {
    @Autowired
    DoctorRepo doctorRepo;

    private static final Logger logger = LoggerFactory.getLogger("DoctorService.class");

    public DoctorDto insert(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorDto, doctor);
        Doctor doctor1 = doctorRepo.save(doctor);
        logger.info("inserted data in service layer");
        BeanUtils.copyProperties(doctor1, doctorDto);
        return doctorDto;
    }

    public DoctorDto updateDoctor(DoctorDto doctorDto, Integer id) {

        logger.info("doctor is going to update next step");
        Doctor doctor = doctorRepo.findById(id).orElseThrow();

        BeanUtils.copyProperties(doctorDto, doctor, "docterId");
        Doctor doctor1 = doctorRepo.save(doctor);
        logger.info("data is updated");
        BeanUtils.copyProperties(doctor1, doctorDto);
        return doctorDto;
    }


    public DoctorDto findByName(String name) {
        Doctor doctor = doctorRepo.findByDocterName(name).orElseThrow(() -> new RuntimeException("Doctor not found"));
        DoctorDto doctorDto = new DoctorDto();
        BeanUtils.copyProperties(doctor, doctorDto);
        return doctorDto;
    }

    public List<DoctorDto> findByNameStartswithM() {
        List<DoctorDto> list = new ArrayList<>();

        List<Doctor> doctor = doctorRepo.findByDoctorNameStartsWithM().orElseThrow(() -> new RuntimeException("Doctor not found"));
        for (Doctor doctor1 : doctor) {
            DoctorDto doctorDto = new DoctorDto();
            BeanUtils.copyProperties(doctor1, doctorDto);
            list.add(doctorDto);

        }
        return list;
    }



    public List<DoctorDto> findByNameStartswithSAndGivenIds() {
        List<DoctorDto> list = new ArrayList<>();

        List<Doctor> doctor = doctorRepo.findByDoctorNameStartsWithSAndWithGivenId().orElseThrow(() -> new RuntimeException("Doctor not found"));
        for (Doctor doctor1 : doctor) {
            DoctorDto doctorDto = new DoctorDto();
            BeanUtils.copyProperties(doctor1, doctorDto);
            list.add(doctorDto);

        }
        return list;
    }




    //patch mapping
//    public DoctorDto updateDoctorPartial(DoctorDto doctorDto,Integer id){
//
//        Doctor doctor=doctorRepo.findById(id).orElseThrow();
//        BeanUtils.copyProperties(doctorDto,doctor);
//        Doctor doctor1=doctorRepo.save(doctor);
//        BeanUtils.copyProperties(doctor1,doctorDto);
//        return doctorDto;
//    }
}
