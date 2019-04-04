package app.sagen.tidderuser.controller;

import app.sagen.tidderuser.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;

@RestController
public class UserController {

    @Autowired
    EmailService emailService;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("/")
    public String home() {
        String response = "<a>Welcome to the User Service!</a>\n";
        response += "<a>I am assigned the following network interfaces:</a>\n";
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                String inetData = "<a>&nbsp&nbsp- " + networkInterface.getDisplayName() + "\n";
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                int num = 0;
                while(inetAddresses.hasMoreElements()) {
                    num++;
                    InetAddress inetAddress = inetAddresses.nextElement();
                    inetData += "<a>&nbsp&nbsp&nbsp&nbsp- " + inetAddress.getHostAddress() + "</a>\n";
                }
                if(num > 0) response += inetData;
            }
        } catch (Exception e) {
            response += "<a>Exception while getting interface information! Error: " + e.getMessage();
            e.printStackTrace();
        }
        response += "<a>Listening to port: " + serverPort + "</a>\n";
        response += "<a>Server time: " + new Date().toString() + "</a>\n";

        return response.replace("\n", "<br/>\n");
    }

    @RequestMapping("/sendMail/{to}")
    public String sendMail(@PathVariable("to") String to) {
        emailService.sendSimpleMessage(to, "Test email from Tidder", "This is a test email from tidder!\nIf you received this, you are cool AF <3");
        return "Sent email to " + to;
    }

}
