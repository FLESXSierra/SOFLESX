package lesx.scene.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lesx.gui.message.LesxMessage;
import lesx.property.price.LesxPriceDataModel;
import lesx.ui.costumer.dialog.LesxCostumerDialogPaneController;
import lesx.ui.costumer.dialog.LesxSelectCostumerDialogController;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import lesx.ui.prices.LesxPricesDialogController;
import lesx.ui.prices.dialog.LesxPriceDialogPaneController;
import lesx.ui.reports.LesxReportController;
import lesx.ui.reports.LesxReportEditDialogController;
import lesx.ui.reports.datamodel.LesxReportsDataModel;
import lesx.utils.LesxString;
import lesx.xml.thread.LesxXMLUtils;
import main.LesxMain;

public class LesxSceneController {

  private final static Logger LOGGER = Logger.getLogger(LesxSceneController.class.getName());

  private Scene main;

  public LesxSceneController(Scene scene) {
    main = scene;
  }

  public void activate(String path) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      Pane root = fxmlLoader.load(getClass().getResource(path)
          .openStream());
      LesxController controller = (LesxController) fxmlLoader.getController();
      LesxMain.setTitle(controller.getTitle());
      controller.setExitOperation(LesxMain.getInstance()
          .getStage());
      main.setRoot(root);
      if (main.getWindow() != null) {
        main.getWindow()
            .sizeToScene();
      }
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      e.printStackTrace();
    }
    catch (Exception ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      ex.printStackTrace();
    }
  }

  public static void showCostumerDialog(LesxController controller, boolean isCreate, LesxCostumerDataModel dataModel, Runnable runnable) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass()
          .getResource(LesxString.COSTUMER_DIALOG_PATH));
      LesxCostumerDialogPaneController controllerCostumer = new LesxCostumerDialogPaneController();
      fxmlLoader.setController(controllerCostumer);
      Pane root = fxmlLoader.load();
      controllerCostumer.init(dataModel, isCreate);
      Stage stage = new Stage();
      stage.setTitle(controllerCostumer.getTitle());
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.setMinHeight(520);
      stage.setMinWidth(465);
      stage.sizeToScene();
      stage.show();
      controllerCostumer.afterSaveProperty()
          .addListener(obs -> runnable.run());
      controllerCostumer.closeProperty()
          .addListener((obs, oldV, newV) -> {
            if (newV) {
              runnable.run();// TODO No se ejecuta si se quita el alert
              stage.close();
            }
          });
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      e.printStackTrace();
    }
    catch (Exception ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      ex.printStackTrace();
    }
  }

  public static void showPricesDialog(LesxController controller) {
    Runnable loadDialog = () -> {
      try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = fxmlLoader.load(controller.getClass()
            .getResource(LesxString.PRICES_DIALOG_PATH)
            .openStream());
        LesxPricesDialogController controllerCostumer = fxmlLoader.getController();
        Stage stage = new Stage();
        controllerCostumer.setExitOperation(stage);
        stage.setTitle(controllerCostumer.getTitle());
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinHeight(515);
        stage.setMinWidth(655);
        stage.sizeToScene();
        stage.show();
      }
      catch (IOException e) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
        e.printStackTrace();
      }
      catch (Exception ex) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
        ex.printStackTrace();
      }
    };
    if (!LesxMain.getInstance()
        .getDbProperty()
        .isPriceLoaded()) {
      LesxXMLUtils.importPriceXMLFileToLesxProperty(loadDialog);
    }
    else {
      loadDialog.run();
    }
  }

  public static void showPriceEditDialog(LesxController controller, boolean isCreate, LesxPriceDataModel dataModel, Runnable runnable) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass()
          .getResource(LesxString.COSTUMER_DIALOG_PATH));
      Stage stage = new Stage();
      LesxPriceDialogPaneController controllerPrice = new LesxPriceDialogPaneController();
      fxmlLoader.setController(controllerPrice);
      Pane root = fxmlLoader.load();
      controllerPrice.init(dataModel, isCreate);
      stage.setTitle(controllerPrice.getTitle());
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.setMinHeight(520);
      stage.setMinWidth(465);
      stage.sizeToScene();
      stage.show();
      controllerPrice.afterSaveProperty()
          .addListener(obs -> runnable.run());
      controllerPrice.closeProperty()
          .addListener((obs, oldV, newV) -> {
            if (newV) {
              runnable.run();// TODO No se ejecuta si se quita el alert
              stage.close();
            }
          });
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      e.printStackTrace();
    }
    catch (Exception ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      ex.printStackTrace();
    }
  }

  public static void showReportDialog(LesxController controller, LesxCostumerDataModel dataModel) {
    Runnable loadDialog = () -> {
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass()
            .getResource(LesxString.REPORT_DIALOG_PATH));
        Stage stage = new Stage();
        LesxReportController controllerReport = new LesxReportController();
        controllerReport.init(dataModel);
        fxmlLoader.setController(controllerReport);
        Pane root = fxmlLoader.load();
        controllerReport.setExitOperation(stage);
        stage.setTitle(controllerReport.getTitle());
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinHeight(680);
        stage.setMinWidth(1024);
        stage.sizeToScene();
        stage.show();
      }
      catch (IOException e) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
        e.printStackTrace();
      }
      catch (Exception ex) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
        ex.printStackTrace();
      }
    };
    if (!LesxMain.getInstance()
        .getDbProperty()
        .isPriceLoaded()
        || !LesxMain.getInstance()
            .getDbProperty()
            .isReportsLoaded()) {
      LesxXMLUtils.importReportsXMLFileToLesxProperty(loadDialog);
    }
    else {
      loadDialog.run();
    }
  }

  public static void showAddCostumerDialog(
      LesxReportController controller,
      LesxCostumerDataModel dataModelCostumer,
      LesxReportsDataModel dataModelReports,
      Runnable runnable) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass()
          .getResource(LesxString.SELECT_COSTUMER_DIALOG_PATH));
      Stage stage = new Stage();
      LesxSelectCostumerDialogController controllerSelect = new LesxSelectCostumerDialogController();
      fxmlLoader.setController(controllerSelect);
      Pane root = fxmlLoader.load();
      controllerSelect.init(dataModelCostumer, dataModelReports);
      stage.setTitle(controllerSelect.getTitle());
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.setMinHeight(500);
      stage.setMinWidth(700);
      stage.show();
      controllerSelect.closeProperty()
          .addListener((obs, oldV, newV) -> {
            if (newV) {
              runnable.run();
              stage.close();
            }
          });
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      e.printStackTrace();
    }
    catch (Exception ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      ex.printStackTrace();
    }
  }

  public static void ShowAddReportTreeDialog(LesxReportController controller, LesxReportsDataModel dataModel, Boolean isCreate, Runnable runnable) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass()
          .getResource(LesxString.COSTUMER_DIALOG_PATH));
      Stage stage = new Stage();
      LesxReportEditDialogController controllerReport = new LesxReportEditDialogController();
      fxmlLoader.setController(controllerReport);
      Pane root = fxmlLoader.load();
      controllerReport.init(dataModel, isCreate);
      stage.setTitle(controllerReport.getTitle());
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.setMinHeight(520);
      stage.setMinWidth(465);
      stage.sizeToScene();
      stage.show();
      controllerReport.afterSaveProperty()
          .addListener(obs -> runnable.run());
      controllerReport.closeProperty()
          .addListener((obs, oldV, newV) -> {
            if (newV) {
              runnable.run();// TODO No se ejecuta si se quita el alert
              stage.close();
            }
          });
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      e.printStackTrace();
    }
    catch (Exception ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-MAIN_ACTIVATE_FXML"));
      ex.printStackTrace();
    }
  }

}
