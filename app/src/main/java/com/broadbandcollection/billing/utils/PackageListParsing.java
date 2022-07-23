/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.broadbandcollection.billing.utils;

import com.broadbandcollection.billing.obj.PackageList;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class PackageListParsing extends DefaultHandler {


	PackageList _packageList;

	private static Map<String, PackageList> mapPackageList;
	private String str_area_code,str_area_code_filter;

	// booleans that check whether it's in a specific tag or not
	private boolean _inTable, _inPlanName, _inPackageRate, _inAreaCode,
			_inAreaCodeFilter,_inPackageValidity,_inServiceTax,_inPackageId,sr_no,package_desc,offer_desc,offer_validity,offerpackagedesc;

	public PackageListParsing(String data) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = null;
		XMLReader xr = null;
		try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		try {
			xr = sp.getXMLReader();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		try {
			xr.setContentHandler(this);
			xr.parse(new InputSource(new ByteArrayInputStream(data
					.getBytes("UTF8"))));



		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		mapPackageList = new Hashtable<String, PackageList>();
		_packageList = new PackageList();
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		setMapPackageList(mapPackageList);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		// Log.i("tag start",localName);

		if (localName.equals("Table")) {
			_inTable = true;
		} else if (localName.equals("PlanName")) {
			_inPlanName = true;
		} else if (localName.equals("PlanAmount")) {
			_inPackageRate = true;
		} else if (localName.equals("AreaCode")) {
			_inAreaCode = true;
		} else if (localName.equals("AreaCodeFilter")) {
			_inAreaCodeFilter = true;
		}
		else if (localName.equals("PackageValidity")) {
			_inPackageValidity = true;
		}
		else if (localName.equals("ServiceTax")) {
			_inServiceTax = true;
		}
		else if (localName.equals("PackageId")) {
			_inPackageId = true;
		}else if(localName.equals("SrNo")){
			sr_no= true;
		}else if(localName.equals("PackageDesc")){
			package_desc= true;
		}else if(localName.equals("Offer")){
			offer_desc =true;
		}else if(localName.equals("offerValidity")){
			offer_validity =true;
		}else if(localName.equals("offerpackageDesc")){
			offerpackagedesc =true;
		}
		// super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		// Log.i("tag start",new String(ch,start,length));
		// super.characters(ch, start, length);
		String chars = new String(ch, start, length);
		chars = chars.trim();

		if (_inPlanName) {
			_packageList.setPlanName(chars.toString());
		} else if (_inPackageRate) {
			_packageList.setPackageRate(chars.toString());
		} else if (_inAreaCode) {
			_packageList.setAreaCode(chars.toString());
		} else if (_inAreaCodeFilter) {
			_packageList.setAreaCodeFilter(chars.toString());
		}
		else if (_inPackageValidity) {
			_packageList.setPackagevalidity(chars.toString());
		}
		else if (_inServiceTax) {
			_packageList.setServiceTax(chars.toString());
		}
		else if (_inPackageId) {
			_packageList.setPackageId(chars.toString());
		}else if (sr_no) {
			_packageList.setSrno(Integer.parseInt(chars.toString()));
		}else if (package_desc) {
			_packageList.setPackagedesc(chars.toString());
		}else if (offer_desc) {
			_packageList.setOfferdesc(chars.toString());
		}else if (offer_validity) {
			_packageList.setOffervalidityc(chars.toString());
		}else if (offerpackagedesc) {
			_packageList.setOfferpackagedesc(chars.toString());
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		// Log.i("tag end",localName);
		if (localName.equals("PlanName")) {
			_inPlanName = false;
		} else if (localName.equals("PlanAmount")) {
			_inPackageRate = false;
		} else if (localName.equals("AreaCode")) {
			_inAreaCode = false;
		} else if (localName.equals("AreaCodeFilter")) {
			_inAreaCodeFilter = false;
		}
		else if (localName.equals("PackageValidity")) {
			_inPackageValidity = false;
		}
		else if (localName.equals("ServiceTax")) {
			_inServiceTax = false;
		}
		else if (localName.equals("PackageId")) {
			_inPackageId = false;
		}else if (localName.equals("SrNo")) {
			sr_no = false;
		}else if (localName.equals("PackageDesc")) {
			package_desc = false;
		}else if (localName.equals("Offer")) {
			offer_desc = false;
		}else if (localName.equals("offerValidity")) {
			offer_validity = false;
		}else if (localName.equals("offerpackageDesc")) {
			offerpackagedesc = false;
		}else if (localName.equals("Table")) {
			_inTable = false;

			String key = _packageList.getPlanName();
			//mapPackageList.put(_packageList.getPlanName(), _packageList);
			mapPackageList.put(key, _packageList);
			_packageList = new PackageList();

		}

		// super.endElement(uri, localName, qName);

	}

	/**
	 * @return the mapPackageList
	 */
	public static Map<String, PackageList> getMapPackageList() {
		return mapPackageList;
	}

	/**
	 * @param mapPackageList
	 *            the mapPackageList to set
	 */
	public static void setMapPackageList(Map<String, PackageList> mapPackageList) {
		PackageListParsing.mapPackageList = mapPackageList;
	}
}
