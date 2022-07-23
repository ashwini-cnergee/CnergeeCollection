/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.Marshals;

/**
 * 
 * 
 * Used to marshal Dates - crucial to serialization for SOAP
 */

import org.kobjects.isodate.IsoDate;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public class MarshalDate implements Marshal {

	public Object readInstance(XmlPullParser parser, String namespace,
			String name, PropertyInfo expected) throws IOException,
			XmlPullParserException {
		return IsoDate.stringToDate(parser.nextText(), IsoDate.DATE);
	}

	public void register(SoapSerializationEnvelope cm) {
		cm.addMapping(cm.xsd, "dateTime", Date.class, this);
	}

	public void writeInstance(XmlSerializer writer, Object obj)
			throws IOException {
		writer.text(IsoDate.dateToString((Date) obj, IsoDate.DATE));
	}

}
