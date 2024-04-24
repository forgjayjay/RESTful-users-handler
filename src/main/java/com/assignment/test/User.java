package com.assignment.test;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class User {
    @NotNull(message = "Birth date is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    @Email(message = "Email is not valid", regexp = "^[\\w\\.-]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotNull(message = "Email is mandatory")
    String email;
    @NotNull(message = "First Name is mandatory")
    String f_name;
    @NotNull(message = "Last Name is mandatory")
    String l_name;
    
    String address;
    long phone;

    
    public User(
            Date birthday,
            String email,
            String f_name,
            String l_name,
            String address,
            long phone
        ){
            this.birthday=birthday;
            this.email=email;
            this.f_name=f_name;
            this.l_name=l_name;
            this.address=address;
            this.phone=phone;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getF_name() {
        return this.f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return this.l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhone() {
        return this.phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthday, email, f_name, l_name, address, phone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return phone == user.phone &&
                Objects.equals(birthday, user.birthday) &&
                Objects.equals(email, user.email) &&
                Objects.equals(f_name, user.f_name) &&
                Objects.equals(l_name, user.l_name) &&
                Objects.equals(address, user.address);
    }

    @Override
    public String toString() {
        return "User{" +
                "birthday=" + birthday +
                ", email='" + email + '\'' +
                ", f_name='" + f_name + '\'' +
                ", l_name='" + l_name + '\'' +
                ", address='" + address + '\'' +
                ", phone=" + phone +
                '}';
    }

}
