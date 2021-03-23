package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.services.OrderService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showAdminDashboard() {
        return "admin-panel";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders-page";
    }

    // эндпоинт, на который ведёт кнопка на фронте.
    // метод делает SOAP запрос к нашему Web Service и отдает на фронт строку из полученного XML
    @GetMapping("/products/all")
    public String showXmlProducts(Model model) throws TransformerException, ParserConfigurationException, IOException, SAXException {

        String strURL = "http://localhost:8189/project/ws/";
        String strXMLFilename = "/home/tipokgg/IdeaProjects/geekspring/src/main/resources/soaprequest.xml";

        File input = new File(strXMLFilename);
        PostMethod post = new PostMethod(strURL);
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        post.setRequestEntity(entity);
        post.setRequestHeader("SOAPAction", "");
        HttpClient httpclient = new HttpClient();
        String text = "";
        try {
            httpclient.executeMethod(post);
            text =  post.getResponseBodyAsString();
            model.addAttribute("products", text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }

        return "products-xml";
    }


    @GetMapping("/orders/ready/{id}")
    public void orderReady(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) throws Exception {
        Order order = orderService.findById(id);
        orderService.changeOrderStatus(order, 2L);
        response.sendRedirect(request.getHeader("referer"));
    }
}
