package com.example.demo;

public enum ApplicationStatus
{
  Pending(0, "申請中"),
  Approve(1, "承認"),
  Rejected(2, "却下");
  
  private int status;
  private String label;
  
  private ApplicationStatus(int status, String label)
  {
    this.status = status;
    this.label = label;
  }
  
  public boolean isPending()
  {
    return this == ApplicationStatus.Pending;
  }
  
  public int getStatus() {
    return status;
  }
  
  public String getLabel() {
    return label;
}
  
  public static ApplicationStatus from(int status) {
    for (ApplicationStatus s : ApplicationStatus.values()) {
      if (s.status == status) {
        return s;
      }
    }
    throw new IllegalArgumentException("Unknown status: " + status);
  }
};
