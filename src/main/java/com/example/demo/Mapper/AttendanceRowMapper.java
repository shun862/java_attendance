package com.example.demo.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.example.demo.Model.Attendance;

public class AttendanceRowMapper implements RowMapper<Attendance> {
  @Override
  public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
    Attendance attendance = new Attendance();
    attendance.setId(rs.getLong("ID"));
    attendance.setUserId(rs.getLong("USER_ID"));
    attendance.setWorkDate(rs.getDate("WORK_DATE").toLocalDate());
    if (rs.getTimestamp("CLOCK_IN") != null) {
      attendance.setClockIn(rs.getTimestamp("CLOCK_IN").toLocalDateTime());
    }
    if (rs.getTimestamp("CLOCK_OUT") != null) {
      attendance.setClockOut(rs.getTimestamp("CLOCK_OUT").toLocalDateTime());
    }
    return attendance;
  }
}
