package remy.pouzet.go4lunch.data.repositories.models;

/**
 * Created by Remy Pouzet on 14/07/2020.
 */
public class Restaurant {
	private String mplaceID;
	private int    mDrawableImage;
	private String mUrlImage;
	private String mName;
	private String mType;
	private String mAdress;
	private String mHorair;
	private String mDistance;
	private int    mWorkmatesInterrested;
	private int    mEvaluation;
	double mlat;
	double mlon;
	
	public Restaurant(String placeID,
	                  int drawableImage,
	                  String urlImage,
	                  String name,
	                  String type,
	                  String adress,
	                  String horair,
	                  String distance,
	                  int workmates,
	                  int evaluation,
	                  double lat,
	                  double lon) {
		mplaceID              = placeID;
		mDrawableImage        = drawableImage;
		mUrlImage             = urlImage;
		mName                 = name;
		mType                 = type;
		mAdress               = adress;
		mHorair               = horair;
		mDistance             = distance;
		mWorkmatesInterrested = workmates;
		mEvaluation           = evaluation;
		mlon                  = lon;
		mlat                  = lat;
		
	}
	
	public String getMplaceID() {
		return mplaceID;
	}
	
	public void setMplaceID(String parameterMplaceID) {
		mplaceID = parameterMplaceID;
	}
	
	public int getDrawableImage() {
		return mDrawableImage;
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
	
	public void setDrawableImage(int parameterDrawableImage) {
		mDrawableImage = parameterDrawableImage;
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
	
	public String getType() {
		return mType;
	}
	
	public void setType(String parameterType) {
		mType = parameterType;
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
	
	public int getEvaluation() {
		return mEvaluation;
	}
	
	public void setEvaluation(int parameterEvaluation) {
		mEvaluation = parameterEvaluation;
	}
}
