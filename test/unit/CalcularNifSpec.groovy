import kuorum.util.cif.CalculaNif
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 24/03/14.
 */
class CalcularNifSpec extends Specification {

    @Unroll
    void "test NIF '#orgNif' (#descripcion) => valid=#isValid asociacion=#isAsociacion legalEntity=#isLegalEntity"() {
        given: "The orginal nif"
        when:
        CalculaNif cif = new CalculaNif(orgNif)
        then:
        isValid == cif.isValid()
        isAsociacion == cif.isAsociacion()
        isLegalEntity == cif.isLegalEntity()
        controlDigit == cif.calcControlDigit()
        and: "asociacion implica persona juridica, y persona juridica implica documento valido"
        !cif.isAsociacion() || cif.isLegalEntity()
        !cif.isLegalEntity() || cif.isValid()
        where:
        descripcion                                            | orgNif          | controlDigit  | isValid | isAsociacion | isLegalEntity
        // A/B (Sociedad Anónima / Limitada): personas jurídicas válidas, pero NO asociaciones
        "Sociedad Anónima válida (entidad, no asociación)"      | "A56349707"     | "A56349707"   | true    | false        | true
        "Sociedad Limitada válida (entidad, no asociación)"     | "B86761459"     | "B86761459"   | true    | false        | true
        "Sociedad Limitada con control erróneo"                 | "B12345678"     | "B12345674"   | false   | false        | false

        // Cualquier otro CIF válido cuenta como asociación (y también es persona jurídica)
        "Asociación válida (G)"                                 | "G28197564"     | "G28197564"   | true    | true         | true
        "Asociación válida (G)"                                 | "G08967713"     | "G08967713"   | true    | true         | true
        "Asociación válida (G) con letra de control"            | "G2819756D"     | "G28197564"   | true    | true         | true
        "Asociación válida (C, no solo G)"                      | "C12345674"     | "C12345674"   | true    | true         | true
        "Asociación válida (F, no solo G)"                      | "F12345674"     | "F12345674"   | true    | true         | true
        "Asociación válida (K, control por letra)"              | "K1234567D"     | "K1234567D"   | true    | true         | true
        "Asociación (G) con control erróneo"                    | "G11111111"     | "G11111119"   | false   | false        | false

        // Control obligatorio por NÚMERO (A, B, E, H): con letra no vale
        "A con letra de control (debe ser número)"              | "A5634970G"     | "A56349707"   | false   | false        | false
        "B con letra de control (debe ser número)"              | "B8676145I"     | "B86761459"   | false   | false        | false
        "E válido con número de control"                        | "E12345674"     | "E12345674"   | true    | true         | true
        "E con letra de control (debe ser número)"              | "E1234567D"     | "E12345674"   | false   | false        | false
        "H válido con número de control"                        | "H12345674"     | "H12345674"   | true    | true         | true
        "H con letra de control (debe ser número)"              | "H1234567D"     | "H12345674"   | false   | false        | false

        // Control obligatorio por LETRA (K, P, Q, R, S, N, W): con número no vale
        "K con número de control (debe ser letra)"              | "K12345674"     | "K1234567D"   | false   | false        | false
        "P válido con letra de control"                         | "P1234567D"     | "P1234567D"   | true    | true         | true
        "Q válido con letra de control"                         | "Q1234567D"     | "Q1234567D"   | true    | true         | true
        "R válido con letra de control"                         | "R1234567D"     | "R1234567D"   | true    | true         | true
        "S válido con letra de control"                         | "S1234567D"     | "S1234567D"   | true    | true         | true
        "N válido con letra de control"                         | "N1234567D"     | "N1234567D"   | true    | true         | true
        "W válido con letra de control"                         | "W1234567D"     | "W1234567D"   | true    | true         | true

        // Dígito de control 0 (la suma da 10 y se normaliza a 0; su letra es J)
        "A, control 0 en número"                                | "A10000040"     | "A10000040"   | true    | false        | true
        "A, control 0 en letra J (no vale para A)"              | "A1000004J"     | "A10000040"   | false   | false        | false
        "P, control 0 en letra J"                               | "P1000004J"     | "P1000004J"   | true    | true         | true
        "P, control 0 en número (no vale para P)"               | "P10000040"     | "P1000004J"   | false   | false        | false
        "G, control 0 en número (vale cualquiera)"              | "G10000040"     | "G10000040"   | true    | true         | true
        "G, control 0 en letra J (vale cualquiera)"             | "G1000004J"     | "G10000040"   | true    | true         | true

        // Personas físicas: un DNI o NIE válido no es asociación ni persona jurídica
        "DNI de persona física válido"                          | "53392474K"     | "53392474K"   | true    | false        | false
        "DNI válido"                                            | "50769719W"     | "50769719W"   | true    | false        | false
        "DNI con letra de control incorrecta"                   | "50769719A"     | "50769719W"   | false   | false        | false
        "DNI de 7 dígitos se completa con un cero"              | "1234567L"      | "01234567L"   | true    | false        | false
        "NIE válido (X)"                                        | "X1234567L"     | "X1234567L"   | true    | false        | false
        "NIE válido (Y)"                                        | "Y1234567X"     | "Y1234567X"   | true    | false        | false
        "NIE válido (Z)"                                        | "Z1234567R"     | "Z1234567R"   | true    | false        | false
        "NIE con letra de control incorrecta"                   | "X1234567A"     | "X1234567L"   | false   | false        | false

        // Normalización de la entrada
        "Minúsculas y espacios alrededor"                       | " g28197564 "   | "G28197564"   | true    | true         | true

        // Formatos inválidos: nunca son válidos ni asociación ni persona jurídica
        "DNI sin letra de control"                              | "53392474"      | null          | false   | false        | false
        "DNI con 6 dígitos"                                     | "123456L"       | null          | false   | false        | false
        "DNI con 9 dígitos"                                     | "123456789L"    | null          | false   | false        | false
        "CIF con letra inicial I (no existe)"                   | "I12345674"     | null          | false   | false        | false
        "CIF con letra inicial O (no existe)"                   | "O12345674"     | null          | false   | false        | false
        "Letra inicial T (ni CIF ni NIE)"                       | "T12345674"     | null          | false   | false        | false
        "CIF demasiado corto (8 caracteres)"                    | "G2819756"      | null          | false   | false        | false
        "CIF demasiado largo (10 caracteres)"                   | "G28197564X"    | null          | false   | false        | false
        "CIF con guion"                                         | "G-2819756D"    | null          | false   | false        | false
        "CIF con espacio en medio"                              | "G 28197564"    | null          | false   | false        | false
        "Cadena vacía"                                          | ""              | null          | false   | false        | false
        "Solo espacios"                                         | "   "           | null          | false   | false        | false
        "Valor nulo"                                            | null            | null          | false   | false        | false
        "Datos inválidos"                                       | "1"             | null          | false   | false        | false
        "Datos inválidos"                                       | "1234"          | null          | false   | false        | false
        "Datos inválidos"                                       | "123D"          | null          | false   | false        | false
    }

    @Unroll
    void "un CIF generado con letra inicial #letra es válido, es persona jurídica y asociación=#esAsociacion"() {
        given: "un CIF al que se le calcula el control con la propia clase"
        String conControl = new CalculaNif(letra + "28197560").calcControlDigit()
        when:
        CalculaNif cif = new CalculaNif(conControl)
        then: "calcular el control y validar son coherentes para todas las letras de CIF"
        cif.isValid()
        cif.isLegalEntity()
        cif.isAsociacion() == esAsociacion
        where:
        letra << "ABCDEFGHJKLMNPQRSUVW".toList()
        esAsociacion = !("AB".contains(letra))
    }

    @Unroll
    void "getNif normaliza '#entrada' a '#esperado'"() {
        expect:
        new CalculaNif(entrada).getNif() == esperado
        where:
        entrada        | esperado
        "  g28197564 " | "G28197564"
        "1234567l"     | "01234567L"
        "x1234567l"    | "X1234567L"
        "   "          | ""
        null           | ""
    }

}
