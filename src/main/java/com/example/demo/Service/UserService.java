package com.example.demo.Service;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.Form.UserForm;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void createUser(UserForm form) {
    var password = passwordEncoder.encode(form.getPassword());
    userRepository.insert(form.getUsername(), password);
  }

  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }
  
  @Transactional(readOnly = true)
  public boolean existsByUserName(String userName) {
    return userRepository.existsByUserName(userName);
  }
}
