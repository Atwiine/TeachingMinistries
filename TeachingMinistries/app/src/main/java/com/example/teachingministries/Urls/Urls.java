package com.example.teachingministries.Urls;

public class Urls {
    public String https = "http://192.168.1.105/alpha/";
//    public String https = "https://teachingministriesapp.com/alpha/";


    public String LOGIN_URL = https + "login.php";///g
    public String FIRSTREG_URL = https + "register.php";///g
    public String STUDENTREG_URL = https + "student_register.php";///g
    public String SEND_ATTENDED = https + "send_attended.php";///g
    public String SEND_MISSED = https + "send_missed.php";///g
    public String ATTENDED_STUDENTS = https + "load_attended.php";///g
    public String MISSED_STUDENTS = https + "load_missed.php";///g
    public String PASS_ATTENDED_TO_NEXT = https + "pass_students.php";///g
    public String LOAD_REGISTERED_STUDENTS = https + "registered_students.php";///g
    public String USERDETS_URL = https + "read_user.php";///g
    public String EDITACCOUNT = https + "edit_user.php";///g
    public String STUDENTDETS_URL = https + "read_student.php";///g
    public String EDITSTUDENTACCOUNT = https + "edit_student.php";///g
    public String TRACK_REGISTERED_STUDENTS = https + "track_registered_students.php";///g
    public String PASS_ATTENDED_STUDENTS_GRADUATION = https + "send_attended_grad_class.php";///g

    public String SEND_REMARKS = https + "send_remarks.php";//
    public String LOAD_RECEIVED_STUDENTS = https + "load_received.php";//

    public String LOAD_GRADUATED_STUDENTS = https + "load_graduated.php";///with a4 the attendance shld remain the same but only add the graduated colum
    //to yes so that wn reading the grad students we check those whose colmns are yes and then mark again and change the columns to yes for baptised
    public String SEND_BAPTISED = https + "send_baptised.php";
    public String SEND_NOT_BAPTISED = https + "send_not_baptised.php";
    public String LOAD_BAPTISED_STUDENTS = https + "load_baptised.php";///with a4 the attendance shld remain the same but only add the graduated colum
    public String LOAD_TBBAPTISED_STUDENTS = https + "load_tb_baptised.php";///with a4 the attendance shld remain the same but only add the graduated colum
    public String SEND_ATTENDEA4 = https + "send_attended_a4.php";///g
    public String SEND_MISSEDA4 = https + "send_missed_a4.php";///g


    //export track attendance
    public String EXPORT_TRACK_STUDENTS_4 = https + "export_track_students.php";///g
    /*download track attendance*/
    public String download_track4 = https + "/excel_files/track/a-4/Track-attendance-alpha-4.xls";///g
    public String download_track3 = https + "/excel_files/track/a-4/Track-attendance-alpha-3.xls";///g
    public String download_track2 = https + "/excel_files/track/a-4/Track-attendance-alpha-2.xls";///g
    public String download_track1 = https + "/excel_files/track/a-4/Track-attendance-alpha-1.xls";///g

    //export to be baptised
    public String EXPORT_TO_BE_BAPTISED = https + "export_to_be_baptised.php";///g
    public String download_tobebaptised = https + "/excel_files/graduation/to_be_baptised/To-be-baptised-alpha-4.xls";///g

    //export to be graduation
    public String EXPORT_GRADUATION = https + "export_graduation.php";///g
    public String download_graduation = https + "/excel_files/graduation/graduated/graduation-class-alpha-4.xls";///g

    //export to be baptised
    public String EXPORT_BAPTISED = https + "export_baptised.php";///g
    public String download_baptised = https + "/excel_files/graduation/baptised/Baptised-class-alpha-4.xls";///g

    //export to be missed
    public String EXPORT_MISSED_STUDENTS = https + "export_missed.php";///g
    public String download_missed1 = https + "/excel_files/Registration/missed_partcipants/Missed-participants-alpha-1.xls";///g
    public String download_missed2 = https + "/excel_files/Registration/missed_partcipants/Missed-participants-alpha-2.xls";///g
    public String download_missed3 = https + "/excel_files/Registration/missed_partcipants/Missed-participants-alpha-3.xls";///g
    public String download_missed4 = https + "/excel_files/Registration/missed_partcipants/Missed-participants-alpha-4.xls";///g

    //export to be registered_students
    public String EXPORT_REGISTERED_STUDENTS = https + "export_registered_students.php";///g
    public String download_registered_students = https + "/excel_files/Registration/registered_particpants/Registered-participants-alpha-1.xls";///g

    //export to be passed_students
    public String EXPORT_PASSED_STUDENTS = https + "export_passed.php";///g
    public String download_passed_students1 = https + "/excel_files/Registration/passed_participants/Passed-participants-alpha-1.xls";///g
    public String download_passed_students2 = https + "/excel_files/Registration/passed_participants/Passed-participants-alpha-2.xls";///g
    public String download_passed_students3 = https + "/excel_files/Registration/passed_participants/Passed-participants-alpha-3.xls";///g
    public String download_passed_students4 = https + "/excel_files/Registration/passed_participants/Passed-participants-alpha-4.xls";///g

    // totals
    public String TOTAL_TRACK_STUDENTS = https + "total_registered_students.php";///g
    public String TOTAL_RECEIVED_STUDENTS = https + "total_received.php";///g
    public String TOTAL_MISSED_STUDENTS = https + "total_missed.php";///g
    public String TOTAL_ATTENDED_STUDENTS = https + "total_attended.php";///g
    public String TOTAL_REGISTERED_STUDENTS = https + "totals_registered_students.php";///g
    public String TOTAL_TBBAPTISED_STUDENTS = https + "total_tb_baptised.php";///
    public String TOTAL_GRADUATED_STUDENTS = https + "total_graduated.php";///
    public String TOTAL_BAPTISED_STUDENTS = https + "total_baptised.php";///


}
