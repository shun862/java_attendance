package com.example.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.Form.UserForm;
import com.example.demo.Service.UserService;

@Controller
public class LoginController {
  private final UserService userService;
  
  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login_view")
  public ModelAndView loginView() {
    ModelAndView mav = new ModelAndView();
    var userForm = new UserForm();
    mav.setViewName("login_view");
    mav.addObject("userForm", userForm);
    return mav;
  }

  @PostMapping("/login_check")
  public String login_check(@Valid @ModelAttribute("userForm") UserForm user,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "login_view";
    }
    return "forward:/login";
  }

  @GetMapping("/create_user")
  public ModelAndView createUserView() {    
    ModelAndView mav = new ModelAndView();
    var userForm = new UserForm();
    mav.setViewName("create_user");
    mav.addObject("userForm", userForm);
    return mav;
  }

  @PostMapping("/create_user")
  public String createUser(@Valid @ModelAttribute("userForm") UserForm user,
      BindingResult bindingResult) {
    // バリデーションエラー
    if (bindingResult.hasErrors()) {
      return "create_user";
    }
    
    if(userService.existsByUserName(user.getUsername()))
    {
      // TODO 作成済みエラー表示
      return "create_user";
    }

    try {
      userService.createUser(user);
    } catch (DuplicateKeyException e) {
      return "user_create";
    }
    
    // TODO 作成成功画面を表示
    return "create_user_success";
  }
  
  @GetMapping("/create_user_success")
  public String createUserSuccess() {
    return "create_user_success";
  }
}
