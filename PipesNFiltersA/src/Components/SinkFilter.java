package Components; /**
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */

import Framework.GeneralFilter;

import java.io.FileWriter;
import java.io.IOException;
/**
 * Copyright(c) 2013 All rights reserved by JU Consulting
 */
/**
 * @author Jungho Kim
 * @date 2019
 * @version 1.1
 * @description
 * file을 내리기만 하는 역할
 */
public class SinkFilter extends GeneralFilter {
    private String filePath;
    int inputPort, outputPort;
    public SinkFilter(String outputFilePath, int inputPort, int outputPort) {
        super(inputPort, outputPort);
        this.filePath = outputFilePath; this.inputPort = inputPort; this.outputPort = outputPort;
    }

    @Override
    public void specificComputationForFilter() throws IOException {
        int databyte;
        try {
            FileWriter fw = new FileWriter(this.filePath);
            while(true) {
                databyte = in.get(outputPort).read();
                if(databyte == -1){
                    fw.close();
                    System.out.print( "::Filtering is finished; Output file is created." );
                    return;
                }
                fw.write(databyte);
            }   
        } catch (Exception e) {
            closePorts();
            e.printStackTrace();
            
        }
    }

}