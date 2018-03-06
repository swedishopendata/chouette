package mobi.chouette.exchange.noptis.converter;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.Converter;
import mobi.chouette.exchange.noptis.importer.ConverterFactory;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.type.ChouetteAreaEnum;
import mobi.chouette.model.type.LongLatTypeEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import java.math.BigDecimal;

@Log4j
public class NoptisStopAreaConverter extends NoptisConverter implements Converter, Constant {

    @Override
    public void convert(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
        mobi.chouette.model.stip.StopArea noptisStopArea = (mobi.chouette.model.stip.StopArea) context.get(NOPTIS_DATA_CONTEXT);
        log.info(noptisStopArea.toString());

        String objectId = NoptisConverter.composeObjectId(configuration.getObjectIdPrefix(),
                mobi.chouette.model.StopArea.STOPAREA_KEY, String.valueOf(noptisStopArea.getGid()));

        mobi.chouette.model.StopArea neptuneStopArea = ObjectFactory.getStopArea(referential, objectId);

        neptuneStopArea.setLatitude(new BigDecimal(noptisStopArea.getCentroidNorthingCoordinate()));
        neptuneStopArea.setLongitude(new BigDecimal(noptisStopArea.getCentroidEastingCoordinate()));

        if (noptisStopArea.getCoordinateSystemName().equals(LongLatTypeEnum.WGS84.name())) {
            neptuneStopArea.setLongLatType(LongLatTypeEnum.WGS84);
        } else {
            neptuneStopArea.setLongLatType(LongLatTypeEnum.WGS84);
        }

        neptuneStopArea.setName(NoptisConverter.getNonEmptyTrimedString(noptisStopArea.getName()));

//        neptuneStopArea.setUrl(...);
//        neptuneStopArea.setComment(...);
//        neptuneStopArea.setTimeZone(...);

        neptuneStopArea.setFareCode(0);

        // TODO setting to commercial stop point for now, consider a check if boarding position
        neptuneStopArea.setAreaType(ChouetteAreaEnum.CommercialStopPoint);

//        neptuneStopArea.setRegistrationNumber(...);
//        neptuneStopArea.setMobilityRestrictedSuitable(WheelchairBoardingType.Allowed.equals(...));
//        neptuneStopArea.setStreetName(...);
//        neptuneStopArea.setCityName(...);
//        neptuneStopArea.setZipCode(...);

        neptuneStopArea.setFilled(true);
    }

    static {
        ConverterFactory.register(NoptisStopAreaConverter.class.getName(), new ConverterFactory() {
            private NoptisStopAreaConverter instance = new NoptisStopAreaConverter();

            @Override
            protected Converter create() {
                return instance;
            }
        });
    }

}
