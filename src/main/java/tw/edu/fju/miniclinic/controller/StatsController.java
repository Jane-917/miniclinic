package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 統計摘要控制器，提供系統整體的資料統計 API
 */
@Controller
public class StatsController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * 1. 統計網頁介面 (HTML)
     */
    @GetMapping("/stats")
    public String showStats(Model model) {
        model.addAttribute("totalDoctors", doctorRepository.count());
        model.addAttribute("totalPatients", patientRepository.count());
        model.addAttribute("totalAppointments", appointmentRepository.count());
        model.addAttribute("bookedCount", appointmentRepository.countByStatus("BOOKED"));
        model.addAttribute("completedCount", appointmentRepository.countByStatus("COMPLETED"));
        model.addAttribute("cancelledCount", appointmentRepository.countByStatus("CANCELLED"));
        return "stats";
    }

    /**
     * 2. 統計數據介面 (JSON API)
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStats() {
        // 獲取各資料表總筆數
        long totalDoctors = doctorRepository.count();
        long totalPatients = patientRepository.count();
        long totalAppointments = appointmentRepository.count();

        // 依狀態統計掛號數量，使用 LinkedHashMap 保持 JSON 欄位順序
        Map<String, Long> byStatus = new LinkedHashMap<>();
        byStatus.put("BOOKED", appointmentRepository.countByStatus("BOOKED"));
        byStatus.put("COMPLETED", appointmentRepository.countByStatus("COMPLETED"));
        byStatus.put("CANCELLED", appointmentRepository.countByStatus("CANCELLED"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalDoctors", totalDoctors);
        result.put("totalPatients", totalPatients);
        result.put("totalAppointments", totalAppointments);
        result.put("byStatus", byStatus);

        return ResponseEntity.ok(result);
    }
}