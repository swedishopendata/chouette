package mobi.chouette.exchange.noptis.converter;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.noptis.Constant;
import org.apache.log4j.Logger;

public class NoptisConverter implements Constant {

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

}
