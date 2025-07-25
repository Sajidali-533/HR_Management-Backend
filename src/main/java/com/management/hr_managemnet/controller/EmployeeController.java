package com.management.hr_managemnet.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import com.management.hr_managemnet.dto.RegisterEmployee;
import com.management.hr_managemnet.model.Employee;
import com.management.hr_managemnet.model.User;
import com.management.hr_managemnet.repository.EmployeeRepository;
import com.management.hr_managemnet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    @PreAuthorize ("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> registerEmployee(@RequestBody RegisterEmployee registerEmployee){
        Optional<User> existingUsers = userRepository.findByUsername(registerEmployee.getUsername());

        if (!existingUsers.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Employee employee = new Employee();
        employee.setUser(existingUsers.get());
        employee.setName(registerEmployee.getName());
        employee.setEmail(registerEmployee.getEmail());
        employee.setPhone(registerEmployee.getPhone());
        employee.setDepartment(registerEmployee.getDepartment());
        employee.setDesignation(registerEmployee.getDesignation());
        employee.setDoj(registerEmployee.getDoj());
        employee.setSalary(registerEmployee.getSalary());

        employeeRepository.save(employee);
        return ResponseEntity.ok("Employee registered successfully");
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue()).trim();
            return "";
    }


    @PostMapping("/addEmployees")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> addEmployees(@RequestParam("file") MultipartFile file) throws Exception {
    try (InputStream is = file.getInputStream()) {
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // Username
            Cell usernameCell = row.getCell(0);
            if (usernameCell == null || usernameCell.getCellType() != CellType.STRING) {
                System.out.println("Skipping row " + i + ": username is missing or not text");
                continue;
            }

            String username = usernameCell.getStringCellValue().trim();
            if (username.isEmpty()) {
                System.out.println("Skipping row " + i + ": username is empty");
                continue;
            }

            Optional<User> existingUser = userRepository.findByUsername(username);
            User user = existingUser.orElseThrow(() ->
                new RuntimeException("User not found for username: " + username)
            );

            // Other fields
            String name = getCellStringValue(row.getCell(1));
            String email = getCellStringValue(row.getCell(2));
            String phone = getCellStringValue(row.getCell(3));
            String department = getCellStringValue(row.getCell(4));
            String designation = getCellStringValue(row.getCell(5));

            // DOJ
            LocalDate doj = null;
            Cell dojCell = row.getCell(6);
            if (dojCell != null && dojCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dojCell)) {
                Date date = dojCell.getDateCellValue();
                if (date != null) {
                    doj = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
            }

            // Salary
            BigDecimal salary = BigDecimal.ZERO;
            Cell salaryCell = row.getCell(7);
            if (salaryCell != null && salaryCell.getCellType() == CellType.NUMERIC) {
                salary = BigDecimal.valueOf(salaryCell.getNumericCellValue());
            }

            // Save Employee
            Employee employee = new Employee();
            employee.setUser(user);
            employee.setName(name);
            employee.setEmail(email);
            employee.setPhone(phone);
            employee.setDepartment(department);
            employee.setDesignation(designation);
            employee.setDoj(doj);
            employee.setSalary(salary);

            employeeRepository.save(employee);
        }

        workbook.close();
        return ResponseEntity.ok("Employees Added Successfully!");
    }
}



    @PutMapping("/updateEmployee")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> updateEmployee (@RequestBody RegisterEmployee registerEmployee){
        Optional<User> existingUser = userRepository.findByUsername(registerEmployee.getUsername());
        if(!existingUser.isPresent()){
            return ResponseEntity.badRequest().body("User not found");
        }
        Long userId = existingUser.get().getId();

        Optional<Employee> existingEmployee = employeeRepository.findByUserId(userId);

        if(existingEmployee.isPresent()){
            Employee employee = existingEmployee.get();
            employee.setName(registerEmployee.getName());
            employee.setEmail(registerEmployee.getEmail());
            employee.setPhone(registerEmployee.getPhone());
            employee.setDepartment(registerEmployee.getDepartment());
            employee.setDesignation(registerEmployee.getDesignation());
            employee.setDoj(registerEmployee.getDoj());
            employee.setSalary(registerEmployee.getSalary());
            employeeRepository.save(employee);
            return ResponseEntity.ok("Employee Updated Successfully!");
        }else{
            return ResponseEntity.badRequest().body("Update failed!");
        }
    }

    @GetMapping("/allEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> getAllEmployee(){
        List<Employee> allEmployees = employeeRepository.findAll();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/department/{dePartment}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<Employee>> getEmployeeByDepartment(@PathVariable String dePartment){
        String d_part = dePartment.toUpperCase();
        List<Employee> d_Employees = employeeRepository.findByDepartment(d_part);
        return ResponseEntity.ok(d_Employees);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or authentication.principal.username == #username")
    public ResponseEntity<Optional<Employee>> getEmployeeByUsername(@PathVariable String username){
        Optional<User> existingUser = userRepository.findByUsername(username);

        if(!existingUser.isPresent()){
            return ResponseEntity.badRequest().body(Optional.empty());
        }
        Long userId = existingUser.get().getId();

        Optional<Employee> getEmployee = employeeRepository.findByUserId(userId);
        return ResponseEntity.ok(getEmployee);
    }
}
