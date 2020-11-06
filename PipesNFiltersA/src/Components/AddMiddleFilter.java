package Components;

import Framework.GeneralFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class AddMiddleFilter extends GeneralFilter {
    int inputPort, outputPort;
    public AddMiddleFilter(int inputPort, int outputPort) {
        super(inputPort, outputPort);
        this.inputPort = inputPort; this.outputPort = outputPort;
        // 생성할 때 사용된 inputPort와 outputPort를 사용하여 필터 구분.
    }

    @Override
    public void specificComputationForFilter() throws IOException {
        Vector<String> temp = new Vector<>();   // 바이트로 읽은 값을 저장해주기 위해 벡터 생성
        String Java = "12345";
        String C_PP = "23456";
        String SW = "17651";
        // 과목 정보를 체크하기 위해 변수를 생성한다.

        int mid_read = 0;   // 한 바이트씩 읽기 위해 변수 생성
        int numOfBlank = 0; // 공백 갯수 구분 위해 변수 생성

        StringBuilder check_line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        // StringBuilder를 사용하면 오버헤드가 줄어든다.

        final int ENTER = 13;
        final int SPACE = 32;
        // 아래 코드에서 구분할 때 사용할 아스키코드 설정

        while(true) {
            while (mid_read != '\n') {  // 이전 설명한 클래스와 형식이 같으므로 설명 생략.
                mid_read = in.get(outputPort).read();
                if (mid_read != ENTER) {
                    if (mid_read == -1) return;
                    else if(mid_read == ' ') numOfBlank++;
                    word.append((char) mid_read);
                }

                if (numOfBlank >= 4) check_line.append((char) mid_read);
                // 공백 갯수가 4 이상일 때, 바이트를 읽어 check_line에 저장한다. (과목 정보만 저장하게 된다)

                if (mid_read == ENTER) {
                    String[] split = word.toString().split(" ");
                    Collections.addAll(temp, split);
                    // word는 텍스트 파일의 한 줄이다. " "에 따라 split 하여 temp 벡터에 저장한다.

                    if (!check_line.toString().contains(Java)) { temp.add(Java);}
                    if (!check_line.toString().contains(C_PP)) { temp.add(C_PP);}
                    if (!check_line.toString().contains(SW)) {temp.add(SW);}
                    // 학생이 수강한 과목에 각각 Java, C_PP, SW가 존재하지 않으면 벡터에 추가해준다.
                    for (String s : temp) { // temp 만큼 반복
                        out.get(inputPort).write(s.getBytes()); // 인풋포트에 값을 전달한다.
                        out.get(inputPort).write(SPACE);
                        // 벡터에 " "가 제거된 상태로 저장되어있으므로, 벡터 값 한 개를 전달한 뒤 공백을 써준다.
                    }
                    check_line = new StringBuilder();
                    word = new StringBuilder();
                    temp.clear();
                }
            }
            numOfBlank = 0;
            mid_read = '\0';
        }
    }
}