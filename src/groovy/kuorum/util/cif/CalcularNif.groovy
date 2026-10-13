package kuorum.util.cif

import java.util.regex.Pattern

/**
 * Clase que calcula o valida un documento de identificación del reino de España. (DNI, NIE, CIF).
 */
public class CalculaNif {

    private static final String LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE"
    private static final String LETRAS_NIE = "XYZ"
    private static final String LETRAS_CIF = "ABCDEFGHJKLMNPQRSUVW"
    private static final String DIGITO_CONTROL_CIF = "JABCDEFGHI"

    private static final String CIF_LETRAS_OBLIGATORIAS = "KPQRSNW"
    private static final String CIF_NUMEROS_OBLIGATORIOS = "ABEH"

    // Personas jurídicas que NO se consideran asociaciones: A = Sociedad Anónima, B = Sociedad Limitada.
    // Cualquier otra letra de CIF válido cuenta como asociación.
    private static final String LETRAS_CIF_NO_AUTORIZADAS_ASOCIACIONES = "AB"

    // Estructura de un documento: el tipo de entidad lo da siempre la primera letra (1 carácter) y
    // los 8 primeros caracteres son la base; el 9º es el dígito o letra de control.
    private static final int LONGITUD_LETRA_TIPO_ENTIDAD = 1
    private static final int LONGITUD_BASE_SIN_CONTROL = 8

    // Variables explicativas precompiladas para el validador de formatos
    private static final Pattern PATRON_DNI = ~/^[0-9]{7,8}[A-Z]$/
    private static final Pattern PATRON_NIE = ~/^[$LETRAS_NIE][0-9]{7}[A-Z]$/
    private static final Pattern PATRON_CIF = ~/^[$LETRAS_CIF][0-9]{7}[0-9A-Z]$/

    private boolean isValidFormat = false
    private String nif
    private TipoDocumento tipo

    enum TipoDocumento {
        DNI, NIE, CIF, DESCONOCIDO
    }

    public CalculaNif(String nifInput) {
        if (nifInput == null) {
            this.nif = ""
        } else {
            this.nif = nifInput.trim().toUpperCase()
        }
        determinarTipoYFormato()
    }

    /**
     * Clasifica el NIF usando un switch con constantes explicativas (Patterns).
     */
    private void determinarTipoYFormato() {
        switch (this.nif) {
            case PATRON_DNI:
                // Si es un DNI, separamos la letra, completamos con ceros si tiene 7, y reconstruimos
                String numeros = this.nif.substring(0, this.nif.length() - 1)
                String letra = this.nif.substring(this.nif.length() - 1)

                this.nif = completaCeros(numeros, LONGITUD_BASE_SIN_CONTROL) + letra
                this.tipo = TipoDocumento.DNI
                this.isValidFormat = true
                break

            case PATRON_NIE:
                this.tipo = TipoDocumento.NIE
                this.isValidFormat = true
                break

            case PATRON_CIF:
                this.tipo = TipoDocumento.CIF
                this.isValidFormat = true
                break

            default:
                this.tipo = TipoDocumento.DESCONOCIDO
                this.isValidFormat = false
                break
        }
    }

    def getNif() {
        return nif
    }

    private String primeraLetra() {
        if (!isValidFormat) {
            return ""
        }
        return nif.substring(0, LONGITUD_LETRA_TIPO_ENTIDAD)
    }

    private String baseSinControl(String documento) {
        return documento.substring(0, LONGITUD_BASE_SIN_CONTROL)
    }

    public String calcControlDigit() {
        if (!isValidFormat) {
            return null
        }

        switch (this.tipo) {
            case TipoDocumento.DNI:
                return calculaDni(baseSinControl(nif))
            case TipoDocumento.NIE:
                return calculaNie(baseSinControl(nif))
            case TipoDocumento.CIF:
                return calculaCif(baseSinControl(nif))
            default:
                return null
        }
    }

    boolean isValid() {
        if (!isValidFormat) {
            return false
        }

        switch (this.tipo) {
            case TipoDocumento.DNI:
                return isDniValido(nif)
            case TipoDocumento.NIE:
                return isNieValido(nif)
            case TipoDocumento.CIF:
                return isCifValido(nif)
            default:
                return false
        }
    }

    /**
     * Asociación: cualquier CIF válido salvo los de Sociedad Anónima (A) y Sociedad Limitada (B).
     */
    boolean isAsociacion() {
        return isValid() && this.tipo == TipoDocumento.CIF &&
                !LETRAS_CIF_NO_AUTORIZADAS_ASOCIACIONES.contains(primeraLetra())
    }

    /**
     * Persona jurídica: cualquier CIF válido (incluye A y B, que no son asociaciones).
     */
    boolean isLegalEntity() {
        return isValid() && this.tipo == TipoDocumento.CIF
    }

    // =========================================================
    // MÉTODOS PRIVADOS DE CÁLCULO MATEMÁTICO
    // =========================================================

    private String completaCeros(String str, int num) {
        while (str.length() < num) {
            str = "0" + str
        }
        return str
    }

    private String calculaDni(String dniNum) {
        return dniNum + calculaLetraPersonal(dniNum)
    }

    private String calculaNie(String nieNum) {
        String calcStr = baseSinControl(nieNum)
                .replace('X', '0')
                .replace('Y', '1')
                .replace('Z', '2')

        return nieNum + calculaLetraPersonal(calcStr)
    }

    private String calculaCif(String cifBase) {
        return cifBase + calculaDigitoControl(cifBase)
    }

    private char calculaLetraPersonal(String numStr) {
        int num = Integer.parseInt(numStr)
        return LETRAS_NIF.charAt(num % 23)
    }

    private boolean isDniValido(String dni) {
        return dni == calculaDni(baseSinControl(dni))
    }

    private boolean isNieValido(String nie) {
        return nie == calculaNie(baseSinControl(nie))
    }

    private boolean isCifValido(String cif) {
        String base = baseSinControl(cif)
        String controlProporcionado = cif.substring(LONGITUD_BASE_SIN_CONTROL)

        int sumaTotal = calculaSumaCif(base)
        String letraEsperada = "" + DIGITO_CONTROL_CIF.charAt(sumaTotal)
        String numEsperado = "" + sumaTotal

        String cabecera = base.substring(0, LONGITUD_LETRA_TIPO_ENTIDAD)

        if (CIF_LETRAS_OBLIGATORIAS.contains(cabecera)) {
            return controlProporcionado == letraEsperada
        }

        if (CIF_NUMEROS_OBLIGATORIOS.contains(cabecera)) {
            return controlProporcionado == numEsperado
        }

        return controlProporcionado == letraEsperada || controlProporcionado == numEsperado
    }

    private int calculaSumaCif(String cifBase) {
        String digitos = cifBase.substring(LONGITUD_LETRA_TIPO_ENTIDAD, LONGITUD_BASE_SIN_CONTROL)
        int sumaPar = 0
        int sumaImpar = 0

        for (int i = 0; i < digitos.length(); i++) {
            int num = Integer.parseInt("" + digitos.charAt(i))
            if (i % 2 == 0) {
                int prod = num * 2
                sumaImpar += (prod.intdiv(10)) + (prod % 10)
            } else {
                sumaPar += num
            }
        }

        int sumaTotal = sumaPar + sumaImpar
        int digito = 10 - (sumaTotal % 10)

        if (digito == 10) {
            return 0
        }
        return digito
    }

    private String calculaDigitoControl(String cifBase) {
        int sumaTotal = calculaSumaCif(cifBase)
        String cabecera = cifBase.substring(0, LONGITUD_LETRA_TIPO_ENTIDAD)

        if (CIF_LETRAS_OBLIGATORIAS.contains(cabecera)) {
            return "" + DIGITO_CONTROL_CIF.charAt(sumaTotal)
        }

        return "" + sumaTotal
    }
}
