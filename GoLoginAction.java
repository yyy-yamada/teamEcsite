package com.internousdev.gerbera.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class GoLoginAction extends ActionSupport implements SessionAware{

	private Map<String, Object> session;

	public String execute(){

	    if(!session.containsKey("mCategoryList")) {
			return "sessionTimeOut";
		}

	    return SUCCESS;
	}

    public Map<String, Object> getSession(){
	    return session;
    }
    public void setSession(Map<String, Object> session){
	    this.session =session;
    }
}
