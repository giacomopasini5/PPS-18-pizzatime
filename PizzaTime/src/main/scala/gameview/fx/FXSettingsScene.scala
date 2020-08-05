package gameview.fx

import gameview.Window
import gameview.scene.SettingsScene
import javafx.application.Platform
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TextField}
import utilities.{Difficulty, SettingPreferences}

import scala.jdk.CollectionConverters

case class FXSettingsScene(override val windowManager: Window) extends FXView(Some("SettingsScene.fxml")) with SettingsScene {
  @FXML protected var playerNameField: TextField = _
  @FXML protected var difficultyComboBox: ComboBox[Difficulty.Value] = _
  @FXML protected var backButton, applyButton: Button = _

  Platform.runLater(() => {
    val difficultyList: ObservableList[Difficulty.Value] = FXCollections.observableArrayList[Difficulty.Value]
    difficultyList.addAll(CollectionConverters.SeqHasAsJava(Difficulty.allDifficulty).asJava)
    difficultyComboBox.setItems(difficultyList)
    //difficultyComboBox.getSelectionModel.selectFirst()
  })

  backButton.setOnMouseClicked(_ => observers.foreach(observer => observer.onBack()))

  applyButton.setOnMouseClicked(_ => {
    val selection = difficultyComboBox.getSelectionModel
    if(selection.isEmpty) selection.selectFirst()

    observers.foreach(_.onApply(SettingPreferences(playerName = playerNameField.getText, selection.getSelectedItem)))
  })

  override def showCurrentPreferences(settingPreferences: SettingPreferences): Unit = {
    playerNameField.setText(settingPreferences.playerName)
    difficultyComboBox.getSelectionModel.select(settingPreferences.difficulty)
  }
}