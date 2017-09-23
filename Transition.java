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
 * Transition.java
 *
 *
 * Created: 2017-08-25
 *
 * @author Emmanuel Desmontils
 * @version 1.0
 */

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
}

class EpsilonTransition extends Transition {
	public EpsilonTransition(String s, String c) {
		super(s,"",c);
	}
}