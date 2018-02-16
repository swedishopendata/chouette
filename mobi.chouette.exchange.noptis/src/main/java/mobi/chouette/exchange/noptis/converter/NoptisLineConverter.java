package mobi.chouette.exchange.noptis.converter;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.Converter;
import mobi.chouette.exchange.noptis.importer.ConverterFactory;
import mobi.chouette.exchange.noptis.importer.ImportableNoptisData;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.Company;
import mobi.chouette.model.Network;
import mobi.chouette.model.stip.TransportAuthority;
import mobi.chouette.model.stip.type.TransportModeCode;
import mobi.chouette.model.type.TransportModeNameEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import java.util.Calendar;

@Log4j
public class NoptisLineConverter extends NoptisConverter implements Converter, Constant {

    @Override
    public void convert(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        ImportableNoptisData importableNoptisData = (ImportableNoptisData) context.get(IMPORTABLE_NOPTIS_DATA);
        mobi.chouette.model.stip.Line noptisLine = importableNoptisData.getLine();

        String lineId = NoptisConverter.composeObjectId(configuration.getObjectIdPrefix(),
                mobi.chouette.model.Line.LINE_KEY, String.valueOf(noptisLine.getGid()));
        mobi.chouette.model.Line neptuneLine = ObjectFactory.getLine(referential, lineId);
        convert(context, noptisLine, neptuneLine);

        neptuneLine.setNetwork(createPTNetwork(referential, configuration));

        if (noptisLine.getIsDefinedByTransportAuthorityId() != null) {
            String transportAuthorityIdAsString = String.valueOf(noptisLine.getIsDefinedByTransportAuthorityId());
            TransportAuthority transportAuthority = importableNoptisData.getSharedTransportAuthorities().get(transportAuthorityIdAsString);
            log.info(transportAuthority.toString());
            context.put(NOPTIS_DATA_CONTEXT, transportAuthority);

            String companyId = NoptisConverter.composeObjectId(configuration.getObjectIdPrefix(), Company.COMPANY_KEY,
                    String.valueOf(noptisLine.getIsDefinedByTransportAuthorityId()));
            Company company = ObjectFactory.getCompany(referential, companyId);
            neptuneLine.setCompany(company);

            NoptisTransportAuthorityConverter transportAuthorityConverter = (NoptisTransportAuthorityConverter) ConverterFactory.create(NoptisTransportAuthorityConverter.class.getName());
            transportAuthorityConverter.convert(context);
        } else if (!referential.getSharedCompanies().isEmpty()) {
            Company company = referential.getSharedCompanies().values().iterator().next();
            neptuneLine.setCompany(company);
        }
    }

    private void convert(Context context, mobi.chouette.model.stip.Line noptisLine, mobi.chouette.model.Line neptuneLine) {
        neptuneLine.setName(NoptisConverter.getNonEmptyTrimedString(noptisLine.getName()));
        if (neptuneLine.getName() == null)
            neptuneLine.setName(NoptisConverter.getNonEmptyTrimedString(noptisLine.getDesignation()));

        neptuneLine.setNumber(NoptisConverter.getNonEmptyTrimedString(noptisLine.getDesignation()));
        neptuneLine.setPublishedName(NoptisConverter.getNonEmptyTrimedString(noptisLine.getName()));

        if (neptuneLine.getPublishedName() != null) {
            neptuneLine.setName(neptuneLine.getPublishedName());
        } else {
            neptuneLine.setName(neptuneLine.getNumber());
        }

        neptuneLine.setTransportModeName(toTransportModeNameEnum(noptisLine.getDefaultTransportModeCode()));

        String[] token = neptuneLine.getObjectId().split(":");
        neptuneLine.setRegistrationNumber(token[2]);
        //neptuneLine.setComment(noptisLine.getDescription());
        //neptuneLine.setColor(toHexa(noptisLine.getLineColor()));
        //neptuneLine.setTextColor(toHexa(noptisLine.getLineTextColor()));
        //neptuneLine.setUrl(NoptisConverter.toString(noptisLine.getLineUrl()));
        neptuneLine.setFilled(true);

        System.out.println(neptuneLine.toString());
    }

    private Network createPTNetwork(Referential referential, NoptisImportParameters configuration) {
        String prefix = configuration.getObjectIdPrefix();
        String ptNetworkId = prefix + ":" + Network.PTNETWORK_KEY + ":" + prefix;
        Network ptNetwork = ObjectFactory.getPTNetwork(referential, ptNetworkId);
        ptNetwork.setVersionDate(Calendar.getInstance().getTime());
        ptNetwork.setName(prefix);
        ptNetwork.setRegistrationNumber(prefix);
        ptNetwork.setSourceName("NOPTIS");
        return ptNetwork;
    }

    private TransportModeNameEnum toTransportModeNameEnum(TransportModeCode type) {
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
