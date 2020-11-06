package Components;

import Framework.GeneralFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class DeleteFilter extends GeneralFilter {
    int inputPort, outputPort;
    public DeleteFilter(int inputPort, int outputPort) {
        super(inputPort, outputPort);
        this.inputPort = inputPort; this.outputPort = outputPort;
    }

    @Override
    public void specificComputationForFilter() throws IOException {
        Vector<String> temp = new Vector<>();
        String MSS = "17651";
        String SW_Dev = "17652";

        int mid_read = 0;
        int numOfBlank = 0;

        StringBuilder check_line = new StringBuilder();
        StringBuilder check_Year = new StringBuilder();
        StringBuilder word = new StringBuilder();

        final int ENTER = 13;
        final int SPACE = 32;

        while(true) {
            while (mid_read != '\n') {
                mid_read = in.get(outputPort).read();
                if (mid_read != ENTER) {
                    if (mid_read == -1) return;
                    else if (mid_read == ' ') numOfBlank++;
                    word.append((char) mid_read);
                }

                if (numOfBlank >= 4) check_line.append((char) mid_read);
                else if (numOfBlank < 1) check_Year.append((char) mid_read);

                if (mid_read == ENTER || mid_read == 10) {
                    String[] split = word.toString().split(" ");
                    Collections.addAll(temp, split);

                    for (int i = 0; i < temp.size(); i++) {
                        if (check_Year.toString().contains("2013")) {
                            if (check_line.toString().contains(MSS)) temp.remove(MSS);
                            if (check_line.toString().contains(SW_Dev)) temp.remove(SW_Dev);
                        }
                    }
                    for (String s : temp) {
                        out.get(inputPort).write(s.getBytes());
                        out.get(inputPort).write(SPACE);
                    }
                    check_line = new StringBuilder();
                    check_Year = new StringBuilder();
                    word = new StringBuilder();
                    temp.clear();
                }
            }
            numOfBlank = 0;
            mid_read = '\0';
        }
    }
}