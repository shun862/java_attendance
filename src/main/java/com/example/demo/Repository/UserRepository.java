package com.example.demo.Repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.Mapper.UserRowMapper;
import com.example.demo.Model.User;

@Repository
public class UserRepository {
  private final JdbcTemplate jdbcTemplate;

  public UserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<User> findAll() {
    String sql = "SELECT * FROM USERS ORDER BY ID";
    return jdbcTemplate.query(sql, new UserRowMapper());
  }

  public User findById(Long id) {
    String sql = "SELECT * FROM USERS WHERE ID = ?";
    return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
  }
  
  public User findByUsername(String username) {
    String sql = "SELECT * FROM USERS WHERE USER_NAME = ?";
    return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
  }

  public int insert(String username, String password) {
    String sql = "INSERT INTO USERS (USER_NAME, USER_PASSWORD, USER_ROLE) VALUES (?, ?, ?)";
    return jdbcTemplate.update(sql, username, password, 0);
  }

  public boolean existsByUserName(String userName) {
    
    String sql = "SELECT COUNT(*) FROM USERS WHERE USER_NAME = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userName);
    return count != null && count > 0;
  }
}
