package utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class ConstantesTest {

	@Test
	void testConstantesNaoPodeSerInstanciada() throws Exception {
		Constructor<Constantes> ctor = Constantes.class.getDeclaredConstructor();
		ctor.setAccessible(true);

		assertThrows(UnsupportedOperationException.class, ctor::newInstance);
	}

	@Test
	void testUIConstantsNaoPodeSerInstanciada() throws Exception {
		Constructor<Constantes.UIConstants> ctor = Constantes.UIConstants.class.getDeclaredConstructor();
		ctor.setAccessible(true);

		assertThrows(UnsupportedOperationException.class, () -> {
			ctor.newInstance();
		});
	}
}
