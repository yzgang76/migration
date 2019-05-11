package com.hpe.ossm.migration;

import com.typesafe.config.ConfigFactory;


public class Tester {
    public static void main(String[] args) {
        ConfigFactory.load("selfmonitor");
        ConfigFactory.load("wsrestservice");
        ConfigFactory.load("cmglobal");
        ConfigFactory.load("dcglobal");
        ConfigFactory.load("selfmonitor_presenter");
        ConfigFactory.load("sink");
        ConfigFactory.load("receiverglobal");
        ConfigFactory.load("receiverservice");
        ConfigFactory.load("receiverconsole");
        ConfigFactory.load("bulk_operation");
        ConfigFactory.load("uoc2config");
    }
}
