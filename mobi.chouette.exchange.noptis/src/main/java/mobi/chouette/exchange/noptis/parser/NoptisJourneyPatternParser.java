package mobi.chouette.exchange.noptis.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.exchange.noptis.importer.util.NoptisReferential;
import mobi.chouette.model.Route;
import mobi.chouette.model.stip.PointInJourneyPattern;
import mobi.chouette.model.stip.StopPoint;
import mobi.chouette.model.stip.TimedJourneyPattern;
import mobi.chouette.model.util.NeptuneUtil;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Log4j
public class NoptisJourneyPatternParser implements Parser, Constant {

    @Getter
    @Setter
    private TimedJourneyPattern timedJourneyPattern;

    @Getter
    @Setter
    private Route route;

    @Getter
    @Setter
    private List<PointInJourneyPattern> pointsInJourneyPattern;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisReferential noptisReferential = (NoptisReferential) context.get(NOPTIS_REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        String journeyPatternObjectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                mobi.chouette.model.JourneyPattern.JOURNEYPATTERN_KEY, String.valueOf(timedJourneyPattern.getIsBasedOnJourneyPatternId()));
        mobi.chouette.model.JourneyPattern neptuneJourneyPattern = ObjectFactory.getJourneyPattern(referential, journeyPatternObjectId);

        neptuneJourneyPattern.setName("");
        neptuneJourneyPattern.setRoute(route);

        for (PointInJourneyPattern pointInJourneyPattern : pointsInJourneyPattern) {
            long journeyPatternPointGid = pointInJourneyPattern.getIsJourneyPatternPointGid();
            StopPoint noptisStopPoint = noptisReferential.getSharedStopPoints().get(journeyPatternPointGid);
            mobi.chouette.model.StopPoint stopPoint = ObjectFactory.getStopPoint(referential, String.valueOf(pointInJourneyPattern.getId()));

            String stopAreaId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    mobi.chouette.model.StopArea.STOPAREA_KEY, String.valueOf(noptisStopPoint.getGid()));
            mobi.chouette.model.StopArea stopArea = ObjectFactory.getStopArea(referential, stopAreaId);
            stopPoint.setContainedInStopArea(stopArea);
            stopPoint.setRoute(route);
            stopPoint.setPosition(pointInJourneyPattern.getSequenceNumber());

            stopPoint.setForBoarding(AbstractNoptisParser.convertDepartureType(pointInJourneyPattern.getDepartureType()));
            stopPoint.setForAlighting(AbstractNoptisParser.convertArrivalType(pointInJourneyPattern.getArrivalType()));

            neptuneJourneyPattern.addStopPoint(stopPoint);
            stopPoint.setFilled(true);
        }

        List<mobi.chouette.model.StopPoint> journeyPatternStopPoints = neptuneJourneyPattern.getStopPoints();
        if (CollectionUtils.isNotEmpty(journeyPatternStopPoints)) {
            neptuneJourneyPattern.setDepartureStopPoint(journeyPatternStopPoints.get(0));
            neptuneJourneyPattern.setArrivalStopPoint(journeyPatternStopPoints.get(journeyPatternStopPoints.size() - 1));
        }

        NeptuneUtil.refreshDepartureArrivals(neptuneJourneyPattern);
        neptuneJourneyPattern.setFilled(true);
    }

    static {
        ParserFactory.register(NoptisJourneyPatternParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisJourneyPatternParser();
            }
        });
    }

}
