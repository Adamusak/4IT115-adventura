/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package com.github.adamusak.adventura.ui;

import com.github.adamusak.adventura.logika.Hra;
import com.github.adamusak.adventura.logika.IHra;
import com.github.adamusak.adventura.uiText.TextoveRozhrani;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Třída slouží ke spuštění adventury. Při spuštění bez parametru konstruuje
 * okno aplikace, s parametrem -text se spouští v textovém režimu
 * 
 * @author Filip Vencovsky, Adam Novak
 *
 */
public class Application extends javafx.application.Application {

	/**
	 * Spouštěcí metoda pro aplikaci
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			launch(args);
		} else {
			if (args[0].equals("-text")) {
				IHra hra = new Hra();
				TextoveRozhrani ui = new TextoveRozhrani(hra);
				ui.hraj();
			} else {
				System.out.println("Neplatný parametr");
			}
		}
	}

	/**
	 * Metoda, ve které se konstruuje okno, kontroler a hra, která se předává
	 * kontroleru
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Home.fxml"));
		Parent root = loader.load();
		HomeController controller = loader.getController();
		controller.inicializuj(new Hra());
		primaryStage.setTitle("Adventura");
		primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(300);
        primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}