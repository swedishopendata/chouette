package mobi.chouette.exchange.noptis.parser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.model.stip.type.ArrivalType;
import mobi.chouette.model.stip.type.DepartureType;
import mobi.chouette.model.stip.type.TransportModeCode;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.type.PTDirectionEnum;
import mobi.chouette.model.type.TransportModeNameEnum;
import org.apache.commons.lang.StringUtils;

@Log4j
public abstract class AbstractNoptisParser implements Constant {

    public static String getNonEmptyTrimedString(String source) {
        if (source == null)
            return null;
        String target = source.trim();
        return (target.length() == 0 ? null : target);
    }

    public static String composeObjectId(String prefix, String type, String id) {
        if (id == null || id.isEmpty() ) return "";
        String[] tokens = id.split("\\.");
        if (tokens.length == 2) {
            return tokens[0].trim().replaceAll("[^a-zA-Z_0-9]", "_") + ":" + type + ":"
                    + tokens[1].trim().replaceAll("[^a-zA-Z_0-9\\-]", "_");
        }
        return prefix + ":" + type + ":" + id.trim().replaceAll("[^a-zA-Z_0-9\\-]", "_");
    }


    public static void resetContext(Context context) {
        Context conversionContext = (Context) context.get(CONVERSION_CONTEXT);
        if (conversionContext != null) {
            for (String key : conversionContext.keySet()) {
                Context localContext = (Context) conversionContext.get(key);
                localContext.clear();
            }
        }
    }

    static Context getLocalContext(Context context, String localContextName) {
        Context conversionContext = (Context) context.get(CONVERSION_CONTEXT);
        if (conversionContext == null) {
            conversionContext = new Context();
            context.put(CONVERSION_CONTEXT, conversionContext);
        }

        Context localContext = (Context) conversionContext.get(localContextName);
        if (localContext == null) {
            localContext = new Context();
            conversionContext.put(localContextName, localContext);
        }

        return localContext;
    }

    static Context getObjectContext(Context context, String localContextName, String objectId) {
        Context conversionContext = (Context) context.get(CONVERSION_CONTEXT);
        if (conversionContext == null) {
            conversionContext = new Context();
            context.put(CONVERSION_CONTEXT, conversionContext);
        }

        Context localContext = (Context) conversionContext.get(localContextName);
        if (localContext == null) {
            localContext = new Context();
            conversionContext.put(localContextName, localContext);
        }

        Context objectContext = (Context) localContext.get(objectId);
        if (objectContext == null) {
            objectContext = new Context();
            localContext.put(objectId, objectContext);
        }

        return objectContext;
    }

    public static TransportModeNameEnum convertTransportModeCode(TransportModeCode type) {
        switch (type) {
            case TRAM:
                return TransportModeNameEnum.Tramway;
            case METRO:
                return TransportModeNameEnum.Metro;
            case TRAIN:
                return TransportModeNameEnum.Train;
            case BUS:
                return TransportModeNameEnum.Bus;
            case FERRY:
                return TransportModeNameEnum.Ferry;
            case SHIP:
                return TransportModeNameEnum.Waterborne;
            case TAXI:
                return TransportModeNameEnum.Taxi;
            case UNSPECIFIED:
                return TransportModeNameEnum.Other;
            default:
                return TransportModeNameEnum.Other;
        }
    }

    public static BoardingPossibilityEnum convertDepartureType(DepartureType departureType) {
        if (departureType != null) {
            switch (departureType) {
                case NO_STOP:
                    return BoardingPossibilityEnum.forbidden;
                case STOP_NO_BOARDING:
                    return BoardingPossibilityEnum.forbidden;
                case STOP_BOARDING_IF_NECESSARY:
                    return BoardingPossibilityEnum.request_stop;
                case STOP_BOARDING_ALWAYS:
                    return BoardingPossibilityEnum.normal;
                case FLEXIBLE_STOP:
                    return BoardingPossibilityEnum.is_flexible;
                default:
                    return BoardingPossibilityEnum.normal;
            }
        }

        return BoardingPossibilityEnum.normal;
    }

    public static AlightingPossibilityEnum convertArrivalType(ArrivalType arrivalType) {
        if (arrivalType != null) {
            switch (arrivalType) {
                case NO_STOP:
                    return AlightingPossibilityEnum.forbidden;
                case STOP_NO_ALIGHTING:
                    return AlightingPossibilityEnum.forbidden;
                case STOP_ALIGHTING_IF_NECESSARY:
                    return AlightingPossibilityEnum.request_stop;
                case STOP_ALIGHTING_ALWAYS:
                    return AlightingPossibilityEnum.normal;
                case FLEXIBLE_STOP:
                    return AlightingPossibilityEnum.is_flexible;
                default:
                    return AlightingPossibilityEnum.normal;
            }
        }

        return AlightingPossibilityEnum.normal;
    }

    public static PTDirectionEnum toPTDirectionType(String value) {
        if (value == null)
            return null;
        PTDirectionEnum result = null;
        try {
            result = PTDirectionEnum.valueOf(StringUtils.capitalize(value));
        } catch (Exception e) {
            log.error("unable to translate " + value + " as PTDirection");
        }
        return result;
    }

}
