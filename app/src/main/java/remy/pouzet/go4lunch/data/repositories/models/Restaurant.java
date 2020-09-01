package remy.pouzet.go4lunch.data.repositories.models;

import java.io.Serializable;

/**
 * Created by Remy Pouzet on 14/07/2020.
 */
public class Restaurant implements Serializable {
	String mphoneNumber;
	String mWebsite;
	double mlat;
	double mlon;
	private String mplaceID;
	
	private String mUrlImage;
	private String mName;
	
	private String mAdress;
	private String mHorair;
	private String mDistance;
	private int    mWorkmatesInterrested;
	private double mEvaluation;
	
	public Restaurant(String placeID,
                      String urlImage,
                      String name,
                      String adress,
                      String horair,
                      String distance,
                      int workmates,
                      double evaluation,
                      String phoneNumber,
                      String webSite,
                      double lat,
                      double lon) {
		mplaceID = placeID;
		mUrlImage = urlImage;
		mName     = name;
		mAdress               = adress;
		mHorair               = horair;
		mDistance             = distance;
		mWorkmatesInterrested = workmates;
		mEvaluation           = evaluation;
		mphoneNumber          = phoneNumber;
		mWebsite              = webSite;
		mlon                  = lon;
		mlat                  = lat;
		
	}
	
	public String getWebsite() {
		return mWebsite;
	}
	
	public void setWebsite(String parameterWebsite) {
		mWebsite = parameterWebsite;
	}
	
	public String getMplaceID() {
		return mplaceID;
	}
	
	public void setMplaceID(String parameterMplaceID) {
		mplaceID = parameterMplaceID;
	}
	
	public String getMphoneNumber() {
		return mphoneNumber;
	}

	
	public void setMphoneNumber(String parameterMphoneNumber) {
		mphoneNumber = parameterMphoneNumber;
	}
	
	public double getMlat() {
		return mlat;
	}
	
	public void setMlat(double parameterMlat) {
		mlat = parameterMlat;
	}
	
	public double getMlon() {
		return mlon;
	}
	
	public void setMlon(double parameterMlon) {
		mlon = parameterMlon;
	}
	
	public String getUrlImage() {
		return mUrlImage;
	}
	
	public void setUrlImage(String parameterUrlImage) {
		mUrlImage = parameterUrlImage;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String parameterName) {
		mName = parameterName;
	}
	
	public String getAdress() {
		return mAdress;
	}
	
	public void setAdress(String parameterAdress) {
		mAdress = parameterAdress;
	}
	
	public String getHorair() {
		return mHorair;
	}
	
	public void setHorair(String parameterHorair) {
		mHorair = parameterHorair;
	}
	
	public String getDistance() {
		return mDistance;
	}
	
	public void setDistance(String parameterDistance) {
		mDistance = parameterDistance;
	}
	
	public int getWorkmatesInterrested() {
		return mWorkmatesInterrested;
	}
	
	public void setWorkmatesInterrested(int parameterWorkmatesInterrested) {
		mWorkmatesInterrested = parameterWorkmatesInterrested;
	}
	
	public double getEvaluation() {
		return mEvaluation;
	}
	
	public void setEvaluation(double parameterEvaluation) {
		mEvaluation = parameterEvaluation;
	}
	
	public void setEvaluation(Double parameterEvaluation) {
		mEvaluation = parameterEvaluation;
	}
}
