package com.audamob.audasingers.stromae.model;

import java.io.Serializable;
import java.util.Comparator;

import android.graphics.drawable.Drawable;
	
public	class ApplicationOnPhone implements java.lang.Comparable , Serializable{
        private String appname = "";
        private String pname = "";
        private String versionName = "";
        private int versionCode = 0;
        private Drawable icon;
        private Boolean Lock=false;
        private int Id;
        private void prettyPrint() {
           // Log.v(appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
        }
		public void setAppname(String appname) {
			this.appname = appname;
		}
		public String getAppname() {
			return appname;
		}
		public void setPname(String pname) {
			this.pname = pname;
		}
		public String getPname() {
			return pname;
		}
		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}
		public String getVersionName() {
			return versionName;
		}
		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}
		public int getVersionCode() {
			return versionCode;
		}
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}
		public Drawable getIcon() {
			return icon;
		}
		public void setId(int id) {
			this.Id = id;
		}
		public int getId() {
			return Id;
		}
		@Override
		public int compareTo(Object paramT) {
			// TODO Auto-generated method stub
			  String Pinfo1 = ((ApplicationOnPhone) paramT).getAppname(); 
		      String Pinfo2 = this.getAppname();
		     int val= Pinfo1.compareTo(Pinfo2);
		     
			return -val;
		}
		
    }
	


