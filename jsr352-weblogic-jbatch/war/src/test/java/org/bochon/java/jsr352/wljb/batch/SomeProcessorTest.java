package org.bochon.java.jsr352.wljb.batch;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CdiRunner.class)
public class SomeProcessorTest
{

   @InjectMocks
   private SomeProcessor someProcessor;

   @Mock
   private Logger logger;

   @Test
   public void processItem()
   {
      // GIVEN
      final String providedItem = "test";
      final String expectedItem = "TEST";

      // WHEN
      final Object processedItem = someProcessor.processItem(providedItem);

      // THEN
      assertTrue(processedItem instanceof String);
      assertEquals(expectedItem, processedItem);
   }

}