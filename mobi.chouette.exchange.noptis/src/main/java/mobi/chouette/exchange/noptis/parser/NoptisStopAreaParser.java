package mobi.chouette.exchange.noptis.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.type.ChouetteAreaEnum;
import mobi.chouette.model.type.LongLatTypeEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Log4j
public class NoptisStopAreaParser implements Parser, Constant {

    @Getter
    @Setter
    private List<mobi.chouette.model.stip.StopArea> noptisStopAreas;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        for (mobi.chouette.model.stip.StopArea noptisStopArea : noptisStopAreas) {
            String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    mobi.chouette.model.StopArea.STOPAREA_KEY, String.valueOf(noptisStopArea.getGid()));

            mobi.chouette.model.StopArea neptuneStopArea = ObjectFactory.getStopArea(referential, objectId);

            if (StringUtils.isNotEmpty(noptisStopArea.getCentroidNorthingCoordinate())) {
                neptuneStopArea.setLatitude(new BigDecimal(noptisStopArea.getCentroidNorthingCoordinate()));
            }
            if (StringUtils.isNotEmpty(noptisStopArea.getCentroidEastingCoordinate())) {
                neptuneStopArea.setLongitude(new BigDecimal(noptisStopArea.getCentroidEastingCoordinate()));
            }
            if (StringUtils.isNotEmpty(noptisStopArea.getCoordinateSystemName())) {
                if (noptisStopArea.getCoordinateSystemName().equals(LongLatTypeEnum.WGS84.name())) {
                    neptuneStopArea.setLongLatType(LongLatTypeEnum.WGS84);
                } else {
                    neptuneStopArea.setLongLatType(LongLatTypeEnum.WGS84);
                }
            }

            neptuneStopArea.setName(AbstractNoptisParser.getNonEmptyTrimedString(noptisStopArea.getName()));
            neptuneStopArea.setFareCode(0);
            neptuneStopArea.setAreaType(ChouetteAreaEnum.CommercialStopPoint);
            neptuneStopArea.setFilled(true);
        }
    }

    static {
        ParserFactory.register(NoptisStopAreaParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisStopAreaParser();
            }
        });
    }

}
