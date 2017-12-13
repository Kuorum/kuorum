package kuorum.web.binder

import org.grails.databinding.converters.FormattedValueConverter

class FormattedDoubleConverter implements FormattedValueConverter{

    def convert(value, String decimalSimbol) {
        def numberParts = value.split(decimalSimbol.replace('.','\\.'))

        String number = numberParts[0].toString().replace(/[^0-9]/,"")
        if (numberParts.size()>1){
            number += "."+numberParts[1].toString().replace(/[^0-9]/,"")
        }
        Double.parseDouble(number)
    }

    Class getTargetType() {
        // specifies the type to which this converter may be applied
        Double
    }
}
