// /package com.management.hr_managemnet.dto;

// import java.math.BigDecimal;
// import java.time.LocalDate;

// import com.management.hr_managemnet.model.Employee;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class ResponseEmployee {
//     private Long id;
//     private String name;
//     private String email;
//     private String phone;
//     private String department;
//     private String designation;
//     private LocalDate doj;
//     private BigDecimal salary;
//     private Long userId;
//     private String username;
//     private String role;

//     public ResponseEmployee fromEmployee(Employee employee){
//         return new ResponseEmployee(
//             employee.getId(),
//             employee.getName(),
//             employee.getEmail(),
//             employee.getPhone(),
//             employee.getDepartment(),
//             employee.getDesignation(),
//             employee.getDoj(),
//             employee.getSalary(),
//             employee.getUser().getId(),
//             employee.getUser().getUsername(),
//             employee.getUser().getRole(),
//             employee.getAttandences();

//         );
//     }
// }
