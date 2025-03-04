package com.example.cumulus.DTOs;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "userDetails")
public class UserDetails {

        private String firstName;
        private String surname;
        private String email;
        private String phoneNumber;
        private String password;
        private String retypePassword;
        private String country;
        private String usage;

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getSurname() {
                return surname;
        }

        public void setSurname(String surname) {
                this.surname = surname;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPhoneNumber() {
                return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getRetypePassword() {
                return retypePassword;
        }

        public void setRetypePassword(String retypePassword) {
                this.retypePassword = retypePassword;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        public String getUsage() {
                return usage;
        }

        public void setUsage(String usage) {
                this.usage = usage;
        }

        @Override
        public String toString() {
                return "UserDetails{" +
                        "firstName='" + firstName + '\'' +
                        ", surname='" + surname + '\'' +
                        ", email='" + email + '\'' +
                        ", phoneNumber='" + phoneNumber + '\'' +
                        ", password='" + password + '\'' +
                        ", retypePassword='" + retypePassword + '\'' +
                        ", country='" + country + '\'' +
                        ", usage='" + usage + '\'' +
                        '}';
        }
}
