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
 * JFSM.java
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

import JFSM.*;
import JFSM.Transducteur.*;

public class JFSM {
    public static void main(String argv []) throws JFSMException {

      Set<String> A = new HashSet<String>();      
      A.add("a");A.add("b");A.add("c");

      Set<Etat> Q = new HashSet<Etat>();
      Q.add(new Etat("1"));Q.add(new Etat("2"));
      Q.add(new Etat("3"));Q.add(new Etat("4"));Q.add(new Etat("5"));

      Set<Transition> mu = new HashSet<Transition>();
      mu.add(new Transition("1","a","2"));
      mu.add(new Transition("1","b","4"));
      mu.add(new Transition("2","b","3"));
      mu.add(new Transition("2","c","4"));
      mu.add(new Transition("3","a","2"));
      mu.add(new Transition("3","b","4"));
      mu.add(new Transition("4","a","5"));
      mu.add(new Transition("5","c","5"));


      Set<String> F = new HashSet<String>();
      F.add("5");
      F.add("4");
      F.add("1");
      Automate afn = new AFD(A, Q, "1", F, mu);

      List<String> l = new ArrayList<String>();
      l.add("a");l.add("b");l.add("a");l.add("c");

      System.out.println(afn);
      System.out.println(afn.run(l));
      System.out.println(afn.emonder());

      System.out.println(afn);
      System.out.println("Epsilon Libre ? "+afn.epsilonLibre());

      afn.save("test.jff");

      Automate afn2 = Automate.load("essai.jff");
      System.out.println(afn2.getClass().getName());
      System.out.println(afn2);

   }
}
