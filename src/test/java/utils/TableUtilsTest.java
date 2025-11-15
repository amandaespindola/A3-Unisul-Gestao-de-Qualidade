package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TableUtilsTest {

    @Test
    @DisplayName("addMouseClickListener deve disparar a ação ao clicar na tabela")
    void testAddMouseClickListener() {

        JTable tabela = new JTable();
        AtomicBoolean clicou = new AtomicBoolean(false);

        // adiciona o listener
        TableUtils.addMouseClickListener(tabela, evt -> clicou.set(true));

        // simula um clique
        MouseEvent evt = new MouseEvent(
                tabela,
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                10, 10,
                1,
                false
        );

        // dispara o evento
        for (var listener : tabela.getMouseListeners()) {
            listener.mouseClicked(evt);
        }

        assertTrue(clicou.get(), "A ação do clique deve ser executada ao clicar na tabela.");
    }

    @Test
    @DisplayName("TableUtils deve possuir construtor privado para impedir instanciação direta")
    void testConstructorIsPrivate() throws Exception {
        var constructor = TableUtils.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertDoesNotThrow(
                (Executable) constructor::newInstance,
                "Construtor privado deve permitir instanciação via reflexão sem erro"
        );

    }
}
