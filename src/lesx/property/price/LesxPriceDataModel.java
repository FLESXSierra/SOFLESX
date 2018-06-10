package lesx.property.price;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import lesx.gui.message.LesxMessage;
import lesx.property.properties.LesxPrice;
import lesx.property.properties.LesxProperty;
import lesx.utils.LesxMisc;

public class LesxPriceDataModel {

  private final static Logger LOGGER = Logger.getLogger(LesxPriceDataModel.class.getName());

  private Map<Long, LesxPrice> data;
  private LesxPrice priceSelected;
  private Long resourceId;

  public LesxPriceDataModel(Map<Long, LesxPrice> data) {
    this.data = data;
  }

  /**
   * Gets all prices in data model
   *
   * @return Collections of prices
   */
  public Collection<LesxPrice> getPrices() {
    return data.values();
  }

  /**
   * Gets all prices related with resource_id
   *
   * @param resource_id Id of resource
   * @return List of prices
   */
  public List<LesxPrice> getPrices(Long resource_id) {
    return data.values()
        .stream()
        .filter(s -> s.getResource_id()
            .equals(resource_id))
        .collect(Collectors.toList());
  }

  /**
   * Gets all prices related with the given Collection
   *
   * @param currentCustomers Collection of resources
   * @return List of prices
   */
  public List<LesxPrice> getPrices(Collection<Long> currentCustomers) {
    return data.values()
        .stream()
        .filter(s -> currentCustomers.contains(s.getResource_id()))
        .collect(Collectors.toList());
  }

  /**
   * Deletes price from data
   *
   * @param id of price
   */
  public void deletePrice(Long id) {
    data.remove(id);
  }

  public Long createNewKeyForProperty(String propertyId) {
    return (Collections.max(data.keySet()) + 1);
  }

  public LesxPrice getPriceSelected() {
    return priceSelected;
  }

  public void setPriceSelected(LesxPrice price) {
    priceSelected = price;
  }

  public boolean isUniqueProperty(LesxProperty property, Long keyPrice, boolean isCreate) {
    String name = property.getName();
    Object newKey = property.getValue();
    for (Entry<Long, LesxPrice> entry : data.entrySet()) {
      if (LesxMisc.equals(entry.getValue()
          .getPropertyByName(name)
          .getValue(), newKey)) {
        if (isCreate || !entry.getKey()
            .equals(keyPrice)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean addPrice(Collection<LesxPrice> prices) {
    LesxPrice temp = new LesxPrice();
    try {
      for (LesxPrice price : prices) {
        temp = (LesxPrice) price.clone();
        data.put(temp.getId(), temp);
      }
      LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-OBJECT_ADDED", prices.size()));
      return true;
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-DATA_MODEL_SAVE", temp.getName(), temp));
      e.printStackTrace();
      return false;
    }
  }

  public Long getResourceId() {
    return resourceId;
  }

  public void setResourceId(Long Id) {
    resourceId = Id;
  }

  public Map<Long, LesxPrice> getPriceMap() {
    return data;
  }

}
