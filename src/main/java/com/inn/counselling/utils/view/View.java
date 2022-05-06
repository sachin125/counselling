package com.inn.counselling.utils.view;

public class View {

	public static interface BasicView{ }
	
	public static interface EducationBasicView extends BasicView{ }
	
	public static interface AddressBasicView extends BasicView{ }
	
	public static interface WorkBasicView extends BasicView{ }

	public static interface UserConfigView extends BasicView{ }
	
	public static interface FamilyView extends BasicView{ }
	
	public static interface UserInContextView extends BasicView,UserConfigView{ }
	
	public static interface UserBasicView extends BasicView,FamilyView,WorkBasicView{ }
	
	public static interface UserProfileBasicView extends BasicView{ }
	
	public static interface UserAllInformationView extends UserBasicView,AddressBasicView,
	EducationBasicView,WorkBasicView,UserProfileBasicView, UserConfigView,FamilyView{}
	
	public static interface NoView{}
	
}
