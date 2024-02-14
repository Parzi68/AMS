package com.project.ams;
import net.wimpi.modbus.ModbusCoupler; 
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

public class ModbusRTUExample{
	
		public static void main(String args[])
		{
			SerialConnection con = null;
			try{
	              SerialParameters params = new SerialParameters();
				  params.setPortName("COM3");
				  params.setBaudRate("9600");
				  params.setDatabits("8");
				  params.setParity("none");
				  params.setStopbits("1"); // only for hubli
				  params.setEncoding("RTU");
				  params.setEcho(false);
				  con = new SerialConnection(params);
				  
				  if(!con.isOpen()){
					   con.open();
			    }
				  
				  String getRes=getDataLNT(4,107,2,"",con);
				  
				  
			}
			catch (Exception e) {
				// 	
			}
		}
										//	Slave ID   RegisterAddress RegisterLength					
		 public static String getDataLNT(int SlaveId,int reference,int register,String headVal,SerialConnection con){

			 	ModbusSerialTransaction trans 		= null;
				ReadMultipleRegistersRequest req 	= null;
				ReadMultipleRegistersResponse res 	= null;

			String getResponse	=	"";
			String response = "";

			int			slaveID		=	SlaveId,
						ref			=	reference,
						registers	=	register;
					
			 try	{	
				
			  ModbusCoupler.getReference().setUnitID(slaveID);
			  //ModbusCoupler.getReference().setMaster(false);
			  
			  req =new ReadMultipleRegistersRequest(ref, registers);
			  req.setUnitID(slaveID);
			  req.setHeadless();
			  trans = new ModbusSerialTransaction(con);
			  System.out.println("Reading............");
			 
			   System.out.println("Sending request  -----slaveid--"+slaveID+"--Reference--"+ref+"--"+registers+"-----Value--"+req.getHexMessage());
			 
			  trans.setRequest(req);
			  trans.execute();
			   res = (ReadMultipleRegistersResponse) trans.getResponse();
			      if(res==null){
				   
				   boolean anyLeft = true;
				  int k=0;
				   while (anyLeft == true) {
					     
					trans.setRequest(req);
			        trans.execute();
			        res = (ReadMultipleRegistersResponse) trans.getResponse();
					 if(res!=null){
						 anyLeft = false;
					}
					   k++;
					   
					      if(slaveID==1){
						   if(k==60){
							   anyLeft = false;
							   
						   }
					 }else{
						   if(k==30){
							   anyLeft = false;
							   
						   }
						 
					 }
				   }
				   
			   }
			   if(res!=null){
			   response = res.getHexMessage();
			   getResponse = res.getHexMessage();
			   getResponse=getResponse.replaceAll(" ","");
			   
			   //if( !peUtil.isNullString(headVal))	{
					getResponse	=	getResponse.substring(6,getResponse.length());
			   //}
					
					con.close();
			   }
			 
		            
		            // Perform hex to float conversion
		            float floatValue = hexToFloat(getResponse);
		            //System.out.println("Hex Value: " + getResponse);
		            System.out.println("Response from meter: " + response);
		            System.out.println("Response in Float - "+floatValue);
			
			  
			   
			 }	                                    
			  
			  		catch (Exception ex) {
				  System.out.println("Reading Error - "+ex);
			 
				
			}
			
			 System.out.println("Response in hexadecimal - "+getResponse);
			 
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
