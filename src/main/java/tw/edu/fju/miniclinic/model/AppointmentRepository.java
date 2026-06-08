package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByApptDate(LocalDate apptDate);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByApptDateAndDoctor(LocalDate apptDate, Doctor doctor);
    List<Appointment> findByDoctorAndApptDate(Doctor doctor, LocalDate apptDate);

    @Query("SELECT d.department, COUNT(a) FROM Appointment a JOIN a.doctor d GROUP BY d.department")
    List<Object[]> countAppointmentsByDepartment();

    long countByStatus(String status);
}