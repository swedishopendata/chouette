package mobi.chouette.exchange.gtfs.model.importer;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import mobi.chouette.common.HTMLTagValidator;
import mobi.chouette.exchange.gtfs.model.GtfsAgency;
import mobi.chouette.exchange.gtfs.model.GtfsRoute;

public class RouteById extends IndexImpl<GtfsRoute> implements GtfsConverter {

	public static enum FIELDS {
		route_id, agency_id, route_short_name, route_long_name, route_desc, route_type, route_url, route_color, route_text_color;
	};

	public static final String FILENAME = "routes.txt";
	public static final String KEY = FIELDS.route_id.name();

	private GtfsRoute bean = new GtfsRoute();
	private String[] array = new String[FIELDS.values().length];

	public RouteById(String name) throws IOException {
		super(name, KEY);
	}
	
	@Override
	protected void checkRequiredFields(Map<String, Integer> fields) {
		for (String fieldName : fields.keySet()) {
			if (fieldName != null) {
				if (!fieldName.equals(fieldName.trim())) {
					// extra spaces in end fields are tolerated : 1-GTFS-CSV-7 warning
					getErrors().add(new GtfsException(_path, 1, fieldName, GtfsException.ERROR.EXTRA_SPACE_IN_HEADER_FIELD, null, null));
				}
				
				if (HTMLTagValidator.validate(fieldName.trim())) {
					getErrors().add(new GtfsException(_path, 1, fieldName.trim(), GtfsException.ERROR.HTML_TAG_IN_HEADER_FIELD, null, null));
				}
				
				boolean fieldNameIsExtra = true;
				for (FIELDS field : FIELDS.values()) {
					if (fieldName.trim().equals(field.name())) {
						fieldNameIsExtra = false;
						break;
					}
				}
				if (fieldNameIsExtra) {
					// extra fields are tolerated : 1-GTFS-Route-11 warning
					getErrors().add(new GtfsException(_path, 1, fieldName, GtfsException.ERROR.EXTRA_HEADER_FIELD, null, null));
				}
			}
		}
		
		// no column agency_id
//		if (fields.get(FIELDS.agency_id.name()) == null) {
//			getErrors().add(new GtfsException(_path, 1, FIELDS.agency_id.name(), GtfsException.ERROR.MISSING_REQUIRED_FIELDS, null, null));
//		}

		// checks for ubiquitous header fields : 1-GTFS-Stop-2 error
		if ( fields.get(FIELDS.route_id.name()) == null ||
				(fields.get(FIELDS.route_long_name.name()) == null && fields.get(FIELDS.route_short_name.name()) == null) ||
				fields.get(FIELDS.route_type.name()) == null) {
			
			if (fields.get(FIELDS.route_id.name()) == null)
				throw new GtfsException(_path, 1, FIELDS.route_id.name(), GtfsException.ERROR.MISSING_REQUIRED_FIELDS, null, null);
			
			if (fields.get(FIELDS.route_long_name.name()) == null && fields.get(FIELDS.route_short_name.name()) == null)
				getErrors().add(new GtfsException(_path, 1, FIELDS.route_long_name.name(), GtfsException.ERROR.MISSING_REQUIRED_FIELDS2, null, null));
			
			if (fields.get(FIELDS.route_type.name()) == null)
				getErrors().add(new GtfsException(_path, 1, FIELDS.route_type.name(), GtfsException.ERROR.MISSING_REQUIRED_FIELDS, null, null));
		}
	}

	@Override
	protected GtfsRoute build(GtfsIterator reader, Context context) {
		int i = 0;
		for (FIELDS field : FIELDS.values()) {
			array[i++] = getField(reader, field.name());
		}

		i = 0;
		String value = null;
		int id = (int) context.get(Context.ID);
		clearBean();
		bean.setId(id);
		
		value = array[i++]; testExtraSpace(FIELDS.route_id.name(), value, bean);
		if (value != null && !value.trim().isEmpty()) {
			bean.setRouteId(STRING_CONVERTER.from(context, FIELDS.route_id, value, "", true));
		}
		
		value = array[i++]; testExtraSpace(FIELDS.agency_id.name(), value, bean);
		if (value != null && !value.trim().isEmpty()) {
			bean.setAgencyId(STRING_CONVERTER.from(context, FIELDS.agency_id, value, false));
		}
		
		boolean noShotName = false;
		value = array[i++]; testExtraSpace(FIELDS.route_short_name.name(), value, bean);
		if (value == null || value.trim().isEmpty()) {
			noShotName = true;
		} else {
			bean.setRouteShortName(STRING_CONVERTER.from(context, FIELDS.route_short_name, value, false));
		}
		
		value = array[i++]; testExtraSpace(FIELDS.route_long_name.name(), value, bean);
		if (value == null || value.trim().isEmpty()) {
			if (noShotName)
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_long_name.name(), GtfsException.ERROR.MISSING_REQUIRED_VALUES2, null, null));
		} else {
			bean.setRouteLongName(STRING_CONVERTER.from(context, FIELDS.route_long_name, value, bean.getRouteShortName() != null));
			if (bean.getRouteLongName().equals(bean.getRouteShortName())) {
				// routeshortName != routeLongName
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_short_name.name(), GtfsException.ERROR.SHARED_VALUE, null, null));
			} else if (!noShotName && bean.getRouteLongName().indexOf(bean.getRouteShortName()) > 0) {
				// routeshortName is not a substring of routeLongName
				bean.getOkTests().add(GtfsException.ERROR.SHARED_VALUE);
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_short_name.name(), GtfsException.ERROR.CONTAINS_ROUTE_NAMES, null, null));
			} else {
				bean.getOkTests().add(GtfsException.ERROR.SHARED_VALUE);
				bean.getOkTests().add(GtfsException.ERROR.CONTAINS_ROUTE_NAMES);
			}
		}
		
		value = array[i++]; testExtraSpace(FIELDS.route_desc.name(), value, bean);
		if (value != null && !value.trim().isEmpty()) {
			bean.setRouteDesc(STRING_CONVERTER.from(context, FIELDS.route_desc, value, false));
			if (bean.getRouteDesc().equals(bean.getRouteLongName())) {
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_long_name.name(), GtfsException.ERROR.SHARED_VALUE, null, null));
			} else if (bean.getRouteDesc().equals(bean.getRouteShortName())) {
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_desc.name(), GtfsException.ERROR.SHARED_VALUE, null, null));
			}
		}
		
		value = array[i++]; testExtraSpace(FIELDS.route_type.name(), value, bean);
		if (value == null || value.trim().isEmpty()) {
			bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_type.name(), GtfsException.ERROR.MISSING_REQUIRED_VALUES, null, null));
		} else {
			try {
				bean.setRouteType(ROUTETYPE_CONVERTER.from(context, FIELDS.route_type, value, true));
			} catch(GtfsException e) {
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_type.name(), GtfsException.ERROR.INVALID_FORMAT, null, value));
			}
		}
		
		value = array[i++]; testExtraSpace(FIELDS.route_url.name(), value, bean);
		if (value != null && !value.trim().isEmpty()) {
			try {
				bean.setRouteUrl(URL_CONVERTER.from(context, FIELDS.route_url, value, false));
			} catch (GtfsException e) {
				// 1-GTFS-Route-7 warning
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_url.name(), GtfsException.ERROR.INVALID_FORMAT, null, value));
			}
		}
		
		value = array[i++]; testExtraSpace(FIELDS.route_color.name(), value, bean);
		try {
			bean.setRouteColor(COLOR_CONVERTER.from(context, FIELDS.route_color, value, Color.WHITE, false));
		} catch (GtfsException e) {
			// 1-GTFS-Route-7 warning
			bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_color.name(), GtfsException.ERROR.INVALID_FORMAT, null, value));	
		}
		if (bean.getRouteColor() == null)
			bean.setRouteColor(Color.WHITE);
		
		value = array[i++]; testExtraSpace(FIELDS.route_text_color.name(), value, bean);
		try {
			bean.setRouteTextColor(COLOR_CONVERTER.from(context, FIELDS.route_text_color, value, Color.BLACK, false));
		} catch (GtfsException e) {
			// 1-GTFS-Route-7 warning
			bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_color.name(), GtfsException.ERROR.INVALID_FORMAT, null, value));	
		}
		if (bean.getRouteTextColor() == null)
			bean.setRouteTextColor(Color.BLACK);
		
		float routeColorBrightness = (float)((299 * bean.getRouteColor().getRed()) + (587 * bean.getRouteColor().getGreen()) + (114 * bean.getRouteColor().getBlue())) / (float)1000;
		float routeTextColorBrightness = (float)((299 * bean.getRouteTextColor().getRed()) + (587 * bean.getRouteTextColor().getGreen()) + (114 * bean.getRouteTextColor().getBlue())) / (float)1000;
		float brightnessDifference = Math.max(routeColorBrightness, routeTextColorBrightness) -Math.min(routeColorBrightness, routeTextColorBrightness);
		int colorDifference = Math.max(bean.getRouteColor().getRed(), bean.getRouteTextColor().getRed()) - Math.min(bean.getRouteColor().getRed(), bean.getRouteTextColor().getRed())
				+ Math.max(bean.getRouteColor().getGreen(), bean.getRouteTextColor().getGreen()) - Math.min(bean.getRouteColor().getGreen(), bean.getRouteTextColor().getGreen())
				+ Math.max(bean.getRouteColor().getBlue(), bean.getRouteTextColor().getBlue()) - Math.min(bean.getRouteColor().getBlue(), bean.getRouteTextColor().getBlue());
		if ( brightnessDifference < 125 || colorDifference < 500) { // Poor visibility between text and background colors
			bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_text_color.name(), GtfsException.ERROR.BAD_COLOR, null, value));
		}
		
		return bean;
	}

	@Override
	public boolean validate(GtfsRoute bean, GtfsImporter dao) {
		boolean result = true;
		
		GtfsRoute copy_bean = new GtfsRoute(bean);
		// Verify the agency_id
		String agencyId = copy_bean.getAgencyId();
		if (agencyId == null || GtfsAgency.DEFAULT_ID.equals(agencyId)) {
			if (dao.getAgencyById().getLength() == 1) {
				agencyId = dao.getAgencyById().iterator().next().getAgencyId();
			} else {
				agencyId = GtfsAgency.DEFAULT_ID;
			}
			copy_bean.setAgencyId(agencyId);
		}
		if (dao.getAgencyById().getValue(agencyId) == null) {
			// this bean has no agency
			bean.getErrors().add(new GtfsException(_path, copy_bean.getId(), FIELDS.agency_id.name(), GtfsException.ERROR.UNREFERENCED_ID, null, agencyId));
			result = false;
		}
		if (result)
			bean.getOkTests().add(GtfsException.ERROR.UNREFERENCED_ID);
		
		// routeUrl != GtfsAgency.agencyUrl
		boolean result2 = true;
		if (copy_bean.getRouteUrl() != null) {
			for (GtfsAgency agency : (AgencyById)dao.getAgencyById()) {
				if (agency.getAgencyUrl() != null) {
					if (copy_bean.getRouteUrl().equals(agency.getAgencyUrl())) {
						result2 = false;
						bean.getErrors().add(new GtfsException(_path, copy_bean.getId(), FIELDS.route_url.name(), GtfsException.ERROR.SHARED_VALUE, null, null));
						break;
					}
				}
			}
		}
		if (result2)
			bean.getOkTests().add(GtfsException.ERROR.SHARED_VALUE);
		result = result && result2;
		
		// (routeShortName, routeLongName) != other GtfsRoute.(routeShortName, routeLongName)
		boolean result3 = true;
		// (routeShortName, routeLongName) != other GtfsRoute.(routeShortName, routeLongName)
		boolean result4 = true;
		String routeShortName = copy_bean.getRouteShortName();
		String routeLongName = copy_bean.getRouteLongName();
		for (GtfsRoute another_bean : dao.getRouteById()) {
			if (another_bean.getId() >= copy_bean.getId())
				break;
			if ( (routeShortName != null && routeShortName.equals(another_bean.getRouteShortName())) || (routeShortName == another_bean.getRouteShortName()) )
				if ( (routeLongName != null && routeLongName.equals(another_bean.getRouteLongName())) || (routeLongName == another_bean.getRouteLongName()) ) {
					bean.getErrors().add(new GtfsException(_path, copy_bean.getId(), FIELDS.route_short_name.name(), GtfsException.ERROR.DUPLICATE_ROUTE_NAMES, null, null));
					result3 = false;
				}
			if ( (routeShortName != null && routeShortName.equals(another_bean.getRouteLongName())) || (routeShortName == another_bean.getRouteLongName()) )
				if ( (routeLongName != null && routeLongName.equals(another_bean.getRouteShortName())) || (routeLongName == another_bean.getRouteShortName()) ) {
					bean.getErrors().add(new GtfsException(_path, copy_bean.getId(), FIELDS.route_long_name.name(), GtfsException.ERROR.INVERSE_DUPLICATE_ROUTE_NAMES, null, null));
					result4 = false;
				}
		}
		if (result3)
			bean.getOkTests().add(GtfsException.ERROR.DUPLICATE_ROUTE_NAMES);
		if (result4)
			bean.getOkTests().add(GtfsException.ERROR.INVERSE_DUPLICATE_ROUTE_NAMES);
		result = result && result3 && result4;
		
		return result;
	}
	
	private void clearBean() {
		//bean.getErrors().clear();
		bean.setId(null);
		bean.setAgencyId(null);
		bean.setRouteColor(null);
		bean.setRouteDesc(null);
		bean.setRouteId(null);
		bean.setRouteLongName(null);
		bean.setRouteShortName(null);
		bean.setRouteTextColor(null);
		bean.setRouteType(null);
		bean.setRouteUrl(null);
	}
	
	public static class DefaultImporterFactory extends IndexFactory {
		@SuppressWarnings("rawtypes")
		@Override
		protected Index create(String name) throws IOException {
			return new RouteById(name);
		}
	}

	static {
		IndexFactory factory = new DefaultImporterFactory();
		IndexFactory.factories.put(RouteById.class.getName(), factory);
	}

}
