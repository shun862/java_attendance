package com.example.demo.Mapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.example.demo.Model.User;

public class UserRowMapper implements RowMapper<User> {
  
  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User user = new User();

    user.setId(rs.getLong("ID"));
    user.setUsername(rs.getString("USER_NAME"));
    user.setPassword(rs.getString("USER_PASSWORD"));
    user.setRole(rs.getInt("USER_ROLE"));

    return user;
  }

}
