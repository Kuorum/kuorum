package kuorum.util

enum Order {
    ASC("asc"), DESC("desc")

    String value

    Order(String value){
        this.value = value
    }

    static Order findByValue(String value){
        Order.values().find{it.value == value}
    }
}