package edu.chalmers.project.data;

import java.util.Comparator;


	public class MatchComparable implements Comparator<Match>{
		 
	    @Override
	    public int compare(Match m1, Match m2) {
	        if(m1.getDate().compareTo(m2.getDate())>0)
	        	return 1;
	        else if(m1.getDate().equals(m2.getDate()))
	        	return m1.getTime().compareTo(m2.getTime());
	        else {
				return -1;
			}
	    }
	}