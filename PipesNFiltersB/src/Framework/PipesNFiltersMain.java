package Framework;

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
 * Copyright(c) 2019 All rights reserved by JU Consulting
 */
/*
 * @author Jungho Kim
 * @date 2019
 * @version 1.1
 * @description
 *
 */
public class PipesNFiltersMain {
    // PNF-B : 선수과목 체크, Out-1, Out-2

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Stack<Node> stack = new Stack<>();
        Vector<GeneralFilter> filters = new Vector<>();
        GeneralFilter filter;
        String className = "";
        String inputPort = "";
        String outputPort = "";
        String fileName;
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse("config.xml");

        Element frame = document.getDocumentElement();
        frame.normalize();
// GeneralFilter filter = (GeneralFilter) Class.forName("components." + element.getAttribute("class")).getConstructors()[0].newInstance();
        for (Node searchNode = frame.getFirstChild(); searchNode != null || !stack.empty(); searchNode = searchNode.getNextSibling()) {
            if (searchNode == null) {
                searchNode = stack.pop();
                continue;
            }
            if (searchNode.getNodeType() == Node.ELEMENT_NODE) {
                stack.push(searchNode);
                switch (searchNode.getNodeName()) {
                    case "class" -> {
                        className = ((Element) searchNode).getAttribute("name");
                        inputPort = ((Element) searchNode).getAttribute("inputPort");
                        outputPort = ((Element) searchNode).getAttribute("outputPort");
                        System.out.println("노드 이름 : " + className + " 인풋타입 : " + inputPort + " 아웃풋타입 : " + outputPort);
                        if (!searchNode.hasChildNodes()) {
                            filter = (GeneralFilter) Class.forName("Components." + className).getConstructors()[0].newInstance(Integer.parseInt(inputPort), Integer.parseInt(outputPort));
                            filters.add(filter);
                        }
                    }
                    case "file" -> {
                        fileName = ((Element) searchNode).getAttribute("name");
                        filter = (GeneralFilter) Class.forName("Components." + className).getConstructors()[0].newInstance(fileName, Integer.parseInt(inputPort), Integer.parseInt(outputPort));
                        filters.add(filter);
                    }
                }
                if (filters.size() > 4) {
                    filters.get(2).connectInputTo(filters.get(0), 0, 0);
                    filters.get(2).connectInputTo(filters.get(1), 1, 0);
                    filters.get(3).connectInputTo(filters.get(2), 0, 0);
                    filters.get(4).connectInputTo(filters.get(2), 0, 1);
                    for (GeneralFilter g : filters) {
                        Thread thread = new Thread(g);
                        thread.start();
                    }
                }
                if (searchNode.hasChildNodes()) searchNode = searchNode.getFirstChild();
                else searchNode = stack.pop();
            }
        }
    }
}