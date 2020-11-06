package Components;

/**
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import Framework.GeneralFilter;

/**
 * Copyright(c) 2013 All rights reserved by JU Consulting
 */

import java.io.IOException;

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
	}

	@Override
	public void specificComputationForFilter() throws IOException {
		int stu_read, cou_read;
		try {
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(new File(filePath)));
			for (;;) {
				if (filePath.equals("Courses.txt")) {
					cou_read = br.read();	// filePath가 Courses.txt일 경우 한 바이트씩 읽는다.
					if (cou_read == -1) {break;}
					// return일 경우 다른 파일을 다 읽기 전에 빠져나오게 되어 오류가 발생할 수 있으므로 break를 걸어준다.
					out.get(inputPort).write(cou_read);	// inputPort(1)에 값을 적는다.
				}
				if (filePath.equals("Students.txt")) {
					stu_read = br.read();	// filePath가 Students.txt일 경우 한 바이트씩 읽는다.
					if (stu_read == -1) {return;}
					out.get(inputPort).write(stu_read);	// inputPort(1)에 값을 적는다.
				}			
			}
		} catch (IOException e) {
			if (e instanceof EOFException) return;
			else System.out.println(e);
		} finally {
			try { out.get(inputPort).close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}