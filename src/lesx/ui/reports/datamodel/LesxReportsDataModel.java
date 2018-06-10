package lesx.ui.reports.datamodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.LesxPrice;
import lesx.property.properties.LesxProperty;
import lesx.property.properties.LesxReportFunction;
import lesx.property.properties.LesxReportItem;
import lesx.property.properties.LesxReportTree;
import lesx.utils.LesxMisc;

public class LesxReportsDataModel {
  private final static Logger LOGGER = Logger.getLogger(LesxReportsDataModel.class.getName());

  // Maps
  private Map<Long, LesxReportTree> tree;
  private Map<Long, LesxReportItem> items;
  // Data
  private LesxReportTree selectedReportTree;
  // formatter
  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LesxMessage.getMessage("DATE-FORMATTER_PERIOD_DATE_FORMAT"), Locale.ENGLISH);

  public LesxReportsDataModel(Map<Long, LesxReportTree> tree, Map<Long, LesxReportItem> items) {
    if (tree == null) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-NO_NULL_VALUE", "Report Tree Map"));
      this.tree = new HashMap<>();
    }
    if (items == null) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-NO_NULL_VALUE", "Report Items Map"));
      this.items = new HashMap<>();
    }
    this.tree = tree;
    this.items = items;
  }

  public Set<LesxReportTree> getSetReportTree() {
    return new HashSet<>(tree.values());
  }

  public LesxReportTree getReportTree(Long parent_id) {
    return tree.get(parent_id);
  }

  public LesxReportItem getFunction(Long value) {
    return items.get(value);
  }

  public String generateReport(Long value, List<LesxPrice> prices) {
    String reportValue = "";
    LesxReportItem item = getReportItem(value);
    switch (item.getFunction()
        .getType()) {
      case SUM:
        reportValue = String.valueOf(prices.stream()
            .mapToLong(price -> Boolean.TRUE.equals(price.getTypePrice()) ? price.getTotal() : -1 * price.getTotal())
            .sum());
        break;
      case END_MONTH:
        LocalDate endMonth = LocalDate.now();
        reportValue = String.valueOf(prices.stream()
            .filter(price -> {
              LocalDate priceDate = LocalDate.parse(price.getValidFrom(), formatter);
              return priceDate.getMonth()
                  .equals(endMonth.getMonth()) && priceDate.getYear() == endMonth.getYear();
            })
            .mapToLong(price -> Boolean.TRUE.equals(price.getTypePrice()) ? price.getTotal() : -1 * price.getTotal())
            .sum());
        break;
      case PERIOD:
        try {
          LocalDate startPeriodMonth = LocalDate.parse(("01." + String.valueOf(item.getFunction()
              .getValue())
              .split("-")[0].trim()), formatter);
          startPeriodMonth.minusDays(1);
          startPeriodMonth.plusMonths(1);
          LocalDate endPeriodMonth = LocalDate.parse(("01." + String.valueOf(item.getFunction()
              .getValue())
              .split("-")[1].trim()), formatter);
          endPeriodMonth.minusDays(1);
          endPeriodMonth.plusMonths(1);
          reportValue = String.valueOf(prices.stream()
              .filter(price -> { // TODO Está mal el filtro
                LocalDate priceDate = LocalDate.parse(price.getValidFrom(), formatter);
                boolean afterOrEqualsEnd = (priceDate.getMonth()
                    .equals(endPeriodMonth.getMonth()) || priceDate.isBefore(endPeriodMonth)) && priceDate.getYear() == endPeriodMonth.getYear();
                boolean afterOrEqualsStart = (priceDate.getMonth()
                    .equals(startPeriodMonth.getMonth()) || priceDate.isBefore(startPeriodMonth)) && priceDate.getYear() == startPeriodMonth.getYear();
                return afterOrEqualsEnd && afterOrEqualsStart;
              })
              .mapToLong(price -> Boolean.TRUE.equals(price.getTypePrice()) ? price.getTotal() : -1 * price.getTotal())
              .sum());
        }
        catch (DateTimeParseException e) {
          break;
        }
        break;
      default:
        reportValue = "0";
        break;
    }
    return reportValue;
  }

  /**
   * Gets the Report Item by the given key
   *
   * @param value key of the report item
   * @return LesxReportItem
   */
  public LesxReportItem getReportItem(Long value) {
    return items.get(value);
  }

  public List<Long> getFunctions(LesxReportTree currentReportTree) {
    return items.values()
        .stream()
        .filter(item -> item.getParent_id()
            .equals(currentReportTree.getId()))
        .map(item -> item.getId())
        .collect(Collectors.toList());
  }

  /**
   * Adds a new Function into the Report Item.
   *
   * @param treeKey
   * @param function
   * @return new Id created
   */
  public Long addFunction(Long treeKey, LesxReportFunction function, List<Long> costumers) {
    final LesxReportItem temp = getReportItemByParentKey(treeKey);
    LesxReportItem newItem;
    if (temp != null) {
      newItem = temp.clone();
    }
    else {
      newItem = new LesxReportItem();
      newItem.setParent_id(selectedReportTree.getId());
      newItem.setCostumer(costumers);
      newItem.setKey(ELesxPropertyKeys.REPORT_ELEMENT);
    }
    newItem.setFunction(function.clone());
    newItem.idProperty()
        .setValue(createNewFunctionId());
    items.put(newItem.getId(), newItem);
    LOGGER.info(LesxMessage.getMessage("INFO-OBJECT_ADDED", 1));
    return newItem.getId();
  }

  /**
   * Creates a new and unique ID
   *
   * @return new ID
   */
  private Long createNewFunctionId() {
    Long highKey = items.keySet()
        .stream()
        .sorted(Comparator.reverseOrder())
        .findFirst()
        .orElse(-1L);
    if (highKey >= -1L) {
      highKey = new Long(highKey + 1);
    }
    else {
      LOGGER.info(LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
    }
    return highKey;
  }

  /**
   * Gets the Report Item by the given Report Tree Key
   *
   * @param treeKey Report Tree Key
   * @return the LesxReportItem, null if can't find any.
   */
  private LesxReportItem getReportItemByParentKey(Long treeKey) {
    return items.values()
        .stream()
        .filter(item -> item.getParent_id()
            .equals(treeKey))
        .findFirst()
        .orElse(null);
  }

  /**
   * Edits a function
   *
   * @param functionSelected LesxReportItem
   * @param function LesxReportFunction
   */
  public void editFunction(LesxReportItem functionSelected, LesxReportFunction function) {
    items.get(functionSelected.getId())
        .setFunction(function);
  }

  public void setSelectedReportTree(LesxReportTree currentReportTree) {
    this.selectedReportTree = currentReportTree;
  }

  public LesxReportTree getSelectedReportTree() {
    return selectedReportTree;
  }

  /**
   * Adds new costumer from current Report
   *
   * @param selectedCostumers List<Long>
   */
  public void addCostumersToCurrentReport(Collection<Long> selectedCostumers) {
    if (selectedReportTree != null) {
      selectedReportTree.getCostumers()
          .addAll(selectedCostumers);
    }
  }

  /**
   * Removes costumer from current report
   *
   * @param selectedItems List<Long>
   */
  public void removeCostumerFromCurrentReport(List<Long> selectedItems) {
    if (selectedReportTree != null) {
      tree.get(selectedReportTree.getId())
          .getCostumers()
          .removeAll(selectedItems);
    }
  }

  public void removeFunctionFromCurrentReport(List<Long> selectedItems) {
    if (selectedReportTree != null) {
      for (Long key : selectedItems) {
        items.remove(key);
      }
    }
  }

  public void removeReport(LesxReportTree currentReportTree) {
    if (currentReportTree != null && currentReportTree.getId() != null) {
      tree.remove(currentReportTree.getId());
      List<Long> relatedItems = items.values()
          .stream()
          .filter(item -> currentReportTree.getId()
              .equals(item.getParent_id()))
          .map(item -> item.getId())
          .collect(Collectors.toList());
      for (Long key : relatedItems) {
        items.remove(key);
      }
    }
  }

  public Long createNewKeyForTreeId() {
    Long highKey = tree.keySet()
        .stream()
        .sorted(Comparator.reverseOrder())
        .findFirst()
        .orElse(-1L);
    if (highKey >= -1L) {
      highKey = new Long(highKey + 1);
    }
    else {
      LOGGER.info(LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
    }
    return highKey;
  }

  public boolean isUniqueProperty(LesxProperty property, Long keyReport, boolean isCreate) {
    String name = property.getName();
    Object newKey = property.getValue();
    for (Entry<Long, LesxReportTree> entry : tree.entrySet()) {
      if (LesxMisc.equals(entry.getValue()
          .getPropertyByName(name)
          .getValue(), newKey)) {
        if (isCreate || !entry.getKey()
            .equals(keyReport)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean addReport(List<LesxReportTree> trees) {
    LesxReportTree temp = new LesxReportTree();
    try {
      for (LesxReportTree price : trees) {
        temp = (LesxReportTree) price.clone();
        tree.put(temp.getId(), temp);
      }
      LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-OBJECT_ADDED", trees.size()));
      return true;
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-DATA_MODEL_SAVE", temp.getName(), temp));
      e.printStackTrace();
      return false;
    }
  }

  public Map<Long, LesxReportTree> getReportTreeMap() {
    return tree;
  }

  public Map<Long, LesxReportItem> getReportItemMap() {
    return items;
  }

}
