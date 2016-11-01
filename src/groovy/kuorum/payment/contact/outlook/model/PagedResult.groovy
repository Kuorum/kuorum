package kuorum.payment.contact.outlook.model

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagedResult<T> {
	//@JsonProperty("@odata.nextLink")
	private String nextPageLink;
	private ArrayList<T> value;

	public String getNextPageLink() {
		return nextPageLink;
	}
	public void setNextPageLink(String nextPageLink) {
		this.nextPageLink = nextPageLink;
	}
	public ArrayList<T> getValue() {
		return value;
	}
	public void setValue(T[] value) {
		this.value = value;
	}
}