package mobi.chouette.exchange.noptis.dao;

import lombok.extern.log4j.Log4j;
import mobi.chouette.exchange.noptis.dao.fixture.LineBuilder;
import mobi.chouette.exchange.noptis.model.Line;
import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import javax.ejb.Stateless;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
@Log4j
public class LineDAO {

    private static final Map<Long, List<Line>> LINE_FIXTURES = new HashMap<>();

    static {
        Line line1 = LineBuilder.newInstance()
                .withId(1L)
                .withNumber((short)1)
                .withName("Line Name")
                .withDesignation("Line Designation")
                .withTransportModeCode(TransportModeCode.BUS)
                .withIsDefinedByTransportAuthorityId(1L)
                .withMonitored(true)
                .withExistsFromDate(LocalDate.now())
                .withExistsUpToDate(LocalDate.now())
                .build();
        Line line2 = LineBuilder.newInstance()
                .withId(2L)
                .withNumber((short)2)
                .withName("Line Name")
                .withDesignation("Line Designation")
                .withTransportModeCode(TransportModeCode.BUS)
                .withIsDefinedByTransportAuthorityId(2L)
                .withMonitored(true)
                .withExistsFromDate(LocalDate.now())
                .withExistsUpToDate(LocalDate.now())
                .build();
        Line line3 = LineBuilder.newInstance()
                .withId(3L)
                .withNumber((short)3)
                .withName("Line Name")
                .withDesignation("Line Designation")
                .withTransportModeCode(TransportModeCode.BUS)
                .withIsDefinedByTransportAuthorityId(3L)
                .withMonitored(true)
                .withExistsFromDate(LocalDate.now())
                .withExistsUpToDate(LocalDate.now())
                .build();

        List<Line> ulaLines = Arrays.asList(line1, line2, line3);
        LINE_FIXTURES.put(1L, ulaLines);
    }

    public List<Line> findByDataSourceId(Long dataSourceId) {
        return LINE_FIXTURES.get(dataSourceId);
        //List<Line> list = dbAccess.query("SELECT l FROM LineEntity l WHERE l.isFromDataSourceId = ?1", dataSourceId);
    }

}
