package magirator.beans;

import java.util.ArrayList;

public class ListHolder {
    private ArrayList<String> filterable;
    private ArrayList<String> sortable;
    private ArrayList<Object> items;
    private String addView;

    public void setAddView(String s){
		this.addView = s;
	}

    public String getAddView(){
		return this.addView;
	}
}