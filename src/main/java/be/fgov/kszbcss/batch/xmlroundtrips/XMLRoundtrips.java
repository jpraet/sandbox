package be.fgov.kszbcss.batch.xmlroundtrips;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.util.JAXBResult;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import be.fgov.kszbcss.batch.jaxb.DetailType;
import be.fgov.kszbcss.batch.jaxb.TransformedDetailType;

public class XMLRoundtrips {

	public static void main(String[] args) throws Exception {
		// JAXB -> XSLT -> DOMSource
		JAXBContext jaxbContext = JAXBContext.newInstance("be.fgov.kszbcss.batch.jaxb");
		DetailType detail = new DetailType();
		detail.setValue("DETAIL");
		JAXBElement<DetailType> element = new JAXBElement<DetailType>(new QName("http://kszbcss.fgov.be/batch",
				"detail"), DetailType.class, detail);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Templates templates = transformerFactory.newTemplates(new StreamSource(XMLRoundtrips.class
				.getResourceAsStream("/transform.xslt")));
		Transformer transformer = templates.newTransformer();
		DOMResult domResult = new DOMResult();
		transformer.transform(new JAXBSource(jaxbContext, element), domResult);
		System.out.println(domResult.getNode());
		StringWriter sw = new StringWriter();
		transformerFactory.newTransformer().transform(new DOMSource(domResult.getNode()), new StreamResult(sw));
		System.out.println(sw.toString());

		// Source -> XSLT -> JAXB
		Source detailSource = new StreamSource(XMLRoundtrips.class.getResourceAsStream("/detail.xml"));
		JAXBResult jaxbResult = new JAXBResult(jaxbContext);
		transformer = templates.newTransformer();
		transformer.transform(detailSource, jaxbResult);
		JAXBElement<TransformedDetailType> output = (JAXBElement<TransformedDetailType>) jaxbResult.getResult();
		System.out.println(output.getValue().getTransformedValue());
	}

}
