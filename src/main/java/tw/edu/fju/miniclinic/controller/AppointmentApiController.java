package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AppointmentApiController {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo; // For /api/stats

    // GET /api/appointments/count：回傳總掛號數 (JSON 格式，例如 {"count": 3})
    @GetMapping("/api/appointments/count")
    public ResponseEntity<Map<String, Long>> getAppointmentCount() {
        long count = appointmentRepo.count();
        return ResponseEntity.ok(Map.of("count", count));
    }

    // GET /api/appointments?date=YYYY-MM-DD：依日期篩選
    // GET /api/appointments?doctorId=D001：依醫師 ID 篩選
    // 兩者皆為選填查詢參數，三種情況（都沒傳、只傳 date、只傳 doctorId）都需能正常回應。
    @GetMapping("/api/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String doctorId) {

        List<Appointment> appointments;

        // Case 1: Filter by both date and doctorId
        if (date != null && !date.isBlank() && doctorId != null && !doctorId.isBlank()) {
            LocalDate apptDate = null;
            try {
                apptDate = LocalDate.parse(date);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null); // Invalid date format
            }
            Doctor doctor = doctorRepo.findByDoctorId(doctorId);
            if (doctor == null) {
                return ResponseEntity.notFound().build(); // Doctor not found
            }
            appointments = appointmentRepo.findByApptDateAndDoctor(apptDate, doctor);
        }
        // Case 2: Filter by date only
        else if (date != null && !date.isBlank()) {
            LocalDate apptDate = null;
            try {
                apptDate = LocalDate.parse(date);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(null); // Invalid date format
            }
            appointments = appointmentRepo.findByApptDate(apptDate);
        }
        // Case 3: Filter by doctorId only
        else if (doctorId != null && !doctorId.isBlank()) {
            Doctor doctor = doctorRepo.findByDoctorId(doctorId);
            if (doctor == null) {
                return ResponseEntity.notFound().build(); // Doctor not found
            }
            appointments = appointmentRepo.findByDoctor(doctor);
        }
        // Case 4: No filters, return all appointments
        else {
            appointments = appointmentRepo.findAll();
        }

        return ResponseEntity.ok(appointments);
    }

    // GET /api/stats：顯示基本統計資訊 (API 端點)
    @GetMapping("/api/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalDoctors", doctorRepo.count());
        stats.put("totalPatients", patientRepo.count());
        stats.put("totalAppointments", appointmentRepo.count());

        // 依科別分組，列出每科的掛號數
        List<Object[]> deptCounts = appointmentRepo.countAppointmentsByDepartment();
        Map<String, Long> departmentAppointmentCounts = deptCounts.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0], // Department name
                        arr -> (Long) arr[1]    // Count
                ));
        stats.put("appointmentsByDepartment", departmentAppointmentCounts);

        return ResponseEntity.ok(stats);
    }
    @PutMapping("/api/appointments/{apptId}/status")
    public ResponseEntity<Appointment> updateStatus(
		@PathVariable Long apptId,
		@RequestBody Map<String, String> payload,
		HttpSession session) {

	String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");

	Appointment appt = appointmentRepo.findById(apptId).orElse(null);
	if (appt == null) {
		return ResponseEntity.notFound().build();
	}

	// 只能修改自己的掛號
	if (!appt.getDoctor().getDoctorId().equals(loggedInDoctorId)) {
		return ResponseEntity.status(403).build();
	}

	String newStatus = payload.get("status");
	if (!List.of("BOOKED", "COMPLETED", "CANCELLED").contains(newStatus)) {
		return ResponseEntity.badRequest().build();
	}

	appt.setStatus(newStatus);
	return ResponseEntity.ok(appointmentRepo.save(appt));
}
}