package org.bochon.java.jsr352.wljb.util;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

import static java.lang.String.format;

@SessionScoped
public class Lock implements Serializable {

   @Override
   public String toString() {
      return format("Lock{%s}", System.identityHashCode(this));
   }

}

