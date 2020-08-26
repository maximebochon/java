package org.bochon.java.jsr352.wljb.batch;

import org.slf4j.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.bochon.java.jsr352.wljb.util.Nap.nap;

@Named("some-reader")
@Dependent
public class SomeReader extends AbstractItemReader
{
   @Inject
   private Logger logger;

   @Inject
   @BatchProperty(name = "duration")
   private String durationInMilliseconds;

   @Inject
   @BatchProperty(name = "partition-id")
   private String partitionIdProperty;

   private Iterator<String> itemIterator = null;

   private static final int itemFactor = 10;

   @Override
   public void open(final Serializable checkpoint)
   {
      final int partitionId = parseInt(partitionIdProperty);

      final List<String> itemList = IntStream.range(0, partitionId * itemFactor)
         .mapToObj(index -> format("partition%d-item%d", partitionId, index))
         .collect(toList());

      if (logger.isDebugEnabled())
      {
         logger.debug("open: partitionId={}, items={}",
            partitionIdProperty, itemList
         );
      }

      nap(durationInMilliseconds);

      itemIterator = itemList.iterator();
   }

   @Override
   public Object readItem()
   {
      String item = null;

      if (itemIterator != null && itemIterator.hasNext())
      {
         item = itemIterator.next();
      }

      if (logger.isDebugEnabled())
      {
         logger.debug("readItem: partitionId={}, {}",
            partitionIdProperty, ((item != null) ? "item read" : "no more item")
         );
      }

      nap(durationInMilliseconds);

      return item;
   }
}
