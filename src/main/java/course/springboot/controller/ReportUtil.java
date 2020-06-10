package course.springboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable {
	
	/* retorn pdf in byte*/ 
	public byte[]  generateReport(List  listData, String report, 
			ServletContext servletContext) throws Exception{
		
		/* create data list for report*/
		JRBeanCollectionDataSource jDataSource = new JRBeanCollectionDataSource(listData);
		
		String jasperPath = servletContext.getRealPath("report")
				+ File.separator + report + ".jasper";
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath , new HashMap(),jDataSource);
	
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
}
