package mobi.chouette.exchange.noptis.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.stip.JourneyPatternPoint;
import mobi.chouette.model.stip.StopPoint;
import mobi.chouette.model.type.ChouetteAreaEnum;
import mobi.chouette.model.type.LongLatTypeEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

@Log4j
public class NoptisStopPointParser implements Parser, Constant {

    @Getter
    @Setter
    private StopPoint stopPoint;

    @Getter
    @Setter
    private mobi.chouette.model.stip.StopArea noptisStopArea;

    @Getter
    @Setter
    private JourneyPatternPoint journeyPatternPoint;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                mobi.chouette.model.StopArea.STOPAREA_KEY, String.valueOf(stopPoint.getGid()));

        mobi.chouette.model.StopArea neptuneStopArea = ObjectFactory.getStopArea(referential, objectId);

        String locationNorthingCoordinate = journeyPatternPoint.getLocationNorthingCoordinate();
        if (StringUtils.isNotEmpty(locationNorthingCoordinate)) {
            neptuneStopArea.setLatitude(new BigDecimal(locationNorthingCoordinate));
        }

        String locationEastingCoordinate = journeyPatternPoint.getLocationEastingCoordinate();
        if (StringUtils.isNotEmpty(locationEastingCoordinate)) {
            neptuneStopArea.setLongitude(new BigDecimal(locationEastingCoordinate));
        }

        String coordinateSystemName = journeyPatternPoint.getCoordinateSystemName();
        if (StringUtils.isNotEmpty(coordinateSystemName)) {
            if (coordinateSystemName.equals(LongLatTypeEnum.WGS84.name())) {
                neptuneStopArea.setLongLatType(LongLatTypeEnum.WGS84);
            } else {
                neptuneStopArea.setLongLatType(LongLatTypeEnum.WGS84);
            }
        }

        if (StringUtils.isNotEmpty(stopPoint.getName())) {
            neptuneStopArea.setName(AbstractNoptisParser.getNonEmptyTrimedString(stopPoint.getName()));
        } else {
            neptuneStopArea.setName(AbstractNoptisParser.getNonEmptyTrimedString(noptisStopArea.getName()));
        }

        neptuneStopArea.setFareCode(0);
        neptuneStopArea.setAreaType(ChouetteAreaEnum.BoardingPosition);

        if (noptisStopArea != null) {
            String parentId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    StopArea.STOPAREA_KEY, String.valueOf(noptisStopArea.getGid()));
            StopArea parent = ObjectFactory.getStopArea(referential, parentId);
            neptuneStopArea.setParent(parent);
        }

        neptuneStopArea.setFilled(true);
    }

    static {
        ParserFactory.register(NoptisStopPointParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisStopPointParser();
            }
        });
    }

}
