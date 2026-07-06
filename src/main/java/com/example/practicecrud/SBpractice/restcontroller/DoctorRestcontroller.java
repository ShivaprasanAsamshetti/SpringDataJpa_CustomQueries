package com.example.practicecrud.SBpractice.restcontroller;

import com.example.practicecrud.SBpractice.apiresponse.Apiresponse;
import com.example.practicecrud.SBpractice.dto.DoctorDto;
import com.example.practicecrud.SBpractice.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class DoctorRestcontroller {

    @Autowired
    DoctorService doctorService;


    @PostMapping()
    public ResponseEntity<Apiresponse<DoctorDto>> insertDoctor(@RequestBody DoctorDto doctorDto){

        DoctorDto doctorDto1=doctorService.insert(doctorDto);

        Apiresponse<DoctorDto> apiresponse=new Apiresponse<>();
        log.info("doctor inserted successfully");
        apiresponse.setMessage("inserted successfully");
        apiresponse.setData(doctorDto1);
        apiresponse.setStatus(201);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiresponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Apiresponse<DoctorDto>> updateDoctor(@RequestBody DoctorDto doctorDto , @PathVariable Integer id){
        DoctorDto doctorDto1=doctorService.updateDoctor(doctorDto,id);
        log.info("Doctor successfully updated and response came to controller");
        Apiresponse<DoctorDto> apiresponse=new Apiresponse<>();
        apiresponse.setStatus(200);
        apiresponse.setData(doctorDto1);
        apiresponse.setMessage("updated successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiresponse);
    }


    @GetMapping("/{doctorName}")
    public ResponseEntity<Apiresponse<DoctorDto>> getDoctorByName(@PathVariable String doctorName){
        DoctorDto doctorDto=doctorService.findByName(doctorName);
        Apiresponse<DoctorDto> doctorDtoApiresponse=new Apiresponse<>();
        doctorDtoApiresponse.setMessage("we  fetched doctor successfully");
        doctorDtoApiresponse.setData(doctorDto);
        doctorDtoApiresponse.setStatus(200);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(doctorDtoApiresponse);
    }

    @GetMapping("/getbynamestartwithM")
    public ResponseEntity<Apiresponse<List<DoctorDto>>> getDoctorByNameStartsWirhM() {
        List<DoctorDto> doctorDtos=doctorService.findByNameStartswithM();
        Apiresponse<List<DoctorDto>> doctorDtoApiresponse=new Apiresponse<>();
        doctorDtoApiresponse.setMessage("we  fetched doctors with nane starting M successfully");
        doctorDtoApiresponse.setData(doctorDtos);
        doctorDtoApiresponse.setStatus(200);

        return ResponseEntity
                .status(200)
                .body(doctorDtoApiresponse);

    }
    @GetMapping()
    public ResponseEntity<Apiresponse<List<DoctorDto>>> getDoctorByNameAndIds() {
        List<DoctorDto> doctorDtos=doctorService.findByNameStartswithSAndGivenIds();
        Apiresponse<List<DoctorDto>> doctorDtoApiresponse=new Apiresponse<>();
        doctorDtoApiresponse.setMessage("we  fetched doctors with nane starting S and given ids successfully");
        doctorDtoApiresponse.setData(doctorDtos);
        doctorDtoApiresponse.setStatus(200);

        return ResponseEntity
                .status(200)
                .body(doctorDtoApiresponse);

    }


}
