package com.example.practicecrud.SBpractice.repo;

import com.example.practicecrud.SBpractice.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
   
   //derived query method
   Optional<Doctor>  findByDocterName(String docterName);

   Doctor findByDoctorType(String doctorType);

//below jpql
   @Query("select d from Doctor d where d.docterName LIKE 'M%' or d.docterName LIKE 'm%' ")
  Optional<List<Doctor>> findByDoctorNameStartsWithM();


//below is sql
    @Query(
            value="select * from doctor where docter_name LIKE 's%' AND docter_id in (2,3,4,5) ",
    nativeQuery=true  //we give here native query true when we want to send sql query saying spring
    )
   Optional<List<Doctor>> findByDoctorNameStartsWithSAndWithGivenId();

}
