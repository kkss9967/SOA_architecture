package Components;
/**
 * Copyright(c) 2013 All rights reserved by JU Consulting
 */

import Framework.GeneralFilter;

import java.io.IOException;

/**
 * @author Jungho Kim
 * @date 2019
 * @version 1.1
 * @description
 */
public class MiddleFilter extends GeneralFilter {
	int inputPort, outputPort;
	public MiddleFilter(int inputPort, int outputPort) {
		super(inputPort, outputPort);
		this.inputPort = inputPort; this.outputPort = outputPort;
		// 생성할 때 사용된 inputPort와 outputPort를 사용하여 필터 구분.
	}

	@Override
	public void specificComputationForFilter() throws IOException {
		int byte_read = 0;	// 한 바이트씩 읽기 위해 변수 생성
		int checkBlank = 4;	// 공백 전체 갯수
		int numOfBlank = 0;	// 공백 갯수
		byte[] buffer = new byte[64];	// 한 바이트씩 읽어서 버퍼에 저장하기 위해 생성
		int idx = 0;	// 버퍼 사이즈 변수
		boolean isCS = false;	// CS인지 확인하기 위한 boolean

		while (true) {          			
			while (byte_read != '\n') {	// 읽은 바이트가 엔터가 아닐 때
				byte_read = in.get(outputPort).read();	// outputPort에서 읽는다.
				if (byte_read == -1) return;	// 파일을 다 읽은 경우 return
				else if (byte_read == ' ') numOfBlank++;	// 읽은 바이트가 ' '일 경우 ++

				buffer[idx++] = (byte) byte_read;	// 버퍼에 한 바이트씩 넣어주고, 버퍼 사이즈를 증가시킨다.

				if (numOfBlank == checkBlank && buffer[idx - 3] == 'C' && buffer[idx - 2] == 'S') {
					isCS = true;	// CS인 경우 boolean을 true로 변경한다.
				}
			}

			if (isCS) {
				for (int i = 0; i < idx; i++) {
					out.get(inputPort).write((char)buffer[i]);
					// 버퍼의 사이즈 만큼 inputport에 값을 적는다.
				}
				isCS = false;	// 불린 값을 false로 바꿔준다.
			}
			idx = 0;
            numOfBlank = 0;
            byte_read = '\0';
            // 사용된 변수들을 초기화해준다.
		}
	}
}
