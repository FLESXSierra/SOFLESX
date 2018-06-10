package lesx.property.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lesx.utils.LesxMisc;

public enum ELesxLocations {

  COLOMBIA(0, 0),
  AMAZONAS(0, 1),
  ANTIOQUIA(0, 2),
  ARAUCA(0, 3),
  ATLANTICO(0, 4) {
    @Override
    public String toString() {
      return "ATLÁNTICO";
    }
  },
  BOGOTA(0, 5) {
    @Override
    public String toString() {
      return "BOGOTÁ";
    }
  },
  BOLIVAR(0, 6) {
    @Override
    public String toString() {
      return "BOLÍVAR";
    }
  },
  BOYACA(0, 7) {
    @Override
    public String toString() {
      return "BOYACÁ";
    }
  },
  CALDAS(0, 8),
  CAQUETA(0, 9) {
    @Override
    public String toString() {
      return "CAQUETÁ";
    }
  },
  CASANARE(0, 10),
  CAUCA(0, 11),
  CESAR(0, 12),
  CHOCO(0, 13) {
    @Override
    public String toString() {
      return "CHOCÓ";
    }
  },
  CORDOBA(0, 14) {
    @Override
    public String toString() {
      return "CÓRDOBA";
    }
  },
  CUNDINAMARCA(0, 15),
  GUAINIA(0, 16) {
    @Override
    public String toString() {
      return "GUAINÍA";
    }
  },
  GUAVIARE(0, 17),
  HUILA(0, 18),
  GUAJIRA(0, 19),
  MAGDALENA(0, 20),
  META(0, 21),
  NARINO(0, 22) {
    @Override
    public String toString() {
      return "NARIÑO";
    }
  },
  NORTE_SANTANDER(0, 23) {
    @Override
    public String toString() {
      return "NORTE DE SANTANDER";
    }
  },
  PUTUMAYO(0, 24),
  QUINDIO(0, 25) {
    @Override
    public String toString() {
      return "QUINDÍO";
    }
  },
  RISARALDA(0, 26),
  SAN_ANDRES_PROVIDENCIA(0, 27) {
    @Override
    public String toString() {
      return "SAN ANDRÉS Y PROVIDENCIA";
    }
  },
  SANTANDER(0, 28),
  SUCRE(0, 29),
  TOLIMA(0, 30),
  VALLE_CAUCA(0, 31) {
    @Override
    public String toString() {
      return "VALLE DEL CAUCA";
    }
  },
  VAUPES(0, 32) {
    @Override
    public String toString() {
      return "VAUPÉS";
    }
  },
  VICHADA(0, 33),
  BUCARAMANGA(28, 34);

  private Long parent_key;
  private Long key;

  private static Map<Long, ELesxLocations> mapValues = new HashMap<>();

  static {
    for (ELesxLocations location : ELesxLocations.values()) {
      mapValues.put(location.getKey(), location);
    }
  }

  private ELesxLocations(int parent_key, int key) {
    this.key = new Long(key);
    this.parent_key = new Long(parent_key);
  }

  public Long getKey() {
    return key;
  }

  public Long getParentKey() {
    return parent_key;
  }

  public static ELesxLocations getLocation(Long key) {
    return mapValues.get(key);
  }

  public static List<ELesxLocations> getValues() {
    return Arrays.asList(ELesxLocations.values());
  }

  public static ELesxLocations getParentLocation(ELesxLocations location) {
    return mapValues.get(location.getParentKey());
  }

  public static ELesxLocations getLocationByName(String name) {
    return getValues().stream()
        .filter(s -> LesxMisc.equals(s.toString(), name))
        .findFirst()
        .orElse(null);
  }

  public static Long getLocationKeyByName(String name) {
    ELesxLocations tempLoc = getLocationByName(name);
    if (tempLoc != null) {
      return tempLoc.getKey();
    }
    return -1L;
  }
}
