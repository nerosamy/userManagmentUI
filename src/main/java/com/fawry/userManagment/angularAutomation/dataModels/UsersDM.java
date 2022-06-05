package com.fawry.userManagment.angularAutomation.dataModels;

public class UsersDM extends MainDataModel{

    private String email;
    private String phone;
    private String role;
    private String branch;
    private String status;
    private String mustChangePassword;
    private String passwordNeverExpire;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(String mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public String getPasswordNeverExpire() {
        return passwordNeverExpire;
    }

    public void setPasswordNeverExpire(String passwordNeverExpire) {
        this.passwordNeverExpire = passwordNeverExpire;
    }
}
