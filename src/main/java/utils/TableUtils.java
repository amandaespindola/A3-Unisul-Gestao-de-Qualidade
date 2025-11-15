package utils;

import java.util.function.Consumer;

import javax.swing.JTable;

public class TableUtils {

	private TableUtils() {
	}

	public static void addMouseClickListener(JTable table, Consumer<java.awt.event.MouseEvent> action) {

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				action.accept(evt);
			}
		});
	}
}
