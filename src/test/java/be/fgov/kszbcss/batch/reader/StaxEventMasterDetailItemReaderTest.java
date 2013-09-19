package be.fgov.kszbcss.batch.reader;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import be.fgov.kszbcss.batch.item.MasterDetailItem;
import be.fgov.kszbcss.batch.jaxb.DetailType;
import be.fgov.kszbcss.batch.jaxb.MasterType;

/**
 * Tests for {@link StaxEventMasterDetailItemReader}.
 * 
 * @author Jimmy Praet
 */
public class StaxEventMasterDetailItemReaderTest {

	private StaxEventMasterDetailItemReader<JAXBElement<MasterType>, JAXBElement<DetailType>> reader;

	private Jaxb2Marshaller marshaller;

	@Before
	public void setUp() throws Exception {
		reader = new StaxEventMasterDetailItemReader<JAXBElement<MasterType>, JAXBElement<DetailType>> ();
		reader.setMasterFragmentRootElementName("master");
		reader.setDetailFragmentRootElementName("detail");
		reader.setResource(new ClassPathResource("/master-detail.xml"));
		marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("be.fgov.kszbcss.batch.jaxb");
		marshaller.afterPropertiesSet();
		reader.setUnmarshaller(marshaller);
	}

	@Test
	public void test() throws Exception {
		reader.open(new ExecutionContext());
		MasterDetailItem<JAXBElement<MasterType>, JAXBElement<DetailType>>  item = null;
		while ((item = reader.read()) != null) {
			System.err.println("M:" + item.getMaster().getValue().getValue() + " - D:"
					+ (item.getDetail() == null ? "null" : item.getDetail().getValue().getValue()));
		}
	}

}
