package com.example.demo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Attendance {

  private Long id;
  private Long userId;
  private LocalDate workDate;
  private LocalDateTime clockIn;
  private LocalDateTime clockOut;

  public Attendance() {}

  public Attendance(Long userId, LocalDate workDate) {
    this.userId = userId;
    this.workDate = workDate;
  }
  
  public Attendance(LocalDate workDate) {
    this.workDate = workDate;
  }

  public Long getId() {
    return id;
  }
  
  public Long getUserId() {
    return userId;
  }
  
  public LocalDate getWorkDate() {
    return workDate;
  }
  
  public LocalDateTime getClockIn() {
    return clockIn;
  }
  
  public LocalDateTime getClockOut() {
    return clockOut;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setWorkDate(LocalDate workDate) {
    this.workDate = workDate;
  }

  public void setClockIn(LocalDateTime clockIn) {
    this.clockIn = clockIn;
  }

  public void setClockOut(LocalDateTime clockOut) {
    this.clockOut = clockOut;
  }
}
