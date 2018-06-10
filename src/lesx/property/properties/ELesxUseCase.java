package lesx.property.properties;

public enum ELesxUseCase {
  UC_TREE(0) {
    @Override
    public String toString() {
      return "Árbol";
    }
  },
  UC_TABLE(1) {
    @Override
    public String toString() {
      return "Tabla";
    }
  },
  UC_READ_ONLY(2) {
    @Override
    public String toString() {
      return "Solo lectura";
    }
  },
  UC_XML_COSTOMER(3) {
    @Override
    public String toString() {
      return "Costumer";
    }
  },
  UC_XML_PRICE(4) {
    @Override
    public String toString() {
      return "Precios";
    }
  },
  UC_XML_REPORT_TREE(5) {
    @Override
    public String toString() {
      return "Árbol de reportes";
    }
  },
  UC_XML_REPORT_ITEMS(6) {
    @Override
    public String toString() {
      return "Report Items";
    }
  },
  UC_TREE_MODIFY(7) {
    @Override
    public String toString() {
      return "Árbol Modificable";
    }
  },
  UC_ADD_REMOVE_ONLY(8) {
    @Override
    public String toString() {
      return "Agregar y Remover";
    }
  },
  UC_DELETE_ONLY(9) {
    @Override
    public String toString() {
      return "Solo Eliminar";
    }
  };

  int key;

  private ELesxUseCase(int key) {
    this.key = key;
  }

  public int getKey() {
    return key;
  }
}
