package magirator.beans;

import java.util.HashMap;
import java.util.List;

public class ListContainer {
	
	private String[] sortOptions;
	private HashMap filterOptions;
	private List<ListItem> listItems;
	
	public String[] getSortOptions() {
		return sortOptions;
	}
	public void setSortOptions(String[] sortOptions) {
		this.sortOptions = sortOptions;
	}
	public HashMap getFilterOptions() {
		return filterOptions;
	}
	public void setFilterOptions(HashMap filterOptions) {
		this.filterOptions = filterOptions;
	}
	public List<ListItem> getListItems() {
		return listItems;
	}
	public void setListItems(List<ListItem> listItems) {
		this.listItems = listItems;
	}

}
