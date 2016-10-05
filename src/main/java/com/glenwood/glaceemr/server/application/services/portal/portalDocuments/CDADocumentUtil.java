package com.glenwood.glaceemr.server.application.services.portal.portalDocuments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class CDADocumentUtil{
	/**
	 * @param xmlSource - cda content as byte array
	 * @param styleSheetPath- main xsl path
	 * @return code as String
	 * @throws TransformerException
	 */
	public static String getHTMLCode(byte[] xmlSource, String styleSheetPath) throws TransformerException{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer(new StreamSource(styleSheetPath));   
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transformer.transform(new StreamSource(new ByteArrayInputStream(xmlSource)),new StreamResult(out));
		return new String(out.toByteArray());
	}
	
	/**
	 * @param xmlPath - cda file path
	 * @param styleSheetPath - main xsl path
	 * @return HTML code as String
	 * @throws TransformerException
	 */
	public static String getHTMLCode(String xmlPath, String styleSheetPath) throws TransformerException{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer(new StreamSource(styleSheetPath));   
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transformer.transform(new StreamSource(new File(xmlPath)),new StreamResult(out));
		return new String(out.toByteArray());
	}
	
}