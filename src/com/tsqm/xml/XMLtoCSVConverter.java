package com.tsqm.xml;
import org.w3c.dom.Document;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLtoCSVConverter {
	private String inputXMLFilename;
	private String srcXSLStylesheet;
	private String srcXML;
	private String destCSV;
	
	/**
	 * Constructor
	 * @param _srcXSLStylesheet The style sheet that should be used for transformation (default: sourceMetricsXSLFile.xsl)
	 * @param _srcXML The XML filename that will be processed (Default:metrics result brief.xml)
	 * @param _destCSV The destination CSV (Default: outputMetrics.csv)
	 */
	public XMLtoCSVConverter(String _srcXSLStylesheet, String _srcXML, String _destCSV){
		srcXSLStylesheet="phpXSLtoTSQM.xsl";
		srcXML="phpresult.xml";
		destCSV="phpresult.csv";
		
		srcXSLStylesheet=_srcXSLStylesheet;
		srcXML=_srcXML;
		destCSV=_destCSV;
		//TODO: Absolute file names
		if (_srcXSLStylesheet!="") srcXSLStylesheet=_srcXSLStylesheet;
		if (_srcXML!="") srcXML=_srcXML;
		if (_destCSV!="") destCSV=_destCSV;
	}
	
	public void convertMetricsXMLToCSV () throws Exception{
		
		File stylesheet = new File(srcXSLStylesheet);  
	    File xmlSource = new File(srcXML);

	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.parse(xmlSource);

	    StreamSource stylesource = new StreamSource(stylesheet);
	    Transformer transformer = TransformerFactory.newInstance()
			  .newTransformer(stylesource);
	    Source source = new DOMSource(document);
	    Result outputTarget = new StreamResult(new File(destCSV));
	    
	    transformer.transform(source, outputTarget);
	    System.out.printf("Conversion of %s Done. Saved to %s \n",xmlSource.toString(),destCSV.toString());
	  }
	

	/**
	 * @return the destCSV
	 */
	public String getDestCSV() {
		return destCSV;
	}

	public String getInputXMLFilename() {
		return inputXMLFilename;
	}


}



