package lesx.ui.mainpage.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxLocations;
import lesx.property.properties.LesxCostumer;
import lesx.property.properties.LesxProperty;
import lesx.utils.LesxMisc;
import lesx.utils.LesxString;
import main.LesxMain;

public class LesxCostumerDataModel {
  private final static Logger LOGGER = Logger.getLogger(LesxCostumerDataModel.class.getName());
  private Map<Long, LesxCostumer> data;
  private ELesxLocations locationsSelected;
  private LesxCostumer costumerSelected;

  public LesxCostumerDataModel(Map<Long, LesxCostumer> data) {
    this.data = data;
  }

  public Set<ELesxLocations> getDataLocations() {
    final Set<ELesxLocations> locations = new HashSet<>();
    if (data != null) {
      for (LesxCostumer costumer : data.values()) {
        locations.add(ELesxLocations.getLocation(costumer.getLocation()));
      }
    }
    return locations;
  }

  /**
   * Return the costumer data.
   *
   * @return Map<Long, LesxCostumer>
   */
  public Map<Long, LesxCostumer> getData() {
    return data;
  }

  public Collection<LesxCostumer> getCostumers() {
    return data.values();
  }

  /**
   * Returns the costumers that belongs to the location given, if getChilderAlso is {@code true} I'll accepts the
   * children of the locations also.
   *
   * @param location Location to verify precedence
   * @param getChilderAlso Search for children also
   * @return List of costumer filtered. Not {@code null}
   */
  public List<LesxCostumer> getValuesByLocation(ELesxLocations location, boolean getChilderAlso) {
    final List<LesxCostumer> result = new ArrayList<>();
    if (data != null) {
      if (getChilderAlso) {
        if (LesxMisc.equals(location, ELesxLocations.COLOMBIA)) {
          result.addAll(data.values());
          return result;
        }
        for (Entry<Long, LesxCostumer> entry : data.entrySet()) {
          Long tempKey = entry.getValue()
              .getLocation();
          if (verifyPrecedenceRecursively(tempKey, location)) {
            result.add(entry.getValue());
          }
        }
      }
      else {
        for (Entry<Long, LesxCostumer> entry : data.entrySet()) {
          if (LesxMisc.equals(entry.getValue()
              .getPropertyByName(LesxString.PROPERTY_LOCATION)
              .getValue(), location.getKey())) {
            result.add(entry.getValue());
          }
        }
      }
    }
    return result;
  }

  /**
   * Verify keys of the parent and children.
   *
   * @param key Long
   * @param location ELesxLocations
   * @return
   */
  private boolean verifyPrecedenceRecursively(Long keyToSearch, ELesxLocations location) {
    final Long keyFiltered = location.getKey();
    final ELesxLocations locationToSearch = ELesxLocations.getLocation(keyToSearch);
    final Long parentKey = locationToSearch.getParentKey();
    if (LesxMisc.equals(keyToSearch, ELesxLocations.COLOMBIA.getKey())) {
      return false;
    }
    if (LesxMisc.equals(parentKey, keyFiltered) || LesxMisc.equals(keyToSearch, keyFiltered)) {
      return true;
    }
    return verifyPrecedenceRecursively(ELesxLocations.getParentLocation(locationToSearch)
        .getKey(), location);
  }

  public ELesxLocations getLocationsSelected() {
    return locationsSelected;
  }

  public void setLocationsSelected(ELesxLocations locationsSelected) {
    this.locationsSelected = locationsSelected;
  }

  /**
   * Verifies that the new key given is unique among all the properties of costumer components
   *
   * @param name of the property
   * @param newKey to validate
   * @return {@code true} if is unique. not null.
   */
  public boolean isUniqueProperty(LesxProperty property, Long keyCostumer, boolean isCreate) {
    String name = property.getName();
    Object newKey = property.getValue();
    for (Entry<Long, LesxCostumer> entry : data.entrySet()) {
      if (LesxMisc.equals(entry.getValue()
          .getPropertyByName(name)
          .getValue(), newKey)) {
        if (isCreate || !entry.getKey()
            .equals(keyCostumer)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Creates a new unique ID (Long) for the property
   *
   * @param name of the property that will be assigned the new key
   * @return a unique and new ID
   */
  public Long createNewKeyForProperty(String name) {
    return (Collections.max(data.keySet()) + 1);
  }

  /**
   * Saves a costumers into the data model
   *
   * @param costumers Collection<LesxCostumer>
   * @return {@code true} if it's successfully saved, otherwise false.
   */
  public boolean addCostumers(Collection<LesxCostumer> costumers) {
    LesxCostumer temp = new LesxCostumer();
    try {
      for (LesxCostumer costumer : costumers) {
        temp = costumer;
        data.put(costumer.getId(), costumer);
      }
      LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-OBJECT_ADDED", costumers.size()));
      return true;
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-DATA_MODEL_SAVE", temp.getName(), temp));
      e.printStackTrace();
      return false;
    }
  }

  public void deleteCostumer(Long id) {
    data.remove(id);
  }

  public void deleteSelectedCostumer() {
    if (costumerSelected != null) {
      data.remove(costumerSelected.getId());
    }
  }

  public LesxCostumer getCostumerSelected() {
    return costumerSelected;
  }

  public void setCostumerSelected(LesxCostumer costumer) {
    if (costumer != null) {
      costumerSelected = costumer.clone();
    }
    else {
      costumerSelected = null;
    }
  }

  public void persistData(Runnable run) {
    LesxMain.getInstance()
        .getDbProperty()
        .saveXML(data, run);
  }

  public LesxCostumer getCostumer(Long value) {
    return data.get(value);
  }

  public List<LesxCostumer> getCostumers(Collection<Long> currentCostomers) {
    return data.values()
        .stream()
        .filter(costumer -> currentCostomers.contains(costumer.getId()))
        .collect(Collectors.toList());
  }

}
