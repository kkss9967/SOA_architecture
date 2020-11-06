package Framework; /**
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Vector;


/**
 * @author Jungho Kim
 * @date 2019
 * @version 1.1
 * @description
 *      Framework.CommonForFilter 인터페이스를 구현한 추상클래스. 모든 Filter는 본 Filter를 상속받아 구현하게 된다.
 */
public abstract class GeneralFilter implements CommonForFilter{
    protected Vector<PipedInputStream> in = new Vector<>();
    protected Vector<PipedOutputStream> out = new Vector<>();
    // 입출력이 여러개일 경우를 대비하여 Vector 사용한다.

    /**********Implementation of public methods defined in Framework.CommonForFilter interface************/
    public GeneralFilter(int inputPort, int outputPort){    // 들어오는 포트 개수, 나가는 포트 개수를 받아온다.
        for(int i=0; i<=inputPort; i++){
            in.add(new PipedInputStream());
        }
        for(int j=0; j<=outputPort; j++){
            out.add(new PipedOutputStream());
        }
        // 반복문을 돌려 포트의 개수 만큼 Pipe를 생성한다.
    }
    public void connectOutputTo(GeneralFilter nextFilter, int inputPort, int outputPort) throws IOException {
        // 필터의 Output과 이후 필터의 Input을 연결해준다.
        out.get(outputPort).connect(nextFilter.getPipedInputStream().get(inputPort));
    }
    public void connectInputTo(GeneralFilter previousFilter, int inputPort, int outputPort) throws IOException {
        // 필터의 Input과 이전 필터의 Output을 연결해준다.
        in.get(inputPort).connect(previousFilter.getPipedOutputStream().get(outputPort));
    }
    // Getters
    public Vector<PipedInputStream> getPipedInputStream() { return in;}
    public Vector<PipedOutputStream> getPipedOutputStream() {return out;}

    /**********Implementation of public methods defined in Runnable interface************/

    public void run() {
        try {
            specificComputationForFilter();
            // 필터가 기능하는 부분이다.
        } catch (IOException e) {
            if (e instanceof EOFException) return;
            else System.out.println(e);
        } finally {
            closePorts();
        }
    }
/**********Implementation of protected methods************/
    /**Filter가 작동을 정지하기 전에 Input/Output Stream port를 닫는다.*/
    protected void closePorts() {
        try {
            // 필터 개수만큼 반복문을 돌려 정상적으로 종료되도록 한다.
            for(int i=0; i<in.size(); i++) {
                out.get(i).close();
                in.get(i).close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

/**********Abstract method that should be implemented************/
    /** 각 Filter의 특수한 기능이 이곳에 기록되며, 이 메소드는 run()에 의해 호출됨으로써 Filter가 기능하게 된다.
     * @throws IOException
     */
    abstract public void specificComputationForFilter() throws IOException;
}

