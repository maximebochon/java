package org.bochon.java.jsr352.wljb.servlet;

import org.bochon.java.jsr352.wljb.util.Lock;
import org.slf4j.Logger;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static javax.batch.runtime.BatchRuntime.getJobOperator;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

public class SomeServlet extends HttpServlet {

   @Inject
   private Lock lock;

   @Inject
   private Logger logger;

   private static final String JOB_NAME = "some-job";

   private static final List<String> COMPONENT_LIST = asList("reader", "processor", "writer");

   private static final int DEFAULT_DURATION = 10;

   private static final int EXECUTION_LIST_COUNT = 15;

   enum Operation {
      START,
      STOP
   }

   private class Data
   {
      Operation operation;
      Long execution;
      String message;
      Properties jobParameters;

      Data(final HttpServletRequest request)
      {
         setOperation(request.getParameter("operation"));
         setExecution(request.getParameter("execution"));
         setJobParameters(request);
      }

      private void setOperation(final String operationString)
      {
         this.operation = !isEmpty(operationString)
            ? Operation.valueOf(operationString)
            : null;
      }

      private void setExecution(final String executionString)
      {
         this.execution = !isEmpty(executionString)
            ? Long.valueOf(executionString)
            : null;
      }

      private void setJobParameters(final HttpServletRequest request)
      {
         this.jobParameters = new Properties();

         for (final String component : COMPONENT_LIST)
         {
            final String parameterName = format("%s-duration", component);

            final String parameterCandidateValue = request.getParameter(parameterName);
            final String parameterActualValue = !isEmpty(parameterCandidateValue)
               ? parameterCandidateValue
               : Integer.toString(DEFAULT_DURATION);

            this.jobParameters.setProperty(parameterName, parameterActualValue);
         }
      }
   }

   /**
    * Processes requests for both HTTP
    * <code>GET</code> and
    * <code>POST</code> methods.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    */
   private void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException
   {
      final Data data = new Data(request);

      operateJob(request, data);

      renderPage(response, data);
   }

   private void operateJob(final HttpServletRequest request, final Data data)
   {
      if (data.operation == null) return;

      switch (data.operation)
      {
         case START: startJob(request, data); break;
         case STOP: stopExecution(request, data); break;
         default: throw new IllegalArgumentException();
      }
   }

   private void startJob(final HttpServletRequest request, final Data data)
   {
      try
      {
         data.execution = getJobOperator().start(JOB_NAME, data.jobParameters);
         data.message = format("Starting execution <u>%s</u> of job <i>%s</i>.", data.execution, JOB_NAME);
         logger.info(data.message);
      }
      catch (final Exception e)
      {
         data.execution = null;
         data.message = format("Unable to start job <i>%s</i>: %s", JOB_NAME, e.toString());
         logger.error(data.message);
      }
   }

   private void stopExecution(final HttpServletRequest request, final Data data)
   {
         if (data.execution == null)
         {
            data.message = "Please provide an <i>execution</i> parameter for the <i>stop</i> operation.";
            logger.error(data.message);
            return;
         }

         try
         {
            getJobOperator().stop(data.execution);
            data.message = format("Stopping execution <u>%s</u> of job <i>%s</i>.", data.execution, JOB_NAME);
            logger.info(data.message);
         }
         catch (final Exception e)
         {
            data.message = format("Unable to stop execution <u>%s</u> of job <i>%s</i> because:<br/>%s", data.execution, JOB_NAME, e.toString());
            logger.error(data.message);
         }
   }

   private void renderPage(final HttpServletResponse response, final Data data) throws ServletException
   {
      response.setContentType("text/html;charset=UTF-8");

      try (final PrintWriter page = response.getWriter())
      {
         page.println("<html>");
         page.println("<head>");
         page.println("<title>JSR-352 Experiment</title>");
         page.println("</head>");
         page.println("<body>");
         page.println("<h1>JSR-352 Experiment</h1>");
         page.println("<p>Simple batch application to experiment with JSR-352.</p>");
         page.println("<br/>");

         // ----

         page.println("<form>");

         page.println("<h2>Duration (in milliseconds)</h2>");
         page.println("<table border=\"yes\">");
         page.println("<tr>");

         for (final String component : COMPONENT_LIST)
         {
            page.format("<th>%s</th>%n", component);
         }

         page.println("</tr>");
         page.println("<tr>");

         for (final String component : COMPONENT_LIST)
         {
            final String parameterName = format("%s-duration", component);
            final String parameterValue = data.jobParameters.getProperty(parameterName);
            page.format("<td><input type=\"number\" name=\"%s\" value=\"%s\" min=\"0\" max=\"9999\" step=\"1\"/></td>%n", parameterName, parameterValue);
         }

         page.println("</tr>");
         page.println("</table>");
         page.println("<br/>");

         // ----

         page.println("<h2>Controls</h2>");
         page.format("<p><input type=\"submit\" name=\"operation\" value=\"%s\"/>", Operation.START.name());
         page.format(" a new execution of job <i>%s</i>.</p>%n", JOB_NAME);

         page.format("<p><input type=\"submit\" name=\"operation\" value=\"%s\"/>", Operation.STOP.name());
         page.format(" execution <input type=\"text\" name=\"execution\" value=\"%s\"/>.</p>%n", (data.execution != null) ? data.execution : EMPTY);

         page.format("<p><input type=\"submit\" name=\"refresh\" value=\"REFRESH\"/>");
         page.format(" status of submitted jobs.</p>%n");

         page.format("<h3>%s</h3>%n", defaultString(data.message));

         page.println("</form>");
         page.println("<br/>");

         // ----

         displayJobDetails(page);

         // ----

         page.println("</body>");
         page.println("</html>");

      }
      catch (Exception e)
      {
         throw new ServletException(e);
      }
   }

   private void displayJobDetails(final PrintWriter page)
   {
      page.println("<h2>Jobs</td></h2>");
      page.println("<table border=\"yes\">");
      page.format("<tr>%s</tr>%n",
         Stream.of(
            "Job Name", "ExecutionID</th>", "Batch Status", "Exit Status", "Start Time Status", "End Time"
         ).map(title -> "<th>" + title + "</th>").collect(joining(EMPTY))
      );

      JobOperator jobOperator = getJobOperator();
      try
      {
         for (final JobInstance jobInstance : jobOperator.getJobInstances(JOB_NAME, INTEGER_ZERO, EXECUTION_LIST_COUNT))
         {
            for (final JobExecution jobExecution : jobOperator.getJobExecutions(jobInstance))
            {
               page.println("<tr>");
               page.println(
                  Stream.of(
                     jobExecution.getJobName(),
                     jobExecution.getExecutionId(),
                     jobExecution.getBatchStatus(),
                     jobExecution.getExitStatus(),
                     jobExecution.getStartTime(),
                     jobExecution.getEndTime())
                  .map(value -> "<td>" + value + "</td>")
                  .collect(joining(EMPTY))
               );
               page.println("</tr>");
            }
         }
      }
      catch (final Exception e)
      {
         page.format("<h3>%s</h3>%n", e);
      }
      page.println("</table>");
   }

   /**
    * Handles the HTTP
    * <code>GET</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException
   {
      processRequest(request, response);
   }

   /**
    * Handles the HTTP
    * <code>POST</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException
   {
      processRequest(request, response);
   }

   /**
    * Returns a short description of the servlet.
    *
    * @return a String containing servlet description
    */
   @Override
   public String getServletInfo()
   {
      return "";
   }
}
