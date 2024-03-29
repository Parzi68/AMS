package com.project.ams;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.spring.Repository.ConfigRepository;
import com.project.ams.spring.Repository.MapRepository;
import com.project.ams.spring.Repository.MeterRepository;
import com.project.ams.spring.model.Meterdata;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.BitVector;
import net.wimpi.modbus.util.SerialParameters;

public class ReadData extends Thread {

	@Autowired
	private MeterRepository meterRepository;

	@Autowired
	private ConfigRepository configRepository;

	@Autowired
	private MapRepository mapRepository;

	SerialConnection con = null;

	String com_port = "", parity = "", baud_rate = "", stop_bits = "", data_bits = "", getRes = "", reg_length = "",
			reg_address = "", point_type = "";
//	LocalDateTime currentDate;

	Vector vcPort = new Vector();
	Vector vcSlave = new Vector();
	Vector vcRegName = new Vector();
	String htConnectionParameters = "";

	public ReadData() {
	}

	public ReadData(MeterRepository meterRepository, ConfigRepository configRepository, MapRepository mapRepository) {
		this.configRepository = configRepository;
		this.mapRepository = mapRepository;
		this.meterRepository = meterRepository;
	}

	@SuppressWarnings("rawtypes")
	public void run() {
		while (true) {
			vcPort = configRepository.available_comPort();
			if (!vcPort.isEmpty()) {
				for (int p = 0; p < vcPort.size(); p++) {
					com_port = vcPort.get(p).toString();
					vcSlave = new Vector();
					vcSlave = configRepository.get_mapped_source(com_port);
					System.out.println("==Sources---++++++++----==" + vcSlave);
				}
				if (!vcSlave.isEmpty()) {
					for (int s = 0; s < vcSlave.size(); s++) {
						int slave_id = Integer.parseInt(vcSlave.get(s).toString());
						Integer source_id = configRepository.get_source_name(slave_id);
						System.out.println("Slave id, Source id....." + slave_id + "..." + source_id);

						htConnectionParameters = configRepository.modbus_rtu_configuration(slave_id, com_port);
						System.out.println(htConnectionParameters);
						baud_rate = htConnectionParameters.split(",")[0].toString();
						data_bits = htConnectionParameters.split(",")[1].toString();
						stop_bits = htConnectionParameters.split(",")[2].toString();
						parity = htConnectionParameters.split(",")[3].toString();
						
//						baud_rate = htConnectionParameters.get("baud_rate").toString();
//						data_bits = htConnectionParameters.get("data_bits").toString();
//						stop_bits = htConnectionParameters.get("stop_bits").toString();
//						parity = htConnectionParameters.get("parity").toString();


						System.out.println("==local_source Sources-- Parameters -+++" + com_port + "+++++-" + slave_id
								+ "---==" + baud_rate + "--" + data_bits + "^^^^" + stop_bits + "--" + parity + "");

						try {
							SerialParameters params = new SerialParameters();
							params.setPortName(com_port);
							params.setBaudRate(baud_rate);
							params.setDatabits(data_bits);
							params.setParity(parity);
							params.setStopbits(stop_bits); // only for hubli
							params.setEncoding("RTU");
							params.setEcho(false);
							con = new SerialConnection(params);
							if (!con.isOpen()) {
								con.open();
							}
							System.out.println("--- Connected!! ----");

							vcRegName = new Vector();
							vcRegName = mapRepository.get_RegName(source_id);

							if (!vcRegName.isEmpty()) {
								for (int t = 0; t < vcRegName.size(); t++) {
									String reg_name = vcRegName.get(t).toString();
									System.out.println("Register name..... " + reg_name);
									String getAll = mapRepository.getAlltags(source_id, reg_name);
									int reg_address = Integer.parseInt(getAll.split(",")[0].toString());
									int reg_length = Integer.parseInt(getAll.split(",")[1].toString());
									String reg_type = getAll.split(",")[2].toString();
									int multiplier = Integer.parseInt(getAll.split(",")[3].toString());
									String point_type = getAll.split(",")[4].toString();

									if (!con.isOpen()) {
										con.open();
									}

									if (point_type.equals("03: HOLDING REGISTER")) {
										getRes = getDataLNT(slave_id, reg_address, reg_length, "", con);
									} else if (point_type.equals("04: INPUT REGISTER")) {
										getRes = getData(slave_id, reg_address, reg_length, "", con);
									} else if (point_type.equals("02: INPUT STATUS")) {
										getRes = getDISCRETESData(slave_id, reg_address, reg_length, "", con);
									} else if (point_type.equals("01: COIL STATUS")) {
										getRes = getCoilData(slave_id, reg_address, reg_length, "", con);
									} else {
										System.out.println("No Modbus Point..........try to check");
									}
									System.out.println("Tag Wise Response ..... " + getRes);

									if (!getRes.isEmpty()) {
										Float hexVal = null;
										if (reg_type.equalsIgnoreCase("float")) {
											hexVal = hexToFloat(getRes);
											System.out.println("Value........" + reg_name + "........." + hexVal);
//											Meterdata mt = new Meterdata();
//											mt.setTime(new Timestamp(System.currentTimeMillis()));
//											mt.setVR(getRes);
//											mt.setValue("" + hexVal);
//											meterRepository.save(mt);
										}
										if (reg_type.equalsIgnoreCase("integer")) {
//											Meterdata mt = new Meterdata();
//											mt.setTime(new Timestamp(System.currentTimeMillis()));
//											mt.setSource_id(source_id);
//											mt.setValue("" + hexToInteger(getRes));
//											meterRepository.save(mt);
										}
									}
								}
							}

						} catch (Exception e) {
							System.out.println("Connection error due to: " + e);
						}
					}
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public synchronized String getCoilData(int slave_id, int reg_address, int reg_length, String headVal,
			SerialConnection con) {
		
		ModbusSerialTransaction trans = null;
		ReadCoilsRequest req = null;
		ReadCoilsResponse res = null;

		String getResponse = "";

		int slaveID = slave_id, ref = reg_address, registers = reg_length;
		StringBuilder sb = null;

		try {
			ModbusCoupler.getReference().setUnitID(slaveID);
			// ModbusCoupler.getReference().setMaster(false);

			req = new ReadCoilsRequest(ref, registers);
			req.setUnitID(slaveID);
			req.setHeadless();
			trans = new ModbusSerialTransaction(con);

			System.out.println("Sending request  -----slaveid--" + slaveID + "--Reference--" + ref + "--"
					+ registers + "-----Value--" + req.getHexMessage());

			trans.setRequest(req);
			trans.execute();
			BitVector bitvector = ((ReadCoilsResponse) trans.getResponse()).getCoils();
			bitvector.forceSize(registers);
			System.out.println("Get responce  1  :::::" + bitvector);
			String str = "" + bitvector;

			if (str == null) {
				boolean anyLeft = true;
				int k = 0;
				while (anyLeft == true) {
					try {
						wait(10000);

					} catch (Exception th) {
					}
					trans.setRequest(req);
					trans.execute();
					bitvector = ((ReadCoilsResponse) trans.getResponse()).getCoils();
					bitvector.forceSize(registers);
					str = "" + bitvector;
					if (str != null) {
						anyLeft = false;
					}
					k++;

					if (k == 30) {
						anyLeft = false;

					}
				}
			}

			sb = new StringBuilder(str);
			sb.reverse();

			System.out.println("Get responce  2 reverse value  :::::" + sb);

			con.close();
		} catch (Exception ex) {
			System.out.println("Reading Error - " + ex);

		}

		return "" + sb.toString().trim();
	}

	public synchronized String getDISCRETESData(int slave_id, int reg_address, int reg_length, String headVal,
			SerialConnection con) {
		
		ModbusSerialTransaction trans = null;
		ReadInputDiscretesRequest req = null;
		ReadInputRegistersResponse res = null;

		String getResponse = "";

		int slaveID = slave_id, ref = reg_address, registers = reg_length;

		try {

			ModbusCoupler.getReference().setUnitID(slaveID);
			// ModbusCoupler.getReference().setMaster(false);

			req = new ReadInputDiscretesRequest(ref, registers);
			req.setUnitID(slaveID);
			req.setHeadless();
			trans = new ModbusSerialTransaction(con);

			System.out.println("Sending request  -----slaveid--" + slaveID + "--Reference--" + ref + "--" + registers
					+ "-----Value--" + req.getHexMessage());

			trans.setRequest(req);
			trans.execute();
			res = (ReadInputRegistersResponse) trans.getResponse();

			if (res == null) {

				boolean anyLeft = true;
				int k = 0;
				while (anyLeft == true) {
					try {
						wait(10000);

					} catch (Exception th) {
					}
					trans.setRequest(req);
					trans.execute();
					res = (ReadInputRegistersResponse) trans.getResponse();
					if (res != null) {
						anyLeft = false;
					}
					k++;

					if (k == 30) {
						anyLeft = false;

					}
				}

			}
			if (res != null) {
				getResponse = res.getHexMessage();
				getResponse = getResponse.replaceAll(" ", "");
				if (!headVal.isEmpty()) {
					getResponse = getResponse.substring(6, getResponse.length());
				}

			}

			con.close();
		} catch (Exception ex) {
			System.out.println("Reading Error - " + ex);

		}

		System.out.println("Response From the Meter - " + getResponse);
		return getResponse;
	}

	public synchronized String getData(int slave_id, int reg_address, int reg_length, String headVal,
			SerialConnection con) {
		
		ModbusSerialTransaction trans = null;
		ReadInputRegistersRequest req = null;
		ReadInputRegistersResponse res = null;

		String getResponse = "";
		int slaveID = slave_id, ref = reg_address, registers = reg_length;
		try {
			ModbusCoupler.getReference().setUnitID(slaveID);
			// ModbusCoupler.getReference().setMaster(false);
			req = new ReadInputRegistersRequest(ref, registers);
			req.setUnitID(slaveID);
			req.setHeadless();
			trans = new ModbusSerialTransaction(con);

			System.out.println("Sending request  -----slaveid--" + slaveID + "--Reference--" + ref + "--" + registers
					+ "-----Value--" + req.getHexMessage());

			trans.setRequest(req);
			trans.execute();
			res = (ReadInputRegistersResponse) trans.getResponse();
			if (res == null) {

				boolean anyLeft = true;
				int k = 0;
				while (anyLeft == true) {
					try {
						wait(10000);

					} catch (Exception th) {
					}
					trans.setRequest(req);
					trans.execute();
					res = (ReadInputRegistersResponse) trans.getResponse();
					if (res != null) {
						anyLeft = false;
					}
					k++;

					if (k == 30) {
						anyLeft = false;

					}
				}
			}

			if (res != null) {
				getResponse = res.getHexMessage();
				getResponse = getResponse.replaceAll(" ", "");
				if (headVal.isEmpty()) {
					getResponse = getResponse.substring(6, getResponse.length());
				}

			}

			con.close();
		} catch (Exception ex) {
			System.out.println("Reading Error - " + ex);

		}

		System.out.println("Response From the Meter - " + getResponse);
		return getResponse;

	}

	public synchronized String getDataLNT(int slave_id, int reg_address, int reg_length, String headval,
			SerialConnection con) {

		ModbusSerialTransaction trans = null;
		ReadMultipleRegistersRequest req = null;
		ReadMultipleRegistersResponse res = null;

		String getResponse = "";

		int slaveID = slave_id, ref = reg_address, registers = reg_length;

		try {

			ModbusCoupler.getReference().setUnitID(slaveID);
			// ModbusCoupler.getReference().setMaster(false);

			req = new ReadMultipleRegistersRequest(ref, registers);
			req.setUnitID(slaveID);
			req.setHeadless();
			trans = new ModbusSerialTransaction(con);
			
			System.out.println("Slave id : "+slave_id+" ref: "+ref+" registers: "+registers);//debugging line

			System.out.println("Sending request  -----slaveid--" + slaveID + "--Reference--" + ref + "--" + registers
					+ "-----Value--" + req.getHexMessage());

			trans.setRequest(req);
			trans.execute();
			res = (ReadMultipleRegistersResponse) trans.getResponse();
			if (res == null) {

				boolean anyLeft = true;
				int k = 0;
				while (anyLeft == true) {
					try {
						wait(10000);

					} catch (Exception th) {
					}
					trans.setRequest(req);
					trans.execute();
					res = (ReadMultipleRegistersResponse) trans.getResponse();
					if (res != null) {
						anyLeft = false;
					}
					k++;

					if (slaveID == 1) {
						if (k == 60) {
							anyLeft = false;

						}
					} else {
						if (k == 30) {
							anyLeft = false;

						}

					}
				}

			}
			if (res != null) {
				getResponse = res.getHexMessage();
				getResponse = getResponse.replaceAll(" ", "");

				//if (headval.isEmpty()) {
					getResponse = getResponse.substring(6, getResponse.length());
				//}

			}

			con.close();
//			System.out.println("---- Connection Closed!! ----");

		} catch (Exception ex) {
			System.out.println("Reading Error - " + ex);

		}

		System.out.println("Response From the Meter - " + getResponse);
		return getResponse;

	}

	public static boolean containsOnlyNumbers(String str) {
		// It can't contain only numbers if it's null or empty...
		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			// If we find a non-digit character we return false.
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}

		return true;
	}

	public static Float hexToFloat(String value) {
		Float f = null;

		String value1 = value.substring(4, 8);
		String value2 = value.substring(0, 4);
		String val = value1 + value2;
		// System.out.print(val);
		try {

			Long i = Long.parseLong(val, 16);
			f = Float.intBitsToFloat(i.intValue());

		} catch (Exception e) {
			System.out.println(e);
		}
		return f;
	}

	public static Integer hexToInteger(String value, int conversion) {
		Integer f = null;

		try {

			Integer i = Integer.parseInt(value, 16);
			f = i / conversion;

		} catch (Exception e) {
			System.out.println(e);
		}
		return f;
	}

	public static Integer hexToInteger(String value) {
		Integer f = null;

		try {

			Integer i = Integer.parseInt(value, 16);
			f = i;

		} catch (Exception e) {
			System.out.println(e);
		}
		return f;
	}

}
