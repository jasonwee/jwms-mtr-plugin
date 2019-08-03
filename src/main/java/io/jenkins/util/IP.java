package io.jenkins.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {

    public static String resolveHostname(String hostname) {
        InetAddress address;
        
        try {
            address = InetAddress.getByName(hostname);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return hostname;
        }
    }

}
