package remy.pouzet.go4lunch.data.service.realAPI.POJOmatrix;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponseOfMatrix {
	
	@SerializedName("destination_addresses") private List<String> mDestinationAddresses;
	@SerializedName("origin_addresses") private      List<String> mOriginAddresses;
	@SerializedName("rows") private                  List<Row>    mRows;
	@SerializedName("status") private                String       mStatus;
	
	public List<String> getDestinationAddresses() {
		return mDestinationAddresses;
	}
	
	public void setDestinationAddresses(List<String> destinationAddresses) {
		mDestinationAddresses = destinationAddresses;
	}
	
	public List<String> getOriginAddresses() {
		return mOriginAddresses;
	}
	
	public void setOriginAddresses(List<String> originAddresses) {
		mOriginAddresses = originAddresses;
	}
	
	public List<Row> getRows() {
		return mRows;
	}
	
	public void setRows(List<Row> rows) {
		mRows = rows;
	}
	
	public String getStatus() {
		return mStatus;
	}
	
	public void setStatus(String status) {
		mStatus = status;
	}
	
}
