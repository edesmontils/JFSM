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

class AFD extends Automate {
	private String i;

	public AFD(Set<String> A, Set<Etat> Q, String i, Set<String> F, Set<Transition> mu) throws JFSMException {
		super(A,Q,new HashSet<String>(),F,mu);
		assert this.Q.containsKey(i) : "i n'est pas un état" ;
		setInitial(i);
		this.i = i;
		assert testDeterminisme(this) : "L'automate doit être déterministe";
		init();
	}

	public void init() {
		super.init();
		current = i;
	}

	static public boolean testDeterminisme(Automate T) {
		if (T.I.size() !=1) { // A compléter !
			// un seul état initial
			return false;
		} else {
			boolean ok = true;
			Iterator<Transition> itT = T.mu.iterator();
			Transition t ;
			while(itT.hasNext() && ok){
				t = itT.next();
				ok = !t.isEpsilon();
			}
			if (ok) {
				Iterator<Etat> itE = T.Q.values().iterator();
				while (itE.hasNext() && ok){
					Etat e = itE.next();
					Iterator<String> itA = T.A.iterator();
					while (itA.hasNext() && ok) {
						String a = itA.next();
						int nb = 0;
						itT = T.mu.iterator();
						while(itT.hasNext()){
							t = itT.next();
							if ((t.source==e.name) && (t.symbol == a)) nb += 1;
						}
						ok = nb <2 ;
					}
				}	
				return ok;
			} else return false;
		}
	}

	public boolean next(String symbol) {
		super.next(symbol);
		Iterator<Transition> it = mu.iterator();
		boolean ok = false;
		Transition t = null ;
		while(it.hasNext() && (!ok)){
			t = it.next();
			ok = t.candidate(current,symbol);
		}
		if (ok) {
			current = t.appliquer();
			return true;
		} else return false;
	}

	public boolean run(List<String> l) {
		String symbol;
		boolean ok = false;
		init();
		Iterator<String> il = l.iterator();
		while(il.hasNext()){
			symbol = il.next();
        	next(symbol);
		}
		return isFinal(current);
	}
}

class AFD2 extends AFD {// TODO.....
	protected Map<String,Map<String,Transition>> mu2;


	public AFD2(Set<String> A, Set<Etat> Q, String i, Set<String> F, Set<Transition> mu) throws JFSMException {
		super(A,Q,i,F,mu);

		// nouvelle structure de données pour optimiser la recherche des transitions
		this.mu2 = new HashMap<String,Map<String,Transition>>();
		Iterator<Transition> j = mu.iterator();
		while(j.hasNext()) {
			Transition t = j.next();
			String s = t.source+t.symbol ;
			Map<String,Transition> m = this.mu2.get(s);
			if (m == null) {
				this.mu2.put(s, new HashMap<String,Transition>());
				this.mu2.get(s).put(t.name, t);
			} else {
				if (m.containsKey(t.name)) {
					System.out.println("Transition dupliquée ! Seule la première version sera conservée.");
				} else {
					m.put(t.name,t);
				}				
			}
		}
	}

}
