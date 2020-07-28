package remy.pouzet.go4lunch.data.service;

/**
 * Created by Remy Pouzet on 28/07/2020.
 */

import android.os.Build;
import android.util.Log;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
	private static Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
			.addConverterFactory(GsonConverterFactory.create())
			
			.client(getNewHttpClient())
			.build();
	
	private static OkHttpClient getNewHttpClient() {
		OkHttpClient.Builder client = new OkHttpClient.Builder()
				.followRedirects(true)
				.followSslRedirects(true)
				.retryOnConnectionFailure(true)
				.cache(null)
				.connectTimeout(5, TimeUnit.SECONDS)
				.writeTimeout(5, TimeUnit.SECONDS)
				.readTimeout(5, TimeUnit.SECONDS);
		
		return enableTls12OnPreLollipop(client).build();
	}
	
	private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
		if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 22) {
			try {
				SSLContext sc = SSLContext.getInstance("TLSv1.2");
				sc.init(null, null, null);
				client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()), getX509Certificate());
				
				ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
						.tlsVersions(TlsVersion.TLS_1_2)
						.build();
				
				List<ConnectionSpec> specs = new ArrayList<>();
				specs.add(cs);
				specs.add(ConnectionSpec.COMPATIBLE_TLS);
				specs.add(ConnectionSpec.CLEARTEXT);
				
				client.connectionSpecs(specs);
			}
			catch (Exception exc) {
				Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
			}
		}
		
		return client;
	}
	
	private static X509TrustManager getX509Certificate() {
		return new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
			                               String authType) throws
			                                                CertificateException {
				try {
					chain[0].checkValidity();
				}
				catch (Exception e) {
					throw new CertificateException("Certificate not valid or trusted.");
				}
			}
			
			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
			                               String authType) throws
			                                                CertificateException {
				try {
					chain[0].checkValidity();
				}
				catch (Exception e) {
					throw new CertificateException("Certificate not valid or trusted.");
				}
			}
			
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[]{};
			}
		};
	}
	
	public static <S> S cteateService(Class<S> serviceClass) {
		return retrofit.create(serviceClass);
	}
	
}
