package dao;

import dal.Contract;
import dal.Dallayer;

public class DaoBridge{
	private static Contract cn=null;
	public static Contract getDalObject() throws Exception {
		if(cn==null) {
			cn=new Dallayer();
		}
		return cn;
	}
	

}
