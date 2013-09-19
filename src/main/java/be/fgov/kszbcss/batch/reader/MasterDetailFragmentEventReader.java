package be.fgov.kszbcss.batch.reader;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.springframework.batch.item.xml.stax.DefaultFragmentEventReader;
import org.springframework.dao.DataAccessResourceFailureException;

public class MasterDetailFragmentEventReader extends DefaultFragmentEventReader {

	private QName masterFragmentQName;

	private QName detailFragmentQName;

	private boolean readingMasterFragment = false;

	private boolean fakeCloseMasterFragment = false;

	private boolean fakeDocumentEnd = false;

	private LinkedList<QName> openedMasterTags = new LinkedList<QName>();

	public MasterDetailFragmentEventReader(XMLEventReader wrappedEventReader, QName masterFragmentQName,
			QName detailFragmentQName) {
		super(wrappedEventReader);
		this.masterFragmentQName = masterFragmentQName;
		this.detailFragmentQName = detailFragmentQName;
	}

	@Override
	public void markStartFragment() {
		try {
			QName fragmentQName = peek().asStartElement().getName();
			if (fragmentQName.getLocalPart().equals(masterFragmentQName.getLocalPart())) {
				openedMasterTags.clear();
				readingMasterFragment = true;
			}
		} catch (XMLStreamException e) {
			throw new DataAccessResourceFailureException("Error reading XML stream", e);
		}
		super.markStartFragment();
	}

	@Override
	public XMLEvent nextEvent() throws XMLStreamException {
		if (fakeDocumentEnd) {
			throw new NoSuchElementException();
		} else if (fakeCloseMasterFragment) {
			if (openedMasterTags.isEmpty()) {
				fakeDocumentEnd = true;
				return XMLEventFactory.newFactory().createEndDocument();
			} else {
				return XMLEventFactory.newFactory().createEndElement(openedMasterTags.removeLast(),
						null);
			}			
		} else {
			XMLEvent next = super.nextEvent();
			if (readingMasterFragment && !fakeCloseMasterFragment && next != null) {
				if (next.isStartElement()) {
					openedMasterTags.add(next.asStartElement().getName());
				} else if (next.isEndElement()) {
					openedMasterTags.removeLast();
				}
			}
			return next;
		}
	}

	@Override
	public XMLEvent peek() throws XMLStreamException {
		if (fakeDocumentEnd) {
			return null;
		}
		XMLEvent peeked = super.peek();
		if (readingMasterFragment && peeked != null && peeked.isStartElement()) {
			if (peeked.asStartElement().getName().getLocalPart().equals(detailFragmentQName.getLocalPart())) {
				fakeCloseMasterFragment = true;
			}
		}
		if (fakeCloseMasterFragment) {
			if (openedMasterTags.isEmpty()) {
				return XMLEventFactory.newFactory().createEndDocument();
			} else {
				return XMLEventFactory.newFactory().createEndElement(openedMasterTags.getLast(),
						null);
			}
		} else {
			return peeked;	
		}		
	}


	@Override
	public void markFragmentProcessed() {
		super.markFragmentProcessed();
		reset();
	}

	@Override
	public void reset() {
		super.reset();
		this.openedMasterTags.clear();
		this.readingMasterFragment = false;
		this.fakeCloseMasterFragment = false;
		this.fakeDocumentEnd = false;
	}

}
