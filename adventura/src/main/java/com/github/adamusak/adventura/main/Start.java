/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package com.github.adamusak.adventura.main;

import com.github.adamusak.adventura.logika.*;
import com.github.adamusak.adventura.uiText.TextoveRozhrani;

/*******************************************************************************
 * Třída {@code Start} je hlavní třídou projektu, který ...
 *
 * @author Adam Novak
 * @version 0.01
 */
public class Start {
	/***************************************************************************
	 * Metoda, prostřednictvím níž se spouští celá aplikace.
	 *
	 * @param args
	 *            Parametry příkazového řádku
	 */
	public static void main(String[] args) {

		IHra hra = new Hra();
		TextoveRozhrani ui = new TextoveRozhrani(hra);
		ui.hraj();
	}
}
