package com.example.demo.Model;

public class User {

  private Long id;
  private String username;
  private String password;
  private int role;

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public int getRole() {
    return role;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRole(int role) {
    this.role = role;
  }

}
