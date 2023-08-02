package com.conv.jasper;

import net.sf.jasperreports.engine.JRException;

public class JasperConversionCLI {

	public static void main(String[] args) {
		String in = args[0];
		String out = args[1];
		
		try {
			ConversionUtil.convert(in, out);
			
			System.out.println("Convert success, check output at file: " + out);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

}
