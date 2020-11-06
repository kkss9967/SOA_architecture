package Components;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

import Framework.GeneralFilter;

public class CheckCoursesFilter extends GeneralFilter {
    int inputPort, outputPort;
    public CheckCoursesFilter(int inputPort, int outputPort) {
        super(inputPort, outputPort);
        this.inputPort = inputPort; this.outputPort = outputPort;
    }

    @Override
    public void specificComputationForFilter() throws IOException {
        Vector<String> course = new Vector<String>();   // 과목 정보를 저장하기 위한 벡터 생성.
        Vector<String> temp = new Vector<String>();     // 텍스트 한 줄을 읽은 뒤 저장했다가 전달할 때 사용하는 벡터.
        Vector<String> student_1 = new Vector<String>();    // 선수과목 요건을 충족한 학생을 저장하는 벡터.
        Vector<String> student_2 = new Vector<String>();    // 선수과목 요건을 충족하지 못한 학생을 저장하는 벡터.

        int course_read = 0;
        int stu_read = 0;
        int numOfBlank = 0;

        StringBuilder check_line = new StringBuilder();
        StringBuilder cword = new StringBuilder();
        StringBuilder sWord = new StringBuilder();

        final int ENTER = 13;
        final int SPACE = 32;

        while (true) {
            while (course_read != '\n') {
                course_read = in.get(1).read();
                // 같은 클래스로부터 다른 포트넘버로 다른 정보가 들어오기 때문에, 포트넘버를 구분하여 연결해주어야 한다.
                // 여기에서는 1번 포트에서 값을 읽어온다.

                if (course_read != ENTER) {
                    if (course_read == -1) break;
                    else if (course_read == ' ') numOfBlank++;
                    else if (course_read >= 48 && course_read <= 57) {
                        // 48과 57은 아스키코드에서 숫자 범위에 해당한다.
                        cword.append((char) course_read);
                    }
                }
                if (course_read == ENTER) {
                    if (cword.length() > 5) {   // 선수과목에 해당하는 정보를 course에 저장한다.
                        course.add(cword.toString());
                    }
                    cword = new StringBuilder();
                }
            }
            numOfBlank = 0; // 사용한 변수 초기화.

            while (stu_read != '\n') {
                stu_read = in.get(0).read(); // 0번 포트를 통해 값을 읽는다.
                if (stu_read != ENTER) {
                    if (stu_read == -1) { return;
                    } else if (stu_read == ' ') numOfBlank++;
                    sWord.append((char) stu_read);
                    if (numOfBlank >= 4) check_line.append((char) stu_read);
                    // 학생 이수 과목 check_line에 저장
                } else {
                    String[] split = sWord.toString().split(" ");
                    if (split.length > 0) Collections.addAll(temp, split);
                    boolean checked = true; // 선수과목 요건에 충족했는지 확인하기 위한 boolean을 생성한다.
                    for (int i = 0; i < course.size(); i++) {
                        String courseId = course.elementAt(i).substring(0, 5);  // 과목id
                        String forCheck = course.elementAt(i).substring(5); // 선수과목
                        if (check_line.toString().contains(courseId)) { // 학생이 수강한 과목 중 과목id가 존재하는 경우
                            if (courseId.equals("17655")) { // 과목 '17655'는 선수과목이 두 개인 특수한 상황이라 따로 조건을 걸어주었다.
                                String preCheck1 = forCheck.substring(0, 5);    // 첫 번째 선수과목
                                String preCheck2 = forCheck.substring(5);       // 두 번째 선수과목
                                if (check_line.toString().contains(preCheck1) && check_line.toString().contains(preCheck2)) {
                                    student_1.addAll(temp); // 선수과목 요건에 충족하는 경우 student_1 벡터에 저장한다.
                                } else {
                                    student_2.addAll(temp); // 선수과목 요건을 충족하지 못하는 경우 student_2  벡터에 저장한다.
                                }
                                checked = false;    // 이미 벡터에 저장이 되었으므로 중복 저장을 막기 위해 false로 바꾼다.
                                break;  // for문을 빠져나간다.
                            } else if (!forCheck.equals(" ")) { // 선수과목이 없을 수 있으므로, if문을 통해 걸러준다.
                                if (!check_line.toString().contains(forCheck)) {    // 학생이 선수과목을 수강하지 않았을 경우
                                    student_2.addAll(temp); // student_2 벡터에 저장한다.
                                    checked = false;    // boolean값을 false로 바꾼다.
                                    break;
                                }
                            } else {
                                break;  // 선수과목이 없는 경우 for문을 빠져나간다.
                            }
                        }
                    }
                    if (checked) student_1.addAll(temp);
                    // checked가 true인 경우 선수과목 요건을 모두 충족한 것이므로 student_1 벡터에 저장해준다.

                    if (student_1.size() > 1) { // Enter로 라인을 구분해주기 위한 if문이다.
                        for (String stu1 : student_1) {
                            out.get(0).write(stu1.getBytes());
                            out.get(0).write(SPACE);
                        }
                        out.get(0).write(ENTER);    // 0번 포트에 값을 적는다.
                    }
                    if (student_2.size() > 1) { // Enter로 라인을 구분해주기 위한 if문이다.
                        for (String stu2 : student_2) {
                            out.get(1).write(stu2.getBytes());
                            out.get(1).write(SPACE);
                        }
                        out.get(1).write(ENTER);    // 1번 포트에 값을 적는다.
                    }
                    check_line = new StringBuilder();
                    sWord = new StringBuilder();
                    student_1.clear();
                    student_2.clear();
                    temp.clear();
                }
            }
            numOfBlank = 0;
            stu_read = '\0';
            course_read = '\0';
        }
    }

}