Notes on Sorting i worked


Spring Data JPA - Sorting
Definition

Sorting is the process of arranging records in ascending or descending order based on one or more entity fields.

Example:

Without Sorting

Id	Doctor Name
3	Mallika
1	Shiva
2	Manoj

After Sorting (Ascending)

Id	Doctor Name
1	Shiva
2	Manoj
3	Mallika
Which Repository Supports Sorting?

JpaRepository

Reason:

Repository
      ↑
CrudRepository
      ↑
PagingAndSortingRepository
      ↑
JpaRepository


JpaRepository inherits sorting support from PagingAndSortingRepository.

Repository

No additional method is required.

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Integer> {

}

Reason:

JpaRepository already contains

findAll(Sort sort);
Sort Class

Sorting is performed using the Sort class.

Import

import org.springframework.data.domain.Sort;
Ascending Order
List<Doctor> doctors =
doctorRepo.findAll(
        Sort.by("doctorName")
);

SQL Generated

SELECT *
FROM doctor
ORDER BY doctor_name ASC;

Note: Sort.by() uses Ascending (ASC) by default.

Descending Order
List<Doctor> doctors =
doctorRepo.findAll(
        Sort.by("doctorId")
            .descending()
);

SQL

SELECT *
FROM doctor
ORDER BY doctor_id DESC;
Sort Object

Instead of writing directly,

doctorRepo.findAll(
        Sort.by("doctorName")
);

you can write

Sort sort = Sort.by("doctorName");

List<Doctor> doctors =
doctorRepo.findAll(sort);

Both are the same.

Multiple Field Sorting

Example

Sort sort =
Sort.by("doctorType")
    .ascending()
    .and(
        Sort.by("doctorName")
            .ascending()
    );

List<Doctor> doctors =
doctorRepo.findAll(sort);

SQL

ORDER BY doctor_type ASC,
         doctor_name ASC;
Different Order
Sort sort =
Sort.by("doctorType")
    .ascending()
    .and(
        Sort.by("doctorId")
            .descending()
    );

SQL

ORDER BY doctor_type ASC,
         doctor_id DESC;
Important Interview Concept

Consider the data:

doctorId	doctorName
5	Shiva
4	Mallika
3	Mallika
2	Manoj
1	Minnuama

Sorting:

Sort.by("doctorId")
    .descending()
    .and(
        Sort.by("doctorName")
    );

Output

5 Shiva
4 Mallika
3 Mallika
2 Manoj
1 Minnuama
Why isn't doctorName affecting the order?

Because doctorId is the primary sorting field and every ID is unique.

The second field is used only when the first field has duplicate values.

Example with Duplicate Values
doctorType	doctorName
Cardio	Shiva
Cardio	Arun
Cardio	Manoj
Neuro	Mallika

Sorting

Sort.by("doctorType")
    .and(
        Sort.by("doctorName")
    );

Output

Cardio Arun

Cardio Manoj

Cardio Shiva

Neuro Mallika

Here doctorName is applied within the same doctorType.

Flow of Sorting
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   │ Creates Sort object
   ▼
Repository
   │
   ▼
Database
Important Rules
Rule 1

Always use Entity variable names, not database column names.

Correct

Sort.by("doctorName")

Wrong

Sort.by("doctor_name")
Rule 2

Default order

Sort.by("doctorName")

means

Ascending
Rule 3

Descending

Sort.by("doctorName").descending();
Rule 4

Ascending

Sort.by("doctorName").ascending();
