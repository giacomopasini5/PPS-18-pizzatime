import gamemanager.{GameManager, ImageLoader, ViewObserver}
import gameview.Window
import gameview.fx.FXWindow
import gameview.scene.SceneType._
import javafx.application.Application
import javafx.stage.Stage
import utilities.Intent
import gameview.fx.FXWindow.addObserver

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class Main extends Application {
  def start(primaryStage: Stage): Unit = {
    initializeGame(primaryStage)
  }

  private def initializeGame(primaryStage: Stage): Future[Unit] = Future {
    val view: Window = FXWindow(primaryStage, "PizzaTime")
    val observers: Set[ViewObserver] = Set(new GameManager())
    addObserver(observers)
    view.scene_(new Intent(MainScene))

    val f = Future {
      ImageLoader.generateImages()
    }

    f.onComplete {
      case Success(value) => view.showView()
      case Failure(e) => e.printStackTrace
    }

  }
}