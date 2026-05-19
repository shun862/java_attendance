package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.ApplicationStatus;
import com.example.demo.LoginUser;
import com.example.demo.Form.AttendanceEditForm;
import com.example.demo.Model.AttendanceApplication;
import com.example.demo.Service.AttendanceApplicationService;
import com.example.demo.Service.AttendanceService;

@Controller
public class AttendanceController {
  private final AttendanceService attendanceService;
  private final AttendanceApplicationService applicationService;

  public AttendanceController(AttendanceService attendanceService, AttendanceApplicationService applicationService) {
      this.attendanceService = attendanceService;
      this.applicationService = applicationService;
  }
  
  @GetMapping("/user/attendance_list")
  public ModelAndView attendanceList(@AuthenticationPrincipal LoginUser loginUser) {
    ModelAndView mav = new ModelAndView();
    var yearMonth = YearMonth.now();
    String currentYearMonth = yearMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月"));
//    var list = attendanceService.findMonthly(loginUser.getUserId(), yearMonth);
//    mav.addObject("attendanceList", list);
    mav.addObject("yearMonth", yearMonth);
    mav.addObject("displayMonth", currentYearMonth);
    mav.setViewName("attendance_list");
    return mav;
  }

  @PostMapping("/user/attendance_list/change")
  @ResponseBody
  public Map<String, Object> changeMonth(
          @RequestBody Map<String, String> body, Authentication auth) {

      String yearMonth = body.get("yearMonth");
      YearMonth ym = YearMonth.parse(yearMonth);

      var userId = ((LoginUser) auth.getPrincipal()).getUserId();

      var list = attendanceService.findMonthly(userId, ym);

      // JSONとして返すMap
      Map<String, Object> res = new HashMap<>();
      res.put("yearMonth", ym.toString());
      res.put("displayMonth",
              ym.getYear() + "年" + ym.getMonthValue() + "月");

      // Entity → Map に変換（DTO不要）
      List<Map<String, Object>> attendanceList = list.stream()
          .map(a -> {
              Map<String, Object> m = new HashMap<>();
              m.put("workDate", a.getWorkDate().toString());
              m.put("clockIn", a.getClockIn());
              m.put("clockOut", a.getClockOut());
              return m;
          })
          .toList();

      res.put("attendanceList", attendanceList);

      return res;
  }
  
  @GetMapping("/user/attendance")
  public ModelAndView attendance(@AuthenticationPrincipal LoginUser loginUser) {
    ModelAndView mav = new ModelAndView();
    var today = LocalDate.now();
    mav.addObject("today", today);
    mav.setViewName("attendance");
    return mav;
  }
  
  
  @PostMapping("user/attendance/clockin")
  public ResponseEntity<Map<String, Object>> clockIn(@AuthenticationPrincipal LoginUser loginUser) {
      try {
          attendanceService.clockIn(loginUser.getUserId());
          return ResponseEntity.ok(
              Map.of(
                  "success", true,
                  "message", "出勤しました"
              )
          );
      } catch (Exception e) {
        return createBadRequest(e);
      }
  }
  
  @PostMapping("user/attendance/clockout")
  public ResponseEntity<Map<String, Object>> clockOut(@AuthenticationPrincipal LoginUser loginUser) {
      try {
          attendanceService.clockOut(loginUser.getUserId());
          return ResponseEntity.ok(
              Map.of(
                  "success", true,
                  "message", "退勤しました"
              )
          );
      } catch (Exception e) {
          return createBadRequest(e);
      }
  }
  
  @GetMapping("user/attendance/application")
  public ModelAndView application(@AuthenticationPrincipal LoginUser loginUser, @RequestParam LocalDate workDate) {
    ModelAndView mav = new ModelAndView();
    var attendance = attendanceService.findByWorkDate(loginUser.getUserId(), workDate);
    var attendanceForm = new AttendanceEditForm(workDate);
    if (attendance != null) {
      if (attendance.getClockIn() != null) {
        attendanceForm.setClockIn(attendance.getClockIn().toLocalTime());
      }
      if (attendance.getClockOut() != null) {
        attendanceForm.setClockOut(attendance.getClockOut().toLocalTime());
      }
  }
    mav.addObject("attendanceForm", attendanceForm);
    mav.setViewName("attendance_application");
    return mav;
  }
  
  @PostMapping("user/attendance/application_check")
  public String applicationCheck(@AuthenticationPrincipal LoginUser loginUser, 
          @Valid @ModelAttribute("attendanceForm") AttendanceEditForm form,
          BindingResult bindingResult) {

      if (bindingResult.hasErrors()) {
          return "attendance_application";
      }
      var attendance = attendanceService.findByWorkDate(loginUser.getUserId(), form.getWorkDate());

      LocalDateTime clockIn = LocalDateTime.of(form.getWorkDate(), form.getClockIn());
      LocalDateTime clockOut = LocalDateTime.of(form.getWorkDate(), form.getClockOut());
      
      var application = new AttendanceApplication();
      if(attendance != null)
      {
        application.setAttendanceId(attendance.getId());
      }
      application.setUserId(loginUser.getUserId());
      application.setWorkDate(form.getWorkDate());
      application.setClockIn(clockIn);
      application.setClockOut(clockOut);
      applicationService.createApplication(application);

      return "attendance_applied";
  }
  
//  @GetMapping("user/attendance/applied")
//  public ModelAndView applied() {
//    ModelAndView mav = new ModelAndView();
//    return mav;
//  }
  
  @GetMapping("user/attendance/application_list")
  public ModelAndView applicationList(@AuthenticationPrincipal LoginUser loginUser) {
    ModelAndView mav = new ModelAndView();
    var applicationList = applicationService.findAll();
    var role = loginUser.getRole();
    mav.setViewName("attendance_application_list");
    mav.addObject("applicationList", applicationList);
    mav.addObject("role", role);
    return mav;
  }
  
  @Transactional
  @PostMapping("user/attendance/application/approve")
  public String applicationApprove(@RequestParam Long applicationId)
  {
    try
    {
      applicationService.updateStatus(applicationId, ApplicationStatus.Approve);
    }
    catch (Exception e) {
      // TODO エラー表示
//      return createBadRequest(e);
    }
    attendanceService.updateAttendance(applicationService.findById(applicationId));
    return "redirect:/user/attendance/application_list";
  }
  
  @PostMapping("user/attendance/application/reject")
  public String applicationReject(@RequestParam Long applicationId)
  {
    applicationService.updateStatus(applicationId, ApplicationStatus.Rejected);
    return "redirect:/user/attendance/application_list";
  }
  
  private ResponseEntity<Map<String, Object>> createBadRequest(Exception e)
  {
    return ResponseEntity.badRequest().body(
        Map.of(
            "success", false,
            "message", e.getMessage()
        )
    );
  }
}
