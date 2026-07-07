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
