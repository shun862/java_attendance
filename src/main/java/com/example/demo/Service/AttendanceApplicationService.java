package com.example.demo.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.ApplicationStatus;
import com.example.demo.Model.AttendanceApplication;
import com.example.demo.Repository.AttendanceApplicationRepository;

@Service
public class AttendanceApplicationService {
  private final AttendanceApplicationRepository applicationRepository;

  public AttendanceApplicationService(AttendanceApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  @Transactional
  public void createApplication(AttendanceApplication application) {
    applicationRepository.insert(application);
  }
  
  @Transactional(readOnly = true)
  public List<AttendanceApplication> findAll() {
    return applicationRepository.findAll();
  }
  
  @Transactional(readOnly = true)
  public AttendanceApplication findById(Long id) {
    return applicationRepository.findById(id);
  }
  
  @Transactional
  public void updateStatus(long id, ApplicationStatus status)
  {
    applicationRepository.updateStatus(id, status);
  }
}
