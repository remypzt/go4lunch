package remy.pouzet.go4lunch.data.service.realAPI.POJOmatrix;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Row {
	
	@SerializedName("elements") private List<Element> mElements;
	
	public List<Element> getElements() {
		return mElements;
	}
	
	public void setElements(List<Element> elements) {
		mElements = elements;
	}
	
}
