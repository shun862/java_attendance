package com.example.demo.Repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.ApplicationStatus;
import com.example.demo.Mapper.AttendanceApplicationRowMapper;
import com.example.demo.Model.AttendanceApplication;

@Repository
public class AttendanceApplicationRepository {
  private final JdbcTemplate jdbcTemplate;

  public AttendanceApplicationRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<AttendanceApplication> findAll() {
    String sql = "SELECT * FROM ATTENDANCE_APPLICATION ORDER BY ID";
    return jdbcTemplate.query(sql, new AttendanceApplicationRowMapper());
  }

  public AttendanceApplication findById(Long id) {
    String sql = "SELECT * FROM ATTENDANCE_APPLICATION WHERE ID = ?";
    var list = jdbcTemplate.query(sql, new AttendanceApplicationRowMapper(), id);
    if(list.isEmpty()) return null;
    return list.getFirst();
  }

  public void insert(AttendanceApplication application) {
    String sql = """
        INSERT INTO ATTENDANCE_APPLICATION (
            USER_ID,
            ATTENDANCE_ID,
            WORK_DATE,
            CLOCK_IN,
            CLOCK_OUT,
            STATUS
        ) VALUES (?, ?, ?, ?, ?, ?)
        """;

    jdbcTemplate.update(sql, application.getUserId(), application.getAttendanceId(),
        application.getWorkDate(), application.getClockIn(), application.getClockOut(), 0);
  }
  
  public void updateStatus(long id, ApplicationStatus status)
  {
    String sql = """
        UPDATE ATTENDANCE_APPLICATION
        SET 
          STATUS = ?
        WHERE ID = ?
        """;
    jdbcTemplate.update(sql, status.getStatus(), id);
  }
}
