package org.bochon.java.jsr352.wljb.util;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

public class Nap
{
   public static void nap(final String durationInMilliseconds)
   {
      try
      {
         nap(Long.valueOf(durationInMilliseconds));
      }
      catch (final NumberFormatException e)
      {
         nap(INTEGER_ZERO);
      }
   }

   private static void nap(final long durationInMilliseconds)
   {
      try
      {
         Thread.sleep(durationInMilliseconds);
      }
      catch (final InterruptedException e)
      {
         // one can interrupt a nap, not a big deal
      }
   }
}
