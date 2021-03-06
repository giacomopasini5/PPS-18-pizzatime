package gameview.fx

import gameview.Window
import gameview.scene.Scene
import javafx.application.Platform
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TextField}
import utilities.{Difficulty, SettingPreferences}

import scala.jdk.CollectionConverters

/** Represents the scene with the settings made with JavaFX.
 *
 * @param windowManager the window on which the scene is applied
 */
case class FXSettingsScene(override val windowManager: Window) extends FXView(Some("SettingsScene.fxml")) with Scene {
  @FXML protected var playerNameField: TextField = _
  @FXML protected var difficultyComboBox: ComboBox[Difficulty.Value] = _
  @FXML protected var backButton, saveButton: Button = _

  Platform.runLater(() => {
    val difficultyList: ObservableList[Difficulty.Value] = FXCollections.observableArrayList[Difficulty.Value]
    difficultyList.addAll(CollectionConverters.SeqHasAsJava(Difficulty.allDifficulty).asJava)
    difficultyComboBox.setItems(difficultyList)
  })

  backButton.setOnMouseClicked(_ => FXWindow.observers.foreach(_.onBack()))

  saveButton.setOnMouseClicked(_ => {
    val selection = difficultyComboBox.getSelectionModel
    if(selection.isEmpty) selection.selectFirst()

    FXWindow.observers.foreach(_.onSave(SettingPreferences(playerNameField.getText, selection.getSelectedItem)))
  })

  def showCurrentPreferences(settingPreferences: SettingPreferences): Unit = {
    playerNameField.setText(settingPreferences.playerName)
    difficultyComboBox.getSelectionModel.select(settingPreferences.difficulty)
  }
}
