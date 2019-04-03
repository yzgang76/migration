package com.hpe.ossm.migration.test;

import com.hpe.ossm.migration.MigrationV256;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class TestMigrationV256 {
    @BeforeClass
    public static void setup() {
        System.out.println("OSSM_HOME="+MigrationV256.OSSM_HOME);
        System.out.println("OSSM_DATA="+MigrationV256.OSSM_DATA);
    }

    @Test
    public final void test() {
//        MigrationV256.setPath(".","./ossm_home");
//        Properties properties = System.getProperties();
//        Iterator it = properties.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry entry = (Map.Entry) it.next();
//            System.out.print(entry.getKey() + "=");
//            System.out.println(entry.getValue());
//
//        }
//        String javaClassPath = System.getProperty("java.class.path");
////        System.out.println(javaClassPath);
//
//        System.setProperty("java.class.path", javaClassPath + ";D:\\");
//
//        javaClassPath = System.getProperty("java.class.path");
//        System.out.println(javaClassPath);
//
//        System.setProperty("java.class.path", javaClassPath + ";D:\\");
//
//        System.out.println("Working Directory = " +System.getProperty("user.dir"));
//        Properties p=new Properties();
//////        p.setProperty("OSSM_DATA",System.getProperty("user.dir"));
//        File file=new File("./target/test-classes/ossm_data/conf");
//        System.out.println("Test as root= " + file.getAbsolutePath());
//        File[] fs = file.listFiles();
//        for(File f:fs){
//                System.out.println(f.getAbsolutePath());
//        }
//        String optionsS[] = {"-s","C:/mycode/java/migration/target/test-classes","C:/mycode/java/migration/target/test-classes/ossm_home"};
//        MigrationV256.main(optionsS);
        final Config dc = ConfigFactory.parseFile(new File("C:/mycode/java/migration/target/test-classes/ossm_data/conf/dcglobal_v256.conf"));
//
        String javaClassPath = System.getProperty("java.class.path");
        System.setProperty("java.class.path", javaClassPath + ";C:/mycode/java/migration/target/test-classes/ossm_data/conf");
        MigrationV256.OSSM_DATA="C:/mycode/java/migration/target/test-classes/ossm_data";
        MigrationV256.OSSM_HOME="C:/mycode/java/migration/target/test-classes/ossm_home";
        System.setProperty("OSSM_DATA", MigrationV256.OSSM_DATA);
//
//        String javaLibraryPath=System.getProperty("java.library.path");
//        System.setProperty("java.library.path", javaLibraryPath + ";C:/mycode/java/migration/target/test-classes/ossm_data/conf");
//        javaLibraryPath=System.getProperty("java.library.path");
//        System.out.println("javaLibraryPath="+javaLibraryPath);

        final Config dc2 = ConfigFactory.load("dcglobal_v256.conf");
        System.out.println(dc.getInt("akka.remote.netty.tcp.port"));


        System.out.println(dc2.origin().url());
        System.out.println(dc2.getInt("akka.remote.netty.tcp.port"));


        try{
//            String javaClassPath = System.getProperty("java.class.path");
//            System.setProperty("java.class.path", javaClassPath + ";C:/mycode/java/migration/target/test-classes/ossm_data/conf");
//            javaClassPath = System.getProperty("java.class.path");
//            System.out.println(javaClassPath);
            MigrationV256.pickupConfigurations();
            Assert.assertTrue(MigrationV256.isHA);
        }catch(ConfigException e){
            System.out.println("Failed to load conf files, please check the classpath." +e.getMessage());
//            System.exit(-1);
        }

//        String confName = "dcglobal_v256";
//        final Config dcglobal = ConfigFactory.load(confName);
//        try {
//            String host = dcglobal.getString("akka.remote.netty.tcp.hostname");
//            int port = dcglobal.getInt("akka.remote.netty.tcp.port");
//            int port2 = dcglobal.getInt("dc.tcpPort");
//            System.out.println(port2);
//        } catch (Exception e) {
//            System.out.println("Failed to get current configurations from " + confName + ". " + e.getMessage());
//            Assert.fail(e.getMessage());
//        }



        String optionsR[] = {"-r"};
        MigrationV256.main(optionsR);

//        String optionsH[] = {"-h"};
//        MigrationV256.main(optionsH);
    }
}
