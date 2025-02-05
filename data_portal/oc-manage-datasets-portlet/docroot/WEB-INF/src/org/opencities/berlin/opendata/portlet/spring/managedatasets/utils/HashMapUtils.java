package org.opencities.berlin.opendata.portlet.spring.managedatasets.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.opencities.berlin.opendata.portlet.spring.managedatasets.domain.MetaDataBean;
import org.opencities.berlin.opendata.portlet.spring.managedatasets.domain.Resource;

public class HashMapUtils {
	public static HashMap<String, String> metaDataToMap(MetaDataBean metaData) {

		HashMap<String, String> metaDataMap = new HashMap<String, String>();
		HashMap<String, String> extrasMap = new HashMap<String, String>();
		List<Map<String, String>> resourcesList = new ArrayList<Map<String, String>>();

		extrasMap.put("temporal_coverage_from", DateUtils
				.dateToStringTemporalCoverage(metaData
						.getTemporal_coverage_from()));
		extrasMap.put("temporal_coverage_to", DateUtils
				.dateToStringTemporalCoverage(metaData
						.getTemporal_coverage_to()));
		extrasMap.put("temporal_granularity",
				metaData.getTemporal_granularity());
		extrasMap.put("geographical_coverage",
				metaData.getTemporal_granularity());
		extrasMap.put("geographical_granularity",
				metaData.getTemporal_granularity());
		extrasMap.put("date_released", DateUtils
				.dateToStringTemporalCoverage(metaData.getDate_released()));
		extrasMap.put("others", metaData.getOthers());

		for (Resource resource : metaData.getResources()) {
			Map<String, String> resourceMap = new HashMap<String, String>();

			if (!resource.getUrl().isEmpty() && !resource.getFormat().isEmpty()) {

				resourceMap.put("url", resource.getUrl());
				resourceMap.put("format", resource.getFormat().toUpperCase());
				resourceMap.put("description", resource.getDescription());
				resourceMap.put("language", resource.getLanguage());

				resourcesList.add(resourceMap);
			}
		}

		metaDataMap.put("extras", JSONValue.toJSONString(extrasMap));
		metaDataMap.put("resources", JSONValue.toJSONString(resourcesList));

		StringBuilder tagsBuilder = new StringBuilder();
		for (String tag : metaData.getTags().split(",")) {
			tagsBuilder.append("\"" + tag.trim() + "\",");
		}

		// remove trailing comma
		tagsBuilder = tagsBuilder.deleteCharAt(tagsBuilder.length() - 1);
		if (tagsBuilder.toString().trim().isEmpty()) {
			metaDataMap.put("tags", "[" + tagsBuilder.toString() + "]");
		} else {
			metaDataMap.put("tags", "[ ]");
		}

		metaDataMap.put("groups", JSONValue.toJSONString(metaData.getGroups()));
		metaDataMap.put("title", "\"" + metaData.getTitle() + "\"");
		metaDataMap.put("name", "\"" + metaData.getName() + "\"");
		metaDataMap.put("author", "\"" + metaData.getAuthor() + "\"");
		metaDataMap.put("author_email", "\"" + metaData.getAuthor_email()
				+ "\"");
		metaDataMap.put("url", "\"" + metaData.getUrl() + "\"");
		metaDataMap.put("notes", "\"" + metaData.getNotes() + "\"");
		metaDataMap.put("license_id", "\"" + metaData.getLicense_id() + "\"");
		metaDataMap.put("version", "\"" + metaData.getVersion() + "\"");
		metaDataMap.put("maintainer", "\"" + metaData.getMaintainer() + "\"");
		metaDataMap.put("maintainer_email",
				"\"" + metaData.getMaintainer_email() + "\"");
		metaDataMap.put(
				"metadata_created",
				"\""
						+ DateUtils.dateToStringMetaDate(metaData
								.getMetadata_created()) + "\"");
		metaDataMap.put("metadata_modified",
				"\"" + metaData.getMetadata_modified() + "\"");

		return metaDataMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static MetaDataBean mapToMetaData(Map map) throws ParseException {
		MetaDataBean metaData = new MetaDataBean();
		metaData.setTitle((String) map.get("title"));
		metaData.setName((String) map.get("name"));
		metaData.setAuthor((String) map.get("author"));
		metaData.setAuthor_email((String) map.get("author_email"));
		metaData.setMaintainer((String) map.get("maintainer"));
		metaData.setMaintainer_email((String) map.get("maintainer_email"));
		metaData.setUrl((String) map.get("url"));
		metaData.setNotes((String) map.get("notes"));
		metaData.setLicense_id((String) map.get("license_id"));
		metaData.setCkanId((String) map.get("id"));
		metaData.setMetadata_created(DateUtils
				.stringToDateMetaData((String) map.get("metadata_created")));
		metaData.setMetadata_modified(DateUtils
				.stringToDateMetaData((String) map.get("metadata_modified")));
		Map<String, String> extrasMap = new HashMap<String, String>();
		extrasMap = (Map) map.get("extras");
		metaData.setDate_released(DateUtils
				.stringToDateTemporalCoverage(extrasMap.get("date_released")));
		metaData.setDate_updated(DateUtils
				.stringToDateTemporalCoverage(extrasMap.get("date_updated")));
		metaData.setTemporal_coverage_from(DateUtils
				.stringToDateTemporalCoverage(extrasMap
						.get("temporal_coverage_from")));
		metaData.setTemporal_coverage_to(DateUtils
				.stringToDateTemporalCoverage(extrasMap
						.get("temporal_coverage_to")));
		metaData.setTemporal_granularity(extrasMap.get("temporal_granularity"));
		metaData.setGeographical_coverage(extrasMap
				.get("geographical_coverage"));
		metaData.setGeographical_granularity(extrasMap
				.get("geographical_granularity"));
		metaData.setOthers(extrasMap.get("others"));
		metaData.setGroups((List<String>) map.get("groups"));
		StringBuilder tagsBuilder = new StringBuilder();
		for (String tag : (List<String>) map.get("tags")) {
			tag.trim();
			tagsBuilder.append(tag).append(",");
		}
		if (tagsBuilder.length() > 0) {
			tagsBuilder = tagsBuilder.deleteCharAt(tagsBuilder.length() - 1);
		}
		metaData.setTags(tagsBuilder.toString());
		metaData.setVersion((String) map.get("version"));

		List<Map<String, String>> resorcesList;
		resorcesList = (List) map.get("resources");

		for (Map<String, String> resMap : resorcesList) {
			Resource resource = new Resource();

			resource.setFormat(resMap.get("format").toUpperCase());
			resource.setDescription(resMap.get("description"));
			resource.setUrl(resMap.get("url"));
			resource.setLanguage(resMap.get("language"));
			metaData.getResources().add(resource);
		}

		return metaData;
	}
}
