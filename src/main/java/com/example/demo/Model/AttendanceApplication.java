package com.example.demo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.demo.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

public class AttendanceApplication {
  @Getter
  @Setter
  private Long id;
  @Getter
  @Setter
  private Long userId;
  @Getter
  @Setter
  private Long attendanceId;
  @Getter
  @Setter
  private LocalDate workDate;
  @Getter
  @Setter
  private LocalDateTime clockIn;
  @Getter
  @Setter
  private LocalDateTime clockOut;
  @Getter
  @Setter
  private ApplicationStatus status;
  
  public AttendanceApplication(){}
}
