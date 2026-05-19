package com.example.demo.Form;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class AttendanceEditForm {
  @Getter
  private LocalDate workDate;

  @Setter
  @Getter
  @NotNull(message = "出勤時刻を入力してください")
  private LocalTime clockIn;
  
  @Setter
  @Getter
  @NotNull(message = "退勤時刻を入力してください")
  private LocalTime clockOut;
  
  public AttendanceEditForm(LocalDate workDate) {
    this.workDate = workDate;
  }

  /* 出勤 < 退勤 の相関チェック */
  @AssertTrue(message = "退勤時刻は出勤時刻より後である必要があります")
  public boolean isValidTimeOrder() {
      if (clockIn == null || clockOut == null) {
          return true; // 未入力チェックは@NotNullに任せる
      }
      return clockOut.isAfter(clockIn);
  }
}
