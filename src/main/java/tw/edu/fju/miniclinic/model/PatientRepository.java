package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, String> {

    // Spring Data JPA 會自動處理 findAll() 和 findById() (即 findByChartNo)
    List<Patient> findByName(String name);

    // 讓 Controller 可以直接使用這個名稱查詢，並回傳 Patient 或 null
    Patient findByChartNo(String chartNo);
}