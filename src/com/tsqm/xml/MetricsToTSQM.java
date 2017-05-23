package com.tsqm.xml;
/*
 * This class manipulates other classes to
 * convert xml files to csv using xsl style shets
 * for transformation
 * Olaye, E
 */
import java.io.File;
import java.io.IOException;




//import java. org.eclipse.core.internal.*;

public class MetricsToTSQM {
	
	public static void main() {
		if (sTransformMetricsToTSQM()[0]=="Sucess")
			System.out.println("Success");
	}
	
	public String[] TransformMetricsToTSQM(){
		String path;
		String filename;
		String output[] = {};
		//Create a file in the current path
		File tmp = new File("");		//OR:  path = ${PROJECT_LOC}.path;
		//NB: We can use a dialog box to get the path
		path = tmp.getAbsolutePath();
		filename="phpresult.xml";

		//Create the full filename for the XML file
		File xmlFile1 = new File(path + File.separatorChar + filename);
		
		System.out.println("The XML input filename is" + xmlFile1.getPath());


		//Convert the XML file to CSV
		XMLtoCSVConverter cvt = new XMLtoCSVConverter("","phpresult.xml", "");
		try {
			cvt.convertMetricsXMLToCSV();
			System.out.println("File converted to CSV");
			System.out.printf("Conversion of %s Done. Saved to %s \n",cvt.getInputXMLFilename(),cvt.getDestCSV());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			output[0]="Fail";
		}
		
		output[0]="Sucess";
		
		return output;
	}
		
	
	public static String[] sTransformMetricsToTSQM(){
	
		String path;
		String filename;
		String output[] = {};
		//Create a file in the current path
		File tmp = new File("");		//OR:  path = ${PROJECT_LOC}.path;
		//NB: We can use a dialog box to get the path
		path = tmp.getAbsolutePath();
		filename="phpresult.xml";

		//Create the full filename for the XML file
		File xmlFile1 = new File(path + File.separatorChar + filename);
		
		System.out.println("The XML input filename is" + xmlFile1.getPath());


		//Convert the XML file to CSV
		XMLtoCSVConverter cvt = new XMLtoCSVConverter("","phpresult.xml", "");
		try {
			cvt.convertMetricsXMLToCSV();
			System.out.println("File converted to CSV");
			System.out.printf("Conversion of %s Done. Saved to %s \n",cvt.getInputXMLFilename(),cvt.getDestCSV());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			output[0]="Fail";
		}
		
		output[0]="Sucess";
		
		return output;
	}
	}


