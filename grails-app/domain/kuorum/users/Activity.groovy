package kuorum.users

import org.bson.types.ObjectId

@Deprecated // se va a sustituir por un número de propuestas
class Activity {

    Integer numQuestions = 0
    List<ObjectId> questions = [] //PostIds => Is the id instead of Post because gorm updates all
    Integer numPurposes = 0
    List<ObjectId> purposes = [] //PostIds => Is the id instead of Post because gorm updates all
    Integer numHistories = 0
    List<ObjectId> histories = [] //PostIds => Is the id instead of Post because gorm updates all
    static constraints = {
    }
    //TODO: Pasar esto a un entero en el propio usuario
    String toString(){
        numQuestions+numPurposes+numHistories
    }
}
