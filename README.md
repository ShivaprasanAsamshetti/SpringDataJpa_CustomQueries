# Spring Data JPA - Sorting

## Definition

**Sorting** is the process of arranging records in **Ascending (ASC)** or **Descending (DESC)** order based on one or more entity fields.

### Example

#### Without Sorting

| Id | Doctor Name |
|----|-------------|
| 3 | Mallika |
| 1 | Shiva |
| 2 | Manoj |

#### After Sorting (Ascending by Id)

| Id | Doctor Name |
|----|-------------|
| 1 | Shiva |
| 2 | Manoj |
| 3 | Mallika |

---

# Which Repository Supports Sorting?

```
Repository
      ↑
CrudRepository
      ↑
PagingAndSortingRepository
      ↑
JpaRepository
```

`JpaRepository` inherits sorting functionality from **PagingAndSortingRepository**.

No additional methods are required.

```java
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

}
```

Internally, `JpaRepository` already provides:

```java
List<T> findAll(Sort sort);
```

---

# Sort Class

Sorting is performed using the `Sort` class.

```java
import org.springframework.data.domain.Sort;
```

---

# Ascending Order

```java
List<Doctor> doctors = doctorRepo.findAll(
        Sort.by("doctorName")
);
```

Generated SQL

```sql
SELECT *
FROM doctor
ORDER BY doctor_name ASC;
```

> **Note:** `Sort.by()` uses **Ascending (ASC)** by default.

---

# Descending Order

```java
List<Doctor> doctors = doctorRepo.findAll(
        Sort.by("doctorId")
            .descending()
);
```

Generated SQL

```sql
SELECT *
FROM doctor
ORDER BY doctor_id DESC;
```

---

# Creating a Sort Object

Instead of writing:

```java
doctorRepo.findAll(
    Sort.by("doctorName")
);
```

You can create a separate `Sort` object.

```java
Sort sort = Sort.by("doctorName");

List<Doctor> doctors = doctorRepo.findAll(sort);
```

Both approaches produce the same result.

---

# Multiple Field Sorting

## Example 1

Sort by **doctorType** and then **doctorName**.

```java
Sort sort = Sort.by("doctorType")
        .ascending()
        .and(
            Sort.by("doctorName")
                .ascending()
        );

List<Doctor> doctors = doctorRepo.findAll(sort);
```

Generated SQL

```sql
ORDER BY doctor_type ASC,
         doctor_name ASC;
```

---

## Example 2

Sort by **doctorType** (ASC) and **doctorId** (DESC).

```java
Sort sort = Sort.by("doctorType")
        .ascending()
        .and(
            Sort.by("doctorId")
                .descending()
        );
```

Generated SQL

```sql
ORDER BY doctor_type ASC,
         doctor_id DESC;
```

---

# Important Interview Concept

Consider the following data:

| doctorId | doctorName |
|-----------|------------|
| 5 | Shiva |
| 4 | Mallika |
| 3 | Mallika |
| 2 | Manoj |
| 1 | Minnuama |

Sorting

```java
Sort.by("doctorId")
    .descending()
    .and(
        Sort.by("doctorName")
    );
```

Output

| doctorId | doctorName |
|-----------|------------|
| 5 | Shiva |
| 4 | Mallika |
| 3 | Mallika |
| 2 | Manoj |
| 1 | Minnuama |

### Why isn't `doctorName` affecting the order?

Because `doctorId` is the **primary sorting field**, and every ID is unique.

The second sorting field is applied **only when the first field contains duplicate values**.

---

# Example with Duplicate Values

Data

| doctorType | doctorName |
|------------|------------|
| Cardio | Shiva |
| Cardio | Arun |
| Cardio | Manoj |
| Neuro | Mallika |

Sorting

```java
Sort.by("doctorType")
    .and(
        Sort.by("doctorName")
    );
```

Output

| doctorType | doctorName |
|------------|------------|
| Cardio | Arun |
| Cardio | Manoj |
| Cardio | Shiva |
| Neuro | Mallika |

### Explanation

1. Records are first grouped by `doctorType`.
2. Within the same `doctorType`, records are sorted by `doctorName`.

---

# Sorting Flow

```
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
```

---

# Important Rules

## Rule 1

Always use **Entity field names**, not database column names.

✔ Correct

```java
Sort.by("doctorName")
```

❌ Wrong

```java
Sort.by("doctor_name")
```

---

## Rule 2

Default order is **Ascending**.

```java
Sort.by("doctorName")
```

Equivalent to

```java
Sort.by("doctorName").ascending();
```

---

## Rule 3

Descending order

```java
Sort.by("doctorName")
    .descending();
```

---

## Rule 4

Explicit Ascending order

```java
Sort.by("doctorName")
    .ascending();
```

---

# Summary

| Method | Description |
|----------|-------------|
| `Sort.by("field")` | Ascending order (default) |
| `.ascending()` | Ascending order |
| `.descending()` | Descending order |
| `.and()` | Sort by multiple fields |
| `findAll(sort)` | Returns sorted records |

---

# Interview Questions

### 1. Which repository provides sorting functionality?

**Answer:** `JpaRepository` (inherits it from `PagingAndSortingRepository`).

---

### 2. Which method is used for sorting?

```java
findAll(Sort sort)
```

---

### 3. What is the default sorting order?

Ascending (`ASC`).

---

### 4. Can we sort using multiple fields?

Yes.

```java
Sort.by("doctorType")
    .and(Sort.by("doctorName"));
```

---

### 5. Which field is given higher priority during multiple sorting?

The **first field**.

The second field is considered only when the first field has duplicate values.

---

### 6. Should we use entity field names or database column names?

Always use **Entity field names**.

✔ `doctorName`

❌ `doctor_name`

---

# Key Takeaways

- `JpaRepository` supports sorting.
- Sorting is performed using the `Sort` class.
- `Sort.by()` defaults to **Ascending**.
- Use `.descending()` for descending order.
- Use `.and()` to sort by multiple fields.
- Secondary sorting is applied only when the primary sorting field has duplicate values.
- Always use **entity field names**, not database column names.





# Spring Data JPA - Pagination

## Definition

**Pagination** is the process of dividing a large number of records into multiple pages, where each page contains a fixed number of records.

Instead of loading all records from the database, **only the requested page is fetched**, improving application performance.

---

# Why Pagination?

Suppose the **Doctor** table contains **100,000 records**.

## Without Pagination

```
Database
   │
100000 Records
   │
Spring Boot
   │
Client
```

### Problems

- Slow response time
- High memory consumption
- Increased network traffic
- Poor user experience
- Longer database execution time

---

## With Pagination

```
Page 0 → Doctor 1  - Doctor 10

Page 1 → Doctor 11 - Doctor 20

Page 2 → Doctor 21 - Doctor 30

...
```

Only the requested page is loaded into memory.

---

# Repository Hierarchy

```
Repository
      ↑
CrudRepository
      ↑
PagingAndSortingRepository
      ↑
JpaRepository
```

`JpaRepository` inherits pagination support from **PagingAndSortingRepository**.

No additional repository method is required.

```java
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

}
```

Internally, `JpaRepository` already provides:

```java
Page<T> findAll(Pageable pageable);
```

---

# Pagination Components

Pagination mainly uses three components.

```
PageRequest
      │
implements
      ▼
Pageable (Interface)
      │
passed to
      ▼
findAll(Pageable pageable)
      │
returns
      ▼
Page<T>
```

---

# 1. Pageable (Interface)

## Definition

`Pageable` is an interface that represents pagination information.

It stores:

- Current page number
- Page size
- Sorting information (optional)

Since it is an **interface**, it cannot create objects directly.

Example

```java
Pageable pageable;
```

---

## Why is Pageable an Interface?

Similar to Java Collections:

```java
List<String> list;
```

`List` is an interface.

Actual object:

```java
List<String> list = new ArrayList<>();
```

Similarly,

```java
Pageable pageable;
```

Actual object:

```java
Pageable pageable = PageRequest.of(0, 5);
```

---

# 2. PageRequest (Class)

## Definition

`PageRequest` is a concrete class that implements the `Pageable` interface.

It is used to create pagination information.

Syntax

```java
PageRequest.of(pageNumber, pageSize);
```

Example

```java
Pageable pageable = PageRequest.of(0, 5);
```

Meaning

- Page Number = **0**
- Page Size = **5**

---

## Why do we use PageRequest?

Interfaces cannot be instantiated.

❌ Wrong

```java
Pageable pageable = new Pageable();
```

✔ Correct

```java
Pageable pageable = PageRequest.of(0,5);
```

Here,

- `PageRequest` creates the object.
- That object is stored in the `Pageable` reference.

---

# Understanding Page Number

Pagination uses **zero-based indexing**.

| Page Number | Records |
|-------------|---------|
| 0 | 1 - 5 |
| 1 | 6 - 10 |
| 2 | 11 - 15 |
| 3 | 16 - 20 |

So,

```java
PageRequest.of(0,5)
```

means:

```
First Page
5 Records
```

---

# 3. Page<T>

## Definition

`Page<T>` represents the paginated result returned from the database.

Unlike `List`, it contains:

- Records of the current page
- Pagination metadata

Example

```java
Page<Doctor> page = doctorRepo.findAll(pageable);
```

---

## Why not List?

This is wrong:

```java
List<Doctor> doctors = doctorRepo.findAll(pageable);
```

Because Spring Data JPA returns

```java
Page<Doctor>
```

not

```java
List<Doctor>
```

---

# Getting Records

The actual records are obtained using

```java
page.getContent();
```

Example

```java
Page<Doctor> page = doctorRepo.findAll(pageable);

List<Doctor> doctors = page.getContent();
```

---

# Useful Methods of Page

| Method | Description |
|----------|-------------|
| `getContent()` | Returns records of the current page |
| `getTotalPages()` | Total number of pages |
| `getTotalElements()` | Total number of records |
| `getNumber()` | Current page number |
| `getSize()` | Page size |
| `hasNext()` | Checks if next page exists |
| `hasPrevious()` | Checks if previous page exists |
| `isFirst()` | Checks whether current page is first |
| `isLast()` | Checks whether current page is last |
| `getNumberOfElements()` | Number of records in current page |

---

# SQL Generated

Example

```java
Pageable pageable = PageRequest.of(1,5);

doctorRepo.findAll(pageable);
```

Generated SQL

```sql
SELECT *
FROM doctor
LIMIT 5 OFFSET 5;
```

Explanation

```
LIMIT 5
```

Return only 5 records.

```
OFFSET 5
```

Skip the first 5 records.

---

# Pagination Flow

```
Client
      │
pageNumber, pageSize
      │
Controller
      │
Service
      │
PageRequest.of(pageNumber, pageSize)
      │
Pageable
      │
Repository
findAll(pageable)
      │
Database
      │
Page<Doctor>
      │
getContent()
      │
List<Doctor>
```

---

# Complete Example

```java
Pageable pageable = PageRequest.of(0,5);

Page<Doctor> page = doctorRepo.findAll(pageable);

List<Doctor> doctors = page.getContent();
```

---

# Important Points

### Page Number starts from **0**

```java
PageRequest.of(0,5);
```

First page.

---

### Page Size

```java
PageRequest.of(0,10);
```

Returns **10 records**.

---

### Metadata is available only in `Page`

Example

```java
page.getTotalPages();

page.getTotalElements();

page.hasNext();
```

A normal `List` cannot provide this information.

---

# Interview Questions

### 1. What is Pagination?

Pagination divides large datasets into multiple pages so that only the required records are fetched from the database.

---

### 2. Which repository supports Pagination?

`JpaRepository`

(It inherits the feature from `PagingAndSortingRepository`.)

---

### 3. Which method is used for Pagination?

```java
findAll(Pageable pageable);
```

---

### 4. Which class creates the Pageable object?

```java
PageRequest
```

Example

```java
PageRequest.of(0,5);
```

---

### 5. Why is Pageable an interface?

Interfaces cannot be instantiated directly.

`PageRequest` provides its implementation.

---

### 6. Why does `findAll(Pageable)` return `Page<T>` instead of `List<T>`?

Because `Page<T>` contains:

- Records
- Total pages
- Total records
- Current page
- Next/Previous page information

---

### 7. Which method returns only the records?

```java
page.getContent();
```

---

### 8. Does pagination start from page 1?

No.

Spring Data JPA uses **zero-based indexing**.

```
0 → First page

1 → Second page

2 → Third page
```

---

### 9. Can Pagination be combined with Sorting?

Yes.

```java
Pageable pageable =
PageRequest.of(
        0,
        5,
        Sort.by("doctorName")
);
```

This returns:

- First page
- 5 records
- Sorted by doctorName

---

# Key Takeaways

- Pagination improves performance by fetching only the required records.
- `JpaRepository` provides pagination support.
- `Pageable` is an interface that stores pagination information.
- `PageRequest` implements `Pageable` and creates pagination objects.
- `Page<T>` contains both records and pagination metadata.
- `getContent()` returns the records of the current page.
- Page numbering starts from **0**.
- Pagination can be combined with sorting.
- Spring Data JPA internally generates SQL using **LIMIT** and **OFFSET**.
