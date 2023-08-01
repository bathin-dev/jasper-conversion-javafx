package com.conv.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class ConversionUtil {
	
	public static void convert(String in, String out) throws JRException {
		JasperCompileManager.compileReportToFile(in, out);
	}
	
}
