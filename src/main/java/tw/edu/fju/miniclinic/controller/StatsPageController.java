package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

/**
 * 統計頁面控制器，負責顯示系統資料摘要的網頁介面
 */
@Controller
public class StatsPageController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/stats")
    public String showStats(Model model) {
        // 獲取各項統計數據並放入 Model
        model.addAttribute("totalDoctors", doctorRepo.count());
        model.addAttribute("totalPatients", patientRepo.count());
        model.addAttribute("totalAppointments", appointmentRepo.count());
        
        model.addAttribute("bookedCount", appointmentRepo.countByStatus("BOOKED"));
        model.addAttribute("completedCount", appointmentRepo.countByStatus("COMPLETED"));
        model.addAttribute("cancelledCount", appointmentRepo.countByStatus("CANCELLED"));
        
        return "stats"; // 對應 src/main/resources/templates/stats.html
    }
}