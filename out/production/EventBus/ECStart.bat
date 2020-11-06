%ECHO OFF
%ECHO Starting ECS System
PAUSE
%ECHO ECS Monitoring Output Console
START "COURSE REGISTRATION SYSTEM OUTPUT CONSOLE" /NORMAL java Components.ClientInput.ClientOutputMain
%ECHO ECS Monitoring Input Console
START "COURSE REGISTRATION SYSTEM INPUT CONSOLE" /NORMAL java Components.ClientOutput.ClientInputMain
%ECHO Starting Student Component Function Console
START "STUDENT COMPONENT FUNCTION CONSOLE" /NORMAL java Components.Student.StudentMain
%ECHO Starting Course Component Function Console
START "COURSE COMPONENT FUNCTION CONSOLE" /NORMAL java Components.Course.CourseMain