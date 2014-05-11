package com.audamob.audasingers.stromae.view.services;

import java.io.IOException;
import java.util.ArrayList;

public interface IServiceDataUpdater<T>  {
	
	public ArrayList<T> getExistingDataFromCache() throws IOException, ClassNotFoundException;
	
	public void setNewDataInCache(ArrayList<T> s) throws IOException;
	
	public void setNewDataCountInCache(int count) throws IOException;

	public ArrayList<T>  getDataFromServer(String serverUrl);

}
