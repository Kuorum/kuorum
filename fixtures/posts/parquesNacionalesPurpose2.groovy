import kuorum.core.model.PostType
import kuorum.post.Post

fixture {

    parquesNacionalesPurpose2(Post){
        law=parquesNacionales
        numClucks=1
        numVotes=1
        owner=equo
        postType=PostType.PURPOSE
        title="Parques Nacionales sometidos al mercadeo con propietarios y corporaciones"
        text="""
            Para EQUO el anteproyecto de Ley de Parques Nacionales, aprobado en Consejo de Ministros, supone un importante retroceso ya que da pasos hacia la explotación comercial y la privatización de servicios en estos espacios naturales.
Esta reforma, además, refuerza el papel de los propietarios de tierras en la gestión de los parques y abre la puerta al marketing de empresas para financiarlos.
Asimismo se elimina el voluntariado en estos espacios acabando así con una importante herramienta de vinculación de la ciudadanía con su patrimonio natural.
EQUO además ha recordado que el Consejo de Estado ha recomendado la retirada de este anteproyecto porque hay cuestiones que podrían ser inconstitucionales en materia competencial con las autonomías, y considera que algunas disposiciones (como el vuelo a motor en Guadarrama o navegación en Monfrague) son impropias de parques nacionales.
Por todo ello, EQUO expresa su rechazo a la reforma y reclama que los Parques Nacionales sean de verdad espacios dedicados a la conservación de la naturaleza, y la divulgación de sus valores, y que por tanto no intenten explotarse comercialmente, rebajando para ellos su protección.
            """
    }
}