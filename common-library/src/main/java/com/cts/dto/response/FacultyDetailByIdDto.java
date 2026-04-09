package com.cts.dto.response;


import lombok.Setter;

@Setter
public class FacultyDetailByIdDto {
    private  String userName;
    private  String userEmail;
    private  Long phoneNumber;
    private  String facultyGender;
    private  int facultyYearOfExperience;
    private  String facultyAddress;
    private  double facultyRating;
}
