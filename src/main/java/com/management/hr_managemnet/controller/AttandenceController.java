package com.management.hr_managemnet.controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.management.hr_managemnet.dto.AttandenceRegister;
import com.management.hr_managemnet.enums.AttandenceStatus;
import com.management.hr_managemnet.model.Attandence;
import com.management.hr_managemnet.model.Employee;
import com.management.hr_managemnet.model.User;
import com.management.hr_managemnet.repository.AttendenceRepository;
import com.management.hr_managemnet.repository.EmployeeRepository;
import com.management.hr_managemnet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/api/attandences")
@RequiredArgsConstructor
public class AttandenceController {
    @Autowired
    private AttendenceRepository attendenceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired 
    private UserRepository userRepository;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> addAttandence(@RequestBody AttandenceRegister attandenceRegister){
        Optional<User> existingUser = userRepository.findByUsername(attandenceRegister.getUsername());
        if(!existingUser.isPresent()){
            return ResponseEntity.badRequest().body("User Not Found!");
        }
        Long userId = existingUser.get().getId();
        Optional<Employee> existingEmployee = employeeRepository.findByUserId(userId);

        Attandence attandence = new Attandence();
        attandence.setDate(attandenceRegister.getDate());
        attandence.setCheckIn(attandenceRegister.getCheckIn());
        attandence.setCheckOut(attandenceRegister.getCheckOut());
        attandence.setStatus(attandenceRegister.getStatus());
        attandence.setEmployee(existingEmployee.get());
        attendenceRepository.save(attandence);
        return ResponseEntity.ok("Added Attandence Successfully!");
        
    }
    @PostMapping("/addAttandences")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> addAttandences(@RequestParam("file") MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                try {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    // Username
                    Cell usernameCell = row.getCell(0);
                    if (usernameCell == null || usernameCell.getCellType() != CellType.STRING) continue;
                    String username = usernameCell.getStringCellValue().trim();
                    if (username.isEmpty()) continue;

                    Optional<User> existingUser = userRepository.findByUsername(username);
                    User user = existingUser.orElseThrow(() -> new RuntimeException("User Not Found: " + username));
                    Optional<Employee> existingEmployee = employeeRepository.findByUserId(user.getId());
                    if (existingEmployee.isEmpty()) continue;

                    // Date
                    LocalDate daTe = null;
                    Cell dateCell = row.getCell(1);
                    if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
                        Date date = dateCell.getDateCellValue();
                        daTe = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    }

                    // CheckIn
                    LocalTime checkIn = null;
                    Cell checkInCell = row.getCell(2);
                    if (checkInCell != null && checkInCell.getCellType() == CellType.STRING) {
                        String checkInStr = checkInCell.getStringCellValue().trim();
                        if (!checkInStr.isEmpty()) {
                            checkIn = LocalTime.parse(checkInStr, DateTimeFormatter.ofPattern("HH:mm"));
                        }
                    }

                    // CheckOut
                    LocalTime checkOut = null;
                    Cell checkOutCell = row.getCell(3);
                    if (checkOutCell != null && checkOutCell.getCellType() == CellType.STRING) {
                        String checkOutStr = checkOutCell.getStringCellValue().trim();
                        if (!checkOutStr.isEmpty()) {
                            checkOut = LocalTime.parse(checkOutStr, DateTimeFormatter.ofPattern("HH:mm"));
                        }
                    }

                    // Status
                    String statusStr = row.getCell(4).getStringCellValue().trim();
                    AttandenceStatus status = AttandenceStatus.valueOf(statusStr.toUpperCase());

                    // Save Attandence
                    Attandence attandence = new Attandence();
                    attandence.setEmployee(existingEmployee.get());
                    attandence.setDate(daTe);
                    attandence.setCheckIn(checkIn);
                    attandence.setCheckOut(checkOut);
                    attandence.setStatus(status);

                    attendenceRepository.save(attandence);
                } catch (Exception e) {
                    // Log or handle row-level error
                    System.out.println("Skipping row " + i + " due to error: " + e.getMessage());
                }
            }

            workbook.close();
            return ResponseEntity.ok("Attendance updated successfully!");
        }
    }

}
