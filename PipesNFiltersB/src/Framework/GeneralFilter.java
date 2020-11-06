package Framework;

/**
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
 *  *      Framework.CommonForFilter 인터페이스를 구현한 추상클래스. 모든 Filter는 본 Filter를 상속받아 구현하게 된다.
 */
public abstract class GeneralFilter implements CommonForFilter {
	protected Vector<PipedInputStream> in = new Vector<>();
	protected Vector<PipedOutputStream> out = new Vector<>();

/*********** Implementation of public methods defined in CommonForFilter interface************/
	public GeneralFilter(int inputPort, int outputPort){    // 포트 개수
		for(int i=0; i<=inputPort; i++){
			in.add(new PipedInputStream());
		}
		for(int j=0; j<=outputPort; j++){
			out.add(new PipedOutputStream());
		}

	}
	public void connectOutputTo(GeneralFilter nextFilter, int inputPort, int outputPort) throws IOException {
		out.get(outputPort).connect(nextFilter.getPipedInputStream().get(inputPort));
	}

	public void connectInputTo(GeneralFilter previousFilter, int inputPort, int outputPort) throws IOException {
		in.get(inputPort).connect(previousFilter.getPipedOutputStream().get(outputPort));
	}

	public Vector<PipedInputStream> getPipedInputStream() {
		return in;
	}

	public Vector<PipedOutputStream> getPipedOutputStream() {
		return out;
	}

	/*********** Implementation of public methods defined in Runnable interface************/

	public void run() {
		try {
			specificComputationForFilter();
		} catch (IOException e) {
			if (e instanceof EOFException)
				return;
			else
				System.out.println(e);
		} finally {
			try {
				for (PipedInputStream pipedInputStream : in) {
					pipedInputStream.close();
				}
				for (PipedOutputStream pipedOutputStream : out) {
					pipedOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/********** Implementation of protected methods ************/

	/**
	 * Filter가 작동을 정지하기 전에 Input/Output Stream port를 닫는다.
	 * @throws Exception 
	 */
	protected void closePorts() {
		try {
			for (PipedInputStream pipedInputStream : in) {
				pipedInputStream.close();
			}
			for (PipedOutputStream pipedOutputStream : out) {
				pipedOutputStream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/********** Abstract method that should be implemented ************/

	/**
	 * 각 Filter의 특수한 기능이 이곳에 기록되며, 이 메소드는 run()에 의해 호출됨으로써 Filter가 기능하게 된다.
	 * 
	 * @throws IOException
	 */
	abstract public void specificComputationForFilter() throws IOException;
}
