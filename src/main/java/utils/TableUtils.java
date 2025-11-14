package utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

public class TableUtils {

    private TableUtils() {
    }

    /**
     * Adiciona um MouseListener a uma JTable para tratar o evento mouseClicked.
     *
     * @param table A JTable a ser configurada.
     * @param clickAction A ação a ser executada quando a tabela for clicada.
     */
    public static void addMouseClickListener(JTable table, java.util.function.Consumer<MouseEvent> action) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                action.accept(evt);
            }
        });
    }
}
