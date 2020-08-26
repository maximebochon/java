package org.bochon.java.jsr352.wljb.batch;

import org.slf4j.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.bochon.java.jsr352.wljb.util.Nap.nap;

@Named("some-writer")
@Dependent
public class SomeWriter extends AbstractItemWriter
{
   @Inject
   private Logger logger;

   @Inject
   @BatchProperty(name = "duration")
   private String durationInMilliseconds;

   @Override
   public void writeItems(final List<Object> items)
   {
      logger.debug("writeItems");

      final List<String> itemList = items.stream().map(i -> (String) i).collect(toList());

      nap(durationInMilliseconds);

      logger.info("writeItems: {}", itemList);
   }
}
