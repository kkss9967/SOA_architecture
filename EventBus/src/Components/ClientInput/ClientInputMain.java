package Components.ClientInput;

/**
 * Copyright(c) 2018 All rights reserved by JU Consulting
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Common.EventPackage.EventId;

/**
 * 
 * @author Jungho Kim
 * @date 2018
 * @version 1.1
 * @description
 */
public class ClientInputMain {
	public static void main(String[] args) {
        ClientInputComponent clientInputComponent = new ClientInputComponent();
        
        if(clientInputComponent.registered) {
        	System.out.println("ClientInputMain (ID:" + clientInputComponent.getComponentId() + ") is successfully registered...");
            clientInputComponent.start();
            
            while(true) {
                //Menu
                System.out.println( "Registeration System\n" );
                System.out.println("1. List Students");
                System.out.println("2. List Courses");
                System.out.println("3. Register a new Student");
                System.out.println("4. Register a new Course");
                System.out.println("5. Delete Student");
                System.out.println("6. Delete Course");
                System.out.println("7. Sign Up Course");
                System.out.println("\n 0. Quit the system");
                
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                
                try {
                    String selection = br.readLine().trim();
                    
                    if(selection.equals("1")) {
                        clientInputComponent.sendClientInput(EventId.ListStudents, null);
                        
                    } else if(selection.equals("2")) {
                        clientInputComponent.sendClientInput(EventId.ListCourses, null);
                        
                    } else if(selection.equals("3")) {
                    	String msg = "";
                    	boolean inputIsDone = false;
                    	while(!inputIsDone) {
                    		System.out.println("\nEnter student ID and press return (Ex. 20131234)>> ");
                            String sSID = br.readLine().trim();
                            System.out.println("\nEnter family name and press return (Ex. Hong)>> ");
                            String firstName = br.readLine().trim();
                            System.out.println("\nEnter first name and press return (Ex. Gildong)>> ");
                            String lastName = br.readLine().trim();
                            System.out.println("\nEnter department and press return (Ex. CS)>> ");
                            String dep = br.readLine().trim();
                            System.out.println("\nEnter a list of IDs (put a space between two different IDs) of the completed courses and press return >> ");
                            System.out.println("(Ex. 17651 17652 17653 17654)");
                            String completedCourse = br.readLine().trim();
                            msg = sSID + " " + lastName + " " + firstName + " " + dep + " " + completedCourse;
                                                        
                            while(true) {
                            	System.out.println("\nIs it correct information? (Y/N)");
                                System.out.println(msg);
                                String ans = br.readLine().trim();
                                if(ans.equalsIgnoreCase("y")) {
                                	inputIsDone = true;
                                	break;
                                } else if(ans.equalsIgnoreCase("n")) {
                                	System.out.println("\nType again...");
                                	msg = "";
                                	break;
                                } else {
                                	System.out.println("\nTyped wrong answer");
                                }
                            }
                    	} 	                    	
                    	
                    	System.out.println("\n ** Sending an event(ID:" + EventId.RegisterStudents + ") with ");
                    	System.out.println("\n ** Message \"" + msg + "\" ...");
                    	clientInputComponent.sendClientInput(EventId.RegisterStudents, msg);
                    	
                    } else if(selection.equals("4")) {
                        String userInput = "";
                        boolean inputIsDone = false;
                        
                        while(!inputIsDone) {
                        	System.out.println("\nEnter course ID and press return (Ex. 12345)>> ");
                            userInput = br.readLine().trim();
                            System.out.println("\nEnter the family name of the instructor and press return (Ex. Hong)>> ");
                            userInput += " " + br.readLine().trim();
                            System.out.println("\nEnter the name of the course ( substitute a space with ab underbar(_) ) and press return (Ex. C++_Programming)>> ");
                            userInput += " " + br.readLine().trim();
                            System.out.println("\nEnter a list of IDs (put a space between two different IDs) of prerequisite courses and press return >> ");
                            System.out.println("(Ex. 12345 17651)");
                            userInput += " " + br.readLine().trim();
                            
                            while(true) {
                            	System.out.println("\nIs it correct information? (Y/N)");
                                System.out.println(userInput);
                                String ans = br.readLine().trim();
                                if(ans.equalsIgnoreCase("y")) {
                                	inputIsDone = true;
                                	break;
                                } else if(ans.equalsIgnoreCase("n")) {
                                	System.out.println("\nType again...");
                                	userInput = "";
                                	break;
                                } else {
                                	System.out.println("\nTyped wrong answer");
                                }
                            }
                        }                        
                        
                        System.out.println("\nSending an event(ID:" + EventId.RegisterCourses + ") with");
                        System.out.println("\n ** Message \"" + userInput + "\" ...");
                        clientInputComponent.sendClientInput(EventId.RegisterCourses, userInput);

                    } else if(selection.contentEquals("5")) {	// delete Students
                    	String msg = "";
                    	boolean inputIsDone = false;
                    	while(!inputIsDone) {
                    		System.out.println("\nEnter student ID to Delete and press return (Ex. 20131234)>> ");
                            msg = br.readLine().trim();
                            
                            while(true) {
                            	System.out.println("\nIs it correct information? (Y/N)");
                                System.out.println(msg);
                                String ans = br.readLine().trim();
                                if(ans.equalsIgnoreCase("y")) {
                                	inputIsDone = true;
                                	break;
                                } else if(ans.equalsIgnoreCase("n")) {
                                	System.out.println("\nType again...");
                                	msg = "";
                                	break;
                                } else {
                                	System.out.println("\nTyped wrong answer");
                                }
                            }
                    	}
                     	
                    	System.out.println("\n ** Sending an event(ID:" + EventId.DeleteStudents + ") with ");
                    	System.out.println("\n ** Message \"" + msg + "\" ...");
                    	clientInputComponent.sendClientInput(EventId.DeleteStudents, msg);
                    		
                    } else if(selection.contentEquals("6")) {	// delete Courses
                        String userInput = "";
                        boolean inputIsDone = false;
                        
                        while(!inputIsDone) {
                        	System.out.println("\nEnter course ID to Delete and press return (Ex. 12345)>> ");
                            userInput = br.readLine().trim();
                            
                            while(true) {
                            	System.out.println("\nIs it correct information? (Y/N)");
                                System.out.println(userInput);
                                String ans = br.readLine().trim();
                                if(ans.equalsIgnoreCase("y")) {
                                	inputIsDone = true;
                                	break;
                                } else if(ans.equalsIgnoreCase("n")) {
                                	System.out.println("\nType again...");
                                	userInput = "";
                                	break;
                                } else {
                                	System.out.println("\nTyped wrong answer");
                                }
                            }
                        }                        
                        
                        System.out.println("\nSending an event(ID:" + EventId.DeleteCourses + ") with");
                        System.out.println("\n ** Message \"" + userInput + "\" ...");
                        clientInputComponent.sendClientInput(EventId.DeleteCourses, userInput);

                    } else if(selection.contentEquals("7")) {	// SignUpCheck
                        String userInput = "";
                        boolean inputIsDone = false;
                        
                        while(!inputIsDone) {
                        	System.out.println("\nEnter student ID and press return (Ex. 20150095)>> ");
                            userInput = br.readLine().trim();
                            System.out.println("\nEnter course ID to signUp and press return (ex. 12345)");
                            userInput += " " + br.readLine().trim();
                            // 20150010 12345
                            
                            while(true) {
                            	System.out.println("\nIs it correct information? (Y/N)");
                                System.out.println(userInput);
                                String ans = br.readLine().trim();
                                if(ans.equalsIgnoreCase("y")) {
                                	inputIsDone = true;
                                	break;
                                } else if(ans.equalsIgnoreCase("n")) {
                                	System.out.println("\nType again...");
                                	userInput = "";
                                	break;
                                } else {
                                	System.out.println("\nTyped wrong answer");
                                }
                            }
                        }                        
                        
                        System.out.println("\nSending an event(ID:" + EventId.SignUpCheck + ") with");
                        System.out.println("\n ** Message \"" + userInput + "\" ...");
                        clientInputComponent.sendClientInput(EventId.SignUpCheck, userInput);

                    }
                    
                    else if(selection.equals("0")) {
                    	System.out.println("\nSending an event(ID:" + EventId.QuitTheSystem + ")...");
                        clientInputComponent.sendClientInput(EventId.QuitTheSystem, null);
                        break;
                    }
               
                } catch (IOException e) {
                    e.printStackTrace();
                }                
            }
        }
}

}
