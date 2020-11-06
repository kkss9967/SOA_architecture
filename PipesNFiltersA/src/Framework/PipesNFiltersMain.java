package Framework;
/**
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */

import Components.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;
import java.util.Vector;

/**
 * @author Jungho Kim
 * @version 1.1
 * @date 2019
 * @description
 */
public class PipesNFiltersMain {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Stack<Node> stack = new Stack<>();  // 필터를 확인하기 위해 stack 클래스를 사용한다. (LIFO구조)
        Vector<GeneralFilter> filters = new Vector<>(); // 이후에 필터를 연결하는 작업을 수월하게 하기 위해 벡터를 생성한다.
        GeneralFilter filter;
        String className = "";
        String inputPort = "";
        String outputPort = "";
        String fileName;
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();  // 파싱에 필요한 클래스를 설정한다.
        Document document = builder.parse("config.xml");    // config.xml 파일을 읽을 것이다.

        Element frame = document.getDocumentElement();  // 부모 element를 frame에 저장한다.
        frame.normalize();  // 노드를 정규화 하는 과정이다.
        for (Node searchNode = frame.getFirstChild(); searchNode != null || !stack.empty(); searchNode = searchNode.getNextSibling()) {
            // searchNode를 부모노드의 첫 자식으로 설정, searchNode가 null이 아닐 때 까지 또는 stack이 비어있지 않을 때까지 반복, searchNode를 searchNode의 형제로 refresh 해준다.
            if (searchNode == null) {   // searchNode가 없으면 stack에서 꺼내온 뒤, searchNode를 바꿔주고 반복문을 돌도록 한다.
                searchNode = stack.pop();
                continue;
            }
            if (searchNode.getNodeType() == Node.ELEMENT_NODE) {    // ELEMENT_NODE를 사용, 주석일 경우를 제외하도록 한다.
                stack.push(searchNode); // searchNode를 stack에 넣는다.
                switch (searchNode.getNodeName()) {
                    case "class" -> {   // 노드 이름이 class인 경우 속성을 저장한다.
                        className = ((Element) searchNode).getAttribute("name");
                        inputPort = ((Element) searchNode).getAttribute("inputPort");
                        outputPort = ((Element) searchNode).getAttribute("outputPort");

                        if (!searchNode.hasChildNodes()) {  // 만일 노드가 자식이 없는 경우 필터 클래스를 New 해준 뒤, filters 벡터에 add한다.
                            filter = (GeneralFilter) Class.forName("Components." + className).getConstructors()[0].newInstance(Integer.parseInt(inputPort), Integer.parseInt(outputPort));
                            filters.add(filter);
                        }
                    }
                    case "file" -> {    // 노드 이름이 file인 경우, 속성을 저장한 뒤 필터 클래스를 New 해주고 벡터에 add한다.
                        fileName = ((Element) searchNode).getAttribute("name");
                        filter = (GeneralFilter) Class.forName("Components." + className).getConstructors()[0].newInstance(fileName, Integer.parseInt(inputPort), Integer.parseInt(outputPort));
                        filters.add(filter);
                    }
                }
                if (filters.size() > 4) {   // 필터 클래스가 다 저장된 경우
                    for (int i = 0; i < filters.size() - 1; i++) {
                        filters.get(i + 1).connectInputTo(filters.get(i), Integer.parseInt(inputPort), Integer.parseInt(outputPort));
                        // inputPort 값과 outputPort 값을 사용해 필터들을 연결해준다.
                    }
                    for (GeneralFilter g : filters) {
                        // 필터에 대한 thread를 생성한 뒤 스레드를 돌리기 시작한다.
                        Thread thread = new Thread(g);
                        thread.start();
                    }
                }
                if (searchNode.hasChildNodes()) searchNode = searchNode.getFirstChild();
                else searchNode = stack.pop();
                // searchNode가 자식이 있으면, searchNode를 searchNode의 자식으로 바꿔준다. 그렇지 않으면 stack의 값을 한 개 빼온다.
            }
        }
    }
}