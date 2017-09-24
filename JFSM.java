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
 * FSM.java
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


class JFSMException extends Exception {
	/** 
	* Crée une nouvelle instance de JFSMException 
	*/  
	public JFSMException() {}  
	/** 
	* Crée une nouvelle instance de JFSMException 
	* @param message Le message détaillant exception 
	*/  
	public JFSMException(String message) {  
		super(message); 
	}  
	/** 
	* Crée une nouvelle instance de JFSMException 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public JFSMException(Throwable cause) {  
		super(cause); 
	}  
	/** 
	* Crée une nouvelle instance de JFSMException 
	* @param message Le message détaillant exception 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public JFSMException(String message, Throwable cause) {  
		super(message, cause); 
	}
}

abstract class Automate {
	public Map<String,Etat> Q;
	public Set<String> F, I;
	public Set<String> A;
	public List<String> histo;
	//protected Map<String,Map<String,Transition>> mu2;
	Set<Transition> mu;
	String current;
	Etat trash;

	class Trash extends Etat {
		public Trash(){
			super("---Trash---");
		}
	}

	public Automate(Set<String> A, Set<Etat> Q, Set<String> I, Set<String> F, Set<Transition> mu) throws JFSMException {
		// Ajout de l'alphabet
		assert A.size()>0 : "A ne peut pas être vide" ;
		this.A = A;
		this.mu = new HashSet<Transition>();

		// Ajout des états
		assert Q.size()>0 : "Q ne peut pas être vide" ;
		this.Q = new HashMap<String,Etat>();

		// Création de l'état poubelle... pour faciliter l'exécution...
		trash = new Trash();
		this.Q.put(trash.name,trash);
		Iterator<String> a = this.A.iterator();
		Transition t;
		String s;
		while(a.hasNext()){
			s = a.next();
			t = new Transition(trash.name, s, trash.name);
			this.mu.add(t);
		}		

		Iterator<Etat> q = Q.iterator();
		while(q.hasNext()) {
			Etat e = q.next();
			if (this.Q.containsKey(e.name)) {
				System.out.println("Etat dupliqué ! Seule la première version sera conservée.");
			} else {
				this.Q.put(e.name,e);
			}
		}

		// Création de l'historique (chemin)
		this.histo = new ArrayList<String>();

		// Ajout des transitions
		this.mu.addAll(mu);

		// On ajoute les transitions vers l'état poubelle
		Iterator<Etat> itE = this.Q.values().iterator();
		while (itE.hasNext()){
			Etat e = itE.next();
			Iterator<String> itA = this.A.iterator();
			while (itA.hasNext()) {
				s = itA.next();
				int nb = 0;
				Iterator<Transition> itT = this.mu.iterator();
				while(itT.hasNext()){
					t = itT.next();
					if ((t.source==e.name) && (t.symbol == s)) nb += 1;
				}
				if (nb==0) {
					t = new Transition(e.name, s, trash.name);
					this.mu.add(t);
				}
			}
		}	

		// this.mu2 = new HashMap<String,Map<String,Transition>>();
		// Iterator<Transition> j = mu.iterator();
		// while(j.hasNext()) {
		// 	Transition t = j.next();
		// 	String s = t.source+t.symbol ;
		// 	Map<String,Transition> m = this.mu2.get(s);
		// 	if (m == null) {
		// 		this.mu2.put(s, new HashMap<String,Transition>());
		// 		this.mu2.get(s).put(t.name, t);
		// 	} else {
		// 		if (m.containsKey(t.name)) {
		// 			System.out.println("Transition dupliquée ! Seule la première version sera conservée.");
		// 		} else {
		// 			m.put(t.name,t);
		// 		}				
		// 	}
		// }

		// On collecte les états initiaux, on les positionne comme tel. S'il n'existe pas, il est oublié.
		// assert I.size()>0 : "I ne peut pas être vide" ;
		this.I = new HashSet<String>();
		Iterator<String> i = I.iterator();
		while(i.hasNext()) {
			String n = i.next();
			setInitial(n);
		}

		// On collecte les états finaux, on les positionne comme tel. S'il n'existe pas, il est oublié.
		this.F = new HashSet<String>();
		Iterator<String> f = F.iterator();
		while(f.hasNext()) {
			String n = f.next();
			setFinal(n);
		}
	}

	public void addTransition(Transition t) {
		mu.add(t);
	}

	public void addEtat(Etat e){
		if (!Q.containsKey(e))
			Q.put(e.name,e);
	}

	public Etat getEtat(String n) {
		if (Q.containsKey(n))
			return Q.get(n);
		else return null;
	}

	public void setA(Set<String> A){
		this.A = A;
	}

	public void setInitial(String e) throws JFSMException {	
		if (Q.containsKey(e)) {
			I.add(e);
			Etat etat = Q.get(e);
		} else throw new JFSMException("Etat absent:"+e);
	}

	public void setFinal(String e) throws JFSMException {	
		if (Q.containsKey(e)) {
			F.add(e);
			Etat etat = Q.get(e);
		} else throw new JFSMException("Etat absent:"+e);
	}

	public boolean isInitial(String e){
		return I.contains(e);
	}

	public boolean isFinal(String e){
		return F.contains(e);
	}

	public void init() {
		histo.clear();
	}

	public boolean next(String symbol) {
		histo.add(current);
		return true;
	}

	public boolean accepte(){return F.contains(current);}

	public abstract boolean run(List<String> l) ;
}

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
			ok = (t.source==current) && (t.symbol == symbol);
		}
		if (ok) {
			current = t.cible;
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

public class JFSM {
    public static void main(String argv []) throws JFSMException {

    	Set<String> Ae = new HashSet<String>();      
    	Ae.add("a");Ae.add("b");Ae.add("c");

    	Set<Etat> Q = new HashSet<Etat>();
    	Q.add(new Etat("1"));Q.add(new Etat("2"));Q.add(new Etat("3"));

    	Set<Transition> mu = new HashSet<Transition>();
    	mu.add(new Transition("1","a","2"));
    	mu.add(new Transition("1","b","3"));
    	mu.add(new Transition("2","a","1"));
    	mu.add(new Transition("2","c","3"));
    	mu.add(new Transition("2","b","2"));
    	mu.add(new Transition("3","b","2"));

    	Set<String> F = new HashSet<String>();
    	F.add("3");
    	Automate afn = new AFD(Ae, Q, "1", F, mu);
    	afn.next("a");
    	afn.next("c");
    	System.out.println(afn.accepte());
    	
    	List<String> l = new ArrayList<String>();
    	l.add("a");l.add("c");
    	afn.run(l);
    	System.out.println(afn.accepte());
    	System.out.println(afn.histo);
   }
}
