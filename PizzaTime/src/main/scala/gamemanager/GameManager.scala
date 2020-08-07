package gamemanager

import gamemanager.handlers.PreferencesHandler
import gameview.fx.FXSettingsScene
import gameview.scene.{Scene, SceneType}
import utilities.MessageTypes.Info
import utilities.{Intent, SettingPreferences}

class GameManager extends ViewObserver {

  var endGame: Boolean = false
  var numCycle: Int = 0

  /** Notifies that the game has started */
  override def notifyStartGame(): Unit = ???

  /** Notifies that there's a shoot */
  override def notifyShoot(): Unit = ???

  /** Notifies that the player has moved */
  override def notifyMovement(): Unit = ???

  override def notifySettings(): Unit = ???

  /** Notifies the transition to the game scene */
  override def onStartGame(): Unit = {
    require(GameManager.view.isDefined)
    GameManager.view.get.windowManager.scene_(new Intent(SceneType.GameScene))
  }

  /** Notifies the transition to the settings scene */
  override def onSettings(): Unit = {
    require(GameManager.view.isDefined)
    GameManager.view.get.windowManager.scene_(new Intent(SceneType.SettingScene))
  }

  /** Notifies the transition to the credits scene */
  override def onCredits(): Unit = {
    require(GameManager.view.isDefined)
    GameManager.view.get.windowManager.scene_(new Intent(SceneType.CreditsScene))
  }

  /** Notifies the intent to exit from game */
  override def onExit(): Unit = GameManager.view.get.windowManager.closeView()


  /** Notifies to go back to the previous scene */
  override def onBack(): Unit = {
    require(GameManager.view.isDefined)
    GameManager.view.get.windowManager.scene_(new Intent(SceneType.MainScene))
  }

  /** Notifies to save new game settings */
  override def onApply(settingPreferences: SettingPreferences): Unit = {
    require(GameManager.view.isDefined)

    PreferencesHandler.setName(settingPreferences.playerName)
    PreferencesHandler.setDifficulty(settingPreferences.difficulty)

    GameManager.view.get.windowManager.showMessage("Save confirmation", "Settings saved successfully.", Info)
  }

  override  def  init () :  Unit  =  GameManager.view match {
    case v if v.isInstanceOf[Option[FXSettingsScene]] => v.asInstanceOf[FXSettingsScene].showCurrentPreferences(SettingPreferences (
      PreferencesHandler.getName,
      PreferencesHandler.getDifficulty
    ))
    case _ =>
  }

}

object GameManager {
  var view: Option[Scene] = None

  def view_(view: Scene): Unit = this.view = Some(view)
}