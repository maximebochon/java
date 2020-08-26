package org.bochon.java.jsr352.wljb.batch;

import org.slf4j.Logger;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;

import static java.lang.Thread.currentThread;

@Named("some-mapper")
@Dependent
public class SomeMapper implements PartitionMapper
{
   @Inject
   private Logger logger;

   @Override
   public PartitionPlan mapPartitions()
   {
      logger.debug("thread={}", currentThread().getId());

      final int partitionCount = 10;
      final int threadCount = 1;

      final Properties[] propertyMapArray = new Properties[partitionCount];

      for (int index = 0; index < partitionCount; ++index)
      {
         propertyMapArray[index] = new Properties();
         propertyMapArray[index].setProperty("partition-id", String.valueOf(index));
      }

      return new PartitionPlanImpl()
      {
         /**
          * {@inheritDoc}
          * @see PartitionPlanImpl#getPartitions()
          */
         @Override
         public int getPartitions()
         {
            logger.debug("thread={} partitionCount={}", currentThread().getId(), partitionCount);

            return partitionCount;
         }

         /**
          * {@inheritDoc}
          * @see PartitionPlanImpl#getThreads()
          */
         @Override
         public int getThreads()
         {
            logger.debug("thread={} threadCount={}", currentThread().getId(), threadCount);

            return threadCount;
         }

         /**
          * {@inheritDoc}
          * @see PartitionPlanImpl#getPartitionProperties()
          */
         @Override
         public Properties[] getPartitionProperties()
         {
            logger.debug("thread={} propertyMapArray={}", currentThread().getId(), propertyMapArray);

            return propertyMapArray;
         }
      };
   }
}
