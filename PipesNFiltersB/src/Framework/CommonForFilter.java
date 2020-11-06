package Framework;
/**
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
 *      모든 Filter ?��?�� 공유?��?�� Interface. Runnable?�� extend ?��?�� 모든 Filter?��?? 기본?��?���? Thread 기반?���? ?��?��감을 보이�?, Filter간의 ?��?��?�� ?��?�� 기본?��?�� connect 메소?��?��?�� ?��?��?��.
 */
public interface CommonForFilter extends Runnable{
    /**
     * Filter?�� Output port�? ?��?�� Filter�? ?��결한?��.
     * @param filter
     * @throws IOException
     */
    public void connectOutputTo(GeneralFilter filter, int inputPort, int outputPort) throws  IOException;

    /**
     * Filter?�� Input port�? 바로 ?�� Filter�? ?��결한?��.
     * @param filter
     * @throws IOException
     */
    public void connectInputTo(GeneralFilter filter, int inputPort, int outputPort) throws IOException;

    /**
     * Pipe and Filter pattern?��?�� Pipe?�� ?��?��?��?�� Input stream?�� 반환?��?��.
     * @return PipedInputStream
     */
    public Vector<PipedInputStream> getPipedInputStream();
    
    /**
     * Pipe and Filter pattern?��?�� Pipe?�� ?��?��?��?�� Output stream?�� 반환?��?��.
     * @return PipedOutputStream
     */
    public Vector<PipedOutputStream> getPipedOutputStream();
}
