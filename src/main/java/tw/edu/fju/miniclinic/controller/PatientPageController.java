package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;

@Controller
public class PatientPageController {

    @Autowired
    private PatientRepository patientRepo;

    // 顯示所有病人列表
    @GetMapping("/patients")
    public String listPatients(Model model) {
        List<Patient> patients = patientRepo.findAll();
        model.addAttribute("patients", patients);
        return "patients"; // 對應 templates/patients.html
    }

    // 顯示特定病人的詳細資料
    @GetMapping("/patients/{chartNo}")
    public String patientDetail(@PathVariable String chartNo, Model model) {
        Patient patient = patientRepo.findByChartNo(chartNo);

        if (patient == null) {
            // 如果找不到該病歷號，跳轉回列表頁
            return "redirect:/patients";
        }

        model.addAttribute("patient", patient);
        return "patient-detail"; // 對應 templates/patient-detail.html
    }
}