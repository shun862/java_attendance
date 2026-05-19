package com.example.demo.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.Model.Attendance;
import com.example.demo.Model.AttendanceApplication;
import com.example.demo.Repository.AttendanceRepository;

@Service
public class AttendanceService {
  private final AttendanceRepository attendanceRepository;

  public AttendanceService(AttendanceRepository attendanceRepository) {
    this.attendanceRepository = attendanceRepository;
  }

  @Transactional
  public void createAttendance(Attendance attendance) {
    attendanceRepository.insert(attendance);
  }
  
  @Transactional(readOnly = true)
  public Attendance findById(Long id) {
    return attendanceRepository.findById(id);
  }
  
  @Transactional(readOnly = true)
  public Attendance findByWorkDate(Long userId, LocalDate workDate) {
    return attendanceRepository.findByWorkDate(userId, workDate);
  }
  
  @Transactional(readOnly = true)
  public List<Attendance> findMonthly(Long userId, YearMonth yearMonth) {
    var attendanceList = attendanceRepository.findMonthly(userId, yearMonth);
    return createMonthlyAttendanceList(attendanceList, yearMonth);
  }
  
  @Transactional
  private List<Attendance> createMonthlyAttendanceList(List<Attendance> attendanceList, YearMonth yearMonth)
  {
    var list = new ArrayList<Attendance>();
    var currentDate = yearMonth.atDay(1);
    var endDate = yearMonth.atEndOfMonth();
    while(!currentDate.isAfter(endDate))
    {
      var workDate = currentDate;
      var attendance = attendanceList.stream().filter(v -> v.getWorkDate().isEqual(workDate)).findFirst();
      if(attendance.isPresent())
      {
        list.add(attendance.get());
      }
      else
      {
        list.add(new Attendance(workDate));
      }
      currentDate = currentDate.plusDays(1);
    }
    return list;
  }
  
  @Transactional
  public void clockIn(long userId)
  {
    var now = LocalDateTime.now();
    var attendance = attendanceRepository.findByWorkDate(userId, now.toLocalDate());
    if(attendance == null)
    {
      attendance = new Attendance(userId, now.toLocalDate());
      attendance.setClockIn(now);
      attendanceRepository.insert(attendance);
    }
    else
    {
      var clockin = attendance.getClockIn();
      if(clockin == null)
      {
        attendance.setClockIn(now);
        attendanceRepository.updateAttendance(attendance);
      }
      else
      {
        throw new RuntimeException("すでに出勤済みです");
      }
    }
  }
  
  @Transactional
  public void clockOut(long userId)
  {
    var now = LocalDateTime.now();
    var attendance = attendanceRepository.findByWorkDate(userId, now.toLocalDate());
    if(attendance == null)
    {
      attendance = new Attendance(userId, now.toLocalDate());
      attendance.setClockOut(now);
      attendanceRepository.insert(attendance);
    }
    else
    {
      var clockOut = attendance.getClockOut();
      if(clockOut == null)
      {
        attendance.setClockOut(now);
        attendanceRepository.updateAttendance(attendance);
      }
      else
      {
        throw new RuntimeException("すでに退勤済みです");
      }
    }
  }
  
  @Transactional
  public void updateAttendance(AttendanceApplication app)
  {
    var attendanceId = app.getAttendanceId();
    var attendance = findById(attendanceId);
    // 対象の日付のレコードがない
    if(attendance == null)
    {
      attendance = new Attendance(app.getUserId(), app.getWorkDate());
      attendance.setClockIn(app.getClockIn());
      attendance.setClockOut(app.getClockOut());
      attendanceRepository.insert(attendance);
    }
    // 別のユーザーの勤怠
    else if(app.getUserId() != attendance.getUserId())
    {
      throw new RuntimeException("attendance_id:" + attendanceId + " idが不正です。");
    }
    else
    {
      attendance.setClockIn(app.getClockIn());
      attendance.setClockOut(app.getClockOut());
      attendanceRepository.updateAttendance(attendance);
    }
  }
  
}
