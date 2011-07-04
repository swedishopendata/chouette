package fr.certu.chouette.struts.json;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import fr.certu.chouette.manager.INeptuneManager;
import fr.certu.chouette.model.neptune.Line;
import fr.certu.chouette.model.neptune.Route;
import fr.certu.chouette.model.neptune.StopArea;
import fr.certu.chouette.model.neptune.StopPoint;
import fr.certu.chouette.struts.GeneriqueAction;
import fr.certu.chouette.struts.json.data.JSONStopArea;


public class JSONLineAction extends GeneriqueAction
{
  private static final Logger log = Logger.getLogger(JSONLineAction.class);
  @Getter @Setter private INeptuneManager<Line> lineManager;
  @Getter @Setter private Long lineId;
  @Getter @Setter private INeptuneManager<StopArea> stopAreaManager;

//  public Long getLineId()
//  {
//    return lineId;
//  }
//
//  public void setLineId(Long lineId)
//  {
//    this.lineId = lineId;
//  }

  public Set<JSONStopArea> getStopPlaces() throws Exception
  {
	log.debug("getStopPlaces");

	Line line = lineManager.getById(lineId);
	
	Set<JSONStopArea> stopPlaces = new HashSet<JSONStopArea>();
    for (Route route : line.getRoutes()) 
    {
		for (StopPoint stopPoint : route.getStopPoints()) 
		{
			StopArea stopPlace = stopPoint.getContainedInStopArea().getParentStopArea();
			if (stopPlace != null)
			{
				stopPlaces.add(new JSONStopArea(stopPlace));
			}
		}
	}
    log.debug("getStopPlaces returns "+stopPlaces.size()+" places");
    return stopPlaces;
  }

  /********************************************************
   *                        MANAGER                       *
   ********************************************************/
//  public void setLigneManager(ILigneManager ligneManager)
//  {
//    this.ligneManager = ligneManager;
//  }
//
//  public void setPositionGeographiqueManager(IPositionGeographiqueManager positionGeographiqueManager)
//  {
//    this.positionGeographiqueManager = positionGeographiqueManager;
//  }
}
