package Components; /**
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */

import Framework.GeneralFilter;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Copyright(c) 2013 All rights reserved by JU Consulting
 */


/**
 * @author Jungho Kim
 * @date 2019
 * @version 1.1
 * @description
 */
public class SourceFilter extends GeneralFilter {
    private String filePath;
    int inputPort, outputPort;
    public SourceFilter(String inputFilePath, int inputPort, int outputPort){
        super(inputPort, outputPort); this.filePath = inputFilePath;
        this.inputPort = inputPort; this.outputPort = outputPort;
        // 파일 경로 저장, 생성할 때 사용된 inputPort와 outputPort를 사용하여 필터 구분.
    }    

    @Override
    public void specificComputationForFilter() throws IOException {
        int byte_read;
        try {
            @SuppressWarnings("resource")
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(new File(filePath)));
            for(;;) {
                byte_read = br.read();
                if (byte_read == -1) { return; }
                out.get(inputPort).write(byte_read);
            }
        } 
        catch (IOException e) {
            if (e instanceof EOFException) return;
            else System.out.println(e);
        }
        finally {
        	try {out.get(inputPort).close();}
        	catch (IOException e) {e.printStackTrace();} }
    }
}
