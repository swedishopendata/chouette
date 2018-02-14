package mobi.chouette.exchange.noptis.importer;

import java.util.HashMap;
import java.util.Map;

public abstract class ConverterFactory {

    protected static Map<String, ConverterFactory> factories = new HashMap<>();

    protected abstract Converter create();

    public static final Converter create(String name) throws ClassNotFoundException {
        if (!factories.containsKey(name)) {
            Class.forName(name);
            if (!factories.containsKey(name))
                throw new ClassNotFoundException(name);
        }
        return factories.get(name).create();
    }

    public static void register(String clazz, ConverterFactory factory) {
        factories.put(clazz, factory);
    }
}
