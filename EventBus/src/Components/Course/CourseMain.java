package Components.Course;

/**
 * Copyright(c) 2018 All rights reserved by JU Consulting
 */

import Common.EventPackage.Event;
import Common.EventPackage.EventBusUtil;
import Common.EventPackage.EventId;
import Common.EventPackage.EventQueue;
import Components.Student.Student;

/**
 * @author Jungho Kim
 * @date 2018
 * @version 1.1
 * @description
 */
public class CourseMain {
	public static void main(String[] args) {
		EventBusUtil eventBusInterface = new EventBusUtil();
		Event event = null;
		EventQueue eventQueue = null;
		boolean done = false;
		CourseComponent coursesList = null;

		if (eventBusInterface.getComponentId() != -1) {
			System.out.println(
					"CourseMain (ID:" + eventBusInterface.getComponentId() + ") is successfully registered...");
		} else {
			System.out.println("CourseMain is failed to register. Restart this component.");
		}

		try {
			coursesList = new CourseComponent("Courses.txt");
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

				if (event.getEventId() == EventId.ListCourses) {
					String returnString = "";
					for (int j = 0; j < coursesList.vCourse.size(); j++) {
						returnString += ((Course) coursesList.vCourse.get(j)).toString() + "\n";
					}

					System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
					System.out.println("\n ** Message: \n" + returnString + "\n  ...");
					eventBusInterface.sendEvent(new Event(EventId.ClientOutput, returnString));

				} else if (event.getEventId() == EventId.RegisterCourses) {
					String courseInfo = event.getMessage();
					Course course = new Course(courseInfo);
					if (coursesList.isRegisteredCourse(course.courseId) == false) {
						coursesList.vCourse.add(new Course(courseInfo));
						System.out.println("A new course is successfully added...");
						System.out.println("\"" + courseInfo + "\"");
					} else {
						System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
						System.out.println("\n ** Message: This course is already registered.  ...");
						eventBusInterface
								.sendEvent(new Event(EventId.ClientOutput, "This course is already registered."));
					}

				} else if (event.getEventId() == EventId.DeleteCourses) {
					String courseInfo = event.getMessage();
					if (coursesList.isRegisteredCourse(courseInfo) == true) { // course 있는지 체
				  		for(Object c : coursesList.vCourse) {
				  			Course course = (Course) c;
				  			int courseIndex = coursesList.vCourse.indexOf(course);
                			if(course.match(courseInfo)) {
                				coursesList.vCourse.remove(courseIndex);
                				break;
                			}
                		}
						System.out.println("Course is successfully deleted...");
						System.out.println("\"" + courseInfo + "\"");

					} else {
						System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
						System.out.println("\n ** Message: This course is not registered.  ...");
						eventBusInterface.sendEvent(new Event(EventId.ClientOutput, "This course is not registered."));
					}

				} else if (event.getEventId() == EventId.SignUpCheck) {
					String courseInfo = event.getMessage().substring(9, 14);		// 20150010 12345
					///
					String studentInfo = event.getMessage().substring(0, 8);
					///
					String prerequisiteCourse = "";
					String returnString = "";
					if (coursesList.isRegisteredCourse(courseInfo) == true) { // course 있는지 체
				  		for(Object c : coursesList.vCourse) {
				  			Course course = (Course) c;
                			if(course.match(courseInfo)) {	// course 형 17651 Kim Models_of_Software_Systems 12345 (코스번호, 교수명, 과목이름, 선수과목) 이니까 선수과목 보내면 ok
            					for(int j=0; j<course.prerequisiteCoursesList.size(); j++) {
            						prerequisiteCourse += " " + course.prerequisiteCoursesList.get(j).toString();
            					}
            					returnString = studentInfo + " " + course.courseId + prerequisiteCourse;
            					System.out.println(returnString.toString());
                			}
                		}
						System.out.println("Course check completed...");
						System.out.println("\n ** Sending an event(ID:" + EventId.SignUpCourse + ") with");
                        System.out.println("\n ** Message \"" + returnString + "\" ...");
						eventBusInterface.sendEvent(new Event(EventId.SignUpCourse, returnString));
						
					} else {
						System.out.println("\n ** Sending an event(ID:" + EventId.ClientOutput + ") with");
						System.out.println("\n ** Message: This course is not registered... : " + courseInfo );
						eventBusInterface.sendEvent(new Event(EventId.ClientOutput, "This course is not registered."));
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
