package org.bochon.java.jsr352.wljb.batch;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import javax.batch.api.partition.PartitionPlan;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(CdiRunner.class)
public class SomeMapperTest
{
   @InjectMocks
   private SomeMapper someMapper;

   @Mock
   private Logger logger;

   @Test
   public void mapPartitions()
   {
      final PartitionPlan partitionPlan = someMapper.mapPartitions();

      // THEN
      assertNotNull(partitionPlan);
      assertEquals(1, partitionPlan.getThreads());
      assertEquals(10, partitionPlan.getPartitions());
      assertNotNull(partitionPlan.getPartitionProperties());
      assertEquals(10, partitionPlan.getPartitionProperties().length);

      for (int index = 0; index < 10; ++index)
      {
         final Properties properties = partitionPlan.getPartitionProperties()[index];
         assertNotNull(properties);
         assertEquals(1, properties.size());

         final String partitionId = properties.getProperty("partition-id");
         assertNotNull(partitionId);
         assertEquals(String.valueOf(index), partitionId);
      }
   }
}
