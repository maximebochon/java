package org.bochon.java.jsr352.wljb.batch;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(CdiRunner.class)
public class SomeWriterTest
{
   @InjectMocks
   private SomeWriter someWriter;

   @Mock
   private Logger logger;

   @Test
   public void writeItems()
   {
      // GIVEN
      final List<String> stringList = asList("a", "b", "c");
      final List<Object> itemList = new ArrayList<>(stringList);

      // WHEN
      someWriter.writeItems(itemList);

      // THEN
      verify(logger, times(1)).info("writeItems: {}", stringList);
   }

}