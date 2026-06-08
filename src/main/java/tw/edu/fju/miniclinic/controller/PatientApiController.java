package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;

@RestController
public class PatientApiController {

    @Autowired
    private PatientRepository patientRepo;

    // API：取得所有病人 JSON
    @GetMapping("/api/patients")
    public List<Patient> getPatients() {
        return patientRepo.findAll();
    }

    // API：取得單一病人 JSON
    @GetMapping("/api/patients/{chartNo}")
    public ResponseEntity<Patient> getPatient(@PathVariable String chartNo) {
        Patient patient = patientRepo.findByChartNo(chartNo);
        return (patient != null) ? ResponseEntity.ok(patient) : ResponseEntity.notFound().build();
    }
}