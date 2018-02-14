package mobi.chouette.exchange.noptis.converter;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.Converter;
import mobi.chouette.exchange.noptis.importer.ConverterFactory;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.util.Referential;

public class NoptisLineConverter extends NoptisConverter implements Converter, Constant {

    @Override
    public void convert(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
        System.out.println("Line conversion in progress...");
    }

    static {
        ConverterFactory.register(NoptisLineConverter.class.getName(), new ConverterFactory() {
            private NoptisLineConverter instance = new NoptisLineConverter();

            @Override
            protected Converter create() {
                return instance;
            }
        });
    }

}
