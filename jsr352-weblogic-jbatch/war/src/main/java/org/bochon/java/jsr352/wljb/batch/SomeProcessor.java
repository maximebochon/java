package org.bochon.java.jsr352.wljb.batch;

import org.slf4j.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.bochon.java.jsr352.wljb.util.Nap.nap;

@Named("some-processor")
@Dependent
public class SomeProcessor implements ItemProcessor
{
   @Inject
   @BatchProperty(name = "duration")
   private String durationInMilliseconds;

   @Inject
   private Logger logger;

   @Override
   public Object processItem(final Object item)
   {
      logger.debug("Processing item: {}", item);

      nap(durationInMilliseconds);

      return upperCase((String) item);
   }
}
