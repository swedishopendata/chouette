package fr.certu.chouette.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import fr.certu.chouette.common.ChouetteException;
import fr.certu.chouette.model.neptune.JourneyPattern;
import fr.certu.chouette.model.neptune.Line;
import fr.certu.chouette.model.neptune.Route;
import fr.certu.chouette.model.neptune.VehicleJourney;
import fr.certu.chouette.model.neptune.VehicleJourneyAtStop;
import fr.certu.chouette.model.neptune.type.TransportModeNameEnum;
import fr.certu.chouette.plugin.exchange.IImportPlugin;
import fr.certu.chouette.plugin.report.Report;
import fr.certu.chouette.plugin.report.ReportItem;
import fr.certu.chouette.plugin.validation.report.CheckPointReportItem;
import fr.certu.chouette.plugin.validation.report.DetailReportItem;
import fr.certu.chouette.plugin.validation.report.PhaseReportItem;
import fr.certu.chouette.plugin.validation.report.PhaseReportItem.PHASE;
import fr.certu.chouette.validation.checkpoint.VehicleJourneyCheckPoints;

@ContextConfiguration(locations = { "classpath:testContext.xml",
      "classpath*:chouetteContext.xml" })
public class ValidationVehicleJourneys extends AbstractValidation
{
   private VehicleJourneyCheckPoints checkPoint;
   private JSONObject fullparameters;
   private VehicleJourney bean1;
   private VehicleJourney bean2;
   private List<VehicleJourney> beansFor4 = new ArrayList<>();
   
   @BeforeGroups (groups = { "vehicleJourney" })
   public void init()
   {
      checkPoint = (VehicleJourneyCheckPoints) applicationContext
            .getBean("vehicleJourneyCheckPoints");
      
      long id = 1;

      fullparameters = null;
      try
      {
         fullparameters = new RuleParameterSet();
         fullparameters.put("check_vehicle_journey","1");

         bean1 = new VehicleJourney();
         bean1.setId(id++);
         bean1.setObjectId("test1:VehicleJourney:1");
         bean1.setName("test1");
         bean2 = new VehicleJourney();
         bean2.setId(id++);
         bean2.setObjectId("test2:VehicleJourney:1");
         bean2.setName("test2");
   
         beansFor4.add(bean1);
         beansFor4.add(bean2);
      } 
      catch (Exception e)
      {
         fullparameters = null;
         e.printStackTrace();
      }
      
   }
   
   @Test(groups = { "vehicleJourney" }, description = "4-VehicleJourney-1 no test")
   public void verifyTest4_1_notest() throws ChouetteException
   {
      // 4-VehicleJourney-1 : check columns
      Assert.assertNotNull(fullparameters, "no parameters for test");

      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      fullparameters.put("check_vehicle_journey","0");
      checkPoint.check(beansFor4, fullparameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      Assert.assertFalse(report.hasItem("4-VehicleJourney-1"), " report must not have item 4-VehicleJourney-1");

      fullparameters.put("check_vehicle_journey","1");
      report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(beansFor4, fullparameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      Assert.assertTrue(report.hasItem("4-VehicleJourney-1"), " report must have item 4-VehicleJourney-1");
      Assert.assertEquals(report.getItem("4-VehicleJourney-1").getItems().size(), 0, " checkpoint must have no detail");

   }
   
   @Test(groups = { "vehicleJourney" }, description = "4-VehicleJourney-1 unicity")
   public void verifyTest4_1_unique() throws ChouetteException
   {
      // 4-VehicleJourney-1 : check columns
      Assert.assertNotNull(fullparameters, "no parameters for test");

      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      // unique
      JSONObject column = fullparameters.getJSONObject("vehicle_journey").getJSONObject("objectid");
      column.put("unique",1);

      checkPoint.check(beansFor4, fullparameters, report, new HashMap<String, Object>());
      report.refreshStatus();
      column.put("unique",0);

      DetailReportItem detail = checkReportForTest4_1(report,"4-VehicleJourney-1",bean2.getObjectId());
      Assert.assertEquals(detail.getArgs().get("column"),"objectid","detail must refer column");
      Assert.assertEquals(detail.getArgs().get("value"),bean2.getObjectId().split(":")[2],"detail must refer value");
      Assert.assertEquals(detail.getArgs().get("alternateId"),bean1.getObjectId(),"detail must refer fisrt bean");
   }

   @SuppressWarnings("unchecked")
   @Test(groups = { "vehicleJourney" }, description = "3-VehicleJourney-1")
   public void verifyTest3_1() throws ChouetteException
   {
      // 3-VehicleJourney-1 : check if time progress correctly on each stop

      VehicleJourneyCheckPoints checkPoint = (VehicleJourneyCheckPoints) applicationContext
            .getBean("vehicleJourneyCheckPoints");
      IImportPlugin<Line> importLine = (IImportPlugin<Line>) applicationContext
            .getBean("NeptuneLineImport");

      long id = 1;

      JSONObject parameters = null;
      try
      {
         parameters = new RuleParameterSet();
      } catch (JSONException | IOException e)
      {
         e.printStackTrace();
      }
      Assert.assertNotNull(parameters, "no parameters for test");

      List<Line> beans = LineLoader.load(importLine,
            "src/test/data/Ligne_OK.xml");
      Assert.assertFalse(beans.isEmpty(), "No data for test");
      Line line1 = beans.get(0);

      // line1 is model;
      line1.setId(id++);

      for (Route route : line1.getRoutes())
      {
         route.setId(id++);
         for (JourneyPattern jp : route.getJourneyPatterns())
         {
            jp.setId(id++);
            for (VehicleJourney vj : jp.getVehicleJourneys())
            {
               vj.setId(id++);
            }
         }
      }

      Route route1 = line1.getRoutes().get(0);
      route1.setObjectId("NINOXE:Route:checkedRoute");
      JourneyPattern jp1 = route1.getJourneyPatterns().get(0);
      jp1.setObjectId("NINOXE:JourneyPattern:checkedJP");
      VehicleJourney vj1 = jp1.getVehicleJourneys().get(0);
      vj1.setObjectId("NINOXE:VehicleJourney:checkedVJ");
      long maxDiffTime = 0;
      for (VehicleJourneyAtStop vjas : vj1.getVehicleJourneyAtStops())
      {
         if (vjas.getArrivalTime().equals(vjas.getDepartureTime()))
         {
            vjas.getArrivalTime().setTime(
                  vjas.getArrivalTime().getTime() - 60000);
         }
         long diffTime = Math.abs(diffTime(vjas.getArrivalTime(),
               vjas.getDepartureTime()));
         if (diffTime > maxDiffTime)
            maxDiffTime = diffTime;
      }
      parameters.put("inter_stop_duration_max", (int) maxDiffTime - 30);

      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      printReport(report);

      Assert.assertEquals(report.getStatus(), Report.STATE.WARNING,
            " report must be on level warning");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("3-VehicleJourney-1"))
         {
            found = true;
            Assert.assertEquals(checkPointReport.getStatus(),
                  Report.STATE.WARNING,
                  " checkPointReport must be on level warning");
            Assert.assertEquals(checkPointReport.hasItems(), true,
                  " checkPointReport must have items");
            Assert.assertEquals(checkPointReport.getItems().size(), 4,
                  " checkPointReport must have 4 items");

            // check detail keys
            boolean objectIdFound = false;
            for (ReportItem ditem : checkPointReport.getItems())
            {
               DetailReportItem detailReport = (DetailReportItem) ditem;
               if (detailReport.getObjectId().equals(vj1.getObjectId()))
                  objectIdFound = true;
            }
            Assert.assertTrue(objectIdFound,
                  "detail report must refer VehicleJourney 1");
         }
      }
      Assert.assertTrue(found,
            "report must contain a 3-VehicleJourney-1 checkPoint");

   }

   @SuppressWarnings("unchecked")
   @Test(groups = { "vehicleJourney" }, description = "3-VehicleJourney-2")
   public void verifyTest3_2() throws ChouetteException
   {
      // 3-VehicleJourney-2 : check speed progression

      VehicleJourneyCheckPoints checkPoint = (VehicleJourneyCheckPoints) applicationContext
            .getBean("vehicleJourneyCheckPoints");
      IImportPlugin<Line> importLine = (IImportPlugin<Line>) applicationContext
            .getBean("NeptuneLineImport");

      long id = 1;

      JSONObject parameters = null;
      try
      {
         parameters = new RuleParameterSet();
      } catch (JSONException | IOException e)
      {
         e.printStackTrace();
      }
      Assert.assertNotNull(parameters, "no parameters for test");

      List<Line> beans = LineLoader.load(importLine,
            "src/test/data/Ligne_OK.xml");
      Assert.assertFalse(beans.isEmpty(), "No data for test");
      Line line1 = beans.get(0);

      // line1 is model;
      line1.setId(id++);

      for (Route route : line1.getRoutes())
      {
         route.setId(id++);
         for (JourneyPattern jp : route.getJourneyPatterns())
         {
            jp.setId(id++);
            for (VehicleJourney vj : jp.getVehicleJourneys())
            {
               vj.setId(id++);
            }
         }
      }

      Route route1 = line1.getRoutes().get(0);
      route1.setObjectId("NINOXE:Route:checkedRoute");
      JourneyPattern jp1 = route1.getJourneyPatterns().get(0);
      jp1.setObjectId("NINOXE:JourneyPattern:checkedJP");

      VehicleJourney vj1 = jp1.getVehicleJourneys().get(0);
      vj1.setObjectId("NINOXE:VehicleJourney:checkedVJ");

      parameters.getJSONObject("mode_bus").put("speed_max", 10);
      parameters.getJSONObject("mode_bus").put("speed_min", 20);

      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      printReport(report);

      Assert.assertEquals(report.getStatus(), Report.STATE.WARNING,
            " report must be on level warning");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("3-VehicleJourney-2"))
         {
            found = true;
            Assert.assertEquals(checkPointReport.getStatus(),
                  Report.STATE.WARNING,
                  " checkPointReport must be on level warning");
            Assert.assertEquals(checkPointReport.hasItems(), true,
                  " checkPointReport must have items");
            Assert.assertEquals(checkPointReport.getItems().size(), 50,
                  " checkPointReport must have 50 items");

            // check detail keys
            boolean objectIdFound = false;
            for (ReportItem ditem : checkPointReport.getItems())
            {
               DetailReportItem detailReport = (DetailReportItem) ditem;
               if (detailReport.getObjectId().equals(vj1.getObjectId()))
                  objectIdFound = true;
            }
            Assert.assertTrue(objectIdFound,
                  "detail report must refer VehicleJourney 1");
         }
      }
      Assert.assertTrue(found,
            "report must contain a 3-VehicleJourney-2 checkPoint");

   }

   @SuppressWarnings("unchecked")
   @Test(groups = { "vehicleJourney" }, description = "3-VehicleJourney-3")
   public void verifyTest3_3() throws ChouetteException
   {
      // 3-VehicleJourney-3 : check if two journeys progress similarly

      VehicleJourneyCheckPoints checkPoint = (VehicleJourneyCheckPoints) applicationContext
            .getBean("vehicleJourneyCheckPoints");
      IImportPlugin<Line> importLine = (IImportPlugin<Line>) applicationContext
            .getBean("NeptuneLineImport");

      long id = 1;

      JSONObject parameters = null;
      try
      {
         parameters = new RuleParameterSet();
      } catch (JSONException | IOException e)
      {
         e.printStackTrace();
      }
      Assert.assertNotNull(parameters, "no parameters for test");

      List<Line> beans = LineLoader.load(importLine,
            "src/test/data/Ligne_OK.xml");
      Assert.assertFalse(beans.isEmpty(), "No data for test");
      Line line1 = beans.get(0);

      // line1 is model;
      line1.setId(id++);
      VehicleJourney vj1 = null;
      JourneyPattern jp1 = null;
      for (Route route : line1.getRoutes())
      {
         route.setId(id++);
         for (JourneyPattern jp : route.getJourneyPatterns())
         {
            jp.setId(id++);
            for (VehicleJourney vj : jp.getVehicleJourneys())
            {
               vj.setId(id++);
               vj.sortVehicleJourneyAtStops();
               if (vj.getObjectId().equals("NINOXE:VehicleJourney:15627288"))
               {
                  vj1 = vj;
                  jp1 = jp;
               }
            }
         }
      }

      Assert.assertNotNull(jp1, "tested jp not found");
      Assert.assertNotNull(vj1, "tested vj not found");

      VehicleJourneyAtStop vjas1 = vj1.getVehicleJourneyAtStops().get(1);
      vjas1.getArrivalTime().setTime(vjas1.getArrivalTime().getTime() - 240000);

      parameters.getJSONObject("mode_bus").put(
            "inter_stop_duration_variation_max", 220);

      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      printReport(report);

      Assert.assertEquals(report.getStatus(), Report.STATE.WARNING,
            " report must be on level warning");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("3-VehicleJourney-3"))
         {
            found = true;
            Assert.assertEquals(checkPointReport.getStatus(),
                  Report.STATE.WARNING,
                  " checkPointReport must be on level warning");
            Assert.assertEquals(checkPointReport.hasItems(), true,
                  " checkPointReport must have items");
            Assert.assertEquals(checkPointReport.getItems().size(), 26,
                  " checkPointReport must have 26 items");

            // check detail keys
            boolean objectIdFound = false;
            for (ReportItem ditem : checkPointReport.getItems())
            {
               DetailReportItem detailReport = (DetailReportItem) ditem;
               if (detailReport.getObjectId().equals(vj1.getObjectId()))
                  objectIdFound = true;
            }
            Assert.assertTrue(objectIdFound,
                  "detail report must refer VehicleJourney 1");
         }
      }
      Assert.assertTrue(found,
            "report must contain a 3-VehicleJourney-3 checkPoint");

   }

   @SuppressWarnings("unchecked")
   @Test(groups = { "vehicleJourney" }, description = "3-VehicleJourney-4")
   public void verifyTest3_4() throws ChouetteException
   {
      // 3-VehicleJourney-4 : check if each journey has minimum one timetable

      VehicleJourneyCheckPoints checkPoint = (VehicleJourneyCheckPoints) applicationContext
            .getBean("vehicleJourneyCheckPoints");
      IImportPlugin<Line> importLine = (IImportPlugin<Line>) applicationContext
            .getBean("NeptuneLineImport");

      long id = 1;

      JSONObject parameters = null;
      try
      {
         parameters = new RuleParameterSet();
      } catch (JSONException | IOException e)
      {
         e.printStackTrace();
      }
      Assert.assertNotNull(parameters, "no parameters for test");

      List<Line> beans = LineLoader.load(importLine,
            "src/test/data/Ligne_OK.xml");
      Assert.assertFalse(beans.isEmpty(), "No data for test");
      Line line1 = beans.get(0);

      // line1 is model;
      line1.setId(id++);

      for (Route route : line1.getRoutes())
      {
         route.setId(id++);
         for (JourneyPattern jp : route.getJourneyPatterns())
         {
            jp.setId(id++);
            for (VehicleJourney vj : jp.getVehicleJourneys())
            {
               vj.setId(id++);
            }
         }
      }

      Route route1 = line1.getRoutes().get(0);
      route1.setObjectId("NINOXE:Route:checkedRoute");
      JourneyPattern jp1 = route1.getJourneyPatterns().get(0);
      jp1.setObjectId("NINOXE:JourneyPattern:checkedJP");
      VehicleJourney vj1 = jp1.getVehicleJourneys().get(0);
      vj1.setObjectId("NINOXE:VehicleJourney:checkedVJ");

      vj1.getTimetables().clear();

      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      printReport(report);

      Assert.assertEquals(report.getStatus(), Report.STATE.WARNING,
            " report must be on level warning");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("3-VehicleJourney-4"))
         {
            found = true;
            Assert.assertEquals(checkPointReport.getStatus(),
                  Report.STATE.WARNING,
                  " checkPointReport must be on level warning");
            Assert.assertEquals(checkPointReport.hasItems(), true,
                  " checkPointReport must have items");
            Assert.assertEquals(checkPointReport.getItems().size(), 1,
                  " checkPointReport must have 1 item");

            // check detail keys
            boolean objectIdFound = false;
            for (ReportItem ditem : checkPointReport.getItems())
            {
               DetailReportItem detailReport = (DetailReportItem) ditem;
               if (detailReport.getObjectId().equals(vj1.getObjectId()))
                  objectIdFound = true;
            }
            Assert.assertTrue(objectIdFound,
                  "detail report must refer VehicleJourney 1");
         }
      }
      Assert.assertTrue(found,
            "report must contain a 3-VehicleJourney-4 checkPoint");

   }
   

   
   @SuppressWarnings("unchecked")
   @Test(groups = { "vehicleJourney" }, description = "4-VehicleJourney-2")
   public void verifyTest4_2() throws ChouetteException
   {
      // 4-VehicleJourney-2 : check transport mode

      VehicleJourneyCheckPoints checkPoint = (VehicleJourneyCheckPoints) applicationContext
            .getBean("vehicleJourneyCheckPoints");
      IImportPlugin<Line> importLine = (IImportPlugin<Line>) applicationContext
            .getBean("NeptuneLineImport");

      long id = 1;

      JSONObject parameters = null;
      try
      {
         parameters = new RuleParameterSet();
      } catch (JSONException | IOException e)
      {
         e.printStackTrace();
      }
      Assert.assertNotNull(parameters, "no parameters for test");

      List<Line> beans = LineLoader.load(importLine,
            "src/test/data/Ligne_OK.xml");
      Assert.assertFalse(beans.isEmpty(), "No data for test");
      Line line1 = beans.get(0);

      // line1 is model;
      line1.setId(id++);
      line1.setObjectId("NINOXE:Line:modelLine");
      
      parameters.remove("check_allowed_transport_modes");
      Route r1 = line1.getRoutes().get(0);
      JourneyPattern jp1 = r1.getJourneyPatterns().get(0);
      
      { // check test not required when missing parameter
      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      Assert.assertEquals(report.getStatus(), Report.STATE.OK,
            " report must be on level ok");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("4-VehicleJourney-2"))
         {
            found = true;
         }
      }
      Assert.assertFalse(found, "report must not contain a 4-VehicleJourney-2 checkPoint");
      }
      
      parameters.append("check_allowed_transport_modes", Integer.valueOf(0));
      
      { // check test not required when check is false
      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      Assert.assertEquals(report.getStatus(), Report.STATE.OK,
            " report must be on level ok");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("4-VehicleJourney-2"))
         {
            found = true;
         }
      }
      Assert.assertFalse(found, "report must not contain a 4-VehicleJourney-2 checkPoint");
      }
      
      parameters.put("check_allowed_transport_modes",Integer.valueOf(1));
      
      { // check test not required when check is false
      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      Assert.assertEquals(report.getStatus(), Report.STATE.OK,
            " report must be on level ok");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("4-VehicleJourney-2"))
         {
            found = true;
         }
      }
      Assert.assertTrue(found, "report must contain a 4-VehicleJourney-2 checkPoint");
      }
 
      JSONObject busParam = parameters.getJSONObject("mode_bus");
      busParam.put("allowed_transport", Integer.valueOf(0));
      jp1.getVehicleJourneys().get(0).setTransportMode(TransportModeNameEnum.Bus);
      { // check test not required when check is false
      PhaseReportItem report = new PhaseReportItem(PHASE.THREE);

      checkPoint.check(jp1.getVehicleJourneys(), parameters, report, new HashMap<String, Object>());
      report.refreshStatus();

      Assert.assertEquals(report.getStatus(), Report.STATE.WARNING,
            " report must be on level warning");
      Assert.assertEquals(report.hasItems(), true, " report must have items");
      boolean found = false;
      for (ReportItem item : report.getItems())
      {
         CheckPointReportItem checkPointReport = (CheckPointReportItem) item;
         if (checkPointReport.getMessageKey().equals("4-VehicleJourney-2"))
         {
            found = true;
            Assert.assertEquals(checkPointReport.getStatus(),
                  Report.STATE.WARNING,
                  " checkPointReport must be on level warning");
            Assert.assertEquals(checkPointReport.hasItems(), true,
                  " checkPointReport must have items");
            Assert.assertEquals(checkPointReport.getItems().size(), 1,
                  " checkPointReport must have 1 item");

            // check detail keys = vj1 objectids
            boolean line1objectIdFound = false;
            for (ReportItem ditem : checkPointReport.getItems())
            {
               DetailReportItem detailReport = (DetailReportItem) ditem;
               if (detailReport.getObjectId().equals(jp1.getVehicleJourneys().get(0).getObjectId()))
                  line1objectIdFound = true;
            }
            Assert.assertTrue(line1objectIdFound,
                  "detail report must refer vj 1");
            
         }
      }
      Assert.assertTrue(found, "report must contain a 4-VehicleJourney-2 checkPoint");
      }
      
      
   }

}