package be.fgov.kszbcss.batch.item;

import org.springframework.batch.item.ItemCountAware;


/**
 * MasterDetailItem interface.
 * 
 * @author Jimmy Praet
 * 
 * @param <M>
 *            the master item type
 * @param <D>
 *            the detail item type
 */
public class MasterDetailItem<M, D> implements ItemCountAware {

	private M master;

	private D detail;

	private int itemCount;

	private int masterCount;

	private int detailCount;

	public M getMaster() {
		return master;
	}

	public void setMaster(M master) {
		this.master = master;
	}

	public D getDetail() {
		return detail;
	}

	public void setDetail(D detail) {
		this.detail = detail;
	}

	public int getItemCount() {
		return itemCount;
	}

	@Override
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getMasterCount() {
		return masterCount;
	}

	public void setMasterCount(int masterCount) {
		this.masterCount = masterCount;
	}

	public int getDetailCount() {
		return detailCount;
	}

	public void setDetailCount(int detailCount) {
		this.detailCount = detailCount;
	}

	@Override
	public String toString() {
		return "MasterDetailItem [master=" + master + ", detail=" + detail + ", itemCount=" + itemCount
				+ ", masterCount=" + masterCount + ", detailCount=" + detailCount + "]";
	}
	
}
