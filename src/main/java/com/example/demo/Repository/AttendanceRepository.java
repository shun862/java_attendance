package com.example.demo.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.Mapper.AttendanceRowMapper;
import com.example.demo.Model.Attendance;

@Repository
public class AttendanceRepository {
  private final JdbcTemplate jdbcTemplate;

  public AttendanceRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Attendance> findAll() {
    String sql = "SELECT * FROM ATTENDANCE ORDER BY ID";
    return jdbcTemplate.query(sql, new AttendanceRowMapper());
  }

  public Attendance findById(Long id) {
    String sql = "SELECT * FROM ATTENDANCE WHERE ID = ?";
    var list = jdbcTemplate.query(sql, new AttendanceRowMapper(), id);
    if(list.isEmpty()) return null;
    return list.getFirst();
  }
  
  public Attendance findByWorkDate(Long userId, LocalDate workDate) {
    String sql = "SELECT * FROM ATTENDANCE WHERE USER_ID = ? AND WORK_DATE = ?";
    var list = jdbcTemplate.query(sql, new AttendanceRowMapper(), userId, workDate);
    if(list.isEmpty()) return null;
    return list.getFirst();
  }

  public List<Attendance> findMonthly(Long userId, YearMonth yearMonth) {

    LocalDate fromDate = yearMonth.atDay(1);
    LocalDate toDate = yearMonth.plusMonths(1).atDay(1);

    String sql = """
        SELECT
            ID,
            USER_ID,
            WORK_DATE,
            CLOCK_IN,
            CLOCK_OUT
        FROM ATTENDANCE
        WHERE USER_ID = ?
          AND WORK_DATE >= ?
          AND WORK_DATE < ?
        ORDER BY WORK_DATE
        """;

    return jdbcTemplate.query(sql, new AttendanceRowMapper(), userId,
        Date.valueOf(fromDate), Date.valueOf(toDate));
  }

  public void insert(Attendance attendance) {
    String sql = """
        INSERT INTO ATTENDANCE (
            USER_ID,
            WORK_DATE,
            CLOCK_IN,
            CLOCK_OUT
        ) VALUES (?, ?, ?, ?)
        """;

    jdbcTemplate.update(sql, attendance.getUserId(), Date.valueOf(attendance.getWorkDate()),
        attendance.getClockIn(), attendance.getClockOut());
  }
  
  public void updateAttendance(Attendance attendance)
  {
    String sql = """
            UPDATE ATTENDANCE
            SET
              CLOCK_IN = ?,
              CLOCK_OUT = ?
            WHERE ID = ?
            """;
    
    jdbcTemplate.update(sql, attendance.getClockIn(), attendance.getClockOut(), attendance.getId());
  }
}
