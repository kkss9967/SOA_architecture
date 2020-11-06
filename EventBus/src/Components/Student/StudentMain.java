package Components.Student;
/**
 * Copyright(c) 2018 All rights reserved by JU Consulting
 */

import Common.EventPackage.Event;
import Common.EventPackage.EventBusUtil;
import Common.EventPackage.EventId;
import Common.EventPackage.EventQueue;


/**
 * @author Jungho Kim
 * @version 1.1
 * @date 2018
 * @description
 */
public class StudentMain {
    public static void main(String args[]) {
        EventBusUtil eventBusInterface = new EventBusUtil();
        Event event = null;
        EventQueue eventQueue = null;
        boolean done = false;
        StudentComponent studentsList = null;
        String forSignUpStudentId = "";

        if (eventBusInterface.getComponentId() != -1) {
            System.out.println("StudentMain (ID:" + eventBusInterface.getComponentId() + ") is successfully registered...");
        } else {
            System.out.println("StudentMain is failed to register. Restart this component.");
        }

        try {
            studentsList = new StudentComponent("Students.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!done) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eventQueue = eventBusInterface.getEventQueue();

            for (int i = 0; i < eventQueue.getSize(); i++) {
                event = eventQueue.getEvent();
                System.out.println("Received an event(ID: " + event.getEventId() + ")...");

                if (event.getEventId() == EventId.ListStudents) {
                    String returnString = "";
                    for (int j = 0; j < studentsList.vStudent.size(); j++) {
                        returnString += (j == 0 ? "" : "\n") + ((Student) studentsList.vStudent.get(j)).toString();
                    }

                    System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
                    System.out.println("\n ** Message: \n" + returnString + "\n  ...");
                    eventBusInterface.sendEvent(new Event(EventId.ClientOutput, returnString));
                } else if (event.getEventId() == EventId.RegisterStudents) {
                    String studentInfo = event.getMessage();
                    System.out.println("Not null");
                    Student student = new Student(studentInfo);
                    if (studentsList.isRegisteredStudent(student.studentId) == false) {
                        studentsList.vStudent.add(new Student(studentInfo));
                        System.out.println("A new student is successfully added...");
                        System.out.println("\"" + studentInfo + "\"");
                    } else {
                        System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
                        System.out.println("\n ** Message: This student is already registered.  ...");
                        eventBusInterface.sendEvent(new Event(EventId.ClientOutput, "This student is already registered."));
                    }

                } else if (event.getEventId() == EventId.DeleteStudents) {
                    String studentInfo = event.getMessage();
                    System.out.println("Not null");
                    if (studentsList.isRegisteredStudent(studentInfo) == true) {
                        for (Object s : studentsList.vStudent) {
                            Student student = (Student) s;
                            int studentIndex = studentsList.vStudent.indexOf(student);
                            if (student.match(studentInfo)) {
                                studentsList.vStudent.remove(studentIndex);
                                break;
                            }
                        }
                        System.out.println("Student is successfully deleted...");
                        System.out.println("\"" + studentInfo + "\"");
                    } else {
                        System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
                        System.out.println("\n ** Message: This student is not registered.  ...");
                        eventBusInterface.sendEvent(new Event(EventId.ClientOutput, "This student is not registered."));
                    }

//                } else if(event.getEventId() == EventId.SignUpCheck){
//            		String studentInfo = event.getMessage().substring(0, 7);	// 20150010 12345
//        			if(studentsList.isRegisteredStudent(studentInfo) == true){	// ??? ????
//                		for(Object s : studentsList.vStudent) {
//                			Student student = (Student) s;
//                			if(student.match(studentInfo)) {	// student ??? ???
//                				forSignUpStudentId = student.studentId;	// 20150010
//                			}
//                		}
//                		System.out.println("Student check completed...");
//                	} else{
//                		System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
//                        System.out.println("\n ** Message: This student is not registered.  ...");
//                        eventBusInterface.sendEvent(new Event(EventId.ClientOutput, "This student is not registered."));
//                	}

                } else if (event.getEventId() == EventId.SignUpCourse) {
                    String courseInfo = event.getMessage();
                    String studentId = courseInfo.substring(0, 8);
                    String courseId = courseInfo.substring(9, 14);
                    String preCourse = "";
                    if (courseInfo.length() > 15) {
                        preCourse = courseInfo.substring(15, courseInfo.length());
                    }
                    if (studentsList.isRegisteredStudent(studentId)) {
                        for (Object s : studentsList.vStudent) {
                            Student student = (Student) s;
                            if (student.match(studentId)) {
                                if (student.completedCoursesList.contains(preCourse) || preCourse == "") {
                                    student.completedCoursesList.add(courseId);
                                    System.out.println("Sign Up completed...");
                                    System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
                                    System.out.println("\"" + student.toString() + "\"");
                                    eventBusInterface.sendEvent(new Event(EventId.ClientOutput, student.toString()));
                                    break;
                                } else {
                                    System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
                                    System.out.println("\n ** Message: This student -" + studentId + "- not completed Course : " + preCourse);
                                    eventBusInterface.sendEvent(new Event(EventId.ClientOutput, "This student not completed preCourse"));
                                    break;
                                }
                            }
                        }
                    }else{
                        System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
                        System.out.println("\n ** Message: This student is not registered.  ...");
                        eventBusInterface.sendEvent(new Event(EventId.ClientOutput, "This student is not registered."));
                    }

                } else if (event.getEventId() == EventId.QuitTheSystem) {
                    eventBusInterface.unRegister();
                    done = true;
                }
            }
        }
        System.out.println("Shut down the component....");
    }
}
