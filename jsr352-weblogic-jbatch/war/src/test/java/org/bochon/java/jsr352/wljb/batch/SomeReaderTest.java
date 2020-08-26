package org.bochon.java.jsr352.wljb.batch;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.io.Serializable;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(CdiRunner.class)
public class SomeReaderTest
{
   @InjectMocks
   private SomeReader someReader;

   @Mock
   private Logger logger;

   private final static Serializable CHECKPOINT_ALWAYS_NULL = null;

   @Test
   public void partition0with0item() throws IllegalAccessException
   {
      // GIVEN
      final Integer partitionId = 0;
      FieldUtils.writeField(someReader, "partitionIdProperty", partitionId.toString(), true);

      // WHEN // THEN
      someReader.open(CHECKPOINT_ALWAYS_NULL);

      assertNull(someReader.readItem());
   }

   @Test
   public void partition5with500items() throws IllegalAccessException
   {
      // GIVEN
      final Integer partitionId = 5;
      final int itemCount = 50;
      FieldUtils.writeField(someReader, "partitionIdProperty", partitionId.toString(), true);

      // WHEN // THEN
      someReader.open(CHECKPOINT_ALWAYS_NULL);

      for (int index = 0; index < itemCount; ++index)
      {
         final Object item = someReader.readItem();
         assertTrue(item instanceof String);
         assertEquals(format("partition%d-item%d", partitionId, index), item);
      }

      assertNull(someReader.readItem());
   }

}