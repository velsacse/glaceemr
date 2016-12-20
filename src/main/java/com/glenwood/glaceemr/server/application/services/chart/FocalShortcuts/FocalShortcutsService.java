package com.glenwood.glaceemr.server.application.services.chart.FocalShortcuts;

import org.json.JSONArray;

public interface FocalShortcutsService {

	JSONArray getFocalshortcutsAvailable(String tabId);

	void deleteElementsInFocalShortcut(String focalShortcutId);

	void updateFocalShortcut(String descriptionShortcut, Boolean isActive,
			String focalId);

	JSONArray getFocalShortcutData(String focalIndex, Integer tabId,
			Integer patientId, Integer chartId, Integer encounterId,
			Integer templateId);

	JSONArray addNewFocalShorctut(Integer tabId, Integer patientId,
			Integer encounterId, String symptomIds);

	JSONArray saveFocalData(String tabid, String shortcutName,
			String focalDescription, String xmlData);

	JSONArray searchFocalShortcuts(Integer tabId, String focalsearch);

}