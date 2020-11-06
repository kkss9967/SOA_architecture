package Framework; /**
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Vector;


/**
 * @author Jungho Kim
 * @date 2019
 * @version 1.1
 * @description
 *      모든 Filter 들이 공유하는 Interface. Runnable을 extend 해서 모든 Filter들은 기본적으로 Thread 기반으로 돌아감을 보이고, Filter간의 소통을 위한 기본적인 connect 메소드들을 정의함.
 */
public interface CommonForFilter extends Runnable{
    /**
     * Filter의 Output port를 다음 Filter로 연결한다.
     * @param filter
     * @throws IOException
     */

    public void connectOutputTo(GeneralFilter filter, int inputPort, int outputPort) throws  IOException;
    
    /**
     * Filter의 Input port를 바로 전 Filter로 연결한다.
     * @param filter
     * @throws IOException
     */
    public void connectInputTo(GeneralFilter filter, int inputPort, int outputPort) throws IOException;
    
    /**
     * Pipe and Filter pattern에서 Pipe에 해당하는 Input stream을 반환한다.
     * @return PipedInputStream
     */
    public Vector<PipedInputStream> getPipedInputStream();
    
    /**
     * Pipe and Filter pattern에서 Pipe에 해당하는 Output stream을 반환한다.
     * @return PipedOutputStream
     */
    public Vector<PipedOutputStream> getPipedOutputStream();
}
