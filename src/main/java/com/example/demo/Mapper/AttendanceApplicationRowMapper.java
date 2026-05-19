package com.example.demo.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.example.demo.ApplicationStatus;
import com.example.demo.Model.AttendanceApplication;

public class AttendanceApplicationRowMapper implements RowMapper<AttendanceApplication> {
  @Override
  public AttendanceApplication mapRow(ResultSet rs, int rowNum) throws SQLException {
    AttendanceApplication application = new AttendanceApplication();
    application.setId(rs.getLong("ID"));
    application.setUserId(rs.getLong("USER_ID"));
    application.setAttendanceId(rs.getLong("ATTENDANCE_ID"));
    application.setWorkDate(rs.getDate("WORK_DATE").toLocalDate());
    application.setClockIn(rs.getTimestamp("CLOCK_IN").toLocalDateTime());
    application.setClockOut(rs.getTimestamp("CLOCK_OUT").toLocalDateTime());
    int status = rs.getInt("status");
    application.setStatus(ApplicationStatus.from(status));
    return application;
  }
}
