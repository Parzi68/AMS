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
				  
				  String getRes=	getDataLNT(4,99,2,"",con);
				  
				  System.out.println("Reading............"+getRes);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		 public static String getDataLNT(int SlaveId,int reference,int register,String headVal,SerialConnection con){

			 	ModbusSerialTransaction trans 		= null;
				ReadMultipleRegistersRequest req 	= null;
				ReadMultipleRegistersResponse res 	= null;

			String getResponse	=	"";

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
			   getResponse = res.getHexMessage();
			   getResponse=getResponse.replaceAll(" ","");
			   
			   //if( !peUtil.isNullString(headVal))	{
					getResponse	=	getResponse.substring(6,getResponse.length());
			   //}

			   }
			
			  con.close();
			   
			                                    
			  
			  } catch (Exception ex) {
				  System.out.println("Reading Error - "+ex);
			 
				
			}
			
			 System.out.println("Response From Meter - "+getResponse);
			return getResponse;
				
			}

}
