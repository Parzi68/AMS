package com.project.ams;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

public class ModbusRTUExample {

    public static void main(String args[]) {
        SerialConnection con = null;
        try {
            SerialParameters params = new SerialParameters();
            params.setPortName("COM3");
            params.setBaudRate("9600");
            params.setDatabits("8");
            params.setParity("none");
            params.setStopbits("1");
            params.setEncoding("RTU");
            params.setEcho(false);
            con = new SerialConnection(params);

            if (!con.isOpen()) {
                con.open();
            }

            Thread thread = new Thread(new ReadingTask(con, 4, 99, 2, 10));
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ReadingTask implements Runnable {
    private int slaveId;
    private int reference;
    private int register;
    private int readings;
    private SerialConnection con;

    public ReadingTask(SerialConnection con, int slaveId, int reference, int register, int readings) {
        this.con = con;
        this.slaveId = slaveId;
        this.reference = reference;
        this.register = register;
        this.readings = readings;
    }

    @Override
    public void run() {
        try {
        	System.out.println("------ Readings Started --------");
            for (int i = 0; i < readings; i++) {
                String getRes = getDataLNT(slaveId, reference, register, con);
                Thread.sleep(3000); // Sleep for 1 second
            }
            System.out.println("------- Readings closed!! -------");
            con.close(); // Close the connection after all readings are done
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getDataLNT(int slaveId, int reference, int register, SerialConnection con) {
        ReadMultipleRegistersResponse res = null;
        String getResponse = "";

        try {
            ModbusCoupler.getReference().setUnitID(slaveId);

            ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(reference, register);
            req.setUnitID(slaveId);
            req.setHeadless();

            ModbusSerialTransaction trans = new ModbusSerialTransaction(con);
            trans.setRequest(req);
            trans.execute();

            res = (ReadMultipleRegistersResponse) trans.getResponse();

            if (res != null) {
                String response = res.getHexMessage();
                getResponse = res.getHexMessage();
                getResponse = getResponse.replaceAll(" ", "");
                getResponse = getResponse.substring(6, getResponse.length());

                float floatValue = hexToFloat(getResponse);
                System.out.println("Response from meter: " + response);
                System.out.println("Response in Float - " + floatValue);
            }
        } catch (Exception ex) {
            System.out.println("Reading Error - " + ex);
        }

        System.out.println("Response in hexadecimal - " + getResponse);

        return getResponse;
    }

    public static float hexToFloat(String hex) {
        if (hex.length() == 8) {
            hex = hex.substring(4) + hex.substring(0, 4);
        }
        long longBits = Long.parseLong(hex, 16);
        return Float.intBitsToFloat((int) longBits);
    }
}
