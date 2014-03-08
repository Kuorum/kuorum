package kuorum

class CustomRegisterController {

    def step1() {
        log.info("Custom register paso1")
    }

    def step1Save(){
        redirect mapping:'customRegisterStep2'
    }

    def step2(){
        log.info("Custom register paso2")
    }
}
