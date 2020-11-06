package Components;

/**
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */

import java.io.FileWriter;
import java.io.IOException;

import Framework.GeneralFilter;

/**
 * @author Jungho Kim
 * @date 2019
 * @version 1.1
 * @description file을 내리기만 하는 역할
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
			while (true) {
				if (filePath.equals("Output-1.txt")) {
					databyte = in.get(0).read();
					if (databyte == -1) {
						fw.close();
						System.out.println("::Filtering is finished; Output-1 file is created.");
						break;
					}
					fw.write(databyte);
				}
				if (filePath.equals("Output-2.txt")) {
					databyte = in.get(0).read();

					if (databyte == -1) {
						fw.close();
						System.out.println("::Filtering is finished; Output-2 file is created.");
						return;
					}
					fw.write(databyte);
				}
			}
		} catch (Exception e) {
			closePorts();
			e.printStackTrace();

		}
	}

}
