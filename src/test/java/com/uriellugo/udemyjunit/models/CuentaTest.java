package com.uriellugo.udemyjunit.models;

import com.uriellugo.udemyjunit.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;
    TestReporter testReporter;
    TestInfo testInfo;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @BeforeEach
    void initMethodTest(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo = testInfo;
        this.testReporter = testReporter;

        System.out.println("Inicializando el metodo: " + testInfo.getTestMethod().orElse(null).getName());
        this.cuenta = new Cuenta(1L, "Andres", new BigDecimal("1234.2345"));
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando metodo de prueba");
    }

    @DisplayName("Clase Anidada usando @DisplayName")
    @Nested
    class NestedTest {

        @BeforeEach
        void setUp() {
            System.out.println("Inicializando el metodo, @BeforeEach de Nested Class");
        }

        @Test
        void test_nombre_cuenta_nested() {

            // Los atributos de la clase padre pasan a las clases anidadas
            String esperado = "Andres";
            String real = cuenta.getPersona();

            assertNotNull(real);
            assertEquals(esperado, real);
            assertTrue(esperado.equals(real));
        }
    }

    @Test
    void test_nombre_cuenta() {
        String esperado = "Andres";
        String real = cuenta.getPersona();

        assertNotNull(real);
        assertEquals(esperado, real);
        assertTrue(esperado.equals(real));
    }

    @Test
    @Tag("cuenta")
    void test_dos_objetos_equals() {
        Cuenta cuenta2 = new Cuenta(1L, "Andres", new BigDecimal("1234.2345"));
        System.out.println("Cuenta 1: " + cuenta);
        System.out.println("Cuenta 2: " + cuenta2);
        assertEquals(cuenta, cuenta2, () -> "Los objetos no son iguales -> equals()");
    }

    @Test
    @Tags({@Tag("cuenta"), @Tag("reference")})
    void test_dos_objetos_same() {
        System.out.println();
        Cuenta cuenta2 = cuenta;
        Cuenta cuenta3 = new Cuenta(1L, "Andres", new BigDecimal("1234.2345"));

        // Método para ver el hash predeterminado en hashCode() sin utilizar override - Básicamente para comparar direcciones de memoria
        System.out.println("cuenta: " + Integer.toHexString(System.identityHashCode(cuenta)));
        System.out.println("cuenta2: " + Integer.toHexString(System.identityHashCode(cuenta2)));
        System.out.println("cuenta3: " + Integer.toHexString(System.identityHashCode(cuenta3)));

        assertSame(cuenta, cuenta2);
        assertNotSame(cuenta, cuenta3);
    }

    @Test
    @DisplayName("Probando el nombre de la cuenta corriente!")
    void test_nombre_cuenta_displayName() {
        String esperado = "Andres";
        String real = cuenta.getPersona();

        assertNotNull(real);
        assertEquals(esperado, real);
    }

    @Test
    @Disabled("El test se deshabilito por estas razones...")
    void test_nombre_cuenta_disabled() {

        // Forzar un error en el test
        fail("Fallo forzado");

        String esperado = "Andres";
        String real = cuenta.getPersona();

        assertNotNull(real);
        assertEquals(esperado, real);
    }

    @Test
    void test_nombre_cuenta_mensaje_error() {
        String esperado = "Andres";
        String real = cuenta.getPersona();

        // Los mensajes van a crear instancias aun si no se ocupan, por lo que podrían consumir recursos a la larga
        assertNotNull(real, "La cuenta no puede ser nula");
        // Para evitar esto, se puede usar una expresión lambda para evitar cargar el mensaje de error
        assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se esperaba");
        assertTrue(esperado.equals(real), () -> "El nombre de la cuenta no es el que se esperaba");
    }

    @Test
    void test_saldo_cuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1234.2345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void test_referencia_cuenta() {
        cuenta = new Cuenta(1L, "Jhon Doe", new BigDecimal("1234.1234"));
        Cuenta cuenta2 = new Cuenta(1L, "Jhon Doe", new BigDecimal("1234.1234"));

        assertEquals(cuenta2, cuenta);
        //assertNotEquals(cuenta2, cuenta);
    }

    @Test
    void test_debito_cuenta() {
        cuenta = new Cuenta(1L, "Andres", new BigDecimal("1000.1234"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.1234", cuenta.getSaldo().toPlainString());

        cuenta.debito(new BigDecimal(100));
    }

    @Test
    void test_credito_cuenta() {
        cuenta = new Cuenta(1L, "Andres", new BigDecimal("1000.1234"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.1234", cuenta.getSaldo().toPlainString());

        cuenta.debito(new BigDecimal(100));
    }

    @Test
    @DisplayName("Test assertThrow")
    void test_dinero_insuficiente_cuenta() {
        cuenta = new Cuenta(1L, "Andres", new BigDecimal("1000.1234"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(new BigDecimal(1500)));
        String mensaje = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, mensaje);
    }

    @Test
    void test_transferir_dinero_cuentas() {
        Cuenta cuentaOrigen = new Cuenta(1L, "Andres", new BigDecimal("1500.1234"));
        Cuenta cuentaDestino = new Cuenta(1L, "Jhon Doe", new BigDecimal("2500"));

        Banco banco = new Banco();
        banco.setNombre("Banco del estado");
        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500));
        assertEquals("1000.1234", cuentaOrigen.getSaldo().toPlainString());
        assertEquals("3000", cuentaDestino.getSaldo().toPlainString());

    }

    @Test
    void test_relacion_banco_cuentas() {
        Cuenta cuenta1 = new Cuenta(1L,"Andres", new BigDecimal("1500.1234"));
        Cuenta cuenta2 = new Cuenta(2L, "Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta3 = new Cuenta(3L, "Mariana", new BigDecimal("1500.1234"));
        Cuenta cuenta4 = new Cuenta(4L, "Jhoana", new BigDecimal("2500"));

        Banco banco = new Banco();

        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta3);
        cuentas.add(cuenta4);
        banco.addCuenta(cuentas);
        banco.setNombre("Banco del estado");

        // Probar relación en ambos sentidos
        assertEquals(4, banco.getCuentas().size()); // Existe relación del banco con sus cuentas
        assertEquals("Banco del estado", cuenta1.getBanco().getNombre()); // Existe relación de la cuenta con el banco
        assertEquals("Banco del estado", cuentas.get(1).getBanco().getNombre()); // Existe relación de una lista de cuentas con el banco

        // Probar que una cuenta a nombre de "Andres" exista en el banco
        assertEquals("Andres", banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Andres"))
                .findFirst()
                .get().getPersona());

        // Probar que una cuenta a nombre de "Andres" exista en el banco 2da Forma
        assertTrue(banco.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("Andres")));
    }

    @Test
    void test_relacion_banco_cuentas_assertAll() {
        Cuenta cuenta1 = new Cuenta(1L, "Andres", new BigDecimal("1500.1234"));
        Cuenta cuenta2 = new Cuenta(2L, "Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta3 = new Cuenta(3L, "Mariana", new BigDecimal("1500.1234"));
        Cuenta cuenta4 = new Cuenta(4L, "Jhoana", new BigDecimal("2500"));

        Banco banco = new Banco();

        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta3);
        cuentas.add(cuenta4);
        banco.addCuenta(cuentas);

        banco.setNombre("Banco del estado");

        // Sirve para en caso de que si varias aserciones de la agrupación fallan, va a generar el reporte de cada uno, y no solo del primero que aparezca
        assertAll(() -> assertEquals(4, banco.getCuentas().size(), () -> "El banco no tiene las cuentas esperadas"),
                () -> assertEquals("Banco del estado", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Banco del estado", cuentas.get(1).getBanco().getNombre()),
                () -> assertEquals("Andres", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Andres"))
                        .findFirst()
                        .get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("Andres"))));


    }

    @Test
    void test_relacion_banco_cuentas_assertAll_doble_agrupacion() {
        Cuenta cuenta1 = new Cuenta(1L, "Andres", new BigDecimal("1500.1234"));
        Cuenta cuenta2 = new Cuenta(2L, "Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta3 = new Cuenta(3L, "Mariana", new BigDecimal("1500.1234"));
        Cuenta cuenta4 = new Cuenta(4L, "Jhoana", new BigDecimal("2500"));

        Banco banco = new Banco();

        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta3);
        cuentas.add(cuenta4);
        banco.addCuenta(cuentas);

        banco.setNombre("Banco del estado");

        // Si se separa por agrupaciones, el test frena en el primer grupo de aserciones, el segundo no es tocado, similar a como si fueran aserciones individuales
        assertAll(() -> assertEquals(4, banco.getCuentas().size()),
                () -> assertEquals("Banco del estado", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Banco del estado", cuentas.get(1).getBanco().getNombre())
        );

        assertAll(() -> assertEquals("Andres", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Andres"))
                        .findFirst()
                        .get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("Andres")))
        );


    }

    @Test
    @DisabledOnOs({ OS.MAC, OS.LINUX})
    void test_disabled_linux_mac() {
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void test_solo_windows() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_11)
    void test_solo_jre11() {

    }

    @Test
    @DisabledOnJre(JRE.JAVA_8)
    void test_disabled_jre8() {
    }

    @Test
    void imprimir_system_properties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "1.8.*")
    void test_java_version_system_property() {
    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = "x86_64")
    void test_disabled_os_arch_x64() {
    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void test_dev() {
        // En build and run JUnit5
        // -ea -DENV=dev
    }

    @Test
    void imprimir_environment_variables() {
        Map<String, String> getEnv = System.getenv();
        getEnv.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = "jdk-8.*")
    void test_java_home_environment_variable() {
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "10")
    void test_numero_procesadores() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "DEV")
    void testEnv() {
        // Configurarlo como 'edit configuration arranque'
        // ENVIRONMENT=DEV
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "PROD")
    void test_environment_prod_disabled() {
        // Configurarlo como
        // ENVIRONMENT=PROD
    }

    @Test
    @DisplayName("Test saldo cuenta Dev assumingTrue")
    void test_saldo_cuenta_dev() {
        final String DEV = "PROD";
        boolean isDev = DEV.equals(System.getProperty("ENV"));

        // Si la condición es true, corre el test, si no, aborta el test de aquí en adelante
        assumeTrue(isDev, "La prueba no se hace en desarrollo: " + DEV);
        System.out.println("El perfil DEV es: " + isDev);

        assertNotNull(cuenta.getSaldo());
        assertEquals(1234.2345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Test saldo cuenta Dev2 assumingThat")
    void test_saldo_cuenta_dev2() {
        final String DEV = "DEV";
        boolean isDev = DEV.equals(System.getProperty("ENV"));

        // If condition is true then executes, else DO NOT abort test continue rest of code in test.
        assumingThat(isDev, () -> {
            System.out.println("El perfil DEV es: " + isDev);
            assertNotNull(cuenta.getSaldo());
            assertEquals(1234.2345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });
    }

    @DisplayName("Probando test repetido")
    @RepeatedTest(value = 5, name = "{displayName} - Repetición número {currentRepetition} de {totalRepetitions}")
    void test_debito_cuenta_repetir(RepetitionInfo repetitionInfo) { //Inyección de dependencia con la información del test repetido gracias a RepetitionInfoParameterResolver sobre la anotación @RepeatedTest
        System.out.println("Estamos en la repetición: " + repetitionInfo.getCurrentRepetition() +
                " de " + repetitionInfo.getTotalRepetitions() + " en total");
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1134, cuenta.getSaldo().intValue());
        assertEquals(1134.2345, cuenta.getSaldo().doubleValue());
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @ValueSource(doubles = {100, 200, 300, 500, 700, 1000, 2000})
    void test_debito_cuenta_parametrized_value_source(double monto) {
        assumingThat(() -> monto < cuenta.getSaldo().doubleValue(), () -> {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

        assumingThat(() -> monto > cuenta.getSaldo().doubleValue(),
                () -> {
            assertThrows(DineroInsuficienteException.class, () -> {
               cuenta.debito(new BigDecimal(monto));
                System.out.println("Dentro de assertThrows");
            }, () -> "Dinero insuficiente");
                });
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvSource(value = {"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000", "7,2000"})
    void test_debito_cuenta_parametrized_csvSource(Double index, String monto) {

        System.out.println(index + " -> " + monto);

        assumingThat(() -> Double.parseDouble(monto) < cuenta.getSaldo().doubleValue(), () -> {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

        assumingThat(() -> Double.parseDouble(monto) > cuenta.getSaldo().doubleValue(),
                () -> {
                    assertThrows(DineroInsuficienteException.class, () -> {
                        cuenta.debito(new BigDecimal(monto));
                        System.out.println("Dentro de assertThrows");
                    }, () -> "Dinero insuficiente");
                });
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvSource(value = {"200,100,John,Andres", "250,200,Pepe,Pepe", "300,300,Maria,Antonieta", "510,500,Lucas,Lucas", "750,700,Lucio,Martin", "950,1000,Colin,Sebastian", "2530,2000,Fernanda,Fernanda"})
    void test_debito_cuenta_parametrized_csvSource2(Double saldo, String monto, String esperado, String actual) {

        System.out.println(saldo + " -> " + monto);

        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.setPersona(actual);

        assumingThat(() -> Double.parseDouble(monto) < cuenta.getSaldo().doubleValue(), () -> {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

        assumingThat(() -> Double.parseDouble(monto) > cuenta.getSaldo().doubleValue(),
                () -> {
                    assertThrows(DineroInsuficienteException.class, () -> {
                        cuenta.debito(new BigDecimal(monto));
                        System.out.println("Dentro de assertThrows");
                    }, () -> "Dinero insuficiente");
                });

        assumingThat(cuenta.getPersona() == esperado, () -> {
            assertEquals(esperado, cuenta.getPersona());
        });
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void test_debito_cuenta_parametrized_csvFileSource(String monto) {

        assumingThat(() -> Double.parseDouble(monto) < cuenta.getSaldo().doubleValue(), () -> {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

        assumingThat(() -> Double.parseDouble(monto) > cuenta.getSaldo().doubleValue(),
                () -> {
                    assertThrows(DineroInsuficienteException.class, () -> {
                        System.out.println("Dentro de assertThrows: monto -> " + monto);
                        cuenta.debito(new BigDecimal(monto));
                    }, () -> "Dinero insuficiente");
                });
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource(value = "montoList")
    void test_debito_cuenta_parametrized_methodSource(String monto) {

        assumingThat(() -> Double.parseDouble(monto) < cuenta.getSaldo().doubleValue(), () -> {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

        assumingThat(() -> Double.parseDouble(monto) > cuenta.getSaldo().doubleValue(),
                () -> {
                    assertThrows(DineroInsuficienteException.class, () -> {
                        System.out.println("Dentro de assertThrows: monto -> " + monto);
                        cuenta.debito(new BigDecimal(monto));
                    }, () -> "Dinero insuficiente");
                });
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700", "1000", "2000");
    }

    @Test
    void test_reporter(TestReporter testReporter) {
        testReporter.publishEntry("Hola mundo desde la salida de JUnit");
        testReporter.publishEntry("STATUS", "OK");
    }

    @Test
    @Tag("TestInfo") @Tag("TestReporter")
    @DisplayName("Desplegando testInfo y testReporter")
    void test_info_and_test_reporter(TestInfo testInfo, TestReporter testReporter) {
        if (testInfo.getTags().contains("TestInfo")) {
            testReporter.publishEntry("TAGS", "Se encontro el Tag " + testInfo.getTags().toString());
        }
    }

    @Test
    @Tag("timeout")
    @Timeout(3)
    void test_timeout() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    @Tag("timeout")
    @Timeout(3)
    void test_timeout_without_throws() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            testReporter.publishEntry("THROW_EXCEPTION", e.getClass().getName());
        }
    }

    @Test
    @Tag("timeout")
    @Timeout(value = 1500, unit = TimeUnit.MILLISECONDS)
    void test_timeout_2() {

        // Espera no lanzar ninguna excepción, en caso que la lance, el test lanza un error
        assertDoesNotThrow(() -> {
            TimeUnit.MILLISECONDS.sleep(1200);
        });
    }

    @Test
    @Tag("timeout")
    void test_timeout_assert_timeout() {
        Duration timeout = Duration.ofMillis(500);


        assertTimeout(timeout, () -> {
            TimeUnit.MILLISECONDS.sleep(200);
        }, () -> "Se demoro mas que " + timeout);
    }

}