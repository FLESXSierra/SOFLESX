package lesx.property.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxCostumer;
import lesx.property.properties.LesxPrice;
import lesx.property.properties.LesxReportItem;
import lesx.property.properties.LesxReportTree;
import lesx.utils.LesxMisc;
import lesx.xml.thread.LesxXMLSaveData;

public class LesxDBProperties {

  private final static Logger LOGGER = Logger.getLogger(LesxDBProperties.class.getName());

  private static Map<Long, Map<Long, ? extends LesxComponent>> dataMap;

  private boolean loadPrice;
  private boolean loadReports;

  public LesxDBProperties() {
    dataMap = new HashMap<>();
  }

  public boolean isPriceLoaded() {
    return loadPrice;
  }

  public boolean isReportsLoaded() {
    return loadReports;
  }

  public void setDataMap(Map<Long, Map<Long, ? extends LesxComponent>> map) {
    dataMap.clear();
    dataMap.putAll(map);
  }

  public void addDataMap(Map<Long, Map<Long, ? extends LesxComponent>> map) {
    if (!LesxMisc.isEmpty(map)) {
      if (map.containsKey(ELesxPropertyKeys.PRICE)) {
        loadPrice = true;
      }
      dataMap.putAll(map);
    }
  }

  /**
   * Assigns a new map into the DBProperties.
   *
   * @param data Map<Long,LesxCostumer>
   */
  public void setCostumerMap(Map<Long, LesxCostumer> data) {
    dataMap.remove(ELesxPropertyKeys.COSTUMER.getValue());
    dataMap.put(ELesxPropertyKeys.COSTUMER.getValue(), data);
  }

  public void setReportTreeMap(Map<Long, LesxReportTree> reportTreeMap) {
    dataMap.remove(ELesxPropertyKeys.REPORT_TREE.getValue());
    dataMap.put(ELesxPropertyKeys.REPORT_TREE.getValue(), reportTreeMap);
  }

  public void setReportItemMap(Map<Long, LesxReportItem> reportItemMap) {
    dataMap.remove(ELesxPropertyKeys.REPORT_ELEMENT.getValue());
    dataMap.put(ELesxPropertyKeys.REPORT_ELEMENT.getValue(), reportItemMap);
  }

  @SuppressWarnings("unchecked")
  public Map<Long, LesxCostumer> getCostumerMap() {
    LOGGER.log(Level.INFO, "Called getCostumerMap");
    return (Map<Long, LesxCostumer>) dataMap.get(ELesxPropertyKeys.COSTUMER.getValue());
  }

  @SuppressWarnings("unchecked")
  public Map<Long, LesxPrice> getPriceMap() {
    LOGGER.log(Level.INFO, "Called getPriceMap");
    return (Map<Long, LesxPrice>) dataMap.get(ELesxPropertyKeys.PRICE.getValue());
  }

  @SuppressWarnings("unchecked")
  public Map<Long, LesxReportTree> getReportTree() {
    LOGGER.log(Level.INFO, "Called getReportTree");
    return (Map<Long, LesxReportTree>) dataMap.get(ELesxPropertyKeys.REPORT_TREE.getValue());
  }

  @SuppressWarnings("unchecked")
  public Map<Long, LesxReportItem> getReportItems() {
    LOGGER.log(Level.INFO, "Called getReportItems");
    return (Map<Long, LesxReportItem>) dataMap.get(ELesxPropertyKeys.REPORT_ELEMENT.getValue());
  }

  /**
   * Gets a list of costumers
   *
   * @param keys ID of the costumers.
   * @return a list of costumers
   */
  public List<LesxCostumer> getCostumerMap(List<Long> keys) {
    List<LesxCostumer> result = new ArrayList<>();
    LesxCostumer value;
    Map<Long, LesxCostumer> costumerMap = getCostumerMap();
    for (Long key : keys) {
      value = costumerMap.get(key);
      if (!LesxMisc.isEmpty(value)) {
        result.add(value);
      }
      else {
        LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-NO_COSTUMER_FOUND_BY_KEY", String.valueOf(key)));
      }
    }
    return result;
  }

  /**
   * Saves into XML the given data map.
   *
   * @param data Map containing data
   * @param run Runnable that runs on succeeded or failed
   */
  public void saveXML(Map<Long, LesxCostumer> data, Runnable run) {
    LOGGER.log(Level.INFO, "Called saveXML");
    setCostumerMap(data);
    LesxXMLSaveData saveThread = new LesxXMLSaveData(data.values(), ELesxUseCase.UC_XML_COSTOMER);
    saveThread.start(); // Saving loading message
    saveThread.setOnSucceeded(obs -> run.run());
    saveThread.setOnFailed(obs -> run.run());
  }

  public void setPriceMap(Map<Long, LesxPrice> priceMap) {
    dataMap.remove(ELesxPropertyKeys.PRICE.getValue());
    dataMap.put(ELesxPropertyKeys.PRICE.getValue(), priceMap);
  }

  public void savePriceXML(Map<Long, LesxPrice> priceMap, Runnable run) {
    LOGGER.log(Level.INFO, "savePriceXML");
    setPriceMap(priceMap);
    LesxXMLSaveData saveThread = new LesxXMLSaveData(priceMap.values(), ELesxUseCase.UC_XML_PRICE);
    saveThread.start();
    saveThread.setOnSucceeded(obs -> run.run());
    saveThread.setOnFailed(obs -> run.run());
  }

  public void saveReportXML(Map<Long, LesxReportTree> reportTreeMap, Map<Long, LesxReportItem> reportItemMap, Runnable run) {
    LOGGER.log(Level.INFO, "saveReportXML");
    setReportTreeMap(reportTreeMap);
    setReportItemMap(reportItemMap);
    LesxXMLSaveData saveThreadTree = new LesxXMLSaveData(reportTreeMap.values(), ELesxUseCase.UC_XML_REPORT_TREE);
    LesxXMLSaveData saveThreadItem = new LesxXMLSaveData(reportItemMap.values(), ELesxUseCase.UC_XML_REPORT_ITEMS);
    saveThreadItem.setOnSucceeded(obs -> run.run());
    saveThreadItem.setOnFailed(obs -> run.run());
    saveThreadTree.start();
    saveThreadTree.setOnSucceeded(obs -> saveThreadItem.start());
    saveThreadTree.setOnFailed(obs -> run.run());
  }

}
