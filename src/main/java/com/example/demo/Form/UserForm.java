package com.example.demo.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForm {
  @NotBlank(message = "ユーザー名は必須です")
  @Size(max = 20, message = "ユーザー名は20文字以内で入力してください")
  private String username;

  @NotBlank(message = "パスワードは必須です")
  @Size(min = 8, max = 20, message = "パスワードは8〜20文字で入力してください")
  private String password;
}
