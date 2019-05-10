package com.hpe.ossm.migration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class Tester {
    public static void main(String[] args){
        final Config dc = ConfigFactory.load("selfmonitor");
    }
}
