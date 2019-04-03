package com.hpe.ossm.migration;

import com.typesafe.config.*;
import lombok.Getter;

import java.io.*;
import java.util.List;

/**
 * class to migrate configuration files from v2.5.6 sp2 to v2.6 MR
 */
@Getter
public class MigrationV256 {
    private static boolean isGlobalChanged = false;
    public static boolean isHA = false;
    private static String HOST_LOCAL = "localhost";
    private static String HOST_HA = "";
    private static int AKKA_SELF_MONITOR = 3713;
    private static int AKKA_REST_SERVICE = 4741;
    private static int AKKA_UMBADAPTER = 4745;
    private static int AKKA_CM = 5556;
    private static int AKKA_DC_ADAPTER = 5555;
    private static int AKKA_PRESENTER = 3733;
    private static int AKKA_SINK = 2555;
    private static int AKKA_RECEIVER = 2553;
    private static int AKKA_RECEIVER_CONSOLE = 2552;
    private static int AKKA_BULK_OP = 5557;
    private static int HTTP_PORT = 18081;
    private static int PRESENTER = 8080;
    private static int H2_DB = 9093;
    private static int DC_TCP = 9292;
    private static int H2_CM_DB = 9192;
    private static int UOC_HTTP = 3000;

    private static int HA_AKKA_CM = 5556;
    private static int HA_UOC_HTTP = 3000;
    private static int HA_AKKA_BULK_OP = 5557;

    private static final String pathAKKAHost = "akka.remote.netty.tcp.hostname";
    private static final String pathAKKAPort = "akka.remote.netty.tcp.port";

    private static final String fileListConf[] = {"selfmonitor", "wsrestservice", "cmglobal", "dcglobal", "selfmonitor_presenter",
            "sink", "receiverglobal", "receiverservice", "receiverconsole", "bulk_operation"};
    private static final String fileListAdapter[] = {"/umb_temip/conf/umbadapter"};

    public static String OSSM_DATA = System.getenv("OSSM_DATA");
    public static String OSSM_HOME = System.getenv("OSSM_HOME");

//    public static MigrationV256 getInstance(){
//        return new MigrationV256();
//    }
  /*  private static void setPath(String OSSM_DATA,String OSSM_HOME){
        MigrationV256.OSSM_DATA=OSSM_DATA;
        MigrationV256.OSSM_HOME=OSSM_HOME;

        String javaClassPath = System.getProperty("java.class.path");
//        File file=new File(".");
//        System.out.println("Test as root= " + file.getAbsolutePath());
        System.setProperty("java.class.path", javaClassPath + ";"+OSSM_DATA+"/conf");
        System.setProperty("OSSM_DATA", MigrationV256.OSSM_DATA);
        javaClassPath = System.getProperty("java.class.path");
        System.out.println("java.class.path = "+javaClassPath);
    }*/

    private static void compareConfFile(String sNew, String sOld) {
        System.out.println("*************************************************************************************");
        System.out.println("Updates of " + sOld + ".conf");
        try{
            final Config cNew = ConfigFactory.load(sNew);
            final Config cOld = ConfigFactory.load(sOld);
            cNew.entrySet().forEach(e -> {
                try {
                    String old = cOld.getValue(e.getKey()).render(ConfigRenderOptions.concise());
                    if (old.equals(e.getValue().render(ConfigRenderOptions.concise()))) {
//                    System.out.println("===\t"+e.getKey()+"===>"+e.getValue().render(ConfigRenderOptions.concise()));
                    } else {
                        System.out.println("[uuu]\t" + e.getKey() + "===>" + old + " to " + e.getValue().render(ConfigRenderOptions.concise()));
                    }
                } catch (Exception err) {
//                    System.out.println("+++\t"+e.getKey()+"===>"+e.getValue().render(ConfigRenderOptions.concise()));
                }
            });
            cOld.entrySet().forEach(e -> {
                try {
                    if (!(e.getKey().startsWith("PORTS.") || e.getKey().equals("HOST"))) {
                        cNew.getValue(e.getKey()).render(ConfigRenderOptions.concise());
                    }
                } catch (Exception err) {
                    System.out.println("[---]\t" + e.getKey() + "===>" + e.getValue().render(ConfigRenderOptions.concise()));
                }
            });
            System.out.println("*************************************************************************************");
        }catch(Exception ex){
            System.out.println("Error in compareConfFile:" + ex.getMessage());
        }

    }

    private static void saveHostAndPort(String OSSM_DATA){
        try{
            File fOutput= new File(OSSM_DATA + "/conf/host_and_port.conf");
            if (!fOutput.exists()) {
                fOutput.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(fOutput, false);
            fileWritter.write("HOST_LOCAL={\n");
            fileWritter.write("\tHOST=\""+HOST_LOCAL+"\"\n");
            fileWritter.write("\tPORTS {\n");
            fileWritter.write("\t\tAKKA_SELF_MONITOR="+AKKA_SELF_MONITOR+"\n");
            fileWritter.write("\t\tAKKA_REST_SERVICE="+AKKA_REST_SERVICE+"\n");
            fileWritter.write("\t\tAKKA_UMBADAPTER="+AKKA_UMBADAPTER+"\n");
            fileWritter.write("\t\tAKKA_CM="+AKKA_CM+"\n");
            fileWritter.write("\t\tAKKA_DC_ADAPTER="+AKKA_DC_ADAPTER+"\n");
            fileWritter.write("\t\tAKKA_PRESENTER="+AKKA_PRESENTER+"\n");
            fileWritter.write("\t\tAKKA_SINK="+AKKA_SINK+"\n");
            fileWritter.write("\t\tAKKA_RECEIVER="+AKKA_RECEIVER+"\n");
            fileWritter.write("\t\tAKKA_RECEIVER_CONSOLE="+AKKA_RECEIVER_CONSOLE+"\n");
            fileWritter.write("\t\tAKKA_BULK_OP="+AKKA_BULK_OP+"\n");
            fileWritter.write("\t\tHTTP_PORT="+HTTP_PORT+"\n");
            fileWritter.write("\t\tPRESENTER="+PRESENTER+"\n");
            fileWritter.write("\t\tH2_DB="+H2_DB+"\n");
            fileWritter.write("\t\tDC_TCP="+DC_TCP+"\n");
            fileWritter.write("\t\tH2_CM_DB="+H2_CM_DB+"\n");
            fileWritter.write("\t\tUOC_HTTP="+UOC_HTTP+"\n");
            fileWritter.write("\t}\n");
            fileWritter.write("}\n");
            fileWritter.write("\n");
            fileWritter.write("HOST_HA_REMOTE={\n");
            fileWritter.write("\tHOST=\""+HOST_HA+"\"\n");
            fileWritter.write("\tPORTS {\n");
            fileWritter.write("\t\tAKKA_CM="+HA_AKKA_CM+"\n");
            fileWritter.write("\t\tUOC_HTTP="+HA_UOC_HTTP+"\n");
            fileWritter.write("\t\tAKKA_BULK_OP="+HA_AKKA_BULK_OP+"\n");
            fileWritter.write("\t}\n");
            fileWritter.write("}\n");
            fileWritter.write("\n");
            fileWritter.write("HA={\n");
            if(isHA){
                fileWritter.write("  cmHA {\n" +
                        "       host = ${HOST_HA_REMOTE.HOST} //use the same hostname configured in the cmglobal.conf of the other server\n" +
                        "       port = ${HOST_HA_REMOTE.PORTS.AKKA_CM}\n" +
                        "     }\n" +
                        "\n" +
                        "    bulkHA {\n" +
                        "       host = ${HOST_HA_REMOTE.HOST} //use the same hostname configured in the cmglobal.conf of the other server\n" +
                        "       port = ${HOST_HA_REMOTE.PORTS.AKKA_BULK_OP}\n" +
                        "     }\n" +
                        "\n" +
                        "  UOC_REMOTE{\n" +
                        "    protocol = \"http\"\n" +
                        "    host = ${HOST_HA_REMOTE.HOST}\n" +
                        "    port = ${HOST_HA_REMOTE.PORTS.UOC_HTTP}\n" +
                        "  }");
            }else{
                fileWritter.write("#  cmHA {\n" +
                        "  #     host = ${HOST_HA_REMOTE.HOST} //use the same hostname configured in the cmglobal.conf of the other server\n" +
                        "  #     port = ${HOST_HA_REMOTE.PORTS.AKKA_CM}\n" +
                        "  #   }\n" +
                        "\n" +
                        "  #  bulkHA {\n" +
                        "  #     host = ${HOST_HA_REMOTE.HOST} //use the same hostname configured in the cmglobal.conf of the other server\n" +
                        "  #     port = ${HOST_HA_REMOTE.PORTS.AKKA_BULK_OP}\n" +
                        "  #   }\n" +
                        "\n" +
                        "  #UOC_REMOTE{\n" +
                        "  #  protocol = \"http\"\n" +
                        "  #  host = ${HOST_HA_REMOTE.HOST}\n" +
                        "  #  port = ${HOST_HA_REMOTE.PORTS.UOC_HTTP}\n" +
                        "  #}");

            }
            fileWritter.write("}\n");
            fileWritter.flush();
            fileWritter.close();
        }catch(Exception e){
            System.out.println("Failed to create host_and_port.conf, "+e.getMessage());
        }
    }

    private static void revert() {
        final String ext = "_v256";
        //rename v256 files
        for (String f : fileListAdapter) {
            File fs = new File(OSSM_DATA + "/adapters" + f + ".conf");
            File fd = new File(OSSM_DATA + "/adapters" + f + "_v256.conf");

            if (fs.exists() && fd.exists()) {
                if (!deleteFile( "/adapters" + f)) {
                    System.exit(-1);
                }
                if (!renameFile( "/adapters" + f, true)) {
                    System.exit(-1);
                }
                if (!deleteFile( "/adapters" + f + ext)) {
                    System.exit(-1);
                }
            }
        }
        for (String f : fileListConf) {
            File fs = new File(OSSM_DATA + "/conf/" + f + ".conf");
            File fd = new File(OSSM_DATA + "/conf/" + f + "_v256.conf");

//            System.out.println("ppppp "+fs.getAbsolutePath());
            if (fs.exists() && fd.exists()) {
                if (!deleteFile( "/conf/" + f)) {
                    System.exit(-1);
                }
                if (!renameFile( "/conf/" + f, true)) {
                    System.exit(-1);
                }
                if (!deleteFile( "/conf/" + f + ext)) {
                    System.exit(-1);
                }
            }

        }
//        if (!deleteFile(OSSM_DATA, "/conf/host_and_port.conf")) {
//            System.exit(-1);
//        }
        if (!deleteFile( "/conf/ossm_akka_global.conf")) {
//            System.exit(-1);
        }
    }

    private static void pickupAkkaPort(Config c, String s, String confName)  {
        try {
            int port = c.getInt(pathAKKAPort);
            switch (s) {
                case "AKKA_BULK_OP":
                    if (port != AKKA_BULK_OP) {
                        AKKA_BULK_OP = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_CM":
                    if (port != AKKA_CM) {
                        AKKA_CM = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_RECEIVER_CONSOLE":
                    if (port != AKKA_RECEIVER_CONSOLE) {
                        AKKA_RECEIVER_CONSOLE = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_RECEIVER":
                    if (port != AKKA_RECEIVER) {
                        AKKA_RECEIVER = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_SELF_MONITOR":
                    if (port != AKKA_SELF_MONITOR) {
                        AKKA_SELF_MONITOR = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_PRESENTER":
                    if (port != AKKA_PRESENTER) {
                        AKKA_PRESENTER = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_SINK":
                    if (port != AKKA_SINK) {
                        AKKA_SINK = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_REST_SERVICE":
                    if (port != AKKA_REST_SERVICE) {
                        AKKA_REST_SERVICE = port;
                        isGlobalChanged = true;
                    }
                    break;
                case "AKKA_UMBADAPTER":
                    if (port != AKKA_UMBADAPTER) {
                        AKKA_UMBADAPTER = port;
                        isGlobalChanged = true;
                    }
                    break;
                default:
                    System.out.println("Unknown port type: " + s);

            }

        } catch (Exception e) {
            System.out.println("Failed to get current configurations from " + confName + ". " + e.getMessage());
            System.exit(-1);
        }
    }

    public static void pickupConfigurations() throws ConfigException{
        final String ext = "_v256";
        //dcglobal
        String confName = "dcglobal";
        final Config dcglobal = ConfigFactory.load(confName + ext);
        try {
            String host = dcglobal.getString(pathAKKAHost);
            int port = dcglobal.getInt(pathAKKAPort);
            int port2 = dcglobal.getInt("dc.tcpPort");
            if (!host.equals(HOST_LOCAL)) {
                HOST_LOCAL = host;
                isGlobalChanged = true;
            }
            if (port != AKKA_DC_ADAPTER) {
                AKKA_DC_ADAPTER = port;
                isGlobalChanged = true;
            }
            if (port2 != DC_TCP) {
                DC_TCP = port2;
                isGlobalChanged = true;
            }

        } catch (Exception e) {
            System.out.println("Failed to get current configurations from " + confName + ". " + e.getMessage());
            System.exit(-1);
        }
        try {
            List ha = dcglobal.getObjectList("dc.clusteredCM");
            Config cf = ((ConfigObject) ha.toArray()[0]).toConfig();
            String host = cf.getString("host");
            int port = cf.getInt("port");
            if (!host.equals(HOST_HA)) {
                HOST_HA = host;
                isGlobalChanged = true;
            }
            if (port != HA_AKKA_CM) {
                HA_AKKA_CM = port;
                isGlobalChanged = true;
            }
            isHA = true;
            System.out.println("In HA environment.");
        } catch (Exception e) {
            System.out.println("In Single Host environment.");
        }

        //bulk_operation
        confName = "bulk_operation";
        final Config bulkOperation = ConfigFactory.load(confName + ext);
//        System.out.println("xxxx="+bulkOperation.getString("slick-h2.db.properties.url"));
        pickupAkkaPort(bulkOperation, "AKKA_BULK_OP", confName);
        try {
            int port = bulkOperation.getInt("PORTS.H2_CM_DB");
            if (port != H2_CM_DB) {
                H2_CM_DB = port;
                isGlobalChanged = true;
            }

            port = bulkOperation.getInt("presenter.port");
            if (port != PRESENTER) {
                PRESENTER = port;
                isGlobalChanged = true;
            }
        } catch (Exception e) {

        }
        if (isHA) {  //get remote bulk_op port
            try {
                List ha = bulkOperation.getObjectList("otherBulkOpAdapters");
                Config cf = ((ConfigObject) ha.toArray()[0]).toConfig();
                int port = cf.getInt("port");
                if (port != HA_AKKA_BULK_OP) {
                    HA_AKKA_BULK_OP = port;
                    isGlobalChanged = true;
                }
            } catch (Exception e) {
                //the current HA setting is wrong, use default values.
            }
        }

        //cmglobal
        confName = "cmglobal";
        final Config cmglobal = ConfigFactory.load(confName + ext);
        pickupAkkaPort(cmglobal, "AKKA_CM", confName);

        //receiverconsole
        confName = "receiverconsole";
        final Config receiverconsole = ConfigFactory.load(confName + ext);
        pickupAkkaPort(receiverconsole, "AKKA_RECEIVER_CONSOLE", confName);

        //receiverglobal
        confName = "receiverglobal";
        final Config receiverglobal = ConfigFactory.load(confName + ext);
        try {
            int port = receiverglobal.getInt("receiver.ServerPort");
            if (port != H2_DB) {
                H2_DB = port;
                isGlobalChanged = true;
            }
        } catch (Exception e) {

        }


        //receiverservice
        confName = "receiverservice";
        final Config receiverservice = ConfigFactory.load(confName + ext);
        pickupAkkaPort(receiverservice, "AKKA_RECEIVER", confName);

        //selfmonitor
        confName = "selfmonitor";
        final Config selfmonitor = ConfigFactory.load(confName + ext);
        pickupAkkaPort(selfmonitor, "AKKA_SELF_MONITOR", confName);

        //selfmonitor_present
        confName = "selfmonitor_presenter";
        final Config selfmonitor_presenter = ConfigFactory.load(confName + ext);
        pickupAkkaPort(selfmonitor_presenter, "AKKA_PRESENTER", confName);

        //sink
        confName = "sink";
        final Config sink = ConfigFactory.load(confName + ext);
        pickupAkkaPort(sink, "AKKA_SINK", confName);

        //uoc2config
        confName = "uoc2config";
        final Config uoc2config = ConfigFactory.load(confName + ext);
        try {
            List ha = uoc2config.getObjectList("uocv2");
            Config cf = ((ConfigObject) ha.toArray()[0]).toConfig();
            int port = cf.getInt("port");
            if (port != UOC_HTTP) {
                UOC_HTTP = port;
                isGlobalChanged = true;
            }
            cf = ((ConfigObject) ha.toArray()[1]).toConfig();
            port = cf.getInt("port");
            if (port != HA_UOC_HTTP) {
                HA_UOC_HTTP = port;
                isGlobalChanged = true;
            }
        } catch (Exception e) {

        }

        //wsrestservice
        confName = "wsrestservice";
        final Config wsrestservice = ConfigFactory.load(confName + ext);
        pickupAkkaPort(wsrestservice, "AKKA_REST_SERVICE", confName);
        try {
            int port = receiverglobal.getInt("configurations.port");
            if (port != HTTP_PORT) {
                HTTP_PORT = port;
                isGlobalChanged = true;
            }
        } catch (Exception e) {

        }

        //umbadapter
        confName = "umbadapter";
        final Config umbadapter = ConfigFactory.parseFile(new File(OSSM_DATA + "/adapters/umb_temip/conf/umbadapter_v256.conf"));
        try {
            int port = umbadapter.getInt("PORTS.REMOTE_UMBADAPTER");
            if (port != AKKA_UMBADAPTER) {
                AKKA_UMBADAPTER = port;
                isGlobalChanged = true;
            }
        } catch (Exception e) {

        }

    }

    private static boolean renameFile(String fileName, boolean revert) {
        File fs, fd;
        fs = new File(OSSM_DATA + fileName + ".conf");
        fd = new File(OSSM_DATA + fileName + "_v256.conf");
        if (!revert && fs.exists()) {
            if (fs.renameTo(fd)) {
                return true;
            } else {
                System.out.println("Error: Failed to rename file " + fileName + ". Target file exists.");
                return false;
            }
        } else if (revert && fd.exists()) {
            if (fd.renameTo(fs)) {
                return true;
            } else {
                System.out.println("Error: Failed to rename file " + fileName + ". Target file exists.");
                return false;
            }
        } else {
            System.out.println("Error: Failed to rename file " + fileName + ". Target file exists.");
            return false;
        }


    }

    private static boolean deleteFile( String fileName) {
        File file = new File(OSSM_DATA + fileName + ".conf");
        if (!file.exists()) {
            return true;
        } else {
            if (file.exists() && file.isFile()) {
                return file.delete();
            }
        }
        return true;
    }

    private static boolean copyFile( String fileName) {
        try {
            FileInputStream input = new FileInputStream(OSSM_HOME + fileName + ".conf");
            FileOutputStream output = new FileOutputStream(OSSM_DATA + fileName + ".conf");
            int in = input.read();
            while (in != -1) {
                output.write(in);
                in = input.read();
            }
            input.close();
            output.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error: Failed to copy file " + fileName + ". " + e.getMessage());
            return false;
        }
    }

    public static void main(String args[]) {
        if(args.length==0){  //migrate all conf files
            System.out.println("Tools to migrate configuration files from v2.5.6 sp2 to v2.6 MR");
            if (null == OSSM_DATA) {  //check OSSM_DATA
                System.out.println("Not found OSSM_DATA path, exit.");
                System.exit(0);
            }
            if (null == OSSM_HOME) { //check OSSM_HOME
                System.out.println("Not found OSSM_HOME path, exit.");
                System.exit(0);
            }
            System.out.println("Migration configuration files from " + OSSM_DATA + "/conf to " + OSSM_HOME + "/conf");
            revert();
            //new files
            if (!copyFile( "/conf/ossm_akka_global")) {
                System.exit(-1);
            }
            //rename v256 files
            for (String f : fileListAdapter) {
                if (!renameFile( "/adapters" + f, false)) {
                    System.exit(-1);
                }
                if (!copyFile( "/adapters" + f)) {
                    System.exit(-1);
                }
            }
            for (String f : fileListConf) {
                if (!renameFile("/conf/" + f, false)) {
                    System.exit(-1);
                }
                if (!copyFile( "/conf/" + f)) {
                    System.exit(-1);
                }
            }

            //pick up current configurations
            try{
                pickupConfigurations();
            }catch(ConfigException e){
                System.out.println("Failed to load conf files, please check the classpath. " + e.getMessage());
                System.exit(-1);
            }

            if (isGlobalChanged) {
                System.out.println("The default value of host and ports are changed.");
                saveHostAndPort(OSSM_DATA);
            } else {
                if (!copyFile("/conf/host_and_port")) {
                    System.exit(-1);
                }
            }

            System.out.println("Migrate complete. Please use -c [file] to show the update configurations");
        }

        if(args.length>0 && args[0].equals("-c")){  //show removed and updated configuration files
            if(args.length==1){
                for (String f : fileListConf) {
                    compareConfFile(f,f+"_v256");
                }
            }else{
                compareConfFile(args[1],args[1]+"_v256");
            }
        }

        if(args.length>0 && args[0].equals("-h")) {  //show helps
            System.out.println("without parameters: to convert configuration files. New file are under in OSSM_DATA. Old files are renamed with _v256.");
            System.out.println("-r: to revert the convert");
            System.out.println("-c [file name]: show updated and removed configuration items. If not set 'file name' it will show changes in all configuration files.");
//            System.out.println("-s OSSM_HOME OSSM_DATA: Update the ");
        }

        if(args.length>0 && args[0].equals("-r")) {  //revert to 2.5.6
            System.out.println("Revert configuration files from v2.6 MR to v2.5.6 SP2");
            revert();
        }

//        if(args.length==3 && args[0].equals("-s")) {  //revert to 2.5.6
//            System.out.println("Set environment path");
//            if (null != OSSM_HOME) {  //check OSSM_DATA
//                System.out.println("OSSM_HOME has value  "+ OSSM_HOME);
//            }
//            if (null != OSSM_DATA) { //check OSSM_HOME
//                System.out.println("OSSM_DATA has value  "+ OSSM_DATA);
//            }
//            setPath(args[1],args[2]);
//            System.out.println("OSSM_HOME ="+OSSM_HOME);
//            System.out.println("OSSM_DATA ="+OSSM_DATA);
//        }
    }
}
