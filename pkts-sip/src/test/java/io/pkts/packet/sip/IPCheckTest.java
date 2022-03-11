package io.pkts.packet.sip;

import org.junit.Test;

public class IPCheckTest {

    @Test
    public void isValidIpv6AddrTest() {
        String ipv6_1 = "2019:db8:a583:64:c68c:d6df:600c:ee9a";
        String ipv6_2 = "2019:db8:a583::9e42:be55:53a7";
        String ipv6_3 = "2019:db8:a583:::9e42:be55:53a7";
        String ipv6_4 = "1:2:3:4:5::192.168.254.254";
        String ipv6_5 = "ABCD:910A:2222:5498:8475:1111:3900:2020";
        String ipv6_6 = "1030::C9B4:FF12:48AA:1A2B";
        String ipv6_7 = "2019:0:0:0:0:0:0:1";
        String ipv6_8 = "::0:0:0:0:0:0:1";
        String ipv6_9 = "2019:0:0:0:0::";
        String ipv6_10 = "2048:877e:31::7";
        String ipv6_11 = "2001:0:d14::1111:5060";

        String resultLine = "\n==> ";
        String splitLine = "\n----------------------------------------------------\n";
        System.out.println(ipv6_1 + resultLine + IPCheck.isValidIpv6Addr(ipv6_1) + splitLine);
        System.out.println(ipv6_2 + resultLine + IPCheck.isValidIpv6Addr(ipv6_2) + splitLine);
        System.out.println(ipv6_3 + resultLine + IPCheck.isValidIpv6Addr(ipv6_3) + splitLine);
        System.out.println(ipv6_4 + resultLine + IPCheck.isValidIpv6Addr(ipv6_4) + splitLine);
        System.out.println(ipv6_5 + resultLine + IPCheck.isValidIpv6Addr(ipv6_5) + splitLine);
        System.out.println(ipv6_6 + resultLine + IPCheck.isValidIpv6Addr(ipv6_6) + splitLine);
        System.out.println(ipv6_7 + resultLine + IPCheck.isValidIpv6Addr(ipv6_7) + splitLine);
        System.out.println(ipv6_8 + resultLine + IPCheck.isValidIpv6Addr(ipv6_8) + splitLine);
        System.out.println(ipv6_9 + resultLine + IPCheck.isValidIpv6Addr(ipv6_9) + splitLine);
        System.out.println(ipv6_10 + resultLine + IPCheck.isValidIpv6Addr(ipv6_10) + splitLine);
        System.out.println(ipv6_11 + resultLine + IPCheck.isValidIpv6Addr(ipv6_11) + splitLine);

        String[] ipv6_11_arr = ipv6_11.split(":", -1);
        String port = ipv6_11_arr[ipv6_11_arr.length - 1];
        String host = ipv6_11.replace(":" + port, "");
        System.out.println(host + " " + port);
    }
}