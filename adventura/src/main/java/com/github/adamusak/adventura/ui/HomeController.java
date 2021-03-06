/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package com.github.adamusak.adventura.ui;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import com.github.adamusak.adventura.logika.Hra;
import com.github.adamusak.adventura.logika.IHra;
import com.github.adamusak.adventura.logika.Prostor;
import com.github.adamusak.adventura.logika.Vec;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Kontroler, který zprostředkovává komunikaci mezi grafikou a logikou adventury
 * 
 * @author Filip Vencovsky, Adam Novak
 *
 */
public class HomeController extends GridPane implements Observer {

	@FXML
	private JFXTextField vstupniText;
	@FXML
	private JFXTextArea vystup;
	@FXML
	private JFXListView<Vec> seznamVeciMistnost;
	@FXML
	private JFXListView<Prostor> seznamVychodu;
	@FXML
	private JFXListView<String> Batoh;
	@FXML
	private ImageView uzivatel;
	@FXML
	private JFXHamburger hamburger;
	@FXML
	private JFXButton Napoveda, InfoHra, ZmenaVzhledu, KonecHry, NovaHra;
	@FXML
	JFXDrawer drawer;
	@FXML
	GridPane scene;
	@FXML
	StackPane dialog;

	private IHra hra;
	private double xOffset = 0;
	private double yOffset = 0;

	/**
	 * metoda čte příkaz ze vstupního textového pole a zpracuje ho
	 */
	@FXML
	public void odesliPrikaz() {
		/* Zpracovává příkaz zadaný do příkazové řádky */
		String vystupPrikazu = hra.zpracujPrikaz(vstupniText.getText());
		vystup.appendText("\n\n-------" + vstupniText.getText() + "-------\n");
		vystup.appendText(vystupPrikazu);
		vstupniText.setText("");
		/* Při vstupu se ptá jestli neskončila hra */
		if (hra.konecHry()) {
			vystup.appendText("\n\n-------Konec hry-------\n");
			vstupniText.setDisable(true);
			/* zobrazit nabídku na novou hru */
		}
	}

	@FXML
	public void Seber() {
		/* Zpracovává příkaz při kliknutí na kontextové menu seber */
		Vec koho = seznamVeciMistnost.getSelectionModel().getSelectedItem();
		/* Kontroluje zda, je označený item, který se má sebrat */
		if (koho == null) {
			String vystupPrikazu = hra.zpracujPrikaz("seber");
			vystup.appendText("\n\n-------seber-------\n");
			vystup.appendText(vystupPrikazu);
		} else {
			String input = ("seber " + seznamVeciMistnost.selectionModelProperty().get().getSelectedItem().toString());
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
			update(null, vystupPrikazu);
		}
	}

	@FXML
	public void Polož() {
		/* Zpracovává příkaz při kliknutí na kontextové menu seber */
		String koho = Batoh.getSelectionModel().getSelectedItem();
		/* Kontroluje zda, je označený item, který se má sebrat */
		if (koho == null) {
			String vystupPrikazu = hra.zpracujPrikaz("poloz");
			vystup.appendText("\n\n-------poloz-------\n");
			vystup.appendText(vystupPrikazu);
		} else {
			String input = ("poloz " + Batoh.selectionModelProperty().get().getSelectedItem().toString());
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
			update(null, vystupPrikazu);
		}
	}

	@FXML
	public void Jdi() {
		/* Zpracovává příkaz při kliknutí na kontextové menu jdi */
		Prostor kam = seznamVychodu.getSelectionModel().getSelectedItem();
		/* Kontroluje zda, je označené umístění, kam se má jít */
		if (kam == null) {
			String vystupPrikazu = hra.zpracujPrikaz("jdi");
			vystup.appendText("\n\n-------jdi-------\n");
			vystup.appendText(vystupPrikazu);
		} else {
			String input = ("jdi " + seznamVychodu.selectionModelProperty().get().getSelectedItem().toString());
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
		}
	}

	@FXML
	public void ZmenaSchema() {
		/* Stisknutí tlačítka Změna vzhledu */
		/* Převeď StackPane do popředí */
		dialog.toFront();
		/* Nastav nový dialog */
		JFXDialogLayout obsah = new JFXDialogLayout();
		obsah.setHeading(new Text("Výběr vzhledu"));
		obsah.setBody(new Text("K výběru je z následujících následujících barevných schémat"));
		JFXDialog vyber = new JFXDialog(dialog, obsah, JFXDialog.DialogTransition.CENTER);
		/* Nastav tlačítko 1 */
		JFXButton Schéma1 = new JFXButton("Červeno-šedé");
		Schéma1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				vyber.close();
				String css = Application.class.getResource("css/scene.css").toExternalForm();
				try {
					ZmenaCSS(css);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		/* Nastav tlačítko 2 */
		JFXButton Schéma2 = new JFXButton("Tyrkysové");
		Schéma2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				vyber.close();
				String css = Application.class.getResource("css/scene2.css").toExternalForm();
				try {
					ZmenaCSS(css);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		/* Nastav tlačítko 3 */
		JFXButton Schéma3 = new JFXButton("Šedé");
		Schéma3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				vyber.close();
				String css = Application.class.getResource("css/scene3.css").toExternalForm();
				try {
					ZmenaCSS(css);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		/* Nastav tlačítko 4 */
		JFXButton Zrušit = new JFXButton("Zrušit");
		Zrušit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				vyber.close();
			}
		});
		/* Nastav akce při otevření a zavření dialogu */
		vyber.setOnDialogOpened(event -> {
			/* Při otevření převeď StackPane do popředí */
			dialog.toFront();
		});
		vyber.setOnDialogClosed(event -> {
			/* Při zavření převeď StackPane do pozadí */
			dialog.toBack();
		});
		/* Přidej tlačítka do dialogu */
		obsah.setActions(Schéma1, Schéma2, Schéma3, Zrušit);
		/* Zobraz dialog */
		vyber.show();
	}

	/**
	 * Metoda bude soužit pro předání objektu se spuštěnou hrou kontroleru a zobrazí
	 * stav hry v grafice.
	 * 
	 * @param objekt
	 *            spuštěné hry
	 */
	public void inicializuj(IHra hra) {
		/* Zavři menu */
		drawer.open();
		/* Nastav log */
		vystup.setText(hra.vratUvitani());
		vystup.setEditable(false);
		/* Naplň listview */
		this.hra = hra;
		seznamVeciMistnost.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVeci());
		seznamVychodu.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
		/* Nastav mapu */
		uzivatel.setX(hra.getHerniPlan().getAktualniProstor().getX());
		uzivatel.setY(hra.getHerniPlan().getAktualniProstor().getY());
		hra.getHerniPlan().addObserver(this);
		// hra.getBatoh().addObserver(this);
		/* Nastavení animace pro menu ikony */
		HamburgerBasicCloseTransition transition = new HamburgerBasicCloseTransition(hamburger);
		transition.setRate(-1);
		
		/* Přidání funkcí tlačítkům */
		hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();
			drawer.toggle();
		});
		Napoveda.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			/* Skrytí menu */
			transition.setRate(transition.getRate() * -1);
			transition.play();
			drawer.toggle();
			/* Zobrazení v logu */
			String input = ("napoveda");
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
			/* Zobrazení v html */
			Stage stage = new Stage();
			stage.setTitle("Nápověda");
			WebView webview = new WebView();
			webview.getEngine().load(Application.class.getResource("html/napoveda.html").toExternalForm());
			stage.setScene(new Scene(webview, 720, 480));
			stage.show();
		});
		ZmenaVzhledu.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
		transition.setRate(transition.getRate() * -1);
		transition.play();
		drawer.toggle();
		});
		InfoHra.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			/* Skrytí menu */
			transition.setRate(transition.getRate() * -1);
			transition.play();
			drawer.toggle();
			/* zobrazení v HTML */
			Stage stage = new Stage();
			stage.setTitle("O hře");
			WebView webview = new WebView();
			webview.getEngine().load(Application.class.getResource("html/hra.html").toExternalForm());
			stage.setScene(new Scene(webview, 720, 480));
			stage.show();
		});
		NovaHra.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			try {
				NoveOkno();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		KonecHry.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			Stage stage = (Stage) KonecHry.getScene().getWindow();
			stage.close();
		});
	}
	
	
	
	private void ZmenaCSS(String css) throws IOException {
		scene.getStylesheets().clear();
		scene.getStylesheets().add(css);
		scene.applyCss();
	}
	public void NoveOkno() throws Exception {
		Stage stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Home.fxml"));
		Parent root = loader.load();
		HomeController controller = loader.getController();
		controller.inicializuj(new Hra());
		stage.setTitle("Adventura");
		stage.setMinWidth(300);
		stage.setMinHeight(300);
		stage.setScene(new Scene(root));
		Stage stageold = (Stage) KonecHry.getScene().getWindow();
		stage.show();
		stageold.close();
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		seznamVeciMistnost.getItems().clear();
		seznamVychodu.getItems().clear();
		seznamVeciMistnost.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVeci());
		seznamVychodu.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
		uzivatel.setX(hra.getHerniPlan().getAktualniProstor().getX());
		uzivatel.setY(hra.getHerniPlan().getAktualniProstor().getY());
		String vystupPrikazu = hra.zpracujPrikaz("obsahBatohu");
		String[] ItemyBatohu = vystupPrikazu.split(":");
		Batoh.getItems().clear();
		if (ItemyBatohu[1].contains(", ")) {
			String[] Itemy = ItemyBatohu[1].split(", ");
			/*
			 * for (String Item : Itemy) { Batoh.getItems().addAll(Item); }
			 */
			Batoh.getItems().addAll(Itemy);
		}
	}
}