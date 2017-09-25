/**
 * 
 * Copyright (C) 2017 Emmanuel DESMONTILS
 * 
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 * 
 * 
 * 
 * E-mail:
 * Emmanuel.Desmontils@univ-nantes.fr
 * 
 * 
 **/


/**
 * AFD.java
 *
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;

public class Mealy extends Transducteur {

	public Mealy(Set<String> A, Set<Etat> Q, String i, Set<String> F, Set<Transition> mu) throws JFSMException {
		super(A,Q,i,F,mu);
		Iterator<Transition> it = mu.iterator();
		while(it.hasNext()){
			Transition t = it.next();
			assert t instanceof TransitionMealy : "Une transition n'est pas une TransitionMealy";
		}
	}

	public String nextT(String symbol) {
		assert A.contains(symbol) : "next() : le symbole doit être un symbole de l'alphabet." ;
		histo.add(current);
		Iterator<Transition> it = mu.iterator();
		boolean ok = false;
		Transition t = null ;
		while(it.hasNext() && (!ok)){
			t = it.next();
			ok = t.candidate(current,symbol);
		}
		if (ok) {
			current = t.appliquer();
			System.out.println(t.getClass()+":"+t.name);
			if (t.cible != trash.name) {
				TransitionMealy tm = (TransitionMealy)t;
				return tm.prod;
			} else return "";
		} else return null;
	}
}