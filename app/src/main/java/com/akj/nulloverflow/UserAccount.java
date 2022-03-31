package com.akj.nulloverflow;

//사용자 정보 계정 모델
public class UserAccount {
    private String idToken;
    private String email;
    private String password;
    private String name;
    private String department;

    public UserAccount() { }

    public String getIdToken() {
        return idToken;
    }  //Firebase Uid

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
