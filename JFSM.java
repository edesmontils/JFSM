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

class Etat {
	public String name;
	public boolean isInitial, isFinal;

	public Etat(String n) {
		this.name = n;
		isInitial = false;
		isFinal = false;
	}

	public void action(Context ctx) {}
}

class Trash extends Etat {
	public Trash(){
		super("---Trash---");
	}
}

class Transition {
	public String name;
	public String source, cible;
	public String symbol;

	public Transition(String s, String symbol, String c) {
		this.symbol = symbol;
		this.source = s;
		this.cible = c;
		this.name = s+"-"+symbol+"->"+c;
	}

	public Transition(String s, String c) {
		this(s,"",c);
	}

	public boolean isEpsilon(){
		return symbol == null;
	}

	public void action(Context ctx) {}
}

class EpsilonTransition extends Transition {
	public EpsilonTransition(String s, String c) {
		super(s,"",c);
	}
}

class Context {

}

abstract class Automate {
	protected Map<String,Etat> Q;
	protected Set<String> F, I;
	protected Set<String> A;
	Set<Transition> mu;
	Context ctx;
	String current;
	int statut;
	Etat trash;

	public Automate(Set<String> A, Set<Etat> Q, Set<Transition> mu, Context ctx) {
		assert A.size()>0 : "A ne peut pas être vide" ;
		this.A = A;
		this.mu = new HashSet<Transition>();
		assert Q.size()>0 : "Q ne peut pas être vide" ;
		this.Q = new HashMap<String,Etat>();

		trash = new Trash();
		this.Q.put(trash.name,trash);
		Iterator<String> ia = this.A.iterator();
		Transition t;
		String s;
		while(ia.hasNext()){
			s = ia.next();
			t = new Transition(trash.name, s, trash.name);
			this.mu.add(t);
		}		

		Iterator<Etat> i = Q.iterator();
		while(i.hasNext()) {
			Etat e = i.next();
			if (this.Q.containsKey(e.name)) {
				System.out.println("Etat dupliqué ! Seule la première version sera conservée.");
			} else {
				this.Q.put(e.name,e);
			}
		}

		histo = new ArrayList<String>();

		this.mu.addAll(mu);

		this.I = new HashSet<String>();
		this.F = new HashSet<String>();

		this.ctx = ctx;
	}

	public Automate(Set<String> A, Set<Etat> Q, Set<Transition> mu) {this(A,Q,mu,new Context());}

	public void addTransition(Transition t) {
		mu.add(t);
	}

	public void addEtat(Etat e){
		Q.put(e.name,e);
	}

	public Etat getEtat(String n) {
		return Q.get(n);
	}

	public void setA(Set<String> A){
		this.A = A;
	}

	public void setInitial(String e) {	
		if (Q.containsKey(e)) {
			I.add(e);
			Etat etat = Q.get(e);
			etat.isInitial = true;
		}
	}

	public void setFinal(String e) {	
		if (Q.containsKey(e)) {
			F.add(e);
			Etat etat = Q.get(e);
			etat.isFinal = true;
		}
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

		histo.add(current);
	}


	public abstract boolean run(String s, String sep) ;
}

class AFD extends Automate {
	private boolean estDeterministe;

	public AFD(Set<String> A, Set<Etat> Q, Set<Transition> mu, String i,  Context ctx){
		super(A,Q,mu,ctx);
		setInitial(i);
		assert testDeterminisme(this) : "L'automate doit être déterministe";
	}

	static public boolean testDeterminisme(Automate T) {
		if (T.I.size() !=1) {
			return false;
		} else {
			return true;
		}
		// return true;
	}


	}
	public boolean run(String s, String sep) {
		return true;
	}
}

public class JFSM {
    public static void main(String argv []) {

    	Set<String> Ae = new HashSet<String>();      
    	Ae.add("a");Ae.add("b");Ae.add("c");

    	Set<Etat> Q = new HashSet<Etat>();
    	Q.add(new Etat("1"));Q.add(new Etat("2"));Q.add(new Etat("3"));

    	Set<Transition> mu = new HashSet<Transition>();
    	Transition t;
    	mu.add(new Transition("1","a","2"));
    	mu.add(new Transition("1","b","3"));
    	mu.add(new Transition("2","c","1"));
    	mu.add(new Transition("2","c","3"));
    	mu.add(new Transition("2","b","2"));
    	mu.add(new Transition("3","b","2"));

    	afn.setFinal("3");
   }
}
