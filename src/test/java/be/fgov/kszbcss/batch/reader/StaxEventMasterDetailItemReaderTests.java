package be.fgov.kszbcss.batch.reader;

import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;

import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import be.fgov.kszbcss.batch.reader.StaxEventMasterDetailItemReader;


/**
 * Tests for {@link StaxEventMasterDetailItemReader}.
 * 
 * @author Jimmy Praet
 */
public class StaxEventMasterDetailItemReaderTests {
	
	private String testXml = "<root>" +
			"<master><value>master 1</value><details><detail><value>detail 1.1</value></detail><detail><value>detail 1.2</value></detail></details></master>" +
			"<master><value>master 2</value><details><detail><value>detail 2.1</value></detail><detail><value>detail 2.2</value></detail></details></master>" +
			"<master><value>master 3</value></master>" +
			"<master><value>master 4</value><details><detail><value>detail 4.1</value></detail></details></master>" +
			"</root>";
	
	private StaxEventMasterDetailItemReader<Master, Detail> reader;
	
	@Test
	public void test() throws Exception {
		initReader();
		
		reader.open(new ExecutionContext());
		
		System.err.println(reader.read());
		System.err.println(reader.read());
		System.err.println(reader.read());
		System.err.println(reader.read());
		System.err.println(reader.read());
		System.err.println(reader.read());
		System.err.println(reader.read());
	}
	
	private void initReader() {
		reader = new StaxEventMasterDetailItemReader<Master, Detail>();
		reader.setMasterFragmentRootElementName("master");
		reader.setDetailFragmentRootElementName("detail");
		reader.setResource(new ByteArrayResource(testXml.getBytes()));
		reader.setUnmarshaller(new MasterDetailUnmarshaller());
	}
	
	public static class MasterDetailUnmarshaller implements Unmarshaller {

		@Override
		public boolean supports(Class<?> clazz) {
			return Master.class.equals(clazz) || Detail.class.equals(clazz);
		}

		@Override
		public Object unmarshal(Source source) throws IOException, XmlMappingException {
			try {
				XMLEventReader reader = StaxUtils.getXmlEventReader(source);
				String elementName = moveCursorToNextStartElement(reader);
				if ("master".equals(elementName)) {
					Master master = new Master();
					reader.nextEvent();
					reader.nextEvent();
					master.value = reader.nextEvent().asCharacters().getData();
					return master;
				} else if ("detail".equals(elementName)) {
					Detail detail = new Detail();
					reader.nextEvent();
					reader.nextEvent();
					detail.value = reader.nextEvent().asCharacters().getData();
					return detail;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		}

		private String moveCursorToNextStartElement(XMLEventReader reader) throws XMLStreamException {
			while (reader.hasNext()) {
				if (reader.peek().isStartElement()) {
					return reader.peek().asStartElement().getName().getLocalPart();
				}
				reader.nextEvent();
			}
			return null;
		}
		
	}

	public static class Master {
		public String value;
		
		@Override
		public String toString() {
			return value;
		}
	}

	public static class Detail {
		public String value;
		
		@Override
		public String toString() {
			return value;
		}
	}

}
