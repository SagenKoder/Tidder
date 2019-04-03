package app.sagen.tidderuser.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;

@RestController
public class UserController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("/")
    public String home() {
        String response = "Welcome to the User Service!\n";
        response += "I am assigned the following network interfaces:\n";
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                String inetData = "  - " + networkInterface.getDisplayName() + "\n";
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                int num = 0;
                while(inetAddresses.hasMoreElements()) {
                    num++;
                    InetAddress inetAddress = inetAddresses.nextElement();
                    inetData += "    - " + inetAddress.getHostAddress() + "\n";
                }
                if(num > 0) response += inetData;
            }
        } catch (Exception e) {
            response += "Exception while getting interface information! Error: " + e.getMessage();
            e.printStackTrace();
        }
        response += "Listening to port: " + serverPort + "\n";
        response += "Server time: " + new Date().toString() + "\n";

        return response;
    }

}
